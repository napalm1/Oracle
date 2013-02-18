package me.botsko.prismoracle;

import me.botsko.prism.ConfigBase;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

public class PrismOracleConfig extends ConfigBase {
	
	
	/**
	 * 
	 * @param plugin
	 */
	public PrismOracleConfig(Plugin plugin) {
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
		
		// Copy defaults
		config.options().copyDefaults(true);
		
		// save the defaults/config
		plugin.saveConfig();
		
		return config;
		
	}
}
