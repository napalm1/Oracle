package me.botsko.oracle.commands;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ru.tehkode.permissions.PermissionGroup;
import ru.tehkode.permissions.PermissionUser;

import me.botsko.dhmcstats.commands.PlayedCommandExecutor;
import me.botsko.dhmcstats.joins.Alts;
import me.botsko.dhmcstats.joins.JoinUtil;
import me.botsko.dhmcstats.playtime.Playtime;
import me.botsko.dhmcstats.playtime.PlaytimeUtil;
import me.botsko.dhmcstats.rank.RankUtil;
import me.botsko.oracle.Oracle;
import me.botsko.oracle.commandlibs.CallInfo;
import me.botsko.oracle.commandlibs.SubHandler;
import me.botsko.oracle.utils.BanUtil;

public class PlayhistCommand implements SubHandler {
	
	/**
	 * 
	 */
	private Oracle plugin;
	
	
	/**
	 * 
	 * @param plugin
	 * @return 
	 */
	public PlayhistCommand(Oracle plugin) {
		this.plugin = plugin;
	}
	
	
	/**
	 * Handle the command
	 */
	public void handle(CallInfo call) {
		
sender.sendMessage( plugin.playerMsg( "Most recent 7 days of playtime for " + username + ": " ) );
    	
    	HashMap<Playtime,String> scores = PlaytimeUtil.getPlayerPlaytimeHistory( plugin, username );
    	Iterator<Entry<Playtime, String>> it = scores.entrySet().iterator();
    	while (it.hasNext()) {
    		Map.Entry<Playtime, String> pairs = (Map.Entry<Playtime, String>)it.next();
    		Playtime pt = pairs.getKey();
    		sender.sendMessage( plugin.playerMsg( pairs.getValue() + ": " + pt.getHours() + "hrs, " + pt.getMinutes() + " mins"  ) );
    	}
    
	}
}