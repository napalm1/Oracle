package me.botsko.oracle.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import me.botsko.oracle.Oracle;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class WarningUtil {
	
	
	/**
	 * Alert staff to a player with three or more warnings
	 * @param plugin
	 * @param username
	 */
	public static void alertStaffOnWarnLimit( String username ){
        List<Warnings> warnings = WarningUtil.getPlayerWarnings( username );
        if(warnings.size() >= 3){
        	for(Player pl: Bukkit.getServer().getOnlinePlayers()) {
        		if(pl.hasPermission("dhmcstats.warn")){
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
	public static void fileWarning( String username, String reason, String filer ){
		Connection conn = null;
		PreparedStatement s = null;
		try {
			
			conn = Oracle.dbc();
	
	        java.util.Date date= new java.util.Date();
	        String ts = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date.getTime());
			
	        s = conn.prepareStatement("INSERT INTO warnings (username,reason,date_created,moderator) VALUES (?,?,?,?)");
	        s.setString(1, username);
	        s.setString(2, reason);
	        s.setString(3, ts);
	        s.setString(4, filer);
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
	public static List<Warnings> getPlayerWarnings( String username ){
		ArrayList<Warnings> warnings = new ArrayList<Warnings>();
		Connection conn = null;
		PreparedStatement s = null;
		ResultSet rs = null;
		try {
			
			conn = Oracle.dbc();
    		s = conn.prepareStatement ("SELECT id, DATE_FORMAT(warnings.date_created,'%m/%d/%y') as warndate, reason, username, moderator FROM warnings WHERE username = ? AND deleted = 0");
    		s.setString(1, username);
    		s.executeQuery();
    		rs = s.getResultSet();

    		while(rs.next()){
    			warnings.add( new Warnings(rs.getInt("id"), rs.getString("warndate"), rs.getString("username"), rs.getString("reason"), rs.getString("moderator")) );
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
	        s = conn.prepareStatement("UPDATE warnings SET deleted = 1 WHERE id = ?");
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