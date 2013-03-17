package me.botsko.oracle.commands;

import me.botsko.oracle.Oracle;
import me.botsko.oracle.commandlibs.CallInfo;
import me.botsko.oracle.commandlibs.Executor;
import me.botsko.oracle.commandlibs.SubHandler;

public class OracleCommands extends Executor {

	
	/**
	 * 
	 * @param oracle
	 */
	public OracleCommands(Oracle oracle) {
		super( oracle, "command", "oracle" );
		setupCommands();
	}
	

	/**
	 * 
	 */
	private void setupCommands() {
		
		final Oracle oracle = (Oracle) plugin;

		
		/**
		 * /ban
		 */
		addSub("ban", "oracle.ban")
		.allowConsole()
		.setHandler(new BanCommand( oracle ));
		
		
		/**
		 * /seen
		 */
		addSub("seen", "oracle.seen")
		.allowConsole()
		.setHandler(new SeenCommand( oracle ));
		
		
		/**
		 * /unban
		 */
		addSub("unban", "oracle.unban")
		.allowConsole()
		.setHandler(new UnbanCommand( oracle ));
		
		
		/**
		 * /oracle reload
		 */
		addSub("reload", "oracle.reload")
		.allowConsole()
		.setHandler(new SubHandler() {
            public void handle(CallInfo call) {
            	oracle.reloadConfig();
            	oracle.config = oracle.getConfig();
				call.getSender().sendMessage( oracle.messenger.playerMsg("Configuration reloaded successfully.") );
            }
		});
	}
}