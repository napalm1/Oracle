package me.botsko.oracle.commands;

import me.botsko.oracle.Oracle;
import me.botsko.oracle.commandlibs.CallInfo;
import me.botsko.oracle.commandlibs.SubHandler;
import me.botsko.oracle.utils.BanUtil;

public class LookupCommand implements SubHandler {
	
	
	/**
	 * Handle the command
	 */
	public void handle(CallInfo call) {

		try {
			BanUtil.playerMayJoin( call.getArg(0) );
			call.getSender().sendMessage(Oracle.messenger.playerHeaderMsg( call.getArg(0) + " is not banned." ));
		} catch ( Exception e ){
			call.getSender().sendMessage(Oracle.messenger.playerHeaderMsg( call.getArg(0) + " is banned. Reason: " + e.getMessage() + "."));
		}
	}
}