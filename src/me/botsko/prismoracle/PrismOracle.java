package me.botsko.prismoracle;

import me.botsko.prism.Prism;
import me.botsko.prismoracle.listeners.PrismOraclePlayerListener;

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
			
		}
	}
	
	
	/**
	 * 
	 * @return
	 */
	public Prism getPrism(){
		return prism;
	}
	
	
//	CREATE TABLE IF NOT EXISTS `prism_oracle_joins` (
//			  `id` int(11) unsigned NOT NULL auto_increment,
//			  `player_count` int(4) NOT NULL,
//			  `username` varchar(16) NOT NULL,
//			  `player_join` timestamp NOT NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
//			  `player_quit` timestamp NULL default NULL,
//			  `playtime` int(45) default NULL,
//			  `ip` varchar(16) NOT NULL,
//			  PRIMARY KEY  (`id`),
//			  KEY `username` (`username`)
//			) ENGINE=MyISAM DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;
	
	
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