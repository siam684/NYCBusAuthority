package com.aschow.nycbusauthority;

public class Route {
	
	private String id;
	private String shortName;
	private String longName;
	private String description;
	
	public Route(String id,String shortName, String longName, String description)
	{
		this.id = id;
		this.shortName = shortName;
		this.longName = longName;
		this.description = description;
	}
	
	public Route()
	{
		id = "";
		shortName = "";
		longName = "";
		description = "";
	}

	public String getId()
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
	
	public void setId(String id)
	{
		this.id = id;
	}
	
	public void setShortName(String shortName)
	{
		this.shortName = shortName;
	}
	
	public void setLongName(String longName)
	{
		this.longName = longName;
	}
	
	public void setDescription(String description)
	{
		this.description = description;
	}
}
