package me.botsko.oracle.utils;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

public class GuideBookUtil {
	
	
	/**
	 * 
	 * @param player
	 */
	public static void giveToPlayer( Player player ){
		
		// Give them a guide book
        ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta meta = (BookMeta) book.getItemMeta();
        meta.setTitle("DHMC Rules");
        meta.setAuthor("viveleroi");
        meta.setPages(getBookContent());
        book.setItemMeta(meta);
		player.getInventory().addItem( book );
		
	}
	
	
    /**
	 * 
	 * @return
	 */
	public static String[] getBookContent(){
		String[] pages = {
				"Welcome to DHMC!\n" +
				"This book contains help and rules also found on the website.\n"+
				"Don't get banned for not knowing our rules. There's NO excuse.\n"+
				"Full rules on website: http://dhmc.us",
				"BASICS\n"+
//				"Use /w r for mining/gathering.\n" +
//				"Never build there, we reset it randomly.\n" +
				"Live in a town, it's safer. However, we don't ignore wilderness players either."+
				"You can also use /res to protect your house.",
				"MONEY\n"+
				"Use /w c or /w m for buying/selling." +
				" Earning money is hard, but it's valuable.",
				"RULES\n"+
				"Griefing is NEVER allowed. Don't grief the wild, people's farms, etc.\n"+
				"XRAY is NOT allowed. We'll find you.\n"+
				"No spam, cursing. No racist/hateful language/creations.\n"+
				"Use chat channels /l, /tc, /msg and /modreq instead of Global",
				"CHAT\n"+
				"Use /l to talk to people near you.\n"+
				"Use /tc for your town. Use /msg (user) for a single person."+
				"/g is for EVERYONE.",
				"HELP\n"+
				"Players are VERY nice and will almost always help you.\n"+
				"Staff can help if the players can't.\n"+
				"Use /modreq (msg) to request staff.\n"+
				"Forums/wiki and more at http://dhmc.us",
				"RANKS\n"+
				"Trusted: 5hrs over 2d+\n" +
				"Respected: 20hrs over 5d+\n" +
				"Legendary = 80hrs over 25d+\n" +
				"Myth/Eternal (see website)\n" +
				"Mod/Admin+ - only by staff choice. Legend+ only.",
				"VIVE\n"+
				"Vive is a cool guy, but VERY busy.\n"+
				"Saying hi is fine but don't ask him stuff that others can answer.\n"+
				"He won't tp to you, see your stuff, etc.\n"+
				"IRC/forums is better way to talk to him.",
				"DONATE\n"+
				"Vive pays per month out of his own pocket.\n"+
				"Not including the time it takes to make DHMC what it is.\n"+
				"Donate to re-pay him. http://dhmc.us/donate"
		};
		return pages;
	}

}
