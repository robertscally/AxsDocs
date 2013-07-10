package com.robertscally.fypapp;

/**
 * Description: 
 * 		This is activity for displaying the text returned from the server
 * 
 * Author: 
 * 		Robert Scally
 * 
 * Date:
 * 		9 April 2013
 */

import com.google.analytics.tracking.android.EasyTracker;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class DisplayResultTextActivity extends Activity implements OnSeekBarChangeListener 
{
	private SharedPreferences sharedPref;
	private Editor prefEditor;
	private RelativeLayout relV; 
	private String text;
	private boolean trackFlings = true;
	private TextView textV;
	private SeekBar seekB;
	private Button hideBarButt;
	private GestureDetector mDetector;
	private View view;
	private SettingsClass settObj;
	private static Context context;
	private Toast toast1, toast2;
	
    @SuppressLint("ShowToast")
	@Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_text_layout2);
        
        context = this;
        
        text = "No Text";
        
        /* Toast messages which will be displayed when
           the user changes the background colour or
           the text colour */
    	toast1 = Toast.makeText(context, "Background Colour Changed", Toast.LENGTH_SHORT);
    	toast1.setGravity(Gravity.CENTER, 0, 0);
    	
    	toast2 = Toast.makeText(context, "Text Colour Changed", Toast.LENGTH_SHORT);
    	toast2.setGravity(Gravity.CENTER, 0, 0);
    	
    	
        Bundle extras = getIntent().getExtras();
        
        /* Get the text which is sent to this activity */
        if (extras != null) 
        {
            text = extras.getString("text");
        }
        else
        {
        	text = "No text";
        }
        
        /* Get users preferences */
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        
        /* Create a new settings object which will use this classes context */
        settObj = new SettingsClass(context);
        
        view = new View(DisplayResultTextActivity.this);
        
        /* Create objects from different elements of the layout */
        hideBarButt = (Button)findViewById(R.id.button3);
        seekB = (SeekBar) findViewById(R.id.seekBar1);
        textV = (TextView) findViewById(R.id.displayTextView);
        relV = (RelativeLayout) findViewById(R.id.rel_lay);
        
        /* Add a listener for the slider bar */
        seekB.setOnSeekBarChangeListener(this);
        
        /* Sets the progress of the slider to be 
           relative to the current text size stored 
           in the preferences */
        seekB.setProgress(settObj.getTextSize());
        
        mDetector = new GestureDetector(this, new MyGestureListener());
        
        /* Display the text */
        displayText();
    }
    
    /** 
     * Function: Used to set what the action should be when the back 
     * key is pressed
     * 
     * */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) 
    {
        if (keyCode == KeyEvent.KEYCODE_BACK) 
        {
        	/* Finish this activity and go to the main screen */
        	finish();
            goToMain();
        }
        
        return super.onKeyDown(keyCode, event);
    }
    
    /** 
     * Function: Implemented as part of the Gesture listener
     * 
     * */
    @Override 
    public boolean onTouchEvent(MotionEvent event)
    { 
        this.mDetector.onTouchEvent(event);
        
        return super.onTouchEvent(event);
    }
    
    /** 
     * Function: Implemented as part of the Gesture listener
     * 
     * */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev)
    {
        super.dispatchTouchEvent(ev); 
        
        return this.onTouchEvent(ev); 
    }
    
    /** 
     * Function: Implemented as part of the Gesture listener
     * 
     * */
    class MyGestureListener extends GestureDetector.SimpleOnGestureListener 
    {
        private static final String DEBUG_TAG = "Gestures"; 
        
        /** 
         * Function: Implemented as part of the Gesture listener
         * 
         * */
        @Override
        public boolean onDown(MotionEvent event) 
        { 
            
            return true;
        }
        
        /** 
         * Function: Listen for fling movements
         * 
         * */
        @Override
    	public boolean onFling(MotionEvent start, MotionEvent end, float velocityX,
    			float velocityY) 
    	{
        	if(trackFlings)
        	{
	    		// if the start X coordinate value is less than the end X value, this
        		// indicates a left to right fling movement.	
	    		if ((start.getRawX() < end.getRawX()) && ((start.getRawX() - end.getRawX()) < -100)) 
	    		{
	    			// sets the background colour to be the next one in the array
	    			settObj.setNextBackColour();
	    			
	    			/* Change the background colour of the text view and relative 
			           layout view to the new colour */
	    			textV.setBackgroundColor(settObj.getBackColour());
	    	        relV.setBackgroundColor(settObj.getBackColour());

	    	        /* set the text colour so that it is not the same as 
				   	   the new background colour */
	    	        textV.setTextColor(settObj.getTextColour());
	    	        
	    	        // show message to inform user the background colour has changed
	    	        toast1.show();
	    		} 
	    		/* else if the start X coordinate value is greater than the end X value, this
	    		   indicates a right to left fling movement */
	    		else if(start.getRawX() > end.getRawX() && ((start.getRawX() - end.getRawX()) > 100))
	    		{
	    			// set the colour of the text to be the next colour in the array
	    			settObj.setNextTextColour();

	    			// set the text colour so that it is not the same as 
	    			// the new background colour
	    			textV.setTextColor(settObj.getTextColour());
	    		
	    			// show message to inform user the text colour has changed
	    	        toast2.show();
	    		}
        	}
    		
    		return true;
    	}
    }
    
    /** 
     * Function: Display the text in a text view
     * 
     * */
    public void displayText() 
    {
        textV.setText(text);
        
        /* Set the background colour of the text view and the relative layout 
           from the preferences */
        textV.setBackgroundColor(settObj.getBackColour());
        relV.setBackgroundColor(settObj.getBackColour());
        
        /* Set the text size and text colour from the preferences */
        textV.setTextSize(settObj.getTextSize());
        textV.setTextColor(settObj.getTextColour());
        
        /* Set the font style from the preferences */
        textV.setTypeface(settObj.getFontFace(), settObj.getTextWeight());
	}

    /** 
     * Function: to show the slider when the text size button is pressed 
     * 
     * */
    public void showSlider(View view) 
    {
    	/* While the slider is visible, stop fling motion being tracked */
    	trackFlings = false;
    	
    	/* Make the hide slider bar button and the slider to be visible */
    	hideBarButt.setVisibility(View.VISIBLE);
    	seekB.setVisibility(View.VISIBLE);
    }
	
    /** Function to hide the slider when the hide slider bar button is pressed */
    public void hideSlider(View view) 
    {
    	/* Make the hide slider bar button and the slider to disappear */
    	hideBarButt.setVisibility(View.GONE);
    	seekB.setVisibility(View.GONE);
    	
    	/* Enable fling motion to be recorded again */
    	trackFlings = true;
    }

	@Override
    public void onResume() 
    {
		super.onResume();
		
		/* on resuming activity, refresh the text display to show
		   changes user may have made to settings */
    	displayText();
    }
    
    /** 
     * 
     * Function: When Text to Speech button is pressed, this function
     * starts the speech synthesis activity 
     *  
     *  */
    public void goToTTS(View v) 
    {
    	Intent myIntent = new Intent(this, TextToSpeechActivity.class);
    	myIntent.putExtra("text", text);
        startActivity(myIntent);
    }
    
    /** 
     * Function which is called when the Settings button is pressed
     * to get the users preferences 
     * 
     */
    public void goToPrefs() 
    {
    	Intent myIntent = new Intent(this, PreferanceActivity.class);
        startActivity(myIntent);
    }
    
    /** 
     * Function which is called when the Help button is 
     * pressed to bring the user to the help activity 
     * 
     * */
    public void goToHelp() 
    {
    	Intent myIntent = new Intent(this, HelpActivity.class);
        startActivity(myIntent);
    }

    /** 
     * Function which is called when the progress bar has changed 
     * 
     * */
	@Override
	public void onProgressChanged(SeekBar barValue, int barPosition, boolean fromUser) 
	{
		/* If the progress has changed because of the users touch */
		if(fromUser)
		{
			/* The following if else statement adds 10 to the bar position
			   to make the increases in text size more noticeable and 
			   require less use of fine movements */
			if(barPosition == 0)
			{
				barPosition = 10;
			}
			else if(barPosition > 0)
			{
				barPosition += 10;
			}
			
			/* Set the text size to the new value based on slider movement */
			textV.setTextSize(barPosition);
			String textSize = Integer.toString(barPosition);
			
			/* Store new text size in the preferences */
			prefEditor = sharedPref.edit();
	        prefEditor.putString("fontSizePref", textSize);
	        prefEditor.commit();
		}
	}

	/** 
	 * 
	 * Function: Implemented as part of the seekbar listener
	 *
	 *   */
	@Override
	public void onStartTrackingTouch(SeekBar arg0) 
	{
			
	}

	/** 
	 * 
	 * Function: Implemented as part of the seekbar listener
	 *
	 *   */
	@Override
	public void onStopTrackingTouch(SeekBar arg0) 
	{
			
	}
	
	/** 
	 * 
	 * Function: Brings the user to the main / home activity
	 * when selected in the menu
	 *
	 *   */
    public void goToMain() 
    {	
    	Intent myIntent = new Intent(this, MainActivity.class);
        startActivity(myIntent);
    }
    
    /** 
	 * 
	 * Function: Brings the user to the set colour activity
	 * when selected in the menu
	 *
	 *   */
    public void goToColours() 
    {	
    	Intent myIntent = new Intent(this, ChangeColourActivity.class);
        startActivity(myIntent);
    }
	
    /** 
	 * Function to create a menu 
	 * 
	 * */
	@Override
    public boolean onCreateOptionsMenu(Menu menu) 
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.display_text_menu, menu);
        
        return true;
    }
	
	/** 
	 * Function to create a font style dialog box 
	 * 
	 * */
	public void fontFaceDialog()
    {
		String[] styleArray = getResources().getStringArray(R.array.fontStyleNames);
		String fontFacePref = sharedPref.getString("fontFacePref", "verdana");
		int checkedItem = 0;
		
		/* Loop to select the radio item which is currently the 
		   users style preferance */
		for(int i=0; i < styleArray.length; i++)
		{
			if(fontFacePref.equalsIgnoreCase(styleArray[i]))
			{
				checkedItem = i;
			}
		}
		
    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
    	
    	this.getLayoutInflater();

        /* Inflate and set the layout for the dialog
           Pass null as the parent view because its going in the dialog layout */
        builder
        .setSingleChoiceItems(R.array.fontStyleNames, checkedItem, new DialogInterface.OnClickListener() 
        {
            public void onClick(DialogInterface dialog, int id) 
            {
            	Log.d("Test","Position: "+id);
            	
            	String[] styleArray = getResources().getStringArray(R.array.fontStyleNames);
            	
                settObj.setFontFace(styleArray[id]);
                
                textV.setTypeface(settObj.getFontFace());
            }
        })
        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) 
            {
            	/* Set the font style when the user clicks ok */
            	textV.setTypeface(settObj.getFontFace());
            }
        })
        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() 
        {
        	/* Do nothing when the user clicks cancel */
            public void onClick(DialogInterface dialog, int id) 
            {
            	
            }
        });
		
		/* create the dialog */
		AlertDialog dialog = builder.create();
		
		/* show the font style dialog */
		dialog.show();
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
    
    /** 
	 * Function to handle menu item selection actions
	 * 
	 * */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) 
    {
        switch (item.getItemId()) 
        {       
            case R.id.home:
                goToMain();
                return true;
                
            case R.id.text_size:
                showSlider(view);
                return true;
                
            case R.id.font_face:
            	fontFaceDialog();
                return true;
                
            case R.id.colour:
            	goToColours();
                return true;
                
            case R.id.text_to_speech:
                goToTTS(view);
                return true;
                
            case R.id.help:
                goToHelp();
                return true;
                
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
