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
		 * /ison
		 */
		addSub("ison", "oracle.ison")
		.allowConsole()
		.setHandler(new IsonCommand( oracle ));
		
		/**
		 * /played
		 */
		addSub("played", "oracle.played")
		.allowConsole()
		.setHandler(new PlayedCommand( oracle ));
		
		/**
		 * /playhist
		 */
		addSub("playhist", "oracle.played")
		.allowConsole()
		.setHandler(new PlayhistCommand());
		
		/**
		 * /seen
		 */
		addSub("seen", "oracle.seen")
		.allowConsole()
		.setHandler(new SeenCommand( oracle ));
		
		/**
		 * /stats
		 */
		addSub("stats", "oracle.stats")
		.allowConsole()
		.setHandler(new StatsCommand());
		
		/**
		 * /unban
		 */
		addSub("unban", "oracle.unban")
		.allowConsole()
		.setHandler(new UnbanCommand());
		
		/**
		 * /unban
		 */
		addSub("warnings", "oracle.warnings")
		.allowConsole()
		.setHandler(new WarningsCommand());
		
		
		/**
		 * /oracle reload
		 */
		addSub("reload", "oracle.reload")
		.allowConsole()
		.setHandler(new SubHandler() {
            public void handle(CallInfo call) {
            	oracle.reloadConfig();
            	oracle.config = oracle.getConfig();
				call.getSender().sendMessage( Oracle.messenger.playerMsg("Configuration reloaded successfully.") );
            }
		});
	}
}