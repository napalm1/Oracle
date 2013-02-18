package me.botsko.prismoracle;

import me.botsko.prism.Language;
import me.botsko.prism.Messenger;
import me.botsko.prism.Prism;
import me.botsko.prismoracle.commands.PrismOracleCommands;
import me.botsko.prismoracle.listeners.PrismOraclePlayerListener;
import me.botsko.prismoracle.utils.JoinUtil;

import org.bukkit.command.CommandExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class PrismOracle extends JavaPlugin {

	/**
	 * Protected/private
	 */
	protected String plugin_name;
	protected String plugin_version;
	protected Prism prism;
	
	/**
	 * Public
	 */
	public Language lang;
	public FileConfiguration config;
	public Messenger messenger;

	
    /**
     * Enables the plugin and activates our player listeners
     */
	@Override
	public void onEnable(){
		
		plugin_name = this.getDescription().getName();
		plugin_version = this.getDescription().getVersion();
		
		// Make sure Prism exists
		checkPluginDependancies();
		
		prism.log("Initializing Prism Oracle " + plugin_version + ". By Viveleroi.");
		
		// Loaf config
		loadConfig();
		
//		if(getConfig().getBoolean("prism.notify-newer-versions")){
//			String notice = UpdateNotification.checkForNewerBuild(plugin_version);
//			if(notice != null){
//				log(notice);
//			}
//		}

		if(isEnabled()){
		
//			try {
//			    Metrics metrics = new Metrics(this);
//			    metrics.start();
//			} catch (IOException e) {
//			    log("MCStats submission failed.");
//			}
			
			// Load re-usable libraries
			messenger = new Messenger( plugin_name );
			
			// Add commands
			getCommand("seen").setExecutor( (CommandExecutor) new PrismOracleCommands(this) );
			
			// Register listeners
			getServer().getPluginManager().registerEvents(new PrismOraclePlayerListener(this), this);
			
			// Register tasks
			catchUncaughtDisconnects();
			
		}
	}
	
	
	/**
	 * Load configuration and language files
	 */
	public void loadConfig(){
		PrismOracleConfig mc = new PrismOracleConfig( this );
		config = mc.getConfig();
		// Load language files
		lang = new Language( mc.getLang( mc.getConfig().getString("oracle.language") ) );
	}
	
	
	/**
	 * 
	 * @return
	 */
	public Prism getPrism(){
		return prism;
	}
	
	
	/**
	 * 
	 */
	public void checkPluginDependancies(){
		
		// Prism 
		Plugin _tempPrism = getServer().getPluginManager().getPlugin("Prism");
		if (_tempPrism != null) {
			prism = (Prism)_tempPrism;
			prism.log("Prism Core (anti-grief) found!");
		}
		else {
			prism.log("Prism Core (anti-grief) not found. Plugin add-on may not run.");
			this.disablePlugin();
		}
	}
	
	
	/**
	 * If a user disconnects in an unknown way that is never caught by onPlayerQuit,
	 * this will force close all records except for players currently online.
	 */
	public void catchUncaughtDisconnects(){
		getServer().getScheduler().runTaskTimerAsynchronously(this, new Runnable(){
		    public void run(){
		    	String on_users = "";
				for(Player pl: getServer().getOnlinePlayers()) {
					on_users += "'"+pl.getName()+"',";
				}
				if(!on_users.isEmpty()){
					on_users = on_users.substring(0, on_users.length()-1);
				}
				JoinUtil.forceDateForOfflinePlayers( prism, on_users );
		    }
		}, 1200L, 1200L);
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
		prism.log("Closing plugin.");
	}
}