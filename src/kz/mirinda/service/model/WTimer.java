package kz.mirinda.service.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import java.util.Date;
import java.util.TimeZone;


public class WTimer implements Serializable {
	
	/**
	 * for serialize
	 */
	private static final long serialVersionUID = 1L;
	private long id;
	private String name;
	private long time;
	
	public static String timeToString(long time){
		DateFormat format = new SimpleDateFormat("HH:mm:ss");
		format.setTimeZone(TimeZone.getTimeZone("UTC"));
		return format.format(new Date(time));		
	}
	
	public WTimer(){}
	public WTimer(long id, String name, long time) {
		super();
		this.id = id;
		this.name = name;
		this.time = time;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
		
	}
	
	public String timeToString(){
		DateFormat format = new SimpleDateFormat("HH:mm:ss");
		format.setTimeZone(TimeZone.getTimeZone("UTC"));
		return format.format(new Date(time));
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	@Override
	public String toString() {
		return name;
	}
	
	
}
