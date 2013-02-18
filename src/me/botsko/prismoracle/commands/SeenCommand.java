package me.botsko.prismoracle.commands;

import java.text.ParseException;
import java.util.Date;

import me.botsko.prism.commandlibs.CallInfo;
import me.botsko.prism.commandlibs.SubHandler;
import me.botsko.prismoracle.PrismOracle;
import me.botsko.prismoracle.utils.SeenUtil;

public class SeenCommand implements SubHandler {
	
	/**
	 * 
	 */
	private PrismOracle plugin;
	
	
	/**
	 * 
	 * @param plugin
	 * @return 
	 */
	public SeenCommand(PrismOracle plugin) {
		this.plugin = plugin;
	}
	
	
	/**
	 * Handle the command
	 */
	public void handle(CallInfo call) {
		
		String username = null;
		if(call.getArgs().length > 0){
			// Expand partials
			String tmp = plugin.expandName( call.getArg(0) );
			if(tmp != null){
	    		username = tmp;
	    	}
		} else {
			username = call.getPlayer().getName();
		}
	

    	Date joined;
		try {
			joined = SeenUtil.getPlayerFirstSeen(username);
			call.getSender().sendMessage( plugin.messenger.playerMsg("Joined " + joined) );
		} catch (ParseException e1) {
			// @todo player error
		}
    	
    	
    	Date seen;
		try {
			seen = SeenUtil.getPlayerLastSeen(username);
			call.getSender().sendMessage( plugin.messenger.playerMsg("Last Seen " + seen) );
		} catch (ParseException e) {
			// @todo player error
		}
	}
}