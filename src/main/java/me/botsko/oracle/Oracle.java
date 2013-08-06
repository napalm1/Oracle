package me.botsko.oracle;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import me.botsko.elixr.TypeUtils;
import me.botsko.oracle.commands.OracleCommands;
import me.botsko.oracle.commands.WarnCommands;
import me.botsko.oracle.listeners.OraclePlayerListener;
import me.botsko.oracle.utils.AnnouncementUtil;
import me.botsko.oracle.utils.BungeeCord;
import me.botsko.oracle.utils.JoinUtil;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Oracle extends JavaPlugin {

	/**
	 * Protected/private
	 */
	private String plugin_name;
	private String plugin_version;
	private Logger log = Logger.getLogger("Minecraft");
	private static DataSource pool = new DataSource();
	private int last_announcement = 0;
	
	/**
	 * Public
	 */
//	public Language lang;
	public FileConfiguration config;
	public static Messenger messenger;
	public static HashMap<Player,Integer> oraclePlayers = new HashMap<Player,Integer>();

	
    /**
     * Enables the plugin and activates our player listeners
     */
	@Override
	public void onEnable(){
		
		plugin_name = this.getDescription().getName();
		plugin_version = this.getDescription().getVersion();
		
		checkPluginDependancies();
		
		log("Initializing Oracle " + plugin_version + ". By Viveleroi.");
		
		// Loaf config
		loadConfig();
		
//		if(getConfig().getBoolean("oracle.notify-newer-versions")){
//			String notice = UpdateNotification.checkForNewerBuild(plugin_version);
//			if(notice != null){
//				log(notice);
//			}
//		}'
		
		// init db
		pool = initDbPool();
		Connection test_conn = dbc();
		if( pool == null || test_conn == null ){
			String[] dbDisabled = new String[3];
			dbDisabled[0] = "Oracle will disable itself because it couldn't connect to a database.";
			dbDisabled[1] = "If you're using MySQL, check your config. Be sure MySQL is running.";
			dbDisabled[2] = "For help - try http://discover-prism.com/wiki/view/troubleshooting/";
			logSection(dbDisabled);
			disablePlugin();
		}
		if(test_conn != null){
			try {
				test_conn.close();
			} catch (SQLException e) {
				logDbError( e );
			}
		}

		if(isEnabled()){
			
			// Setup databases
			setupDatabase();
		
			try {
			    Metrics metrics = new Metrics(this);
			    metrics.start();
			} catch (IOException e) {
			    log("MCStats submission failed.");
			}
			
			// Load re-usable libraries
			messenger = new Messenger( plugin_name );
			
			/**
			 * Bungee Proxy
			 */
			if( getConfig().getBoolean("oracle.joins.use-bungeecord") ){
				this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
				this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new BungeeCord(this));
			}
			
			// Add commands
			if( getConfig().getBoolean("oracle.bans.enabled") ){
				getCommand("ban").setExecutor( (CommandExecutor) new OracleCommands(this) );
				getCommand("lookup").setExecutor( (CommandExecutor) new OracleCommands(this) );
				getCommand("unban").setExecutor( (CommandExecutor) new OracleCommands(this) );
			}
			if( getConfig().getBoolean("oracle.joins.enabled") ){
				getCommand("seen").setExecutor( (CommandExecutor) new OracleCommands(this) );
				getCommand("played").setExecutor( (CommandExecutor) new OracleCommands(this) );
				getCommand("playhist").setExecutor( (CommandExecutor) new OracleCommands(this) );
				getCommand("stats").setExecutor( (CommandExecutor) new OracleCommands(this) );
			}
			if( getConfig().getBoolean("oracle.warnings.enabled") ){
				getCommand("warn").setExecutor( (CommandExecutor) new WarnCommands(this) );
				getCommand("warnings").setExecutor( (CommandExecutor) new WarnCommands(this) );
			}
			getCommand("ison").setExecutor( (CommandExecutor) new OracleCommands(this) );
			
			// Register listeners
			getServer().getPluginManager().registerEvents(new OraclePlayerListener(this), this);
			
			// Force offline date for everyone
			if( getConfig().getBoolean("oracle.joins.enabled") ){
				JoinUtil.forceDateForAllPlayers();
			}
			
			// Register tasks
			catchUncaughtDisconnects();
			runAnnouncements();
			
		}
	}
	
	
	/**
	 * Load configuration and language files
	 */
	public void loadConfig(){
		OracleConfig mc = new OracleConfig( this );
		config = mc.getConfig();
		// Load language files
//		lang = new Language( mc.getLang( config.getString("oracle.language") ) );
	}
	
	
	/**
	 * 
	 */
	public void checkPluginDependancies(){
		

	}
	
	
	/**
	 * 
	 * @return
	 */
	public DataSource initDbPool(){
		
		DataSource pool = null;
		String dns = "jdbc:mysql://"+config.getString("oracle.mysql.hostname")+":"+config.getString("oracle.mysql.port")+"/"+config.getString("oracle.mysql.database");
		pool = new DataSource();
		pool.setDriverClassName("com.mysql.jdbc.Driver");
		pool.setUrl(dns);
	    pool.setUsername(config.getString("oracle.mysql.username"));
	    pool.setPassword(config.getString("oracle.mysql.password"));
		pool.setMaxActive( config.getInt("oracle.database.max-pool-connections") );
		pool.setMaxIdle( config.getInt("oracle.database.max-pool-connections") );
	    pool.setMaxWait( config.getInt("oracle.database.max-wait") );
	    pool.setRemoveAbandoned(true);
		pool.setRemoveAbandonedTimeout(60);
		pool.setTestOnBorrow(true);
		pool.setValidationQuery("/* ping */SELECT 1");
		pool.setValidationInterval(30000);
	
		return pool;
	}
	
	
	/**
	 * Attempt to rebuild the pool, useful for reloads and failed database
	 * connections being restored
	 */
	public void rebuildPool() {
		// Close pool connections when plugin disables
		if (pool != null) {
			pool.close();
		}
		pool = initDbPool();
	}
	
	
	/**
	 * 
	 * @return
	 */
	public static DataSource getPool(){
		return pool;
	}
	
	
	/**
	 * 
	 * @return
	 * @throws SQLException
	 */
	public static Connection dbc() {
		Connection con = null;
		try {
			con = pool.getConnection();
		} catch (SQLException e) {
			System.out.print("Database connection failed. " + e.getMessage());
			if (!e.getMessage().contains("Pool empty")) {
				e.printStackTrace();
			}
		}
		return con;
	}
	
	
	/**
	 * 
	 */
	protected void setupDatabase() {

		try {
			final Connection conn = dbc();
			if (conn == null)
				return;
			
			String query = "CREATE TABLE IF NOT EXISTS `oracle_announcements` (" +
					"`id` int(11) NOT NULL AUTO_INCREMENT," +
					"`announcement` varchar(255) NOT NULL," +
					"`type` varchar(16) NOT NULL," +
					"`is_active` tinyint(1) NOT NULL," +
					"PRIMARY KEY (`id`)" +
					") ENGINE=InnoDB DEFAULT CHARSET=latin1;";
			Statement st = conn.createStatement();
			st.executeUpdate(query);

			query = "CREATE TABLE IF NOT EXISTS `oracle_bans` (" +
					"`ban_id` int(11) NOT NULL AUTO_INCREMENT," +
					"`player_id` int(11) NOT NULL," +
					"`staff_player_id` int(11) NOT NULL," +
					"`reason` varchar(155) NOT NULL," +
					"`epoch` int(11) NOT NULL," +
					"`unbanned` tinyint(1) NOT NULL DEFAULT '0'," +
					"PRIMARY KEY (`id`)" +
					") ENGINE=InnoDB DEFAULT CHARSET=latin1;";
			st.executeUpdate(query);
			
			query = "CREATE TABLE IF NOT EXISTS `oracle_ips` (" +
					"`ip_id` int(11) unsigned NOT NULL AUTO_INCREMENT," +
					"`ip` int(11) unsigned NOT NULL," +
					"PRIMARY KEY (`ip_id`)" +
					") ENGINE=InnoDB  DEFAULT CHARSET=latin1;";
			st.executeUpdate(query);
			
			query = "CREATE TABLE IF NOT EXISTS `oracle_joins` (" +
					"`join_id` int(11) unsigned NOT NULL AUTO_INCREMENT," +
					"`player_count` smallint(4) unsigned NOT NULL," +
					"`player_id` int(11) unsigned NOT NULL," +
					"`player_join` int(11) NOT NULL," +
					"`player_quit` int(11) unsigned DEFAULT NULL," +
					"`playtime` int(11) unsigned DEFAULT NULL," +
					"`ip_id` int(11) unsigned NOT NULL," +
					"PRIMARY KEY (`join_id`)" +
					") ENGINE=InnoDB  DEFAULT CHARSET=latin1;";
			st.executeUpdate(query);

			query = "CREATE TABLE IF NOT EXISTS `oracle_players` (" +
					"`player_id` int(11) unsigned NOT NULL AUTO_INCREMENT," +
					"`player` varchar(16) NOT NULL," +
					"PRIMARY KEY (`player_id`)" +
					") ENGINE=InnoDB  DEFAULT CHARSET=latin1;";
			st.executeUpdate(query);
			
			query = "CREATE TABLE IF NOT EXISTS `oracle_unbans` (" +
					"`unban_id` int(11) NOT NULL AUTO_INCREMENT," +
					"`player_id` int(11) unsigned NOT NULL," +
					"`staff_player_id` int(11) unsigned NOT NULL," +
					"`epoch` int(11) unsigned NOT NULL," +
					"PRIMARY KEY (`id`)" +
					") ENGINE=InnoDB DEFAULT CHARSET=latin1;";
			st.executeUpdate(query);
			
			query = "CREATE TABLE IF NOT EXISTS `oracle_warnings` (" +
					"`warning_id` int(11) unsigned NOT NULL AUTO_INCREMENT," +
					"`player_id` int(11) unsigned NOT NULL," +
					"`reason` varchar(155) NOT NULL," +
					"`staff_player_id` int(11) unsigned NOT NULL," +
					"`date_created` int(11) unsigned NOT NULL," +
					"`deleted` tinyint(1) NOT NULL DEFAULT '0'," +
					"PRIMARY KEY (`warning_id`)" +
					") ENGINE=InnoDB DEFAULT CHARSET=latin1;";
			st.executeUpdate(query);


			st.close();
			conn.close();
			
		} catch (SQLException e) {
			log("Database connection error: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	
//	/**
//	 * Attempt to reconnect to the database
//	 * @return
//	 * @throws SQLException 
//	 */
//	protected boolean attemptToRescueConnection( SQLException e ) throws SQLException{
//		if( e.getMessage().contains("connection closed") ){
//			rebuildPool();
//			if( pool != null ){
//				Connection conn = dbc();
//				if( conn != null && !conn.isClosed() ){
//					return true;
//				}
//			}
//		}
//		return false;
//	}
	
	
//	/**
//	 * 
//	 */
//	public void handleDatabaseException(SQLException e) {
//		// Attempt to rescue
//		try {
//			if( attemptToRescueConnection( e ) ){
//				return;
//			}
//		} catch (SQLException e1){
//		}
//		log("Database connection error: " + e.getMessage());
//		if (e.getMessage().contains("marked as crashed")) {
//			String[] msg = new String[2];
//			msg[0] = "If MySQL crashes during write it may corrupt it's indexes.";
//			msg[1] = "Try running `CHECK TABLE prism_actions` and then `REPAIR TABLE prism_actions`.";
//			logSection(msg);
//		}
//		e.printStackTrace();
//	}
	
	
	/**
	 * If a user disconnects in an unknown way that is never caught by onPlayerQuit,
	 * this will force close all records except for players currently online.
	 */
	public void catchUncaughtDisconnects(){
		if( getConfig().getBoolean("oracle.joins.enabled") ){
			getServer().getScheduler().runTaskTimerAsynchronously(this, new Runnable(){
			    public void run(){
			    	String on_users = "";
					for(Player pl: getServer().getOnlinePlayers()) {
						int player_id = JoinUtil.lookupPlayer(pl);
						on_users += ""+player_id+",";
					}
					if(!on_users.isEmpty()){
						on_users = on_users.substring(0, on_users.length()-1);
					}
					JoinUtil.forceDateForOfflinePlayers( on_users );
			    }
			}, 1200L, 1200L);
		}
	}
	
	
	/**
	 * If a user disconnects in an unknown way that is never caught by onPlayerQuit,
	 * this will force close all records except for players currently online.
	 */
	public void runAnnouncements(){
		getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {

		    public void run() {

		    	// Pull all items matching this name
				List<String> announces = AnnouncementUtil.getActiveAnnouncements();
				if(!announces.isEmpty()){
					
					if(last_announcement >= announces.size()){
						last_announcement = 0;
					}
					
					String msg = announces.get(last_announcement);
					for(Player pl : getServer().getOnlinePlayers()) {
			    		pl.sendMessage( TypeUtils.colorize(msg) );
			    	}
					log( msg );
					
					last_announcement++;
				}
		    }
		}, 6000L, 6000L);
	}
	
	
	/**
     * Partial username matching
     * @param Name
     * @return
     */
    public String expandName(String Name) {
        int m = 0;
        String Result = "";
        for (int n = 0; n < getServer().getOnlinePlayers().length; n++) {
            String str = getServer().getOnlinePlayers()[n].getName();
            if (str.matches("(?i).*" + Name + ".*")) {
                m++;
                Result = str;
                if(m==2) {
                    return null;
                }
            }
            if (str.equalsIgnoreCase(Name))
                return str;
        }
        if (m == 1)
            return Result;
        if (m > 1) {
            return Name;
        }
        if (m < 1) {
            return Name;
        }
        return Name;
    }
    
    
    /**
	 * 
	 * @param message
	 */
	public void log(String message){
		log.info("["+plugin_name+"]: " + message);
	}
	
    
    /**
	 * 
	 * @param message
	 */
	public void logSection(String[] messages){
		if(messages.length > 0){
			log("--------------------- ## Important ## ---------------------");
			for(String msg : messages){
				log(msg);
			}
			log("--------------------- ## ========= ## ---------------------");
		}
	}
	
	
	/**
	 * 
	 */
	public void logDbError( SQLException e ){
		log("Database connection error: " + e.getMessage());
//		if(e.getMessage().contains("marked as crashed")){
//			String[] msg = new String[2];
//			msg[0] = "If MySQL crashes during write it may corrupt it's indexes.";
//			msg[1] = "Try running `CHECK TABLE oracle_joins` and then `REPAIR TABLE oracle_joins`.";
//			logSection(msg);
//		}
		e.printStackTrace();
	}
	
	
	/**
	 * Disable the plugin
	 */
	public void disablePlugin(){
		this.setEnabled(false);
	}
	
	
	/**
	 * Shutdown
	 */
	@Override
	public void onDisable(){
		
		// Force offline date for everyone
		if( getConfig().getBoolean("oracle.joins.enabled") ){
			JoinUtil.forceDateForAllPlayers();
		}
		
		// Close pool connections when plugin disables
		if (pool != null) {
			pool.close();
		}
		
		log("Closing plugin.");
		
	}
}