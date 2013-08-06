package me.botsko.oracle.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.botsko.oracle.Oracle;

public class BanUtil {
	
	
	/**
	 * 
	 * @param person
	 * @param account_name
	 */
	public static void banByUsername( CommandSender staff, OfflinePlayer player, String reason ){
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
	        s = conn.prepareStatement("INSERT INTO oracle_bans (player_id,staff_player_id,reason,epoch) VALUES (?,?,?,?)");
	        s.setInt(1, player_id);
	        s.setInt(2, staff_player_id);
	        s.setString(3, reason);
	        s.setLong(4, System.currentTimeMillis() / 1000L);
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
	public static void unbanByUsername( CommandSender staff, OfflinePlayer player ){
		
		if( player == null ){
			throw new IllegalArgumentException("Argument may not be null");
		}
		
		Connection conn = null;
		PreparedStatement s = null;
		try {
			conn = Oracle.dbc();
			
			// Insert/Get Player ID
			int player_id = JoinUtil.lookupPlayer( player );
			
			int staff_player_id = 0;
			if( staff instanceof Player ){
				staff_player_id = JoinUtil.lookupPlayer( (Player) staff );
			}
			
			// Add unban record
	        s = conn.prepareStatement("INSERT INTO oracle_unbans (player_id,staff_player,epoch) VALUES (?,?,?)");
	        s.setInt(1, player_id);
	        s.setInt(2, staff_player_id);
	        s.setLong(3, System.currentTimeMillis() / 1000L);
	        s.executeUpdate();
	        
	        // Mark as unbanned
	        s = conn.prepareStatement("UPDATE oracle_bans SET unbanned = 1 WHERE player_id = ?");
	        s.setInt(1, player_id);
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
	 * @param username
	 * @throws Exception 
	 */
	public static void playerMayJoin( OfflinePlayer player ) throws Exception{
		
		if( player == null ){
			throw new IllegalArgumentException("Argument may not be null");
		}
		
		Connection conn = null;
		PreparedStatement s = null;
		ResultSet rs = null;
		try {
			
			// Insert/Get Player ID
			int player_id = JoinUtil.lookupPlayer( player );
			
			conn = Oracle.dbc();
    		s = conn.prepareStatement ("SELECT * FROM oracle_bans WHERE player_id = ? AND unbanned = 0 LIMIT 1");
    		s.setInt(1, player_id);
    		s.executeQuery();
    		rs = s.getResultSet();
    		
    		if(rs.first()){
    			throw new Exception( rs.getString("reason") );
    		}

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
        	if(rs != null) try { rs.close(); } catch (SQLException e) {}
        	if(s != null) try { s.close(); } catch (SQLException e) {}
        	if(conn != null) try { conn.close(); } catch (SQLException e) {}
        }
	}
}