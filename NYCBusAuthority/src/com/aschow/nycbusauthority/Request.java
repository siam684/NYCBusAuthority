package com.aschow.nycbusauthority;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.util.Log;

public interface Request {
	
	public ArrayList<Stop> getStops();
	public String getShape();
	
	public static class Requestor {
		
		public static ArrayList<Stop> getStops(String url, ArrayList<Stop> stops) throws XmlPullParserException, IOException
		{
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
	        factory.setNamespaceAware(false);
	        XmlPullParser parser = factory.newPullParser();
	        
	        //ArrayList<Stop> stops = new ArrayList<Stop>();
			
/*	        String baseAdress = "http://bustime.mta.info/api/where/";
	        String functionRequest = "stops-for-location.xml?";
	        String key = "key=f3c06459-2018-43b6-b9ab-6e4356b4f1ad";
	        String parameters = "&lat=40.732917&lon=-73.868229&radius=600";
	        		
	        
	        String url1 = baseAdress + functionRequest + key + parameters;*/
			
	        URL input = new URL(url);
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
		
		public static String getShape(String url) throws XmlPullParserException, IOException
		{
			String encodedPolyline = null;
			Log.e("shapeRequestReturnValue", "in get shape");
			
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
	        factory.setNamespaceAware(false);
	        XmlPullParser parser = factory.newPullParser();
	        

			URL input = new URL(url);
	        InputStream stream = input.openStream();
	        parser.setInput(stream, null);
			
	        
	        while(parser.getEventType()!=XmlPullParser.END_DOCUMENT)
	        {
	        	if(parser.getEventType() != XmlPullParser.START_TAG)
	        	{
	        		parser.nextTag();
	        	}
	        	else
	        	{
	        		skipToNextDefinedStartTag(parser,"points");
	    	        Log.e("shapeRequestReturnValue", "tag title: " + parser.getName());
	    	        if(parser.getName().equals("points"))
	    	        {
	    	        	parser.next();
	    	        	encodedPolyline = parser.getText();
	    	        	Log.e("shapeRequestReturnValue", "encodedPolyline: " + parser.getText());
	    	        }
	        	}
	        }
	        
	        stream.close();
			return encodedPolyline;
		}
		
		private static void skipToNextStartTag(XmlPullParser parser) throws XmlPullParserException, IOException
		{
			parser.next();
			while(parser.getEventType()!=XmlPullParser.START_TAG)
			{
				parser.next();
			}
				
		}
		
		private static void skipToNextDefinedStartTag(XmlPullParser parser, String tag) throws XmlPullParserException, IOException
		{
			skipToNextStartTag(parser);
			Log.i("stopArraylistTest",parser.getPositionDescription());
			while(!parser.getName().equalsIgnoreCase(tag))
			{
				skipToNextStartTag(parser);
			}
		}
		
		private static void processRoutes(XmlPullParser parser, Stop stop, int stopCount) throws XmlPullParserException, IOException
		{
			
			skipToNextDefinedStartTag(parser, "routes");
			Log.i("stopArraylistTest",parser.getPositionDescription());
			String tagName = " ";
			parser.next();
			Route tempRoute = null;
			int routeCount = 0;
			boolean idNotSet = true;
			while(!tagName.equals("routes"))
			{
				parser.next();
				if(parser.getEventType()==XmlPullParser.START_TAG)
				{
					if(parser.getName().equalsIgnoreCase("route"))
					{
						Log.i("stopArraylistTest","route number " + ++routeCount + "for stop number " + stopCount);
						tempRoute = new Route();
						idNotSet = true;
					}
					else if(parser.getName().equalsIgnoreCase("id")&&idNotSet)
					{
						parser.next();
						tempRoute.setId(parser.getText());
						idNotSet = false;
					}
					else if(parser.getName().equalsIgnoreCase("shortName"))
					{
						parser.next();
						tempRoute.setShortName(parser.getText());
					}
					else if(parser.getName().equalsIgnoreCase("longName"))
					{
						parser.next();
						tempRoute.setLongName(parser.getText());
					}
					else if(parser.getName().equalsIgnoreCase("description"))
					{
						parser.next();
						tempRoute.setDescription(parser.getText());
					}
				}
				
				if(parser.getEventType()==XmlPullParser.END_TAG)
				{
					tagName = parser.getName();
					
					if(tagName.equalsIgnoreCase("route"))
					{
						stop.addRoute(tempRoute);
					}
				}
			}
		}		
	}	
}
