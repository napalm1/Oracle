package me.botsko.oracle.commands;

import org.apache.commons.lang.StringUtils;

import me.botsko.oracle.Oracle;
import me.botsko.oracle.commandlibs.CallInfo;
import me.botsko.oracle.commandlibs.SubHandler;
import me.botsko.oracle.utils.BanUtil;

public class BanIpCommand implements SubHandler {
	
	
	/**
	 * Handle the command
	 */
	public void handle(CallInfo call) {
		
		if(call.getArgs().length <= 0){
			call.getSender().sendMessage( Oracle.messenger.playerError("You must provide an IP address to ban.") );
			return;
		}
		
		if(call.getArg(0).equals("127.0.0.1")){
			call.getSender().sendMessage( Oracle.messenger.playerError("You may not ban a localhost IP.") );
			return;
		}
		
		String reason = "You're banned. No reason provided.";
		if(call.getArgs().length > 1){
			String[] messageArgs = new String[(call.getArgs().length - 1)];
			for(int i = 1; i < call.getArgs().length; i++ ){
				messageArgs[ (i-1) ] = call.getArgs()[i];
			}
			reason = StringUtils.join( messageArgs, " ");
		}

		// Save to db
		BanUtil.banByIp( call.getSender(), call.getArg(0), reason );
		
		// Tell banner
		call.getSender().sendMessage( Oracle.messenger.playerHeaderMsg( call.getSender().getName() + " banned IP " + call.getArg(0) + " for: " + reason ) );
    
	}
}