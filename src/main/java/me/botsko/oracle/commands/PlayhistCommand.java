package me.botsko.oracle.commands;

import me.botsko.oracle.commandlibs.CallInfo;
import me.botsko.oracle.commandlibs.SubHandler;

public class PlayhistCommand implements SubHandler {
	
	
	/**
	 * Handle the command
	 */
	public void handle(CallInfo call) {
		
//		call.getSender().sendMessage( Oracle.messenger.playerMsg( "Most recent 7 days of playtime for " + call.getArg(1) + ": " ) );
//    	
//    	HashMap<Playtime,String> scores = PlaytimeUtil.getPlayerPlaytimeHistory( plugin, call.getArg(1) );
//    	Iterator<Entry<Playtime, String>> it = scores.entrySet().iterator();
//    	while (it.hasNext()) {
//    		Map.Entry<Playtime, String> pairs = (Map.Entry<Playtime, String>)it.next();
//    		Playtime pt = pairs.getKey();
//    		call.getSender().sendMessage( Oracle.messenger.playerMsg( pairs.getValue() + ": " + pt.getHours() + "hrs, " + pt.getMinutes() + " mins"  ) );
//    	}
    
	}
}