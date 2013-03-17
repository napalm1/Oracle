package me.botsko.oracle.commands;

import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.Player;

import me.botsko.oracle.Oracle;
import me.botsko.oracle.commandlibs.CallInfo;
import me.botsko.oracle.commandlibs.SubHandler;
import me.botsko.oracle.utils.BanUtil;

public class BanCommand implements SubHandler {
	
	/**
	 * 
	 */
	private Oracle plugin;
	
	
	/**
	 * 
	 * @param plugin
	 * @return 
	 */
	public BanCommand(Oracle plugin) {
		this.plugin = plugin;
	}
	
	
	/**
	 * Handle the command
	 */
	public void handle(CallInfo call) {
		
		if(call.getArgs().length <= 0){
			call.getSender().sendMessage( plugin.messenger.playerError("You must provide a username to ban.") );
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
		
		// Who
		String username = call.getArg(0);
		
		// Why
		String moderator = "console";
		if( call.getSender() instanceof Player ){
			moderator = ((Player)call.getSender()).getName();
		}
		
		// Is player online - kick them with ban reason
		Player bannedPlayer = plugin.getServer().getPlayer( username );
		if( bannedPlayer != null ){
			bannedPlayer.kickPlayer( reason );
		}
	
		// Save to db
		BanUtil.banByUsername( moderator, username, reason );
		
		// Success
		call.getSender().sendMessage( plugin.messenger.playerHeaderMsg("Player has been banned.") );
		
		// Tell everyone
		plugin.getServer().broadcastMessage( plugin.messenger.playerHeaderMsg( moderator + " banned " + username + " for: " + reason ) );
    
	}
}