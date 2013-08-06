package me.botsko.oracle.commands;


import org.bukkit.command.CommandSender;

import me.botsko.oracle.commandlibs.CallInfo;
import me.botsko.oracle.commandlibs.SubHandler;

public class PlayerCommand implements SubHandler {
	

	/**
	 * Handle the command
	 */
	public void handle(CallInfo call) {
		
		if ( call.getArgs().length == 2 && call.getArg(0).equalsIgnoreCase("alts")){
			playerAlts( call.getArg(1), call.getSender() );
		} else {

//			String username = call.getSender().getName();
//			if( call.getArgs().length > 0 ){
//				username = plugin.expandName(username);
//			}
//			playerStats( username, call.getSender() );
		}
	}
	
	
//	/**
//     * Returns a bunch of player stats
//     * 
//     * @param username
//     * @throws SQLException 
//     * @throws ParseException 
//     */
//    private void playerStats(String username, CommandSender sender) throws SQLException, ParseException {
//    	
//    	// Expand partials
//    	String tmp = plugin.expandName(username);
//    	if(tmp != null){
//    		username = tmp;
//    	}
//    	
//    
////    	played.checkPlayTime(username, sender);
//    	
//    	
//    }
    
    /**
     * 
     * @param username
     * @param sender
     */
    private void playerAlts(String username, CommandSender sender){
    	
//    	sender.sendMessage( Oracle.messenger.playerHeaderMsg( "Comparing IPs for " + username + " - showing *possible* alt accounts: " ) );
//    	
//    	// Pull all items matching this name
//		if( !username.equalsIgnoreCase("orbital_ecliptic")){
//			List<Alts> alt_accts = JoinUtil.getPlayerAlts( username );
//			if(!alt_accts.isEmpty()){
//				for(Alts alt : alt_accts){
//					try {
//						sender.sendMessage( plugin.playerMsg( "["+ alt.ip + "] " + alt.username ));
//					} catch (ParseException e) {
//						e.printStackTrace();
//					}
//				}
//			}
//    	}
    }
}