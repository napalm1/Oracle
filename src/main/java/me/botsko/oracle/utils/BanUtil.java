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
		try {
			Connection conn = Oracle.dbc();
	        PreparedStatement s = conn.prepareStatement("INSERT INTO oracle_bans (player,moderator,reason) VALUES (?,?,?)");
	        s.setString(1, player);
	        s.setString(2, moderator);
	        s.setString(3, reason);
	        s.executeUpdate();
    		s.close();
    		conn.close();
        } catch (SQLException e){
            e.printStackTrace();
        }
	}
	
	
	/**
	 * 
	 * @param person
	 * @param account_name
	 */
	public static void unbanByUsername( String moderator, String player ){
		try {
			Connection conn = Oracle.dbc();
			
			// Add unban record
	        PreparedStatement s = conn.prepareStatement("INSERT INTO oracle_unbans (player,moderator) VALUES (?,?)");
	        s.setString(1, player);
	        s.setString(2, moderator);
	        s.executeUpdate();
	        
	        // Mark as unbanned
	        s = conn.prepareStatement("UPDATE oracle_bans SET unbanned = 1 WHERE player = ?");
	        s.setString(1, player);
	        s.executeUpdate();
	        
    		s.close();
    		conn.close();
        } catch (SQLException e){
            e.printStackTrace();
        }
	}
	
	
	/**
	 * 
	 * @param username
	 * @throws Exception 
	 */
	public static void playerMayJoin( String username ) throws Exception{
		
		try {
			
			Connection conn = Oracle.dbc();
            
            PreparedStatement s;
    		s = conn.prepareStatement ("SELECT * FROM oracle_bans WHERE player = ? AND unbanned = 0 ORDER BY id DESC LIMIT 1");
    		s.setString(1, username);
    		s.executeQuery();
    		ResultSet rs = s.getResultSet();
    		
    		if(rs.first()){
    			throw new Exception( rs.getString("reason") );
    		}
    		
    		rs.close();
    		s.close();
            conn.close();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
	}
}