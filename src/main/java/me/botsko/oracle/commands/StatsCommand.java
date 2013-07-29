package me.botsko.oracle.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import me.botsko.oracle.Oracle;
import me.botsko.oracle.commandlibs.CallInfo;
import me.botsko.oracle.commandlibs.SubHandler;

public class StatsCommand implements SubHandler {
	
	/**
	 * 
	 */
	private Oracle plugin;
	
	
	/**
	 * 
	 * @param plugin
	 * @return 
	 */
	public StatsCommand(Oracle plugin) {
		this.plugin = plugin;
	}
	
	
	/**
	 * Handle the command
	 */
	public void handle(CallInfo call) {
		
		// Pull how many players joined in total
				int total = StatsUtil.getPlayerJoinCount( plugin );
				int playedtoday = StatsUtil.getPlayerJoinTodayCount( plugin );

				sender.sendMessage(ChatColor.GOLD  + "Total Players: " + total);
				sender.sendMessage(ChatColor.GOLD  + "Unique Today: " + playedtoday);
	}
}