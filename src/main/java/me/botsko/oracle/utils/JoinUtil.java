package me.botsko.oracle.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import me.botsko.oracle.Oracle;
import me.botsko.oracle.events.OracleFirstTimePlayerEvent;

public class JoinUtil {
	
	
	/**
	 * 
	 */
	public static int lookupPlayer( OfflinePlayer player ){
		
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
	protected static int registerPlayer( OfflinePlayer player ){
		
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
	protected static int lookupIp( String ip ){

		Connection conn = null;
		PreparedStatement s = null;
		ResultSet rs = null;
		try {

			conn = Oracle.dbc();
    		s = conn.prepareStatement( "SELECT ip_id FROM oracle_ips WHERE ip = INET_ATON(?)" );
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
	protected static int registerIp( String ip ){
		
		Connection conn = null;
		PreparedStatement s = null;
		ResultSet rs = null;
		try {

			conn = Oracle.dbc();
            s = conn.prepareStatement( "INSERT INTO oracle_ips (ip) VALUES (INET_ATON(?))" , Statement.RETURN_GENERATED_KEYS);
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
	 * Creates a join record for this player, and stores the player username/ip
	 * to the appropriate tables.
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
			if( player_id == 0 ){
				
				// Throw event as this is a new player
				OracleFirstTimePlayerEvent event = new OracleFirstTimePlayerEvent( player );
				Bukkit.getServer().getPluginManager().callEvent( event );
				
				player_id = registerPlayer( player );
				
			}
			
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
	 * Updates a current play session IP if received separately from the join event.
	 * (Primarily for BungeeCord use)
	 * @param person
	 * @param account_name
	 * @throws Exception 
	 */
	public static void setPlayerSessionIp( Player player, String ip ) throws Exception{
		Connection conn = null;
		PreparedStatement s = null;
		try {
			
			// Insert/Get IP ID
			int ip_id = lookupIp( ip );
			
			// Insert/Get Player ID
			int player_id = lookupPlayer( player );
			if( player_id == 0 ){
				throw new Exception("Could not find joins for this player.");
			}
			
			conn = Oracle.dbc();
	        s = conn.prepareStatement("UPDATE oracle_joins SET ip_id = ? WHERE player_quit IS NULL AND player_id = ?");
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
	 * End a play session for the player
	 * @param person
	 * @param account_name
	 * @throws Exception 
	 */
	public static void registerPlayerQuit( Player player ) throws Exception{
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet trs = null;
		PreparedStatement pstmt1 = null;
		try {
			
			// Insert/Get Player ID
			int player_id = lookupPlayer( player );
			if( player_id == 0 ){
				throw new Exception("Could not find joins for this player.");
			}
			
			conn = Oracle.dbc();
			
			// Set the quit date for the players join session
			pstmt = conn.prepareStatement("UPDATE oracle_joins SET player_quit = ? WHERE player_quit IS NULL AND player_id = ?");
			pstmt.setLong(1, System.currentTimeMillis() / 1000L);
			pstmt.setInt(2, player_id);
			pstmt.executeUpdate();
  
			// Update playtime
			pstmt = conn.prepareStatement("UPDATE oracle_joins SET playtime = (player_quit - player_join) WHERE player_id = ? AND playtime IS NULL");
			pstmt.setInt(1, player_id);
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
	
	
	/**
	 * 
	 * @param person
	 * @param account_name
	 */
	public static void forceDateForAllPlayers(){
		Connection conn = null;
		PreparedStatement s = null;
		ResultSet trs = null;
		try {

			conn = Oracle.dbc();
           
			// Log as having quit
	        s = conn.prepareStatement( "UPDATE oracle_joins SET player_quit = ? WHERE player_quit IS NULL" );
	        s.setLong(1, System.currentTimeMillis() / 1000L);
    		s.executeUpdate();
    		
    		// Update playtime
			s = conn.prepareStatement("UPDATE oracle_joins SET playtime = (player_quit - player_join) WHERE playtime IS NULL");
			s.executeUpdate();
    		
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
        	if(trs != null) try { trs.close(); } catch (SQLException e) {}
        	if(s != null) try { s.close(); } catch (SQLException e) {}
        	if(conn != null) try { conn.close(); } catch (SQLException e) {}
        }
	}
	
	
	/**
	 * 
	 * @param person
	 * @param account_name
	 * @throws Exception 
	 */
	public static List<Alt> getPlayerAlts( OfflinePlayer player ) throws Exception{
		ArrayList<Alt> accounts = new ArrayList<Alt>();
		Connection conn = null;
		PreparedStatement s = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		try {
			
			// Insert/Get Player ID
			int player_id = lookupPlayer( player );
			if( player_id == 0 ){
				throw new Exception("Could not find joins for this player.");
			}
            
			conn = Oracle.dbc();
			
			// Pull a list of all unique IPs this player has used
    		s = conn.prepareStatement ("SELECT ip, ip_id FROM oracle_joins j LEFT JOIN oracle_ips i ON i.ip_id = j.ip_id WHERE player_id = ? GROUP BY j.ip_id");
    		s.setInt(1, player_id);
    		s.executeQuery();
    		rs = s.getResultSet();
    		
    		while(rs.next()){

        		s = conn.prepareStatement ("SELECT p.player FROM oracle_joins j" +
        				"LEFT JOIN oracle_players p ON p.player_id = j.player_id" +
        				"WHERE ip_id = ? AND j.player_id != ?" +
        				"GROUP BY j.player_id" +
        				"ORDER BY p.player");
        		s.setInt(1, rs.getInt("ip_id"));
        		s.setInt(2, player_id);
        		s.executeQuery();
        		rs1 = s.getResultSet();
	        	while(rs1.next()){
	    			accounts.add( new Alt(rs.getString("ip"), rs1.getString("player")) );
				}
    		}   
		} catch (SQLException e) {
            e.printStackTrace();
        } finally {
        	if(rs1 != null) try { rs1.close(); } catch (SQLException e) {}
        	if(rs != null) try { rs.close(); } catch (SQLException e) {}
        	if(s != null) try { s.close(); } catch (SQLException e) {}
        	if(conn != null) try { conn.close(); } catch (SQLException e) {}
        }
		return accounts;
	}
}