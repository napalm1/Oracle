package me.botsko.oracle.commands;

import org.bukkit.ChatColor;

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
	public void handle(CallInfo call) {
		
		String username = call.getSender().getName();
		if( call.getArgs().length > 0 ){
			username = plugin.expandName(username);
		}
		
		Playtime playtime = PlaytimeUtil.getPlaytime( username );
		
		String msg = ChatColor.GOLD + username + " has played for " + playtime.getHours() + " hours, " + playtime.getMinutes() + " minutes, and " + playtime.getSeconds() + " seconds. Nice!";
		call.getSender().sendMessage( plugin.messenger.playerHeaderMsg( msg ) );
    
	}
}