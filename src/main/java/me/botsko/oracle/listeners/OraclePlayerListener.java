package me.botsko.oracle.listeners;

import me.botsko.oracle.Oracle;
import me.botsko.oracle.utils.JoinUtil;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
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
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerQuit(final PlayerQuitEvent event){
        JoinUtil.registerPlayerQuit( event.getPlayer().getName() );
    }
}
