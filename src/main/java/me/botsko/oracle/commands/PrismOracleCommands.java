package me.botsko.oracle.commands;

import me.botsko.oracle.Oracle;
import me.botsko.oracle.commandlibs.CallInfo;
import me.botsko.oracle.commandlibs.Executor;
import me.botsko.oracle.commandlibs.SubHandler;

public class PrismOracleCommands extends Executor {

	
	/**
	 * 
	 * @param prism
	 */
	public PrismOracleCommands(Oracle oracle) {
		super( oracle, "command", "oracle" );
		setupCommands();
	}
	

	/**
	 * 
	 */
	private void setupCommands() {
		
		final Oracle oracle = (Oracle) plugin;

		/**
		 * /seen
		 */
		addSub("seen", "prismoracle.seen")
		.setHandler(new SeenCommand( oracle ));
		
		
		/**
		 * /oracle reload
		 */
		addSub("reload", "prismoracle.reload")
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