package me.botsko.oracle.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;

import me.botsko.oracle.Oracle;
import me.botsko.oracle.commandlibs.CallInfo;
import me.botsko.oracle.commandlibs.SubHandler;
import me.botsko.oracle.utils.Playtime;
import me.botsko.oracle.utils.PlaytimeUtil;

public class PlayedCommand implements SubHandler {
	
	/**
	 * 
	 */
	private Oracle plugin;
	
	
	/**
	 * 
	 * @param plugin
	 * @return 
	 */
	public PlayedCommand(Oracle plugin) {
		this.plugin = plugin;
	}
	
	
	/**
	 * Handle the command
	 */
	public void handle( final CallInfo call ){
		
		String username = call.getSender().getName();
		if( call.getArgs().length > 0 ){
			username = plugin.expandName( call.getArg(0) );
		}
		
		final OfflinePlayer player = Bukkit.getOfflinePlayer(username);
		
		if( player == null ){
			call.getSender().sendMessage( Oracle.messenger.playerError( "Could not find a player by that name." ) );
			return;
		}
		
		// Check for alt accounts in async thread
    	plugin.getServer().getScheduler().runTaskAsynchronously(plugin, new Runnable(){
			public void run(){
		
				Playtime playtime = PlaytimeUtil.getPlaytime( player );
				String msg = ChatColor.GOLD + player.getName() + " has played for " + playtime.getHours() + " hours, " + playtime.getMinutes() + " minutes, and " + playtime.getSeconds() + " seconds. Nice!";
				call.getSender().sendMessage( Oracle.messenger.playerHeaderMsg( msg ) );
			}
    	});
	}
}