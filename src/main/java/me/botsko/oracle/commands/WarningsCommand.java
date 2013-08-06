package me.botsko.oracle.commands;

import java.sql.SQLException;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

import me.botsko.oracle.Oracle;
import me.botsko.oracle.commandlibs.CallInfo;
import me.botsko.oracle.commandlibs.SubHandler;
import me.botsko.oracle.utils.WarningUtil;
import me.botsko.oracle.utils.Warning;

public class WarningsCommand implements SubHandler {
	
	
	/**
	 * Handle the command
	 */
	public void handle(CallInfo call) {
		
		
		// /warnings (player)
		if( call.getSender() instanceof ConsoleCommandSender || ( call.getPlayer() != null && call.getPlayer().hasPermission("oracle.warnings")) ){
			
			// If no username found, assume they mean themselves
			String user = "";
			if(call.getArgs().length == 0){
				if( call.getPlayer() != null ){
					user = call.getPlayer().getName();
				}
			} else {
				user = call.getArg(1);
			}

			if(!user.isEmpty()){
				try {
					listWarnings(user, call.getSender());
				} catch (SQLException e) {
					e.printStackTrace();
				}
			} else {
				call.getSender().sendMessage( Oracle.messenger.playerError("Player name must be specified.") );
			}
		}
	}
	
	

	/**
	 * 
	 * @param username
	 * @param sender
	 * @throws SQLException
	 */
    public void listWarnings(String username, CommandSender sender) throws SQLException{
    	
    	OfflinePlayer warned_player = Bukkit.getOfflinePlayer( username );
    	if( warned_player == null ){
    		sender.sendMessage( Oracle.messenger.playerError( "Could not find a player by that name." ) );
			return;
		}
    	
    	sender.sendMessage( Oracle.messenger.playerMsg( "Warnings filed for " + username + ": " ) );
    	
    	// Pull all items matching this name
		List<Warning> warnings = WarningUtil.getPlayerWarnings( warned_player );
		if(!warnings.isEmpty()){
			for(Warning warn : warnings){
				sender.sendMessage( Oracle.messenger.playerMsg( "["+ warn.id + "] " + warn.epoch + ": " + ChatColor.RED + warn.reason ) );
			}
		} else {
			sender.sendMessage( Oracle.messenger.playerError("No warnings filed.") );
		}
    }
}