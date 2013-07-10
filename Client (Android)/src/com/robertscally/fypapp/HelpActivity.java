package com.robertscally.fypapp;

/**
 * Description: 
 * 		This is the help activity
 * 
 * Author: 
 * 		Robert Scally
 * 
 * Date:
 * 		9 April 2013
 */

import com.google.analytics.tracking.android.EasyTracker;
import android.os.Bundle;
import android.app.Activity;
import android.webkit.WebView;

public class HelpActivity extends Activity 
{
	
	@Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.help_layout);
        
        /* Load the webview */
        WebView webV = (WebView)findViewById(R.id.webView1);
        
        /* Load the html page from the assets folder of the application */
        webV.loadUrl("file:///android_asset/help.html");
    }
    
	/** When the activity is started */
	@Override
    public void onStart() 
    {
      super.onStart();
      
      // used by Google Analytics to track activity starting
      EasyTracker.getInstance().activityStart(this); 
    }

    /** When the activity is stopped */
    @Override
    public void onStop() 
    {
      super.onStop();
      
      // used by Google Analytics to track activity stopping
      EasyTracker.getInstance().activityStop(this); 
    }
    
}
