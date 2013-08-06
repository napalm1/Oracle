package me.botsko.oracle.listeners;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

import me.botsko.oracle.Oracle;
import me.botsko.oracle.events.OracleFirstTimePlayerEvent;
import me.botsko.oracle.utils.Alt;
import me.botsko.oracle.utils.BanUtil;
import me.botsko.oracle.utils.JoinUtil;
import me.botsko.oracle.utils.WarningUtil;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.player.PlayerQuitEvent;

public class OraclePlayerListener implements Listener {
	
	/**
	 * 
	 */
	protected Oracle plugin;
	
	
	/**
	 * 
	 * @param plugin
	 */
	public OraclePlayerListener( Oracle plugin ){
		this.plugin = plugin;
	}
	
	
	/**
	 * 
	 * @param event
	 */
    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {

        Player player = event.getPlayer();
        String cmd = event.getMessage();

        if( !plugin.getConfig().getBoolean("oracle.log-command-use-to-console") ) return;
        
    	int x = player.getLocation().getBlockX();
        int y = player.getLocation().getBlockY();
        int z = player.getLocation().getBlockZ();
        plugin.log( "[Cmd] " + player.getName() + " " + cmd + " @" + player.getWorld().getName() + " " + x + " " + y + " " + z);
        
    }
	
	
	/**
	 * 
	 * @param event
	 */
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerJoin(final PlayerJoinEvent event) {
        
        Player player = event.getPlayer();
        final String username = player.getName();
        
        // Track joins
        if( plugin.getConfig().getBoolean("oracle.joins.enabled") ){

	        // Save join into table
	        JoinUtil.registerPlayerJoin( player, plugin.getServer().getOnlinePlayers().length );
	        
	        // Determine if we're using bungeecord as a proxy
	        if( plugin.getConfig().getBoolean("oracle.joins.use-bungeecord") ){
		        // Pass the information from bungee so we properly track the ip
		        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
		            public void run() {
		                try {
		                	
		                    ByteArrayOutputStream b = new ByteArrayOutputStream();
		                    DataOutputStream out = new DataOutputStream(b);
		
		                    try {
		                        out.writeUTF("IP");
		                    } catch (IOException e){
		                    }
		
		                    plugin.getServer().getPlayerExact( username ).sendPluginMessage(plugin, "BungeeCord", b.toByteArray());
		
		                } catch (Exception exception) {
		                    exception.printStackTrace();
		                }
		            }
		        }, 30L);
	        }
        }
        
        // Track warnings
        if( plugin.getConfig().getBoolean("oracle.warnings.enabled") ){
        
	        WarningUtil.alertStaffOnWarnLimit( username );
	        
        }
    }
    
    
    /**
     * 
     * @param event
     */
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerFirstJoin(final OracleFirstTimePlayerEvent event){
    	
    	if( !plugin.getConfig().getBoolean("oracle.joins.enabled") ) return;
    	
    	Player player = event.getPlayer();
    	
//		// Give them a guide book
//		Guide.giveToPlayer(player);

    	List<Alt> alt_accts;
		try {
			
			alt_accts = JoinUtil.getPlayerAlts( player );
			
			if( alt_accts.isEmpty() ) return;
			
			String alts_list = "";
			int i = 1;
			for(Alt alt : alt_accts){
				alts_list += alt.username + (i == alt_accts.size() ? "" : ", ");
				i++;
			}
			for(Player pl: plugin.getServer().getOnlinePlayers()) {
	    		if(pl.hasPermission("oracle.alerts.alt")){
	    			pl.sendMessage( Oracle.messenger.playerMsg( player.getName() + "'s alts: " + alts_list) );
	    		}
	    	}
			
		} catch (Exception e) {
		}
    }
    
    
    /**
     * 
     * @param event
     */
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerLogin(final PlayerLoginEvent event){
    	
    	if( !plugin.getConfig().getBoolean("oracle.bans.enabled") ) return;
    	
    	Player player = event.getPlayer();
    	
    	try {
			BanUtil.playerMayJoin( player.getName() );
		} catch (Exception e){
			event.setKickMessage( "Banned. " + e.getMessage() );
			event.setResult( Result.KICK_OTHER );
			plugin.log( "Rejecting player login due to ban. For: " + player.getName() );
		}
    }
    
    
    /**
     * 
     * @param event
     */
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerQuit(final PlayerQuitEvent event){
    	if( !plugin.getConfig().getBoolean("oracle.joins.enabled") ) return;
        try {
			JoinUtil.registerPlayerQuit( event.getPlayer() );
		} catch (Exception e) {
		}
    }
}