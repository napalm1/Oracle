package me.botsko.prismoracle.commands;

import me.botsko.prism.Prism;
import me.botsko.prism.commandlibs.CallInfo;
import me.botsko.prism.commandlibs.Executor;
import me.botsko.prism.commandlibs.SubHandler;

public class PrismOracleCommands extends Executor {

	
	/**
	 * 
	 * @param prism
	 */
	public PrismOracleCommands(Prism prism) {
		super(prism);
		setupCommands();
	}
	

	/**
	 * 
	 */
	private void setupCommands() {
		

		/**
		 * /prism lookup 
		 */
		addSub("lookup", "prism.lookup")
		.setMinArgs(1)
		.setUsage("(params)")
		.setDescription("Search for actions.")
		.addAlias("l")
		.setHandler(new SeenCommand(plugin));
		
		
		/**
		 * /prism reload
		 */
		addSub("reload", "prismoracle.reload")
		.allowConsole()
		.setDescription("Reloads the configuration files.")
		.setHandler(new SubHandler() {
            public void handle(CallInfo call) {
            	plugin.reloadConfig();
				plugin.config = plugin.getConfig();
				call.getSender().sendMessage( plugin.playerMsg("Configuration reloaded successfully.") );
            }
		});
	}
}