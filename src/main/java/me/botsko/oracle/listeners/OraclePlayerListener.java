package me.botsko.oracle.listeners;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import me.botsko.oracle.Oracle;
import me.botsko.oracle.utils.BanUtil;
import me.botsko.oracle.utils.JoinUtil;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
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
    	
    	if( !plugin.getConfig().getBoolean("oracle.joins.enabled") ) return;
        
        Player player = event.getPlayer();
        final String username = player.getName();
        final String ip = player.getAddress().getAddress().getHostAddress().toString();
        
        // Save join into table
        JoinUtil.registerPlayerJoin( username, ip, plugin.getServer().getOnlinePlayers().length );
        
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
        JoinUtil.registerPlayerQuit( event.getPlayer().getName() );
    }
    
    
    /**
	 * 
	 * @param event
	 */
	@EventHandler(priority = EventPriority.LOW)
	public void onPlayerChat(AsyncPlayerChatEvent event){
		
		Player player = event.getPlayer();
		
		if( !plugin.getConfig().getBoolean("oracle.kick-minechat") ) return;
		
		if( event.getMessage().matches("connected.*MineChat") ){
			player.kickPlayer( "MineChat is not allowed... sorry" );
		}
	}
}
