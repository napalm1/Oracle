package me.botsko.oracle.commands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

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
			OfflinePlayer pl = Bukkit.getOfflinePlayer(call.getArg(0));
			BanUtil.playerMayJoin( pl );
			call.getSender().sendMessage(Oracle.messenger.playerHeaderMsg( call.getArg(0) + " is not banned." ));
		} catch ( Exception e ){
			call.getSender().sendMessage(Oracle.messenger.playerHeaderMsg( call.getArg(0) + " is banned. Reason: " + e.getMessage() + "."));
		}
	}
}