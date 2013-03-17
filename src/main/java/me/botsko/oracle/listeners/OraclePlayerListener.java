package me.botsko.oracle.listeners;

import me.botsko.oracle.Oracle;
import me.botsko.oracle.utils.BanUtil;
import me.botsko.oracle.utils.JoinUtil;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
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
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerJoin(final PlayerJoinEvent event) {
    	
    	if( plugin.getConfig().getBoolean("oracle.joins.enabled") ) return;
        
        Player player = event.getPlayer();
        String username = player.getName();
        String ip = player.getAddress().getAddress().getHostAddress().toString();

        // Save join into table
        JoinUtil.registerPlayerJoin( username, ip, plugin.getServer().getOnlinePlayers().length );
        
    }
    
    
    /**
     * 
     * @param event
     */
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerLogin(final PlayerLoginEvent event){
    	
    	if( plugin.getConfig().getBoolean("oracle.bans.enabled") ) return;
    	
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
    	if( plugin.getConfig().getBoolean("oracle.joins.enabled") ) return;
        JoinUtil.registerPlayerQuit( event.getPlayer().getName() );
    }
}
