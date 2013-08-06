package me.botsko.oracle.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;

import me.botsko.oracle.Oracle;

public class PlaytimeUtil {

	
	/**
	 * 
	 * @param person
	 * @param account_name
	 * @throws ParseException 
	 */
	public static Playtime getPlaytime( String username ) {
		Playtime playtime = null;
		Connection conn = null;
		PreparedStatement s = null;
		try {
			
			conn = Oracle.dbc();
			
			s = conn.prepareStatement ("SELECT SUM(playtime) as playtime FROM oracle_joins j LEFT JOIN oracle_players p ON p.player_id = j.player_id WHERE p.player = ?");
			s.setString(1, username);
			s.executeQuery();
			ResultSet rs = s.getResultSet();
	
			rs.first();
			int before_current = rs.getInt(1);
			
			// We also need to pull any incomplete join and calc up-to-the-minute playtime
			s = conn.prepareStatement ("SELECT player_join FROM oracle_joins j LEFT JOIN oracle_players p ON p.player_id = j.player_id WHERE p.player = ? AND player_quit IS NULL");
			s.setString(1, username);
			s.executeQuery();
			rs = s.getResultSet();
			
			long session_hours = 0;
			try {
				if(rs.first()){
			    	Date joined = new Date(rs.getLong("player_join") * 1000);
			    	Date today = new Date();
			    	session_hours = today.getTime() - joined.getTime();
			    	session_hours = session_hours / 1000;
				}
			}
			catch ( SQLException e ) {
				e.printStackTrace();
			}
			
			playtime = new Playtime( (int) (before_current + session_hours) );

		} catch (SQLException e){
            e.printStackTrace();
        } finally {
        	if(s != null) try { s.close(); } catch (SQLException e) {}
        	if(conn != null) try { conn.close(); } catch (SQLException e) {}
        }
		return playtime;
	}
	
	
//	/**
//	 * 
//	 * @param person
//	 * @param account_name
//	 */
//	public static HashMap<Playtime,String> getPlayerPlaytimeHistory( String username ){
//		Connection conn = null;
//		PreparedStatement s = null;
//		try {
//            
//			conn = Oracle.dbc();
//			
//    		s = conn.prepareStatement ("SELECT DATE_FORMAT(player_join,'%Y-%m-%d') as playdate, SUM(playtime) as playtime FROM oracle_joins WHERE player = ? GROUP BY DATE_FORMAT(player_join,'%Y-%m-%d') ORDER BY player_join DESC LIMIT 7;");
//    		s.setString(1, username);
//    		s.executeQuery();
//    		ResultSet rs = s.getResultSet();
//
//    		HashMap<Playtime,String> scores = new HashMap<Playtime, String>();
//    		while(rs.next()){
//    			scores.put( new Playtime(rs.getInt("playtime")), rs.getString("playdate") );
//			}
//		} catch (SQLException e){
//            e.printStackTrace();
//        } finally {
//        	if(s != null) try { s.close(); } catch (SQLException e) {}
//        	if(conn != null) try { conn.close(); } catch (SQLException e) {}
//        }
//		return null;
//	}
}
