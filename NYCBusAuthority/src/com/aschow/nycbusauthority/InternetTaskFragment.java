package com.aschow.nycbusauthority;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParserException;

import com.aschow.nycbusauthority.StopsRequest.StopsRequestor;

import android.app.Activity;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

public class InternetTaskFragment extends Fragment
{

	interface TaskCallbacks 
	{
		String onPreExecute();
		void onProgressUpdate(int percent);
		void onCancelled();
		void onPostExecute(ArrayList<Stop> stopList);
	}

	/**
	 * Hold a reference to the parent Activity so we can report the
	 * task's current progress and results. The Android framework 
	 * will pass us a reference to the newly created Activity after 
	 * each configuration change.
	 */

	private TaskCallbacks mCallbacks;
	private StopsRequestAsyncTask mTask;

	@Override
	public void onAttach(Activity activity) 
	{
		super.onAttach(activity);
		mCallbacks = (TaskCallbacks) activity;
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
		mTask = new StopsRequestAsyncTask();
		mTask.execute();
	}

	/**
	 * Set the callback to null so we don't accidentally leak the 
	 * Activity instance.
	 */
	@Override
	public void onDetach() 
	{
		super.onDetach();
		mCallbacks = null;
	}

	private class StopsRequestAsyncTask extends AsyncTask<Void, Integer, Void>
	{
		ArrayList<Stop> stops;
		private String stopRequestUrl;

		
		@Override
		protected void onPreExecute() {
			if (mCallbacks != null) {
				
				
				
				
				stopRequestUrl = mCallbacks.onPreExecute();
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
			try {
				Log.i("stopArraylistTest","in doInBackground");
				stops = new ArrayList<Stop>();
				stops = StopsRequestor.getStops(stopRequestUrl,stops);
				Log.i("stopArraylistTest","size of stop array in fragment: "+ stops.size());
			} catch (XmlPullParserException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onProgressUpdate(Integer... percent) {
			if (mCallbacks != null) {
				mCallbacks.onProgressUpdate(percent[0]);
			}
		}

		@Override
		protected void onCancelled() {
			if (mCallbacks != null) {
				mCallbacks.onCancelled();
			}
		}
		@Override
		protected void onPostExecute(Void ignore) {
			if (mCallbacks != null) {
				Log.i("stopArraylistTest","size of stop array in fragment: "+ stops.size());
				mCallbacks.onPostExecute(stops);
			}
		}
	}

	private class ShapeRequestAsyncTask extends AsyncTask<Void, Integer,Void>
	{
		String encodedPolyline;		
		private String shapeRequestUrl;
		Bundle retrievedBundle;
		
		public ShapeRequestAsyncTask(Bundle bundle)
		{
			retrievedBundle = retrievedBundle;
		}
		
		@Override
		protected void onPreExecute()
		{
			shapeRequestUrl  = retrievedBundle.getString("shapeRequestUrl");
		}
		
		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		protected void onPostExecute(Void ignore)
		{
			
		}
		
	}
}
