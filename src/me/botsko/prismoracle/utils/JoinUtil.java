package me.botsko.prismoracle.utils;

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
	public static void registerPlayerJoin( Prism prism, String username, String ip, int online_count ){
		try {
			prism.dbc();
	        PreparedStatement s = prism.conn.prepareStatement("INSERT INTO prism_oracle_joins (player_count,player,player_join,ip) VALUES (?,?,now(),?)");
	        s.setInt(1, online_count);
	        s.setString(2, username);
	        s.setString(3, ip);
	        s.executeUpdate();
    		s.close();
    		prism.conn.close();
        } catch (SQLException e){
            e.printStackTrace();
        }
	}
	
	
	/**
	 * 
	 * @param person
	 * @param account_name
	 */
	public static void registerPlayerQuit( Prism prism, String username ){
		try {
			
			prism.dbc();
			
			// Set the quit date for the players join session
			PreparedStatement pstmt = prism.conn.prepareStatement("UPDATE prism_oracle_joins SET player_quit = now() WHERE player_quit IS NULL AND player = ?");
			pstmt.setString(1, username);
			pstmt.executeUpdate();
  
			// Find all join sessions we must calc playtime for
			pstmt = prism.conn.prepareStatement ("SELECT id, TIME_TO_SEC(TIMEDIFF(player_quit,player_join)) FROM prism_oracle_joins WHERE player = ? AND playtime IS NULL");
			pstmt.setString(1, username);
			pstmt.executeQuery();
			ResultSet trs = pstmt.getResultSet();
 				
			while( trs.next() ){
				PreparedStatement pstmt1 = prism.conn.prepareStatement("UPDATE prism_oracle_joins SET playtime = ? WHERE id = ?");
				pstmt1.setInt(1, trs.getInt(2)); // playtime
				pstmt1.setInt(2, trs.getInt(1)); // id
				pstmt1.executeUpdate();
				pstmt1.close();
			}
			
			trs.close();
			pstmt.close();
    		prism.conn.close();
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

			prism.dbc();
			
			// Ensure we ignore online players
			if(!users.isEmpty()){
				users = " AND username NOT IN ("+users+")";
			}
           
			// Log as having quit
	        PreparedStatement s = prism.conn.prepareStatement( "UPDATE prism_oracle_joins SET player_quit = now() WHERE player_quit IS NULL"+users );
    		s.executeUpdate();
    		
    		// Update playtime
	        s = prism.conn.prepareStatement( "SELECT id, TIME_TO_SEC(TIMEDIFF(player_quit,player_join)) FROM prism_oracle_joins WHERE playtime IS NULL"+users );
    		s.executeQuery();
			ResultSet trs = s.getResultSet();
			while( trs.next() ){
				PreparedStatement pstmt1 = prism.conn.prepareStatement("UPDATE prism_oracle_joins SET playtime = ? WHERE id = ?");
				pstmt1.setInt(1, trs.getInt(2)); // playtime
				pstmt1.setInt(2, trs.getInt(1)); // id
				pstmt1.executeUpdate();
				pstmt1.close();
			}
			trs.close();

			// Close
    		s.close();
    		prism.conn.close();
    		
        } catch (SQLException e) {
            e.printStackTrace();
        }
	}
}