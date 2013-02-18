package me.botsko.prismoracle.commands;

import me.botsko.prism.commandlibs.CallInfo;
import me.botsko.prism.commandlibs.SubHandler;
import me.botsko.prismoracle.PrismOracle;
import me.botsko.prismoracle.commandlibs.Executor;

public class PrismOracleCommands extends Executor {

	
	/**
	 * 
	 * @param prism
	 */
	public PrismOracleCommands(PrismOracle prism) {
		super(prism);
		setupCommands();
	}
	

	/**
	 * 
	 */
	private void setupCommands() {
		

		/**
		 * /seen
		 */
		addSub("seen", "prismoracle.seen")
		.setUsage("(username)")
		.setDescription("View join and last seen dates for players")
		.setHandler(new SeenCommand(plugin));
		
		
		/**
		 * /oracle reload
		 */
		addSub("reload", "prismoracle.reload")
		.allowConsole()
		.setDescription("Reloads the configuration files.")
		.setHandler(new SubHandler() {
            public void handle(CallInfo call) {
            	plugin.reloadConfig();
				plugin.config = plugin.getConfig();
				call.getSender().sendMessage( plugin.messenger.playerMsg("Configuration reloaded successfully.") );
            }
		});
	}
}