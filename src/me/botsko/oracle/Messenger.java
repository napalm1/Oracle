package me.botsko.oracle;

import org.bukkit.ChatColor;

public class Messenger {
	
	/**
	 * 
	 */
	protected String plugin_name;
	
	
	/**
	 * 
	 * @param plugin_name
	 */
	public Messenger( String plugin_name ){
		this.plugin_name = plugin_name;
	}
	

	/**
	 * 
	 * @param msg
	 * @return
	 */
	public String playerHeaderMsg(String msg){
		if(msg != null){
			return ChatColor.LIGHT_PURPLE + plugin_name+" // " + ChatColor.WHITE + msg;
		}
		return "";
	}

	
	/**
	 * 
	 * @param msg
	 * @return
	 */
	public String playerSubduedHeaderMsg(String msg){
		if(msg != null){
			return ChatColor.LIGHT_PURPLE + plugin_name+" // " + ChatColor.GRAY + msg;
		}
		return "";
	}

	
	/**
	 * 
	 * @param msg
	 * @return
	 */
	public String playerMsg(String msg){
		if(msg != null){
			return ChatColor.WHITE + msg;
		}
		return "";
	}
	
	
	/**
	 * 
	 * @param msg
	 * @return
	 */
	public String[] playerMsg(String[] msg){
		if(msg != null){
			for(int i = 0; i < msg.length; i++){
				msg[i] = playerMsg(msg[i]);
			}
		}
		return msg;
	}

	
	/**
	 * 
	 * @param player_name
	 * @param cmd
	 * @param help
	 */
	public String playerHelp( String base_command, String subCmdOrArgs, String help ){
		return ChatColor.GRAY + "/"+base_command + " " + ChatColor.AQUA + subCmdOrArgs + ChatColor.WHITE + " - " + help;
	}

	
	/**
	 * 
	 * @param msg
	 * @return
	 */
	public String playerError(String msg){
		if(msg != null){
			return ChatColor.LIGHT_PURPLE + plugin_name+" // " + ChatColor.RED + msg;
		}
		return "";
	}
}