package me.botsko.oracle.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import me.botsko.oracle.Oracle;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WarningUtil {
	
	
	/**
	 * Alert staff to a player with three or more warnings
	 * @param plugin
	 * @param username
	 */
	public static void alertStaffOnWarnLimit( String username ){
        List<Warning> warnings = WarningUtil.getPlayerWarnings( username );
        if(warnings.size() >= 3){
        	for(Player pl: Bukkit.getServer().getOnlinePlayers()) {
        		if(pl.hasPermission("oracle.warn")){
        			pl.sendMessage( Oracle.messenger.playerMsg(username + " now has three warnings. " + ChatColor.RED + "Action must be taken.") );
        		}
        	}
        }
	}
	
	
	/**
	 * 
	 * @param person
	 * @param account_name
	 */
	public static void fileWarning( OfflinePlayer player, String reason, CommandSender staff ){
		Connection conn = null;
		PreparedStatement s = null;
		try {
			
			// Insert/Get Player ID
			int player_id = JoinUtil.lookupPlayer( player );
			
			int staff_player_id = 0;
			if( staff instanceof Player ){
				staff_player_id = JoinUtil.lookupPlayer( (Player) staff );
			}
			
			conn = Oracle.dbc();

	        s = conn.prepareStatement("INSERT INTO oracle_warnings (player_id,reason,date_created,staff_player_id) VALUES (?,?,?,?)");
	        s.setInt(1, player_id);
	        s.setString(2, reason);
	        s.setLong(3, System.currentTimeMillis() / 1000L);
	        s.setInt(4, staff_player_id);
	        s.executeUpdate();

		} catch (SQLException e){
            e.printStackTrace();
        } finally {
        	if(s != null) try { s.close(); } catch (SQLException e) {}
        	if(conn != null) try { conn.close(); } catch (SQLException e) {}
        }
	}
	
	
	/**
	 * 
	 * @param person
	 * @param account_name
	 */
	public static List<Warning> getPlayerWarnings( String username ){
		ArrayList<Warning> warnings = new ArrayList<Warning>();
		Connection conn = null;
		PreparedStatement s = null;
		ResultSet rs = null;
		try {
			
			conn = Oracle.dbc();
    		s = conn.prepareStatement ("SELECT id, date_created, reason, p.player, s.player as staff FROM oracle_warnings w " +
    				"LEFT JOIN oracle_players p ON p.id = w.player_id " + 
    				"LEFT JOIN oracle_players s ON s.id = w.staff_player_id " + 
    				"WHERE p.player = ? AND deleted = 0");
    		s.setString(1, username);
    		s.executeQuery();
    		rs = s.getResultSet();

    		while(rs.next()){
    			warnings.add( new Warning(rs.getInt("id"), rs.getLong("date_created"), rs.getString("player"), rs.getString("reason"), rs.getString("staff")) );
			}
            
		} catch (SQLException e){
            e.printStackTrace();
        } finally {
        	if(rs != null) try { rs.close(); } catch (SQLException e) {}
        	if(s != null) try { s.close(); } catch (SQLException e) {}
        	if(conn != null) try { conn.close(); } catch (SQLException e) {}
        }
		return warnings;
	}
	
	
	/**
	 * 
	 * @param person
	 * @param account_name
	 */
	public static void deleteWarning( int id ){
		Connection conn = null;
		PreparedStatement s = null;
		try {
			
			conn = Oracle.dbc();
	        s = conn.prepareStatement("UPDATE oracle_warnings SET deleted = 1 WHERE id = ?");
	        s.setInt(1, id);
	        s.executeUpdate();
     
		} catch (SQLException e){
            e.printStackTrace();
        } finally {
        	if(s != null) try { s.close(); } catch (SQLException e) {}
        	if(conn != null) try { conn.close(); } catch (SQLException e) {}
        }
	}
}