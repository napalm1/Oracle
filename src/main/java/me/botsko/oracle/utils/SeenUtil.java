package me.botsko.oracle.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;

import me.botsko.oracle.Oracle;

public class SeenUtil {
	
	
	/**
	 * 
	 * @param person
	 * @param account_name
	 * @throws ParseException 
	 */
	public static boolean hasPlayerBeenSeen( String username ) throws ParseException{
		boolean seen = false;
		Connection conn = null;
		PreparedStatement s = null;
		ResultSet rs = null;
		try {
			
			conn = Oracle.dbc();
    		s = conn.prepareStatement ("SELECT join_id FROM oracle_joins j LEFT JOIN oracle_players p ON p.player_id = j.player_id WHERE p.player = ? ORDER BY player_join LIMIT 1;");
    		s.setString(1, username);
    		s.executeQuery();
    		rs = s.getResultSet();
    		
    		if(rs.first()){
    			seen = true;
    		}
            
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
        	if(rs != null) try { rs.close(); } catch (SQLException e) {}
        	if(s != null) try { s.close(); } catch (SQLException e) {}
        	if(conn != null) try { conn.close(); } catch (SQLException e) {}
        }
		return seen;
	}
	
	
	/**
	 * 
	 * @param person
	 * @param account_name
	 * @throws ParseException 
	 */
	public static Date getPlayerFirstSeen( String username ) throws ParseException{
		Date joined = null;
		Connection conn = null;
		PreparedStatement s = null;
		ResultSet rs = null;
		try {
			
			conn = Oracle.dbc();
    		s = conn.prepareStatement ("SELECT player_join FROM oracle_joins j LEFT JOIN oracle_players p ON p.player_id = j.player_id WHERE p.player = ? ORDER BY player_join LIMIT 1;");
    		s.setString(1, username);
    		s.executeQuery();
    		rs = s.getResultSet();
    		
    		if(rs.first()){
	        	joined = new Date(rs.getLong("player_join") * 1000);
    		}
            
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
        	if(rs != null) try { rs.close(); } catch (SQLException e) {}
        	if(s != null) try { s.close(); } catch (SQLException e) {}
        	if(conn != null) try { conn.close(); } catch (SQLException e) {}
        }
		return joined;
	}
	
	
	/**
	 * 
	 * @param person
	 * @param account_name
	 * @throws ParseException 
	 */
	public static Date getPlayerLastSeen( String username ) throws ParseException{
		Date seen = null;
		Connection conn = null;
		PreparedStatement s = null;
		ResultSet rs = null;
		try {
			
			conn = Oracle.dbc();
    		s = conn.prepareStatement ("SELECT player_quit FROM oracle_joins j LEFT JOIN oracle_players p ON p.player_id = j.player_id WHERE p.player = ? AND player_quit IS NOT NULL ORDER BY player_quit DESC LIMIT 1;");
    		s.setString(1, username);
    		s.executeQuery();
    		rs = s.getResultSet();
    		
    		if(rs.first()){
	        	seen = new Date(rs.getLong("player_quit") * 1000);
    		}
    		
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
        	if(rs != null) try { rs.close(); } catch (SQLException e) {}
        	if(s != null) try { s.close(); } catch (SQLException e) {}
        	if(conn != null) try { conn.close(); } catch (SQLException e) {}
        }
		return seen;
	}
}