package com.aschow.nycbusauthority;

public class Route {
	
	private int id;
	private String shortName;
	private String longName;
	private String description;
	
	public Route(int id,String shortName, String longName, String description)
	{
		this.id = id;
		this.shortName = shortName;
		this.longName = longName;
		this.description = description;
	}
	
	public int getId()
	{
		return id;
	}
	
	public String getShortName()
	{
		return shortName;
	}
	
	public String getLongName()
	{
		return longName;
	}
	
	public String getDescription()
	{
		return description;
	}
}
