package com.aschow.nycbusauthority;

import java.io.Serializable;
import java.util.ArrayList;

public class Stop implements Serializable
{
    private String id;
    private double lat;
    private double longi;
    private String direction;
    private String name;
    private ArrayList<Route> routes;

    public Stop(String id, double lat, double longi, String direction, String name) 
    {
    	this.id = id;
    	this.lat = lat;
    	this.longi = longi; 
    	this.direction = direction;
    	this.name = name;
    	routes = new ArrayList<Route>();
    }
    
    public Stop() 
    {
		id = "";
		lat = 0;
		longi = 0;
		direction = null;
		name = null;
		routes = new ArrayList<Route>();
	}

	public String getId()
    {
		return id;
    }
    
    public double getLat()
    {
		return lat;
    }
    
    public double getLongi()
    {
		return longi;
    }
    
    public String getDirection()
    {
		return direction;
    }
    
    public String getName()
    {
		return name;
    }
    
    public void setId(String id)
    {
    	this.id = id;
    	
    }
    
    public void setLat(double lat)
    {
    	this.lat = lat;
    }
    
    public void setLongi(double longi)
    {
    	this.longi = longi;
    }
    
    public void setDirection(String direction)
    {
    	this.direction = direction;
    }
    
    public void setName(String name)
    {
    	this.name = name;
    }
    
    public void addRoute(Route route)
    {
    	routes.add(route);
    }
    
    public ArrayList<Route> getRoutes()
    {
    	return routes;
    }
}
  
