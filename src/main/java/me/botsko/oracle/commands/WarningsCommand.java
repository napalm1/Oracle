package me.botsko.oracle.commands;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import me.botsko.dhmcstats.warnings.WarningUtil;
import me.botsko.dhmcstats.warnings.Warnings;
import me.botsko.oracle.Oracle;
import me.botsko.oracle.commandlibs.CallInfo;
import me.botsko.oracle.commandlibs.SubHandler;
import me.botsko.oracle.utils.BanUtil;

public class WarningsCommand implements SubHandler {
	
	/**
	 * 
	 */
	private Oracle plugin;
	
	
	/**
	 * 
	 * @param plugin
	 * @return 
	 */
	public WarningsCommand(Oracle plugin) {
		this.plugin = plugin;
	}
	
	
	/**
	 * Handle the command
	 */
	public void handle(CallInfo call) {
		
		Player player = null;
		if(sender instanceof Player){
			player = (Player) sender;
		}
		
		// /warnings (player)
		if(sender instanceof ConsoleCommandSender || (player != null && player.hasPermission("dhmcstats.warnings")) ){
			
			// If no username found, assume they mean themselves
			String user = "";
			if(args.length == 0){
				if(sender instanceof Player){
					user = player.getName();
				}
			} else {
				user = args[0];
			}

			if(!user.isEmpty()){
				try {
					listWarnings(user, sender);
					return true;
				} catch (SQLException e) {
					e.printStackTrace();
				}
			} else {
				sender.sendMessage( plugin.playerError("Player name must be specified.") );
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
    	
    	sender.sendMessage( plugin.playerMsg( "Warnings filed for " + username + ": " ) );
    	
    	// Pull all items matching this name
		List<Warnings> warnings = WarningUtil.getPlayerWarnings(plugin,username);
		if(!warnings.isEmpty()){
			for(Warnings warn : warnings){
				sender.sendMessage( plugin.playerMsg( "["+ warn.id + "] " + warn.datewarned + ": " + ChatColor.RED + warn.reason ) );
			}
		} else {
			sender.sendMessage( plugin.playerError("No warnings filed.") );
		}
    }
}