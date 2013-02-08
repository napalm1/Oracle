package me.botsko.prismoracle;

import me.botsko.prism.Prism;
import me.botsko.prismoracle.listeners.PrismOraclePlayerListener;
import me.botsko.prismoracle.utils.JoinUtil;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class PrismOracle extends JavaPlugin {

	/**
	 * Protected/private
	 */
	protected String plugin_name;
	protected String plugin_version;
	protected Prism prism;

	
    /**
     * Enables the plugin and activates our player listeners
     */
	@Override
	public void onEnable(){
		
		plugin_name = this.getDescription().getName();
		plugin_version = this.getDescription().getVersion();
		
		// Make sure Prism exists
		checkPluginDependancies();
		
		prism.log("Initializing Prism Oracle " + plugin_version + ". By Viveleroi.");
		
//		if(getConfig().getBoolean("prism.notify-newer-versions")){
//			String notice = UpdateNotification.checkForNewerBuild(plugin_version);
//			if(notice != null){
//				log(notice);
//			}
//		}

		if(isEnabled()){
		
//			try {
//			    Metrics metrics = new Metrics(this);
//			    metrics.start();
//			} catch (IOException e) {
//			    log("MCStats submission failed.");
//			}
			
			// Register listeners
			getServer().getPluginManager().registerEvents(new PrismOraclePlayerListener(this), this);
			
			
			// Register tasks
			catchUncaughtDisconnects();
			
		}
	}
	
	
	/**
	 * 
	 * @return
	 */
	public Prism getPrism(){
		return prism;
	}
	
	

//CREATE TABLE IF NOT EXISTS `prism_oracle_joins` (
//  `id` int(11) unsigned NOT NULL auto_increment,
//  `player_count` int(4) NOT NULL,
//  `player` varchar(16) NOT NULL,
//  `player_join` timestamp NULL default NULL,
//  `player_quit` timestamp NULL default NULL,
//  `playtime` int(7) default NULL,
//  `ip` varchar(16) NOT NULL,
//  PRIMARY KEY  (`id`),
//  KEY `username` (`player`)
//) ENGINE=MyISAM  DEFAULT CHARSET=latin1;
	
	
	/**
	 * 
	 */
	public void checkPluginDependancies(){
		
		// Prism 
		Plugin _tempPrism = getServer().getPluginManager().getPlugin("Prism");
		if (_tempPrism != null) {
			prism = (Prism)_tempPrism;
			prism.log("Prism core (anti-grief) found!");
		}
		else {
			prism.log("Prism Core (anti-grief) not found. Plugin add-on may not run.");
			this.disablePlugin();
		}
	}
	
	
	/**
	 * If a user disconnects in an unknown way that is never caught by onPlayerQuit,
	 * this will force close all records except for players currently online.
	 */
	public void catchUncaughtDisconnects(){
		getServer().getScheduler().runTaskTimerAsynchronously(this, new Runnable(){
		    public void run(){
		    	String on_users = "";
				for(Player pl: getServer().getOnlinePlayers()) {
					on_users += "'"+pl.getName()+"',";
				}
				if(!on_users.isEmpty()){
					on_users = on_users.substring(0, on_users.length()-1);
				}
				JoinUtil.forceDateForOfflinePlayers( prism, on_users );
		    }
		}, 1200L, 1200L);
	}
	
	
	/**
	 * 
	 * @return
	 */
	public String getPrismVersion(){
		return this.plugin_version;
	}
	
	
	/**
	 * Disable the plugin
	 */
	public void disablePlugin(){
		this.setEnabled(false);
	}
	
	
	/**
	 * Shutdown
	 */
	@Override
	public void onDisable(){
		prism.log("Closing plugin.");
	}
}