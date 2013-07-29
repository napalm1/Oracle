package me.botsko.oracle.commands;

import me.botsko.oracle.Oracle;
import me.botsko.oracle.commandlibs.CallInfo;
import me.botsko.oracle.commandlibs.SubHandler;
import me.botsko.oracle.utils.BanUtil;

public class LookupCommand implements SubHandler {
	
	/**
	 * 
	 */
	private Oracle plugin;
	
	
	/**
	 * 
	 * @param plugin
	 * @return 
	 */
	public LookupCommand(Oracle plugin) {
		this.plugin = plugin;
	}
	
	
	/**
	 * Handle the command
	 */
	public void handle(CallInfo call) {
		
//		boolean is_banned = false;

		try {
			BanUtil.playerMayJoin( call.getArg(0) );
			call.getSender().sendMessage(plugin.messenger.playerHeaderMsg( call.getArg(0) + " is not banned." ));
		} catch ( Exception e ){
			call.getSender().sendMessage(plugin.messenger.playerHeaderMsg( call.getArg(0) + " is banned. Reason: " + e.getMessage() + "."));
		}
	}
}