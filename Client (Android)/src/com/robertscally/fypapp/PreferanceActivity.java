package com.robertscally.fypapp;

/**
 * Description: 
 * 		This activity is for creating the preferences page 
 * 		for the app.
 * 
 * Author: 
 * 		Robert Scally
 * 
 * Date:
 * 		9 April 2013
 */

import com.google.analytics.tracking.android.EasyTracker;

import android.os.Bundle;
import android.preference.PreferenceActivity;


public class PreferanceActivity extends PreferenceActivity 
{    
	
	@Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        
        // uses the xml file in res/xml to provide preferences layout
        addPreferencesFromResource(R.xml.settings);  
    }
	
	/** 
	 *  Function: onStart used for Google Analytics tracking to track when an
	 *  activity in the start part of the lifecycle 
	 */
	@Override
    public void onStart() 
    {
      super.onStart();
      
      EasyTracker.getInstance().activityStart(this); 
    }

	/** 
	 *  Function: onStop used for Google Analytics tracking to track when an
	 *  activity in the stop part of the lifecycle 
	 */
    @Override
    public void onStop() 
    {
      super.onStop();
      
      EasyTracker.getInstance().activityStop(this); 
    }
}
