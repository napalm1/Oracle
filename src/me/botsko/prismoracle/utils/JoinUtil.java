package me.botsko.prismoracle.utils;

import java.sql.PreparedStatement;
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
	        PreparedStatement s = prism.conn.prepareStatement("INSERT INTO prism_oracle_joins (player_count,username,ip) VALUES (?,?,?)");
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
}