package me.botsko.oracle.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import me.botsko.oracle.Oracle;

public class JoinUtil {
	
	
	/**
	 * 
	 * @param person
	 * @param account_name
	 */
	public static void registerPlayerJoin( String player, String ip, int online_count ){
		Connection conn = null;
		PreparedStatement s = null;
		try {
			conn = Oracle.dbc();
	        s = conn.prepareStatement("INSERT INTO oracle_joins (player_count,player,player_join,ip) VALUES (?,?,now(),?)");
	        s.setInt(1, online_count);
	        s.setString(2, player);
	        s.setString(3, ip);
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
	public static void setPlayerSessionIp( String player, String ip ){
		Connection conn = null;
		PreparedStatement s = null;
		try {
			conn = Oracle.dbc();
	        s = conn.prepareStatement("UPDATE oracle_joins SET ip = ? WHERE player_quit IS NULL AND player = ?");
	        s.setString(1, ip);
	        s.setString(2, player);
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
	public static void registerPlayerQuit( String player ){
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet trs = null;
		PreparedStatement pstmt1 = null;
		try {
			
			conn = Oracle.dbc();
			
			// Set the quit date for the players join session
			pstmt = conn.prepareStatement("UPDATE oracle_joins SET player_quit = now() WHERE player_quit IS NULL AND player = ?");
			pstmt.setString(1, player);
			pstmt.executeUpdate();
  
			// Find all join sessions we must calc playtime for
			pstmt = conn.prepareStatement ("SELECT id, TIME_TO_SEC(TIMEDIFF(player_quit,player_join)) FROM oracle_joins WHERE player = ? AND playtime IS NULL");
			pstmt.setString(1, player);
			pstmt.executeQuery();
			trs = pstmt.getResultSet();
 				
			while( trs.next() ){
				pstmt1 = conn.prepareStatement("UPDATE oracle_joins SET playtime = ? WHERE id = ?");
				pstmt1.setInt(1, trs.getInt(2)); // playtime
				pstmt1.setInt(2, trs.getInt(1)); // id
				pstmt1.executeUpdate();
			}

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
        	if(pstmt1 != null) try { pstmt1.close(); } catch (SQLException e) {}
        	if(trs != null) try { trs.close(); } catch (SQLException e) {}
        	if(pstmt != null) try { pstmt.close(); } catch (SQLException e) {}
        	if(conn != null) try { conn.close(); } catch (SQLException e) {}
        }
	}
	
	
	/**
	 * 
	 * @param person
	 * @param account_name
	 */
	public static void forceDateForOfflinePlayers( String users ){
		Connection conn = null;
		PreparedStatement s = null;
		ResultSet trs = null;
		PreparedStatement pstmt1 = null;
		try {

			conn = Oracle.dbc();
			
			// Ensure we ignore online players
			if(!users.isEmpty()){
				users = " AND player NOT IN ("+users+")";
			}
           
			// Log as having quit
	        s = conn.prepareStatement( "UPDATE oracle_joins SET player_quit = now() WHERE player_quit IS NULL"+users );
    		s.executeUpdate();
    		
    		// Update playtime
	        s = conn.prepareStatement( "SELECT id, TIME_TO_SEC(TIMEDIFF(player_quit,player_join)) FROM oracle_joins WHERE playtime IS NULL"+users );
    		s.executeQuery();
			trs = s.getResultSet();
			while( trs.next() ){
				pstmt1 = conn.prepareStatement("UPDATE oracle_joins SET playtime = ? WHERE id = ?");
				pstmt1.setInt(1, trs.getInt(2)); // playtime
				pstmt1.setInt(2, trs.getInt(1)); // id
				pstmt1.executeUpdate();
				pstmt1.close();
			}
    		
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
        	if(pstmt1 != null) try { pstmt1.close(); } catch (SQLException e) {}
        	if(trs != null) try { trs.close(); } catch (SQLException e) {}
        	if(s != null) try { s.close(); } catch (SQLException e) {}
        	if(conn != null) try { conn.close(); } catch (SQLException e) {}
        }
	}
}