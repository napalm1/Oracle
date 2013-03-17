package me.botsko.oracle;


import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

public class OracleConfig extends ConfigBase {
	
	
	/**
	 * 
	 * @param plugin
	 */
	public OracleConfig(Plugin plugin) {
		super(plugin);
	}
	
	
	/**
	 * 
	 * @param plugin
	 */
	public FileConfiguration getConfig(){
		
		FileConfiguration config = plugin.getConfig();
		
		// set defaults
		config.addDefault("oracle.debug", false);
		config.addDefault("oracle.language", "en-us");
		
		// Database
		config.addDefault("oracle.database.max-pool-connections", 20);
		config.addDefault("oracle.database.max-wait", 20000);

		// Mysql
		config.addDefault("oracle.mysql.hostname", "127.0.0.1");
		config.addDefault("oracle.mysql.username", "root");
		config.addDefault("oracle.mysql.password", "");
		config.addDefault("oracle.mysql.database", "minecraft");
		config.addDefault("oracle.mysql.port", 3306);
		
		
		config.addDefault("oracle.bans.enabled", true);
		config.addDefault("oracle.joins.enabled", true);
		
		// Copy defaults
		config.options().copyDefaults(true);
		
		// save the defaults/config
		plugin.saveConfig();
		
		return config;
		
	}
}
