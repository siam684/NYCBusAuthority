package com.aschow.nycbusauthority;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.util.Log;

public interface StopsRequest {
	
	public ArrayList<Stop> getStops();
	
	public static class StopsRequestor {
		
		public static ArrayList<Stop> getStops(String url, ArrayList<Stop> stops) throws XmlPullParserException, IOException
		{
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
	        factory.setNamespaceAware(false);
	        XmlPullParser parser = factory.newPullParser();
	        
	        //ArrayList<Stop> stops = new ArrayList<Stop>();
			
	        String baseAdress = "http://bustime.mta.info/api/where/";
	        String functionRequest = "stops-for-location.xml?";
	        String key = "key=f3c06459-2018-43b6-b9ab-6e4356b4f1ad";
	        String parameters = "&lat=40.732917&lon=-73.868229&radius=400";
	        		
	        
	        String url1 = baseAdress + functionRequest + key + parameters;
			
	        URL input = new URL(url1);
	        InputStream stream = input.openStream();
	        parser.setInput(stream, null);
	        int count = 1;
	        while(parser.getEventType()!=XmlPullParser.END_DOCUMENT)
	        {
	        	
	        		if(parser.getEventType() != XmlPullParser.START_TAG)
		        	{
		        		parser.nextTag();
		        	}
		        	else
		        	{
		        		if(parser.getName().equals("stop"))
		        		{
		        			Log.i("stopArraylistTest","stop noticed "+ count);
		        			Stop tempStop = new Stop();
		        			
		        			skipToNextStartTag(parser);
		        			Log.i("stopArraylistTest","next tag "+ parser.getName() + "L#: "+ parser.getLineNumber());
		        			if(parser.getName().equals("id"));
		        			{
		        				parser.next();
		        				tempStop.setId(parser.getText());
		        			}
		        			
		        			skipToNextStartTag(parser);
		        			Log.i("stopArraylistTest","next tag "+ parser.getName() + "L#: "+ parser.getLineNumber());
		        			if(parser.getName().equals("lat"));
		        			{
		        				parser.next();
		        				tempStop.setLat(Double.valueOf(parser.getText()));
		        			}
		        			
		        			skipToNextStartTag(parser);
		        			Log.i("stopArraylistTest","next tag "+ parser.getName() + "L#: "+ parser.getLineNumber());
		        			if(parser.getName().equals("lon"));
		        			{
		        				parser.next();
		        				tempStop.setLongi(Double.valueOf(parser.getText()));
		        			}
		        			
		        			skipToNextStartTag(parser);
		        			Log.i("stopArraylistTest","next tag "+ parser.getName() + "L#: "+ parser.getLineNumber());
		        			if(parser.getName().equals("direction"));
		        			{
		        				parser.next();
		        				tempStop.setDirection(parser.getText());
		        			}
		        			
		        			skipToNextStartTag(parser);
		        			Log.i("stopArraylistTest","next tag "+ parser.getName() + "L#: "+ parser.getLineNumber());
		        			if(parser.getName().equals("name"));
		        			{
		        				parser.next();
		        				tempStop.setName(parser.getText());
		        			}
		        			
		        			//TODO add routes to stop object
		        			processRoutes(parser,tempStop,count);
		        			
		        			stops.add(tempStop);
		        			Log.i("stopArraylistTest","size of stop array inside getStops "+ stops.size());
		        			count++;
		        			parser.next();	        			
		        		}
		        		else
		        		{
		        			parser.next();
		        		}
		        	}
	        	}
	        stream.close();
    		return stops;
    		
		}
		
		private static void skipToNextStartTag(XmlPullParser parser) throws XmlPullParserException, IOException
		{
			parser.next();
			while(parser.getEventType()!=XmlPullParser.START_TAG)
			{
				parser.next();
			}
				
		}
		
		private static void processRoutes(XmlPullParser parser, Stop stop, int stopCount) throws XmlPullParserException, IOException
		{
			String tag = "";
			int routeCount = 0;
			while(!tag.equals("routes"))
			{
				
				if(parser.getEventType()==XmlPullParser.END_TAG)
				{
					tag = parser.getText();
					parser.next();
				}
				else if(parser.getEventType()==XmlPullParser.START_TAG)
				{
					if(parser.getText().equals("route"))
					{
						Log.i("stopArraylistTest","route #" + routeCount++ + " found for stop #" + stopCount);
						parser.next();
					}
					else
					{
						parser.next();
					}
				}
			}
		}
		
		

	}
	
}
