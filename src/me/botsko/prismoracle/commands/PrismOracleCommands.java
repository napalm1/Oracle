package me.botsko.prismoracle.commands;

import me.botsko.prism.commandlibs.CallInfo;
import me.botsko.prism.commandlibs.SubHandler;
import me.botsko.prism.commandlibs.Executor;
import me.botsko.prismoracle.PrismOracle;

public class PrismOracleCommands extends Executor {

	
	/**
	 * 
	 * @param prism
	 */
	public PrismOracleCommands(PrismOracle prism) {
		super( prism, "command", "prismoracle" );
		setupCommands();
	}
	

	/**
	 * 
	 */
	private void setupCommands() {
		
		final PrismOracle oracle = (PrismOracle) plugin;

		/**
		 * /seen
		 */
		addSub("seen", "prismoracle.seen")
		.setUsage("(username)")
		.setDescription("View join and last seen dates for players")
		.setHandler(new SeenCommand( oracle ));
		
		
		/**
		 * /oracle reload
		 */
		addSub("reload", "prismoracle.reload")
		.allowConsole()
		.setDescription("Reloads the configuration files.")
		.setHandler(new SubHandler() {
            public void handle(CallInfo call) {
            	oracle.reloadConfig();
            	oracle.config = oracle.getConfig();
				call.getSender().sendMessage( oracle.messenger.playerMsg("Configuration reloaded successfully.") );
            }
		});
	}
}