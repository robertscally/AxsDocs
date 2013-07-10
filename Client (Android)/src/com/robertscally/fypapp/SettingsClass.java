package com.robertscally.fypapp;

/**
 * 
 * Description: 
 * 		This class is used to get and set the different settings 
 * 		available to the user from the preferences.
 * 
 * Author: 
 * 		Robert Scally
 * 
 * Date:
 * 		9 April 2013
 * 
 */
 
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.util.Log;
 
public class SettingsClass 
{
	private SharedPreferences sharedPref;
	private Editor prefEditor;
	private Context context;
	private String[] colourArray;
	
    public SettingsClass(Context aContext)
    {
    	context = aContext;
    	
    	// Get users shared preferences
        sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        
        // array of resource ID's 
        colourArray = context.getResources().getStringArray(R.array.backColourValues);
        
    }
    
    /**
     *  
     * Function: Set the background colour in the shared preferances
     * for the named colour provided
     * 
     *  */
    public void setStringBackColour(String colour)
    {
		prefEditor = sharedPref.edit();
        prefEditor.putString("backColourPref", colour);
        prefEditor.commit();
    }
    
	/** 
	 * Function: to return the background colour from the shared preferances 
	 * 
	 * */
    public int getBackColour()
    {
    	int color = 0;
    	
    	String backColourPref;
    	
    	backColourPref = sharedPref.getString("backColourPref", "");
    	
    	if(backColourPref.equalsIgnoreCase("creme"))
    	{
    		Log.d("Colour","Creme");
    		color = context.getResources().getColor(R.color.creme);
    	}
    	if(backColourPref.equalsIgnoreCase("yellow"))		
    	{
    		Log.d("Colour","Yellow");
    		color = context.getResources().getColor(R.color.yellow);
    	}
    	if(backColourPref.equalsIgnoreCase("black"))		
    	{
    		Log.d("Colour","Black");
    		color = context.getResources().getColor(R.color.black);
    	}
    	if(backColourPref.equalsIgnoreCase("white"))		
    	{
    		Log.d("Colour","White");
    		color = context.getResources().getColor(R.color.white);
    	}
    	
		return color;
    }
    
    /** 
     * 
     * Function: Get the background colours integer resource value for the
     * colour name provided
     *  
     * Input: A string variable of a colour name
     *  
     * */
    public int getBackColour(String nameColour)
    {
    	int color = 0;
    	
    	String backColourPref;
    	
    	backColourPref = nameColour;
    	
    	
       	if(backColourPref.equalsIgnoreCase("creme"))
    	{
    		Log.d("Colour","Creme");
    		
    		// get resource ID for the colour 
    		color = context.getResources().getColor(R.color.creme);
    	}
    	if(backColourPref.equalsIgnoreCase("yellow"))		
    	{
    		Log.d("Colour","Yellow");
    		
    		// get resource ID for the colour 
    		color = context.getResources().getColor(R.color.yellow);
    	}
    	if(backColourPref.equalsIgnoreCase("black"))		
    	{
    		Log.d("Colour","Black");
    		
    		// get resource ID for the colour 
    		color = context.getResources().getColor(R.color.black);
    	}
    	if(backColourPref.equalsIgnoreCase("white"))		
    	{
    		Log.d("Colour","White");
    		
    		// get resource ID for the colour 
    		color = context.getResources().getColor(R.color.white);
    	}
    	
		return color;
    }
    
    /** 
     * 
     * Function: Get the background colour from the shared preferances 
     * 
     * */
    public int getTextColour()
    {
    	int color = 0;
    	
    	String textColorPref;
    	
    	textColorPref = sharedPref.getString("textColorPref", "white");
    	
    	
    	if(textColorPref.equalsIgnoreCase("creme"))
    	{
    		Log.d("Colour","Creme");
    		color = context.getResources().getColor(R.color.creme);
    	}
    	if(textColorPref.equalsIgnoreCase("yellow"))		
    	{
    		Log.d("Colour","Yellow");
    		color = context.getResources().getColor(R.color.yellow);
    	}
    	if(textColorPref.equalsIgnoreCase("black"))		
    	{
    		Log.d("Colour","Black");
    		color = context.getResources().getColor(R.color.black);
    	}
    	if(textColorPref.equalsIgnoreCase("white"))		
    	{
    		Log.d("Colour","White");
    		color = context.getResources().getColor(R.color.white);
    	}
    	
		return color;
    }
    
    /** 
     * 
     * Function: Set the next Text colour in the colour array 
     *  
     * */
    public void setNextTextColour() 
    {
    	String nameColour = null;
    	
    	int currentColour = 0;
    	
    	String textColorPref = sharedPref.getString("textColorPref", "white");
    	
    	nameColour = textColorPref;
    	
    	
    	for(int i = 0; i < colourArray.length; i++)
    	{
    		if(textColorPref.equalsIgnoreCase(colourArray[i]))
    		{
    			currentColour = i;
    		}
    	}
    	
    	/* If the next colour is larger than the array then
    	   reset the colour to the first element in the array */
    	if((currentColour+1) < colourArray.length)
    	{
    		nameColour = colourArray[currentColour+1];
    		currentColour++;
    	}
    	else
    	{
    		nameColour = colourArray[0];
    		currentColour = 0;
    	}
    	
    	String backColourPref = sharedPref.getString("backColourPref", "");
    	
    	/* This conditional statement checks if the current 
 	   	   colours are the same, if they are then the next colour in
 	       the array is selected */
    	if(nameColour.equalsIgnoreCase(backColourPref))
    	{
    		
    		if((currentColour+1) < colourArray.length) 
        	{
        		nameColour = colourArray[currentColour+1];
        		currentColour++;
        	}
        	else
        	{
        		nameColour = colourArray[0];
        		currentColour = 0;
        	}
    	}
    	
    	/* Store the preference */
    	prefEditor = sharedPref.edit();
        prefEditor.putString("textColorPref", nameColour);
        prefEditor.commit();
	}
    
