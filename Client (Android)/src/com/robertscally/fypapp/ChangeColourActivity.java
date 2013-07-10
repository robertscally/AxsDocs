package com.robertscally.fypapp;

/**
 * Description: 
 * 		This is the activity for changing the text and background colour
 * 
 * Author: 
 * 		Robert Scally 
 * 
 * Date:
 * 		9 April 2013
 */

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ChangeColourActivity extends Activity implements OnClickListener
{
	private Button textColour1,textColour2,textColour3,textColour4,backColour1,backColour2,backColour3,backColour4;
	private String[] colourArray;
	private ChangeColourActivity context;
	private SettingsClass settObj;
	private TextView text;
	private Button[] textButtonArray;
	private Button[] backButtonArray;
	
	
	@Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.colour_layout);
        
        context = this;
        
        /* Get the array of colours available to the user and
           store in an array */
        colourArray = getResources().getStringArray(R.array.backColourValues);
        
        settObj = new SettingsClass(context);
        
        text = (TextView) findViewById(R.id.textView1);
        
        /* Text colour buttons */
        textColour1 = (Button) findViewById(R.id.textColour1);
        textColour2 = (Button) findViewById(R.id.textColour2);
        textColour3 = (Button) findViewById(R.id.textColour3);
        textColour4 = (Button) findViewById(R.id.textColour4);
        
        /* Background colour buttons */
        backColour1 = (Button) findViewById(R.id.backColour1);
        backColour2 = (Button) findViewById(R.id.backColour2);
        backColour3 = (Button) findViewById(R.id.backColour3);
        backColour4 = (Button) findViewById(R.id.backColour4);
        
        /* Set button listeners for all of the colour buttons */
        textColour1.setOnClickListener(this);
        textColour2.setOnClickListener(this);
        textColour3.setOnClickListener(this);
        textColour4.setOnClickListener(this);
        backColour1.setOnClickListener(this);
        backColour2.setOnClickListener(this);
        backColour3.setOnClickListener(this);
        backColour4.setOnClickListener(this);
        
        /* Set the colour of all the buttons */
        textColour1.setBackgroundColor(settObj.getBackColour(colourArray[0]));
        textColour2.setBackgroundColor(settObj.getBackColour(colourArray[1]));
        textColour3.setBackgroundColor(settObj.getBackColour(colourArray[2]));
        textColour4.setBackgroundColor(settObj.getBackColour(colourArray[3]));
        backColour1.setBackgroundColor(settObj.getBackColour(colourArray[0]));
        backColour2.setBackgroundColor(settObj.getBackColour(colourArray[1]));
        backColour3.setBackgroundColor(settObj.getBackColour(colourArray[2]));
        backColour4.setBackgroundColor(settObj.getBackColour(colourArray[3]));
        
        /* Store the Text colour buttons in an array */
        textButtonArray = new Button[4];
        textButtonArray[0] = textColour1;
        textButtonArray[1] = textColour2;
        textButtonArray[2] = textColour3;
        textButtonArray[3] = textColour4;
        
        /* Store the background colour buttons in an array */
        backButtonArray = new Button[4];
        backButtonArray[0] = backColour1;
        backButtonArray[1] = backColour2;
        backButtonArray[2] = backColour3;
        backButtonArray[3] = backColour4;
        
        
        /* Update the text colour */
        updateTextColour();
    }
    
    /** 
     * Function: To update the colour of the text based on the user preferences  
     * 
     * */
    public void updateTextColour()
    {
    	text.setTextColor(settObj.getTextColour());
    	text.setBackgroundColor(settObj.getBackColour());
    }
    
    /** 
     * Function: To remove the x which marks the colour which is currently selected  
     * 
     * */
    public void removeAllX(String type)
    {
    	
    	for(int i=0; i < textButtonArray.length; i++)
		{
			if(type == "text")
			{
				textButtonArray[i].setText("");
			}
			
			if(type == "back")
			{
				backButtonArray[i].setText("");
			}
		}
    }

    /** 
     * Listener for clicks to the buttons 
     * 
     * */
	@Override
	public void onClick(View v) 
	{
		for(int i=0; i < textButtonArray.length; i++)
		{
			/* if button clicked is a text colour button */
			if(v == textButtonArray[i])
			{
				/* remove all X markers and set the button clicked
				   to contain an X marker */
				removeAllX("text");
				textButtonArray[i].setText("X");
				
				/* Set the text colour in the preferences and then update the text colour */
				settObj.setTextColour(colourArray[i]);
				updateTextColour();
			}
			/* if button clicked is a background colour button */
			if(v == backButtonArray[i])
			{
				/* remove all X markers and set the button clicked
				   to contain an X marker */
				removeAllX("back");
				backButtonArray[i].setText("X");
				
				/* Set the background colour in the preferences and then update the text colour */
				settObj.setBackColour(colourArray[i]);
				updateTextColour();
			}
		}
		
	}
	
	/** 
     * Function which is called when the Help menu item is pressed 
     * 
     * */
	public void goToHelp() 
    {
    	Intent myIntent = new Intent(this, HelpActivity.class);
        startActivity(myIntent);
    }
	
	/** 
     * Function which is called when the Home menu item button is pressed 
     * 
     * */
    public void goToMain() 
    {	
    	Intent myIntent = new Intent(this, MainActivity.class);
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
        inflater.inflate(R.menu.colours_menu, menu);
        
        return true;
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
                
            case R.id.help:
                goToHelp();
                return true;
                
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
