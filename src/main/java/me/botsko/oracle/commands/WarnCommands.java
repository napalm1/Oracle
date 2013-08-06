package me.botsko.oracle.commands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import me.botsko.oracle.Oracle;
import me.botsko.oracle.commandlibs.CallInfo;
import me.botsko.oracle.commandlibs.Executor;
import me.botsko.oracle.commandlibs.SubHandler;
import me.botsko.oracle.utils.WarningUtil;

public class WarnCommands extends Executor {
	
	/**
	 * 
	 * @param prism
	 */
	public WarnCommands(Oracle oracle) {
		super( oracle, "command", "warn" );
		setupCommands();
	}
	

	/**
	 * 
	 */
	private void setupCommands() {
		

		/**
		 * /warn delete [id]
		 */
		addSub( new String[]{"delete"}, "oracle.warn")
		.allowConsole()
		.setMinArgs(1)
		.setHandler(new SubHandler() {
            public void handle(CallInfo call) {
            	// delete the warning
				WarningUtil.deleteWarning( Integer.parseInt( call.getArg(0) ) );
				call.getSender().sendMessage( Oracle.messenger.playerMsg("Warning deleted successfully."));
				return;
            }
		});
		
		
		/**
		 * /warn [username] [msg]
		 */
		addSub( new String[]{"default"}, "oracle.warn")
		.allowConsole()
		.setMinArgs(1)
		.setHandler(new SubHandler() {
            public void handle(CallInfo call) {
            	
            	if(call.getArgs().length >= 2){
					
					String reason = "";
					for (int i = 1; i < call.getArgs().length; i = i + 1){
						reason += call.getArgs()[i]+" ";
					}
					
					String warned_by = "console";
					if( call.getPlayer() != null ){
						warned_by = call.getPlayer().getName();
					}
					
					call.getSender().sendMessage( Oracle.messenger.playerMsg("Warning file successfully."));
					
					fileWarning( call.getArg(0), reason, warned_by);
					
					// This may be a third warning!
					WarningUtil.alertStaffOnWarnLimit( call.getArg(0) );

				}       	
            }
		});
	}
	
	
	/**
	 * Handle the command
	 */
	public void handle(CallInfo call) {
		

    
	}
	
	
	/**
	 * 
	 * @param username
	 * @param reason
	 * @param reporter
	 */
	protected void fileWarning(String username, String reason, String reporter){
		
		WarningUtil.fileWarning( username, reason, reporter);
		
		for(Player pl: Bukkit.getServer().getOnlinePlayers()) {
			if(username.equalsIgnoreCase(pl.getName())){
				pl.sendMessage( Oracle.messenger.playerError("=== OFFICIAL WARNING FILED FOR YOU ===") );
				pl.sendMessage( Oracle.messenger.playerMsg(reason) );
				pl.sendMessage( Oracle.messenger.playerError("Three warnings will result in a ban!") );
			}
		}
	}
}