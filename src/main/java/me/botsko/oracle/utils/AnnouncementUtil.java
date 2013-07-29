package me.botsko.oracle.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import me.botsko.oracle.Oracle;

import org.bukkit.ChatColor;

public class AnnouncementUtil {

	
	/**
	 * 
	 * @param person
	 * @param account_name
	 */
	public static List<String> getActiveAnnouncements(){
		ArrayList<String> announces = new ArrayList<String>();
		Connection conn = null;
		PreparedStatement s = null;
		try {
			conn = Oracle.dbc();
	        
    		s = conn.prepareStatement ("SELECT message FROM oracle_announcements WHERE is_active = 1");
    		s.executeQuery();
    		ResultSet rs = s.getResultSet();

    		while(rs.next()){
    			String msg = ChatColor.GOLD + "["+rs.getString("type")+"]: " + ChatColor.RED + rs.getString("message");
    			announces.add(msg);
			}
    		rs.close();
    		
//    		// pull forum announcements
//    		s = conn.prepareStatement ("SELECT * FROM posts WHERE category_id = 9 AND announcement = 1 AND closed = 0 AND hidden = 0");
//    		s.executeQuery();
//    		rs = s.getResultSet();
//    		while(rs.next()){
//    			String msg = ChatColor.GOLD + "[forums]: " + ChatColor.RED + rs.getString("title") + " http://dhmc.us/r/"+rs.getString("id")+"";
//    			announces.add(msg);
//			}
	        
        } catch (SQLException e){
            e.printStackTrace();
        } finally {
        	if(s != null) try { s.close(); } catch (SQLException e) {}
        	if(conn != null) try { conn.close(); } catch (SQLException e) {}
        }
		return announces;
	}
}