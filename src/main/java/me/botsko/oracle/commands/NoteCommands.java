package me.botsko.oracle.commands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import me.botsko.oracle.Oracle;
import me.botsko.oracle.commandlibs.CallInfo;
import me.botsko.oracle.commandlibs.Executor;
import me.botsko.oracle.commandlibs.SubHandler;
import me.botsko.oracle.utils.WarningUtil;

public class NotesCommands extends Executor {
	
	/**
	 * 
	 * @param prism
	 */
	public NotesCommands(Oracle oracle) {
		super( oracle, "command", "note" );
		setupCommands();
	}
	

	/**
	 * 
	 */
	private void setupCommands() {
		

		/**
		 * /note delete [id]
		 */
		addSub( new String[]{"delete"}, "oracle.note")
		.allowConsole()
		.setMinArgs(1)
		.setHandler(new SubHandler() {
            public void handle(CallInfo call) {
            	// delete the warning
				WarningUtil.deleteWarning( Integer.parseInt( call.getArg(0) ) );
				call.getSender().sendMessage( Oracle.messenger.playerMsg("Note deleted successfully."));
				return;
            }
		});
		
		
		/**
		 * /note [username] [msg]
		 */
		addSub( new String[]{"default"}, "oracle.note")
		.allowConsole()
		.setMinArgs(1)
		.setHandler(new SubHandler() {
            public void handle(CallInfo call) {
            	
            	if(call.getArgs().length >= 2){
					
					String reason = "";
					for (int i = 1; i < call.getArgs().length; i = i + 1){
						reason += call.getArgs()[i]+" ";
					}
					
					// Find the player whether online or not
					OfflinePlayer warned_player = Bukkit.getPlayer( call.getArg(0) );
					if( warned_player == null ){
						warned_player = Bukkit.getOfflinePlayer( call.getArg(0) );
					}
					
					if( warned_player == null ){
						call.getSender().sendMessage( Oracle.messenger.playerError( "Could not find a player by that name." ) );
						return;
					}

					// File warning
					WarningUtil.fileWarning( warned_player, reason, call.getSender() );
					
					// Alert them
				
					call.getSender().sendMessage( Oracle.messenger.playerMsg("Note filed successfully."));

					

				}       	
            }
		});
	}
}
