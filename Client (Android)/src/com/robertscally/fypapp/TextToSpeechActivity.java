package com.robertscally.fypapp;

/**
 * 
 * Description: 
 * 		This class is used to provide the text to speech feature for 
 * 		the user.
 * 
 * Author: 
 * 		Robert Scally except where otherwise stated
 * 
 * Date:
 * 		9 April 2013
 * 
 */


import java.util.Locale;

import com.google.analytics.tracking.android.EasyTracker;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TextToSpeechActivity extends Activity implements TextToSpeech.OnInitListener
{
	private TextToSpeech tts;
	private Button startStopButt;
	private String text;
	private SharedPreferences sharedPref;
	private TextView textV;
	private RelativeLayout relV;
	private SettingsClass settObj;
	private TextToSpeechActivity context;
	
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text_to_speech);
        
        text = "No Text";
        
        context = this;
        
        /* Get the text which has been sent to this activity and store it
           in a local variable   */
        Bundle extras = getIntent().getExtras();
        
        if (extras != null) 
        {
            text = extras.getString("text");
        }
        
        
        // create an object of the TextToSpeech class
        tts = new TextToSpeech(this, this);
 
        // Get users preferences
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        
        // initially set the text colour and background colour
        // of the start / stop button
        startStopButt = (Button) findViewById(R.id.start);
        startStopButt.setBackgroundColor(Color.rgb(0, 200, 0));
		startStopButt.setTextColor(Color.rgb(0, 0, 0));
        
		// store the TextView and RelativeLayout view
        textV = (TextView) findViewById(R.id.tts_text);
        relV = (RelativeLayout) findViewById(R.id.rel_lay);
        
        
        // create an object of the SettingsClass and pass through
        // the context of this class
        settObj = new SettingsClass(context);
        
        /* set the text of the TextView to the text which was received by
           this activity */
        textV.setText(text);
        textV.setBackgroundColor(settObj.getBackColour());
        textV.setTextSize(settObj.getTextSize());
        textV.setTextColor(settObj.getTextColour());
        
        textV.setTypeface(settObj.getFontFace(), settObj.getTextWeight());
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
 
    /** When the activity is destroyed */
    @Override
    public void onDestroy() 
    {
    	/* The following lines are taken from 
    	   source: http://www.androidhive.info/2012/01/android-text-to-speech-tutorial/ */
        
        if (tts != null) 
        {
        	// shutdown the text to speed engine
            tts.stop();
            tts.shutdown();
        }
        
        super.onDestroy();
    }
    
    /** When the activity is resumed */
    @Override
    public void onResume() 
    {
		super.onResume();
    }
    
    /** 
	 * 
	 * Function: onInit function to check if the text to speech is available
	 * and the language chosen by user is supported
	 * 
	 * Source: This whole function is from http://www.androidhive.info/2012/01/android-text-to-speech-tutorial/
	 * 
	 *  */
	@Override
	public void onInit(int status) 
	{
		if (status == TextToSpeech.SUCCESS) 
		{	 
            int result = tts.setLanguage(getVoiceLocale());
 
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) 
            {
                Log.e("TTS", "This Language is not supported");
            } 
        } 
		else 
        {
            Log.e("TTS", "Initilization Failed!");
        }
	}
	
	/** 
	 * 
	 * Function: Get the current voice locale from the shared preferences
	 * 
	 *  */
    public Locale getVoiceLocale()
    {
    	Locale locale = null;
    	
    	String voicePref = sharedPref.getString("voicePref", "us");
    	
    	/* The following are if statements which set the locale to
    	 * whichever language has been chosen by the user in the 
    	 * preferences */
    	if(voicePref.equalsIgnoreCase("us"))
    	{
    		locale = Locale.US;
    	}
    	if(voicePref.equalsIgnoreCase("uk"))		
    	{
    		locale = Locale.UK;
    	}
    	if(voicePref.equalsIgnoreCase("canada"))		
    	{
    		locale = Locale.CANADA;
    	}
    	if(voicePref.equalsIgnoreCase("english"))		
    	{
    		locale = Locale.ENGLISH;
    	}
    	
		return locale;
    }
    
    /** 
     * 
     * Function: Get current speech rate preferance from the shared preferances 
     * 
     * */
    public float getSpeechRate()
    {
    	float speechRate = 1.0f;
    	
    	/* Get the speech rate preference */
    	String speechRatePref = sharedPref.getString("speechRatePref", "1.0");
    	
    	/* Parse the float which is in the string */
    	speechRate = Float.parseFloat(speechRatePref);
    	
		return speechRate;
    }
	
    /** 
     * 
     * Function: When the Settings menu item is selected, the user is
     * brought to the preferences activity 
     * 
     * */
    public void goToPrefs() 
    {
    	// the speech is stopped if it is already running
    	tts.stop();
    	
    	Intent myIntent = new Intent(this, PreferanceActivity.class);
        startActivity(myIntent);
    }
    
    /** 
     * 
     * Function: When the Home menu item is selected, the user is
     * brought to the main / home activity 
     * 
     * */
    public void goToMain() 
    {
    	// the speech is stopped if it is already running
    	tts.stop();
    	
    	Intent myIntent = new Intent(this, MainActivity.class);
        startActivity(myIntent);
    }
    
    /** 
     * 
     * Function: When the start button is pressed, this starts the 
     * text to speech
     * 
     * */
	public void startSpeech(View view) 
	{
		/* 
		 * If the button currently has the word "Stop" then 
		 * change the colour of it and replace the word with
		 * "Start" 
		 * 
		 * */
		if(startStopButt.getText().toString().equalsIgnoreCase("Stop"))
		{
			tts.stop();
			
			startStopButt.setText("Start");
			startStopButt.setBackgroundColor(Color.rgb(0, 200, 0));
			startStopButt.setTextColor(Color.rgb(0, 0, 0));
		}
		/* 
		 * Else if the button currently has the word "Start" then 
		 * change the colour of it and replace the word with
		 * "Stop" 
		 * 
		 * */
		else if(startStopButt.getText().toString().equalsIgnoreCase("Start"))
		{
			startStopButt.setText("Stop");
			
			startStopButt.setBackgroundColor(Color.rgb(204, 0, 0));
			startStopButt.setTextColor(Color.rgb(255, 255, 255));
	        
			String theText = text;
	        
			/* Set the speech rate by getting the user preference */
	        tts.setSpeechRate(getSpeechRate());
	        
	        /* Start text to speech */
	        tts.speak(theText, TextToSpeech.QUEUE_FLUSH, null);
		}
    }
	
	/** 
	 * 
	 * Function: Inflates a menu when the menu button is pressed
	 * 
	 * */
	@Override
    public boolean onCreateOptionsMenu(Menu menu) 
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.text_to_speech_menu, menu);
        
        return true;
    }
    
	/** 
	 * 
	 * Function: Handles menu item selection actions
	 * 
	 * */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) 
    {
        switch (item.getItemId()) 
        {
            case R.id.speed:
                goToPrefs();
                return true;
                
            case R.id.home:
                goToMain();
                return true;
            
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
