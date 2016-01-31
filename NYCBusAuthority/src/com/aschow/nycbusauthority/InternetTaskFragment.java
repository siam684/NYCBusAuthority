package com.aschow.nycbusauthority;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParserException;

import com.aschow.nycbusauthority.Request.Requestor;

import android.app.Activity;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.annotation.TargetApi;
import java.util.ArrayList;

public class InternetTaskFragment extends Fragment
{

	interface TaskCallbacks 
	{
		void onPreExecute();
		void onProgressUpdate(int percent);
		void onCancelled();
		void onPostExecute(Bundle bundleToSend);
	}

	/**
	 * Hold a reference to the parent Activity so we can report the
	 * task's current progress and results. The Android framework 
	 * will pass us a reference to the newly created Activity after 
	 * each configuration change.
	 */

	private TaskCallbacks callIngActivity;
	private MTARequestAsyncTask stopRequestTask;
	private MTARequestAsyncTask shapeRequestTask;
	private boolean stopRequested;
	private boolean shapeRequested;

	@SuppressWarnings("deprecation")
	@Override
	public void onAttach(Activity activity) 
	{
		super.onAttach(activity);
		callIngActivity = (TaskCallbacks) activity;
	}


	/**
	 * This method will only be called once when the retained
	 * Fragment is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		
		// Retain this fragment across configuration changes.
		setRetainInstance(true);

		// Create and execute the background task.
		Bundle retrievedBundle = this.getArguments();
		
		stopRequested = retrievedBundle.getBoolean("stopRequested");
		shapeRequested = retrievedBundle.getBoolean("shapeRequested");
		
		if(stopRequested)
		{
			Bundle bundle = new Bundle();
			bundle.putBoolean("stopRequested", true);
			bundle.putString("StopRequestUrl",  retrievedBundle.getString("StopRequestUrl"));
			stopRequestTask = new MTARequestAsyncTask(retrievedBundle);
			this.StartAsyncTaskInParallel(stopRequestTask);
			//stopRequestTask.execute();
		}
		
		if(shapeRequested)
		{
			Bundle bundle = new Bundle();
			bundle.putBoolean("shapeRequested", true);
			bundle.putString("shapeRequestUrl",  retrievedBundle.getString("shapeRequestUrl"));
			shapeRequestTask = new MTARequestAsyncTask(retrievedBundle);
			this.StartAsyncTaskInParallel(shapeRequestTask);
			//shapeRequestTask.execute();
		}
	} 

	/**
	 * Set the callback to null so we don't accidentally leak the 
	 * Activity instance.
	 */
	@Override
	public void onDetach() 
	{
		super.onDetach();
		callIngActivity = null;
	}
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void StartAsyncTaskInParallel(MTARequestAsyncTask task) 
	{
	     if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
	     {
	    	 task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
	     }
	     else
	     {
	    	 task.execute();
	     }
	}

	private class MTARequestAsyncTask extends AsyncTask<Void, Integer, Void>
	{
		Bundle retrievedBundle;
		ArrayList<Stop> stops;
		String encodedPolyline;		
		private String stopRequestUrl;
		private String shapeRequestUrl;
		Boolean stopRequested;
		Boolean shapeRequested;

		
		public MTARequestAsyncTask(Bundle bundle)
		{
			retrievedBundle = bundle;
		}
		
		@Override
		protected void onPreExecute() 
		{
			Log.i("MethodCalls","STRAT onPreExecute");
			
			if (callIngActivity != null) 
			{
				stopRequested = retrievedBundle.getBoolean("stopRequested");
				shapeRequested = retrievedBundle.getBoolean("shapeRequested");
				
				if(stopRequested)
				{					
					stopRequestUrl = retrievedBundle.getString("StopRequestUrl");					
				}

				if(shapeRequested)
				{
					Log.e("shapeRequestReturnValue", "shapeRequested: " +shapeRequested);
					shapeRequestUrl = retrievedBundle.getString("shapeRequestUrl");
					Log.e("shapeRequestReturnValue", "shapeRequestUrl: " + shapeRequestUrl);
				}
			}
		}

		/**
		 * Note that we do NOT call the callback object's methods
		 * directly from the background thread, as this could result 
		 * in a race condition.
		 * @return 
		 */
		@Override
		protected Void doInBackground(Void... ignore) 
		{
			Log.i("MethodCalls","STRAT doInBackground");
			if(this.stopRequested)
			{
				this.makeStopRequest();
			}
			
			if(this.shapeRequested)
			{
				this.makeShapeRequest();
			}
			Log.i("MethodCalls",this.getStatus().name());
			return null;		
		}
		
		@Override
		protected void onPostExecute(Void ignore) {
			Log.i("MethodCalls","STRAT onPostExecute");
			Log.e("shapeRequestReturnValue", "ITF OnPost size of stops array: "+ stops.size());
			if (callIngActivity != null) {
				//Log.i("stopArraylistTest","size of stop array in fragment: "+ stops.size());
				Bundle bundleToSend = new Bundle();
				Log.e("shapeRequestReturnValue", "ITF OnPost size of stops array: "+ stops.size());
				
				if(this.stopRequested)
				{
					bundleToSend.putSerializable("stopsArrayList", stops);
					bundleToSend.putBoolean("stopRequested", true);
				}
				
				if(this.shapeRequested)
				{
					bundleToSend.putString("encodedPolyline", encodedPolyline);
					bundleToSend.putBoolean("shapeRequested", true);
				}
				
				callIngActivity.onPostExecute(bundleToSend);
			}
		}

		@Override
		protected void onProgressUpdate(Integer... percent) {
			Log.i("MethodCalls","STRAT onProgressUpdate");
			if (callIngActivity != null) {
				callIngActivity.onProgressUpdate(percent[0]);
			}
		}

		@Override
		protected void onCancelled() {
			Log.i("MethodCalls","STRAT onCancelled");
			if (callIngActivity != null) {
				callIngActivity.onCancelled();
				Log.e("shapeRequestReturnValue", "doinbackground canceled");
			}
		}
		
		
		private void makeStopRequest()
		{
			Log.i("MethodCalls","STRAT makeStopRequest");
			try {
				Log.i("stopArraylistTest","in makeStopRequest");
				stops = new ArrayList<Stop>();
				stops = Requestor.getStops(stopRequestUrl,stops);
				Log.e("shapeRequestReturnValue", "stops count" + stops.size());
				Log.i("stopArraylistTest","size of stop array in fragment: "+ stops.size());
			} catch (XmlPullParserException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		private void makeShapeRequest()
		{
			Log.i("MethodCalls","STRAT makeShapeRequest");
			Log.i("shapeRequestReturnValue", "making shape request");
			try {
				Log.e("shapeRequestReturnValue", "shapeRequestUrl from makeshaperequest: " + shapeRequestUrl);
				encodedPolyline = Requestor.getShape(shapeRequestUrl);
			} catch (XmlPullParserException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}		
	}
}
