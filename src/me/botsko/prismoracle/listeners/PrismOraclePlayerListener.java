package me.botsko.prismoracle.listeners;

import me.botsko.prismoracle.PrismOracle;
import me.botsko.prismoracle.utils.JoinUtil;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PrismOraclePlayerListener implements Listener {
	
	/**
	 * 
	 */
	protected PrismOracle plugin;
	
	
	/**
	 * 
	 * @param plugin
	 */
	public PrismOraclePlayerListener( PrismOracle plugin ){
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
        JoinUtil.registerPlayerJoin( plugin.getPrism(), username, ip, plugin.getServer().getOnlinePlayers().length );
        
    }
    
    
    /**
     * 
     * @param event
     */
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerQuit(final PlayerQuitEvent event){
        JoinUtil.registerPlayerQuit( plugin.getPrism(), event.getPlayer().getName() );
    }
}
