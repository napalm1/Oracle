package me.botsko.oracle.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import me.botsko.oracle.Oracle;

public class BanUtil {
	
	
	/**
	 * 
	 * @param person
	 * @param account_name
	 */
	public static void banByUsername( String moderator, String player, String reason ){
		Connection conn = null;
		PreparedStatement s = null;
		try {
			conn = Oracle.dbc();
	        s = conn.prepareStatement("INSERT INTO oracle_bans (player,moderator,reason) VALUES (?,?,?)");
	        s.setString(1, player);
	        s.setString(2, moderator);
	        s.setString(3, reason);
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
	public static void unbanByUsername( String moderator, String player ){
		Connection conn = null;
		PreparedStatement s = null;
		try {
			conn = Oracle.dbc();
			
			// Add unban record
	        s = conn.prepareStatement("INSERT INTO oracle_unbans (player,moderator) VALUES (?,?)");
	        s.setString(1, player);
	        s.setString(2, moderator);
	        s.executeUpdate();
	        
	        // Mark as unbanned
	        s = conn.prepareStatement("UPDATE oracle_bans SET unbanned = 1 WHERE player = ?");
	        s.setString(1, player);
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
	public static void playerMayJoin( String username ) throws Exception{
		Connection conn = null;
		PreparedStatement s = null;
		ResultSet rs = null;
		try {
			
			conn = Oracle.dbc();
    		s = conn.prepareStatement ("SELECT * FROM oracle_bans WHERE player = ? AND unbanned = 0 ORDER BY id DESC LIMIT 1");
    		s.setString(1, username);
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