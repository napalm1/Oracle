package me.botsko.oracle.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.bukkit.entity.Player;

import me.botsko.oracle.Oracle;

public class JoinUtil {
	
	
	/**
	 * 
	 */
	public static int lookupPlayer( Player player ){
		
		// Look at cache first
		if( Oracle.oraclePlayers.containsKey(player) ){
			return Oracle.oraclePlayers.get(player);
		}

		Connection conn = null;
		PreparedStatement s = null;
		ResultSet rs = null;
		try {

			conn = Oracle.dbc();
    		s = conn.prepareStatement( "SELECT player_id FROM oracle_players WHERE player = ?" );
    		s.setString(1, player.getName());
    		rs = s.executeQuery();

    		if( rs.next() ){
    			return rs.getInt("player_id");
    		} else {
    			return registerPlayer( player );
    		}
		} catch (SQLException e) {
//        	handleDatabaseException( e );
        } finally {
        	if(rs != null) try { rs.close(); } catch (SQLException e) {}
        	if(s != null) try { s.close(); } catch (SQLException e) {}
        	if(conn != null) try { conn.close(); } catch (SQLException e) {}
        }
		return 0;
	}
	
	
	/**
	 * Saves a player name to the database, and adds the id to the cache hashmap
	 */
	public static int registerPlayer( Player player ){
		
		Connection conn = null;
		PreparedStatement s = null;
		ResultSet rs = null;
		try {

			conn = Oracle.dbc();
            s = conn.prepareStatement( "INSERT INTO oracle_players (player) VALUES (?)" , Statement.RETURN_GENERATED_KEYS);
            s.setString(1, player.getName());
            s.executeUpdate();
            
            rs = s.getGeneratedKeys();
            if (rs.next()) {
            	return rs.getInt(1);
            } else {
                throw new SQLException("Insert statement failed - no generated key obtained.");
            }
		} catch (SQLException e) {
        	
        } finally {
        	if(rs != null) try { rs.close(); } catch (SQLException e) {}
        	if(s != null) try { s.close(); } catch (SQLException e) {}
        	if(conn != null) try { conn.close(); } catch (SQLException e) {}
        }
		return 0;
	}
	
	
	/**
	 * 
	 */
	public static int lookupIp( String ip ){

		Connection conn = null;
		PreparedStatement s = null;
		ResultSet rs = null;
		try {

			conn = Oracle.dbc();
    		s = conn.prepareStatement( "SELECT ip_id FROM oracle_ips WHERE ip = ?" );
    		s.setString(1, ip);
    		rs = s.executeQuery();

    		if( rs.next() ){
    			return rs.getInt("ip_id");
    		} else {
    			return registerIp( ip );
    		}
		} catch (SQLException e) {
//        	handleDatabaseException( e );
        } finally {
        	if(rs != null) try { rs.close(); } catch (SQLException e) {}
        	if(s != null) try { s.close(); } catch (SQLException e) {}
        	if(conn != null) try { conn.close(); } catch (SQLException e) {}
        }
		return 0;
	}
	
	
	/**
	 * Saves a player name to the database, and adds the id to the cache hashmap
	 */
	public static int registerIp( String ip ){
		
		Connection conn = null;
		PreparedStatement s = null;
		ResultSet rs = null;
		try {

			conn = Oracle.dbc();
            s = conn.prepareStatement( "INSERT INTO oracle_ips (ip) VALUES (?)" , Statement.RETURN_GENERATED_KEYS);
            s.setString(1, ip);
            s.executeUpdate();
            
            rs = s.getGeneratedKeys();
            if (rs.next()) {
            	return rs.getInt(1);
            } else {
                throw new SQLException("Insert statement failed - no generated key obtained.");
            }
		} catch (SQLException e) {
        	
        } finally {
        	if(rs != null) try { rs.close(); } catch (SQLException e) {}
        	if(s != null) try { s.close(); } catch (SQLException e) {}
        	if(conn != null) try { conn.close(); } catch (SQLException e) {}
        }
		return 0;
	}
	
	
	/**
	 * 
	 * @param person
	 * @param account_name
	 */
	public static void registerPlayerJoin( Player player, int online_count ){
		Connection conn = null;
		PreparedStatement s = null;
		try {
			
			final String ip = player.getAddress().getAddress().getHostAddress().toString();
			
			// Insert/Get IP ID
			int ip_id = lookupIp( ip );
			
			// Insert/Get Player ID
			int player_id = lookupPlayer( player );
			
			// Cache player id
			Oracle.oraclePlayers.put( player, player_id );
			
			conn = Oracle.dbc();
	        s = conn.prepareStatement("INSERT INTO oracle_joins (player_count,player_id,player_join,ip_id) VALUES (?,?,?,?)");
	        s.setInt(1, online_count);
	        s.setInt(2, player_id);
	        s.setLong(3, System.currentTimeMillis() / 1000L);
	        s.setInt(4, ip_id);
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
	public static void setPlayerSessionIp( Player player, String ip ){
		Connection conn = null;
		PreparedStatement s = null;
		try {
			
			// Insert/Get IP ID
			int ip_id = lookupIp( ip );
			
			// Insert/Get Player ID
			int player_id = lookupPlayer( player );
			
			conn = Oracle.dbc();
	        s = conn.prepareStatement("UPDATE oracle_joins SET ip_id = ? WHERE player_quit IS NULL AND pplayer_id = ?");
	        s.setInt(1, ip_id);
	        s.setInt(2, player_id);
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
	public static void registerPlayerQuit( Player player ){
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet trs = null;
		PreparedStatement pstmt1 = null;
		try {
			
			conn = Oracle.dbc();
			
			// Set the quit date for the players join session
			pstmt = conn.prepareStatement("UPDATE oracle_joins SET player_quit = ? WHERE player_quit IS NULL AND player = ?");
			pstmt.setLong(1, System.currentTimeMillis() / 1000L);
			pstmt.setString(2, player.getName());
			pstmt.executeUpdate();
  
			// Update playtime
			pstmt = conn.prepareStatement("UPDATE oracle_joins SET playtime = (player_quit - player_join) WHERE player = ? AND playtime IS NULL");
			pstmt.setString(1, player.getName());
			pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
        	if(pstmt1 != null) try { pstmt1.close(); } catch (SQLException e) {}
        	if(trs != null) try { trs.close(); } catch (SQLException e) {}
        	if(pstmt != null) try { pstmt.close(); } catch (SQLException e) {}
        	if(conn != null) try { conn.close(); } catch (SQLException e) {}
        }
		
		// Remove id from cache
		if( Oracle.oraclePlayers.containsKey(player) ){
			Oracle.oraclePlayers.remove(player);
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
		try {

			conn = Oracle.dbc();
			
			// Ensure we ignore online players
			if(!users.isEmpty()){
				users = " AND player_id NOT IN ("+users+")";
			}
           
			// Log as having quit
	        s = conn.prepareStatement( "UPDATE oracle_joins SET player_quit = ? WHERE player_quit IS NULL"+users );
	        s.setLong(1, System.currentTimeMillis() / 1000L);
    		s.executeUpdate();
    		
    		// Update playtime
			s = conn.prepareStatement("UPDATE oracle_joins SET playtime = (player_quit - player_join) WHERE playtime IS NULL"+users);
			s.executeUpdate();
    		
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
        	if(trs != null) try { trs.close(); } catch (SQLException e) {}
        	if(s != null) try { s.close(); } catch (SQLException e) {}
        	if(conn != null) try { conn.close(); } catch (SQLException e) {}
        }
	}
}