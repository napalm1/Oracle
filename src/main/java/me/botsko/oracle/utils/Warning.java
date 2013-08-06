package me.botsko.oracle.utils;

public class Warning {
	
	public final int id;
	public final Long epoch;
	public final String username;
	public final String moderator;
	public final String reason;
	
	public Warning( int id, Long epoch, String username, String reason, String moderator ){
		this.id = id;
		this.username = username;
		this.epoch = epoch;
		this.moderator = moderator;
		this.reason = reason;
	}
}