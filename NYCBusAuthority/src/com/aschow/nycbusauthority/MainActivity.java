package com.aschow.nycbusauthority;

import java.util.ArrayList;
import java.util.Iterator;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


@SuppressWarnings("deprecation")
public class MainActivity extends ActionBarActivity implements InternetTaskFragment.TaskCallbacks {

	private static final String TAG_TASK_FRAGMENT = "task_fragment";

	private InternetTaskFragment mTaskFragment;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        android.app.FragmentManager fm = getFragmentManager();
        mTaskFragment = (InternetTaskFragment) fm.findFragmentByTag(TAG_TASK_FRAGMENT);

        // If the Fragment is non-null, then it is currently being
        // retained across a configuration change.
        if (mTaskFragment == null) 
        {
        	
          mTaskFragment = new InternetTaskFragment();
          fm.beginTransaction().add(mTaskFragment, TAG_TASK_FRAGMENT).commit();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
	public void onPreExecute() {
		// TODO Auto-generated method stub
		
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
	public void onPostExecute(ArrayList<Stop> stopList) {
		// TODO Auto-generated method stub
		
		Iterator<Stop> it = stopList.iterator();
		
		Log.i("stopArraylistTest","size of stop array in main: "+ stopList.size());
		int pos = 0;
		while(it.hasNext())
		{
			Stop stop = it.next();
			Log.i("stopArraylistTest","stop #"+ pos);
			Log.i("stopArraylistTest","id: " + stop.getId());
			Log.i("stopArraylistTest","latitude: " + stop.getLat());
			Log.i("stopArraylistTest","longitude: " + stop.getLongi());
			Log.i("stopArraylistTest","direction: " + stop.getDirection());
			Log.i("stopArraylistTest","name: " + stop.getName());
			pos++;
		}
	}
}
