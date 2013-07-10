package com.robertscally.fypapp;

/**
 * Description: 
 * 		This is the main / home activity
 * 
 * Author: 
 * 		Robert Scally except parts stated below
 * 
 * Date:
 * 		9 April 2013
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.analytics.tracking.android.EasyTracker;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

public class MainActivity extends Activity 
{
	public static String SD_FILE_PATH;
	public static File mediaStorageDir;
	
	/* These variables are related to the camera function */
	public static final int MEDIA_TYPE_IMAGE = 1;
	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
	private static final int GALLERY_PHOTO = 101;
	
	private Uri fileUri;
	private Intent cameraIntent = new Intent();
	private static File mediaFile;
	private IpAddressValidator ipMan;
	private LinearLayout linearLay;
	private View view;
	private static Context context;
	private static boolean decision = true;
	private static String filePathString;
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
       
        context = this;
        
        view = new View(MainActivity.this);
        
        ipMan = new IpAddressValidator(); 
        
        mediaStorageDir = new File(Environment.getExternalStorageDirectory(), "OcrAppPhotos");
    	
    	linearLay = (LinearLayout)findViewById(R.id.progressNotifyBox);
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
        	finish(); // finish this activity
        }
        
        return super.onKeyDown(keyCode, event);
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
     * Function: Used to display a warning message if the SD CARD is not 
     * present. The SD CARD is required to save the image which is captured
     * 
     * */
    private static boolean displayWarning(String type) 
    {
    	
    	String title = null;
    	String message = null;
    	
    	if(type.equalsIgnoreCase("sdcard"))
    	{
    		title = "Sorry";
    		message = "The SD Card or internal memory appears to be missing. Please fix this problem and try again";
    	}
    	
    	
    	AlertDialog.Builder builder = new AlertDialog.Builder(context);
    	
		builder.setMessage(message)
		.setTitle(title)
        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) 
            {
            	decision = true;
            }
        })
        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) 
            {
            	decision = false;
            }
        });
		
		/* create the dialog */
		AlertDialog dialog = builder.create();
		
		/* show the connection error message dialog */
		dialog.show();
		
		return decision;
	}

	/** 
	 * Function which is called when the Settings button is pressed 
	 * 
	 * */
    public void goToPrefs(View view) 
    {
    	Intent myIntent = new Intent(this, PreferanceActivity.class);
        startActivity(myIntent);
    }
    
    /** 
     * Function which is called when the Help button is pressed 
     * 
     * */
    public void goHelp(View view) 
    {
    	Intent myIntent = new Intent(this, HelpActivity.class);
        startActivity(myIntent);
    }
    
    /** 
     * Function which is called when the Sample Text button is pressed 
     * 
     * */
    public void goTest(View view) 
    {
    	Intent myIntent = new Intent(this, DisplayResultTextActivity.class);
    	myIntent.putExtra("text", "With the deep, unconscious sigh which not even the nearness of the telescreen could prevent him from uttering when his day's work started, Winston pulled the speakwrite toward him, blew the dust from its mouthpiece, and put on his spectacles. Then he unrolled and clipped together four small cylinders of paper which had already flopped out of the pneumatic tube on the right-hand side of his desk In the walls of the cubicle there were three orifices. To the right of the speakwrite, a small pneumatic tube for written messages; to the left, a larger one for newspapers; and in the side wall, within easy reach of Winston's arm, a large oblong slit protected by a wire grating. This last was for the disposal of waste paper. Similar slits existed in thousands or tens of thousands throughout the building, not only in every room but at short intervals in every corridor. For some reason they were nicknamed memory holes. When one knew that any document was due for destruction, or even when one saw a scrap of waste paper lying about, it was an automatic action to lift the flap of the nearest memory hole and drop it in, whereupon it would be whirled away on a current of warm air to the enormous furnaces which were hidden somewhere in the recesses of the building.");
        startActivity(myIntent);
    }
    
    /** 
     * Function: which is called when the Gallery button is pressed 
     * this function allows user to select an image from their gallery
  	 * 
     */
    public void goGallery(View view) 
    {
	    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
	    photoPickerIntent.setType("image/*");
	    startActivityForResult(photoPickerIntent, GALLERY_PHOTO);   
    }
    
    /** Function: which is called when the Camera button is pressed 
     * 
     *  This function was sourced from: http://developer.android.com/guide/topics/media/camera.html
     * */
    public void goToCamera(View view) 
    {		
    	// create Intent to take a picture and return control to the calling application
        cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE); // create a file to save the image
        
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name

        // start the image capture Intent
        startActivityForResult(cameraIntent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
    }
    
    /** 
     * Function to send text returned from server to the text display activity 
     * 
     * */
    public void sendTextToDisplay(String text) 
    {
	    Intent myIntent = new Intent(this, DisplayResultTextActivity.class);
	    myIntent.putExtra("text", text);
	    startActivity(myIntent); 
    }
    
    /** 
     * Function to send text returned from server to the text display activity 
     * 
     * */
    public void goToUploadImageActivity(File aMediaFile) 
    {
    	String filePath;
    	
    	/* Get the absolute path of the file to be uploaded */
    	filePath = aMediaFile.getAbsolutePath();
    	
	    Intent myIntent = new Intent(this, UploadImageActivity.class);
	    myIntent.putExtra("path", filePath);
	    startActivity(myIntent); 
    }
    
    /** 
     * Function: Create a file Uri for saving an image or video 
     * 
     * This function was sourced from: http://developer.android.com/guide/topics/media/camera.html
     * 
     * */
    private static Uri getOutputMediaFileUri(int type)
    {
    	try
    	{
    		return Uri.fromFile(getOutputMediaFile(type));
    	}
    	catch(NullPointerException e)
    	{
    		Log.d("NULL POINTER","Null pointer at media file");
    		
    		return null;
    	}
    }
    
    /** 
     * Function: Create a File for saving an image or video 
     * 
     * This function was sourced from: http://developer.android.com/guide/topics/media/camera.html
     * */
    private static File getOutputMediaFile(int type)
    {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
    	
    	Log.d("External state:",Environment.getExternalStorageState());

    	/* check if the SD CARD is mounted */
    	if(Environment.getExternalStorageState().equalsIgnoreCase("mounted"))
    	{
	        // This location works best if you want the created images to be shared
	        // between applications and persist after your app has been uninstalled.
	
	        // Create the storage directory if it does not exist
	        if (! mediaStorageDir.exists())
	        {
	            if (! mediaStorageDir.mkdirs())
	            {
	                Log.d("OCRApp", "failed to create directory");
	                
	                return null;
	            }
	            else 
	            {
	            	Log.d("OCRApp", "Success: Directory created");
	            }
	        
	        }
	
	        /* Create a media file name using a timestamp */ 
	        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
	        
	        if (type == MEDIA_TYPE_IMAGE)
	        {
	            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
	            "IMG_"+ timeStamp + ".jpg");
	            
	            filePathString = mediaFile.getAbsolutePath();
	        }
	        else 
	        {
	            return null;
	        }
	        
	        return mediaFile;
    	}
    	else
    	{
    		displayWarning("sdcard");
    		
    		return null;
    	}
    }

    /** Function
     * 
     *  This function was source from: http://developer.android.com/guide/topics/media/camera.html
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) 
    {
    	if (requestCode == GALLERY_PHOTO)
    	{
    		
            if(resultCode == RESULT_OK)
            {  
            	/* following from source: http://stackoverflow.com/a/2508138 
            	   ===== BEGINNING ======= */
            	Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                Cursor cursor = getContentResolver().query(
                                   selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String filePath = cursor.getString(columnIndex);
                cursor.close();

                mediaFile = new File(filePath);
                
                goToUploadImageActivity(mediaFile);
                
                /* ===== END OF SOURCE: http://stackoverflow.com/a/2508138 ======= */
            }
    	}
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) 
        {
            if (resultCode == RESULT_OK) 
            {       
               try 
               {
            	    /* Save Image */
            	    new FileInputStream(Environment.getExternalStorageDirectory().toString()+"/OcrAppPhotos/"+mediaFile.getName());
            	   
            	    /* proceed to upload the image using the UploadImageActivity */
	        	    goToUploadImageActivity(mediaFile);
	        	    		
            	  } 
                  catch (FileNotFoundException e) 
            	  {
                	  
            	  } 
               	  catch (IOException e) 
            	  {
               		  e.printStackTrace();
            	  }
            } 
        }
    }
    
    /** 
	 * Function to create a menu 
	 * 
	 * */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) 
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_screen_menu, menu);
        
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
            case R.id.settings:
                goToPrefs(view);
                return true;
                
            case R.id.sample:
                goTest(view);
                return true;
            
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
