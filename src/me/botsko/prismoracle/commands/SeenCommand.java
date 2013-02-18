package me.botsko.prismoracle.commands;

import java.util.Date;

import me.botsko.prism.Prism;
import me.botsko.prism.commandlibs.CallInfo;
import me.botsko.prism.commandlibs.SubHandler;
import me.botsko.prismoracle.utils.SeenUtil;

public class SeenCommand implements SubHandler {
	
	/**
	 * 
	 */
	private Prism plugin;
	
	
	/**
	 * 
	 * @param plugin
	 * @return 
	 */
	public SeenCommand(Prism plugin) {
		this.plugin = plugin;
	}
	
	
	/**
	 * Handle the command
	 */
	public void handle(CallInfo call) {
		
		String username = call.getArg(1);
		
		// Expand partials
    	String tmp = plugin.expandName(username);
    	if(tmp != null){
    		username = tmp;
    	}

    	Date joined = SeenUtil.getPlayerFirstSeen(username);
    	call.getSender().sendMessage( plugin.playerMsg("Joined " + joined) );
    	
    	Date seen = SeenUtil.getPlayerLastSeen(username);
    	call.getSender().sendMessage( plugin.playerMsg("Last Seen " + seen) );
		
	}
}