    /** 
     * 
     * Function: Set the text colour in the shared preferences 
     * Input: A string variable of a colour name
     *  
     * */
    public void setTextColour(String nameColour) 
    {
    	/* Store the preference */
    	prefEditor = sharedPref.edit();
        prefEditor.putString("textColorPref", nameColour);
        prefEditor.commit();
	}
    
    public void setBackColour(String nameColour) 
    {
    	/* Store the preference */
    	prefEditor = sharedPref.edit();
        prefEditor.putString("backColourPref", nameColour);
        prefEditor.commit();
	}
    
    /** 
     * Function: Get the next background colour from the array of colours 
     * 
     * */
    public void setNextBackColour() 
    {
    	String nameColour = null;
    	
    	int currentColour = 0;
    	
    	String backColourPref = sharedPref.getString("backColourPref", "");
    	
    	nameColour = backColourPref;
    	
    	for(int i = 0; i < colourArray.length; i++)
    	{
    		if(backColourPref.equalsIgnoreCase(colourArray[i]))
    		{
    			currentColour = i;
    		}
    	}
    	
    	/* If the next colour is larger than the array then
 	   	   reset the colour to the first element in the array */
    	if((currentColour+1) < colourArray.length)
    	{
    		nameColour = colourArray[currentColour+1];
    		currentColour++;
    	}
    	else
    	{
    		nameColour = colourArray[0];
    		currentColour = 0;
    	}
    	
    	String textColourPref = sharedPref.getString("textColorPref", "");
    	
    	/* This conditional statement checks if the current 
    	   colours are the same, if they are then the next colour in
    	   the array is selected */
    	if(nameColour.equalsIgnoreCase(textColourPref))
    	{
    		
    		if((currentColour+1) < colourArray.length) 
        	{
        		nameColour = colourArray[currentColour+1];
        		currentColour++;
        	}
        	else
        	{
        		nameColour = colourArray[0];
        		currentColour = 0;
        	}
    	}
    	
    	/* Store the preference */
    	prefEditor = sharedPref.edit();
        prefEditor.putString("backColourPref", nameColour);
        prefEditor.commit();
	}
    
    /** 
     * 
     * Function: Set the font face in the shared preferences 
     * 
     * */
    public void setFontFace(String face) 
    {
    	/* Store the preference */
    	prefEditor = sharedPref.edit();
        prefEditor.putString("fontFacePref", face);
        prefEditor.commit();
	}
    
    /** 
     * 
     * Function: Get the font face from the shared preferences 
     * 
     * */
    public Typeface getFontFace()
    {
    	Typeface font = null;
    	
    	String fontFacePref = sharedPref.getString("fontFacePref", "verdana");
    	
    	if(fontFacePref.equalsIgnoreCase("century gothic"))
    	{
    		font = Typeface.createFromAsset(context.getAssets(), "fonts/century_gothic.ttf");
    	}
    	if(fontFacePref.equalsIgnoreCase("tahoma"))
    	{
    		font = Typeface.createFromAsset(context.getAssets(), "fonts/tahoma.ttf");
    	}
    	if(fontFacePref.equalsIgnoreCase("verdana"))
    	{
    		font = Typeface.createFromAsset(context.getAssets(), "fonts/verdana.ttf");
    	}
    	if(fontFacePref.equalsIgnoreCase("open dyslexic"))
    	{
    		font = Typeface.createFromAsset(context.getAssets(), "fonts/OpenDyslexic-Regular.otf");
    	}
    	if(fontFacePref.equalsIgnoreCase("Tiresias PC"))
    	{
    		font = Typeface.createFromAsset(context.getAssets(), "fonts/Tiresias-PCfont.ttf");
    	}
    	
		return font;
    }
    
    /** 
     * 
     * Function: Get the text size from the shared preferences 
     * 
     * */
    public int getTextSize()
    {
    	String fontSizePref = sharedPref.getString("fontSizePref", "20");
    	
    	int fontSizePref2 = 0;
    	
    	if(fontSizePref != null)
    	{
    		fontSizePref2 = Integer.parseInt(fontSizePref);
    	}
    	else 
    	{
    		fontSizePref2 = 1;
    	}
    	
		return fontSizePref2;
    }

    /** 
     * Function: Get the text weight from the shared preferences 
     * 
     * */
	public int getTextWeight() 
	{
		boolean boldTextPref = sharedPref.getBoolean("boldTextPref", false);
		
		int bold = 0;
		
		if(boldTextPref == true)
			bold = Typeface.BOLD;
		
		return bold;
	}
 
}