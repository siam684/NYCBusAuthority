package com.aschow.nycbusauthority;

import java.util.ArrayList;
import java.util.Iterator;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.support.v7.app.ActionBarActivity;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

@SuppressWarnings("deprecation")
public class MapActivity extends ActionBarActivity implements InternetTaskFragment.TaskCallbacks, OnMapReadyCallback, ConnectionCallbacks, OnConnectionFailedListener
{
	
	private static final String TAG_TASK_FRAGMENT = "stop_request_fragment";
	private InternetTaskFragment mTaskFragment;
	MapFragment mapFragment;
	ArrayList<Stop> retreivedStopList;
	private GoogleApiClient mGoogleApiClient;
	private GoogleMap map;
	String LocationUrl;
	private FragmentTransaction trans;
	public Activity activity;
	Bundle bundleToSend;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
	    
		ActionBar actionBar = getActionBar();		
		if(actionBar!=null)
			actionBar.hide();
		
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.activity_map);
		activity = this;
		
		mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
		mapFragment.getMapAsync(this);
		
		if (mGoogleApiClient == null) {
		    mGoogleApiClient = new GoogleApiClient.Builder(this)
		        .addConnectionCallbacks(this)
		        .addOnConnectionFailedListener(this)
		        .addApi(LocationServices.API)
		        .build();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.map, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onMapReady(GoogleMap googleMap) 
	{
		// TODO Auto-generated method stub

        
        if(mGoogleApiClient!=null)
        {
        	mGoogleApiClient.connect();
        }
        else
        {
        	Log.i("LocationMessage","mGoogleApiClient is null");
        }
        
        map = googleMap;
        map.setMyLocationEnabled(true);
        map.setInfoWindowAdapter(new StopsRouteInfoWindowAdapter());
	}

	@Override
	public void onPreExecute() {
		// TODO Auto-generated method stub
		Log.i("requestUrl",LocationUrl);
	}

	@Override
	public void onProgressUpdate(int percent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCancelled() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPostExecute(Bundle recievedBundle) 
	{
		// TODO Auto-generated method stub
		if(recievedBundle.getBoolean("shapeRequested")==true)
		{
			Log.e("shapeRequestReturnValue", "from mapact onpost retrieved: " + recievedBundle.getString("encodedPolyline"));
			Log.e("shapeRequestReturnValue", "size of retreivedStopList: " + retreivedStopList.size());
		}


		if(recievedBundle.getBoolean("stopRequested")==true)
		{
			retreivedStopList = (ArrayList<Stop>)recievedBundle.getSerializable("stopsArrayList");
			//Log.i("stopArraylistTest","size of stop array in main: "+  ((ArrayList<Stop>) recievedBundle.getSerializable("stopsArrayList")).size());
			int pos = 0;
			for(int i = 0;i<retreivedStopList.size();i++)
			{
				
				Log.i("stopArraylistTest","stop #"+ pos);
				Log.i("stopArraylistTest","id: " + retreivedStopList.get(i).getId());
				Log.i("stopArraylistTest","latitude: " + retreivedStopList.get(i).getLat());
				Log.i("stopArraylistTest","longitude: " + retreivedStopList.get(i).getLongi());
				Log.i("stopArraylistTest","direction: " + retreivedStopList.get(i).getDirection());
				Log.i("stopArraylistTest","name: " + retreivedStopList.get(i).getName());
				map.addMarker(new MarkerOptions()
	            .position(new LatLng(retreivedStopList.get(i).getLat(), retreivedStopList.get(i).getLongi()))
	            .title(retreivedStopList.get(i).getName())
	            .snippet(String.valueOf(i)));
				if(retreivedStopList.get(i).getRoutes()!=null)
				{
					Log.i("stopArraylistTest","routes in stop: " + retreivedStopList.get(i).getRoutes().size());
					Iterator<Route> routeIt = retreivedStopList.get(i).getRoutes().iterator();
					while(routeIt.hasNext())
					{
						Route temp = routeIt.next();
						Log.i("stopArraylistTest","routes id: " + temp.getId());
						Log.i("stopArraylistTest","routes id: " + temp.getShortName());
						Log.i("stopArraylistTest","routes id: " + temp.getLongName());
						
					}
				}
				pos++;
			}
		}
		return;
	}

	@Override
	public void onConnected(Bundle arg0) 
	{
		// TODO Auto-generated method stub
		 Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
		 String baseAdress = "http://bustime.mta.info/api/where/";
         String functionRequest = "stops-for-location.xml?";
         String key = "key=f3c06459-2018-43b6-b9ab-6e4356b4f1ad";
         String parameters;
         
		if (mLastLocation != null) 
        {
        	map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastLocation.getLatitude(),mLastLocation.getLongitude()),(float) 15.0));
        	
            parameters = "&lat=" + mLastLocation.getLatitude() +"&lon=" + mLastLocation.getLongitude() + "&radius=600";
            		
            
            LocationUrl = baseAdress + functionRequest + key + parameters;
        }
        else
        {
        	Log.i("LocationMessage","mLastLocation is null");
        	parameters = "&lat=" + 0 +"&lon=" + 0 + "&radius=600";
			LocationUrl = baseAdress + functionRequest + key + parameters;
        }
		
        
		
		android.app.FragmentManager fm = getFragmentManager();
        mTaskFragment = (InternetTaskFragment) fm.findFragmentByTag(TAG_TASK_FRAGMENT);
        
        // If the Fragment is non-null, then it is currently being
        // retained across a configuration change.
        if (mTaskFragment == null) 
        {
        	
          mTaskFragment = new InternetTaskFragment();
          bundleToSend = new Bundle();
  		  bundleToSend.putBoolean("stopRequested", true);
  		  bundleToSend.putString("StopRequestUrl", LocationUrl);
  		  bundleToSend.putBoolean("shapeRequested", true);
  		  bundleToSend.putString("shapeRequestUrl", "http://bustime.mta.info/api/where/shape/MTA_Q020070.xml?key=f3c06459-2018-43b6-b9ab-6e4356b4f1ad");
  		  mTaskFragment.setArguments(bundleToSend);
  		  
          fm.beginTransaction().add(mTaskFragment, TAG_TASK_FRAGMENT).commit();
          
          trans  = fm.beginTransaction();
          if (mTaskFragment != null)
          {
        	  Log.i("FragmentMessage","Fragment created");
          }
          
        }
        else
        {
        	Log.i("FragmentMessage","mTaskFragment not null");
        }
	}

	@Override
	public void onConnectionSuspended(int cause) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		// TODO Auto-generated method stub
		
	}
	
	class StopsRouteInfoWindowAdapter implements InfoWindowAdapter {

		@Override
		public View getInfoContents(Marker marker) {
			// TODO Auto-generated method stub
			return null;
		}

		@SuppressLint("InflateParams") @Override
		public View getInfoWindow(Marker marker)
		{
			LayoutInflater li = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View view =  li.inflate(R.layout.stops_routes_info_window,null);
			
			//find stop object from retrieved list that matches the marker
			Stop markerStop = retreivedStopList.get(Integer.valueOf(marker.getSnippet()));
						
			//programmaticaly add text views for the name of the stop, and 1 text view for each route under the stop
			LinearLayout layoutView = (LinearLayout)view;
			layoutView.setBackgroundColor(Color.WHITE);
			Iterator<Route> routeIterator  = markerStop.getRoutes().iterator();
			
			TextView titleView = new TextView(MapActivity.this);
			titleView.setText(marker.getTitle() + " " + markerStop.getDirection());
			layoutView.addView(titleView);
			
			while(routeIterator.hasNext())
			{
				Route tempRoute = routeIterator.next();
				
				TextView longNameTextView = new TextView(MapActivity.this);
				TextView shortNameText = new TextView(MapActivity.this);
				TextView descTextView = new TextView(MapActivity.this);
				TextView idView = new TextView(MapActivity.this);
				
				longNameTextView.setText("LONG NAME: " + tempRoute.getLongName());
				shortNameText.setText("SHORT NAME:  " + tempRoute.getShortName());
				descTextView.setText("DESC: " + tempRoute.getDescription());
				idView.setText("ID: " + tempRoute.getId());
				
				layoutView.addView(shortNameText);
				layoutView.addView(longNameTextView);
				layoutView.addView(descTextView);
				layoutView.addView(idView);
			}
			
			return layoutView;			
		}
	}
}



