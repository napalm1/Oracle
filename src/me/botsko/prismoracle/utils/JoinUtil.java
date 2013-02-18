package me.botsko.prismoracle.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import me.botsko.prism.Prism;

public class JoinUtil {
	
	
	/**
	 * 
	 * @param person
	 * @param account_name
	 */
	public static void registerPlayerJoin( String player, String ip, int online_count ){
		try {
			Connection conn = Prism.dbc();
	        PreparedStatement s = conn.prepareStatement("INSERT INTO prism_oracle_joins (player_count,player,player_join,ip) VALUES (?,?,now(),?)");
	        s.setInt(1, online_count);
	        s.setString(2, player);
	        s.setString(3, ip);
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
	public static void registerPlayerQuit( String player ){
		try {
			
			Connection conn = Prism.dbc();
			
			// Set the quit date for the players join session
			PreparedStatement pstmt = conn.prepareStatement("UPDATE prism_oracle_joins SET player_quit = now() WHERE player_quit IS NULL AND player = ?");
			pstmt.setString(1, player);
			pstmt.executeUpdate();
  
			// Find all join sessions we must calc playtime for
			pstmt = conn.prepareStatement ("SELECT id, TIME_TO_SEC(TIMEDIFF(player_quit,player_join)) FROM prism_oracle_joins WHERE player = ? AND playtime IS NULL");
			pstmt.setString(1, player);
			pstmt.executeQuery();
			ResultSet trs = pstmt.getResultSet();
 				
			while( trs.next() ){
				PreparedStatement pstmt1 = conn.prepareStatement("UPDATE prism_oracle_joins SET playtime = ? WHERE id = ?");
				pstmt1.setInt(1, trs.getInt(2)); // playtime
				pstmt1.setInt(2, trs.getInt(1)); // id
				pstmt1.executeUpdate();
				pstmt1.close();
			}
			
			trs.close();
			pstmt.close();
    		conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
	}
	
	
	/**
	 * 
	 * @param person
	 * @param account_name
	 */
	public static void forceDateForOfflinePlayers( Prism prism, String users ){
		try {

			Connection conn = Prism.dbc();
			
			// Ensure we ignore online players
			if(!users.isEmpty()){
				users = " AND player NOT IN ("+users+")";
			}
           
			// Log as having quit
	        PreparedStatement s = conn.prepareStatement( "UPDATE prism_oracle_joins SET player_quit = now() WHERE player_quit IS NULL"+users );
    		s.executeUpdate();
    		
    		// Update playtime
	        s = conn.prepareStatement( "SELECT id, TIME_TO_SEC(TIMEDIFF(player_quit,player_join)) FROM prism_oracle_joins WHERE playtime IS NULL"+users );
    		s.executeQuery();
			ResultSet trs = s.getResultSet();
			while( trs.next() ){
				PreparedStatement pstmt1 = conn.prepareStatement("UPDATE prism_oracle_joins SET playtime = ? WHERE id = ?");
				pstmt1.setInt(1, trs.getInt(2)); // playtime
				pstmt1.setInt(2, trs.getInt(1)); // id
				pstmt1.executeUpdate();
				pstmt1.close();
			}
			trs.close();

			// Close
    		s.close();
    		conn.close();
    		
        } catch (SQLException e) {
            e.printStackTrace();
        }
	}
}