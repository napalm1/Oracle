package me.botsko.oracle.commands;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import me.botsko.dhmcstats.warnings.WarningUtil;
import me.botsko.oracle.Oracle;
import me.botsko.oracle.commandlibs.CallInfo;
import me.botsko.oracle.commandlibs.SubHandler;
import me.botsko.oracle.utils.BanUtil;

public class WarnCommand implements SubHandler {
	
	/**
	 * 
	 */
	private Oracle plugin;
	
	
	/**
	 * 
	 * @param plugin
	 * @return 
	 */
	public WarnCommand(Oracle plugin) {
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
		
		Player player = null;
		if(sender instanceof Player){
			player = (Player) sender;
		}
		
		// /warn [player] [msg]
		if(sender instanceof ConsoleCommandSender || (player != null && player.hasPermission("dhmcstats.warn")) ){
			if(args[0].equalsIgnoreCase("delete")){
				if(args.length == 2){
				
					// delete the warning
					WarningUtil.deleteWarning( plugin, new Integer(args[1]) );
					sender.sendMessage( plugin.playerMsg("Warning deleted successfully."));
					return true;
					
				}
			} else {
				if(args.length >= 2){
					
					String reason = "";
					for (int i = 1; i < args.length; i = i + 1){
						reason += args[i]+" ";
					}
					
					String warned_by = "console";
					if(sender instanceof Player){
						warned_by = player.getName();
					}
					
					sender.sendMessage( plugin.playerMsg("Warning file successfully."));
					
					fileWarning(args[0], reason, warned_by);
					
					// This may be a third warning!
					WarningUtil.alertStaffOnWarnLimit(plugin, args[0]);
					
					return true;
				}
			}
		}
    
	}
	
	
	/**
	 * 
	 * @param username
	 * @param reason
	 * @param reporter
	 */
	protected void fileWarning(String username, String reason, String reporter){
		
		WarningUtil.fileWarning( plugin, username, reason, reporter);
		
		for(Player pl: plugin.getServer().getOnlinePlayers()) {
			if(username.equalsIgnoreCase(pl.getName())){
				pl.sendMessage( plugin.playerError("=== OFFICIAL WARNING FILED FOR YOU ===") );
				pl.sendMessage( plugin.playerMsg(reason) );
				pl.sendMessage( plugin.playerError("Three warnings will result in a ban!") );
			}
		}
	}
}