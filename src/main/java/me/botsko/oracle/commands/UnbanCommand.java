package me.botsko.oracle.commands;

import org.bukkit.entity.Player;

import me.botsko.oracle.Oracle;
import me.botsko.oracle.commandlibs.CallInfo;
import me.botsko.oracle.commandlibs.SubHandler;
import me.botsko.oracle.utils.BanUtil;

public class UnbanCommand implements SubHandler {
	
	
	/**
	 * Handle the command
	 */
	public void handle(CallInfo call) {
		
		if(call.getArgs().length <= 0){
			call.getSender().sendMessage( Oracle.messenger.playerError("You must provide a username to unban.") );
			return;
		}
		
		// Who
		String username = call.getArg(0);
		
		// Who unbanned
		String moderator = "console";
		if( call.getSender() instanceof Player ){
			moderator = ((Player)call.getSender()).getName();
		}
	
		// Save to db
		BanUtil.unbanByUsername( moderator, username );
		
		// Success
		call.getSender().sendMessage( Oracle.messenger.playerHeaderMsg("Player has been unbanned.") );
    
	}
}