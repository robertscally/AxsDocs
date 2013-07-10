package com.robertscally.fypapp;

/**
 * Description: 
 * 		This class is used to check for internet connection
 * 		and upload the image to the server
 * 
 * Author: 
 * 		Robert Scally except for parts stated below
 * 
 * Date:
 * 		9 April 2013
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import com.google.analytics.tracking.android.EasyTracker;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

public class UploadImageActivity extends Activity 
{
	/* These two variables are related to the camera function */
	public static final int MEDIA_TYPE_IMAGE = 1;
	
	private String serverUrl;
	private Intent cameraIntent = new Intent();
	private static File mediaFile;
	private String testString; 
	private NetworkInfo mWifi, m3G;
	private ConnectivityManager connManager;
	private IpAddressValidator ipMan;
	private ProgressBar progressBar;
	private LinearLayout linearLay;
	private View view;
	private String filePath;
	private static Context context;
	private static boolean decision = true;
	private AlertDialog dialog;
	
	/** When the activity is created */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.uploading_layout);
       
        /* variable to store data which is sent to this activity 
           as part of an intent  */
        Bundle extras = getIntent().getExtras();
        
        /** 
         * 
         * Next lines relating to handling intents from other applications 
         * BEGIN FROM SOURCE: Modified from http://developer.android.com/training/sharing/receive.html#update-manifest 
         * 
         * */
        /* Get intent, action and MIME type */
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        // if another activity has sent a file and the type is not null
        if (Intent.ACTION_SEND.equals(action) && type != null) 
        {	
        	// if the type begins with image/ , i.e. image/jpeg
            if (type.startsWith("image/")) 
            {
            	Log.d("SHARE","Someone shared an image!");
            	
                try 
                {
					mediaFile = handleSendImage(intent);
				} 
                catch (URISyntaxException e) 
				{
					e.printStackTrace();
				} 
            }
        } 
        else 
        {
            // Handle other intents, such as being started from the home screen
        	if (extras != null) 
            {
                filePath = extras.getString("path");
                
                mediaFile = new File(filePath);
            }
        }
        
        /** ======= END OF SOURCE FROM http://developer.android.com/training/sharing/receive.html#update-manifest ====== */
        
        /* Store the context of this class */
        context = this;
        
        view = new View(UploadImageActivity.this);
        ipMan = new IpAddressValidator(); 
    	linearLay = (LinearLayout)findViewById(R.id.progressNotifyBox);
    	
    	
    	/*  Try to upload the image which was recieved	*/
    	try 
    	{
			initiateUploading();
		} 
    	catch (IOException e) 
    	{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
    
    /** Function: Goes to the main / home screen */
    public void goToMain() 
    {	
    	Intent myIntent = new Intent(this, MainActivity.class);
        startActivity(myIntent);
    }
    
    /** 
     * Function: Handle image sent to this activity
     * 
     * BEGIN FROM SOURCE: Modified from http://developer.android.com/training/sharing/receive.html#update-manifest 
     *
     * */
    public File handleSendImage(Intent intent) throws URISyntaxException 
    {
    	Uri imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
        
        Log.d("SHARE",imageUri.toString());
        
        if (imageUri != null) 
        {
            mediaFile = new File(getPath(imageUri));
            
            return mediaFile;
        }
        
        return mediaFile;
    }
    
    /** ======= END OF SOURCE FROM http://developer.android.com/training/sharing/receive.html#update-manifest ====== */
    
    
    
    /** 
     * Function: Used to get the absolute file path of
     * an image which has been received by the activity
     * through a share intent.
     * 
     * Source of this whole function from: http://stackoverflow.com/a/3414749
     * 
     * */
    public String getPath(Uri uri) 
    {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        
        return cursor.getString(column_index);
    }
    
    /** When the activity is resumed	*/
    @Override
    public void onResume() 
    {
		Log.d("TEST","OnResume has been called!");
		
		super.onResume();
    }
    
    /** 
     * Function: Used to get an update on the status of 
     * the network connection. Whether it is connected or not.
     * Returns a true or false.
     * 
     * */
    public boolean updateConnectionStatus()
    {
    	/* Connection manager to get network information on the status
    	   of the wifi or 3G (mobile) connection	*/
    	connManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
    	mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
    	m3G = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
    	
    	// variable to store state of connection
    	boolean connectionOK = true;
    	
    	/* Check if WIFI or 3G (mobile) is connected and set accordingly */
    	if(mWifi.isConnected() || m3G.isConnected()) 
    	{
    		connectionOK = true;
    	}
    	else if(!mWifi.isConnected() || !m3G.isConnected()) 
    	{
    		connectionOK = false;
    	}
    	
    	return connectionOK;
    }
    
    /** 
     * 
     * Function: Used to get the IP Address of the server.
     * The reason for this was because the server IP changed 
     * regularily so users who already installed the app would
     * still work by dynamically requesting the IP address
     * 
     * */
    public boolean getServerIPAddress() 
    {
    	boolean serverOK = true;
    	
    	/* If the connection to the network is enabled 
    	   then check the IP of the server */
    	if(updateConnectionStatus())
    	{
		    HttpClient httpClient = new DefaultHttpClient();
		    HttpContext localContext = new BasicHttpContext();
		    HttpGet httpGet = new HttpGet("http://www1.robertscally.me/serverip.php");
		    
		    HttpResponse response = null;
			
		    try 
		    {
				response = httpClient.execute(httpGet, localContext);
			} 
			catch (ClientProtocolException e) 
			{
				e.printStackTrace();
			} 
		    catch (IOException e) 
			{
				e.printStackTrace();
			}
		     
		    BufferedReader reader = null;
		    
		    /* Try to read the input returned from the server */
			try 
			{
				reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			} 
			catch (IllegalStateException e) 
			{
				e.printStackTrace();
			} 
			catch (NullPointerException e) 
			{
				e.printStackTrace();
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}
		    
			/* Store the first line returned which is the IP address */
		    try 
		    {
				serverUrl = reader.readLine();
			} 
		    catch (IOException e) 
		    {
				e.printStackTrace();
			}
		    
		    /* If server url is not null then remove all the spaces
		       from the string  */
		    if(serverUrl != null)
		    {
		    	serverUrl = serverUrl.replaceAll("\\s","");
		    	
		    	/* Check if the URL is valid, then set
		    	   serverOK to be true */
		    	if(ipMan.validate(serverUrl))
	        	{
					serverOK = true;
					
					Log.d("TEST","Server IP address:"+serverUrl);
	        	}
			    else
			    {
			    	serverOK = false;
			    }
		    }	
		    else
		    {
		    	serverOK = false;
		    }
    	}
    	
		return serverOK;
	}
    
    /** 
     * Function: Used to display a connection warning in the 
     * form of a popup message depending on the type of error 
     * supplied to the function.
     * 
     * */
    private boolean displayConnectionWarning(String type) 
    {
    	/* Variables to store the title and message to be displayed */
    	String title = null;
    	String message = null;
    	
    	/* Different types of messages for different warnings and errors */
    	if(type.equalsIgnoreCase("sdcard"))
    	{
    		title = "Sorry";
    		message = "The SD Card or internal memory appears to be missing. Please fix this problem and try again";
    	}
    	if(type.equalsIgnoreCase("server"))
    	{
    		title = "Sorry";
    		message = "The server is not responding. Please try again later.";
    	}
    	if(type.equalsIgnoreCase("wifi"))
    	{
    		title = "Sorry no wifi connected";
    		message = "Wifi will be required to send images. Please enable your wifi and ensure it is connected";
    	}
    	if(type.equalsIgnoreCase("mobile"))
    	{
    		title = "Sorry no mobile data connection";
    		message = "An internet connection will be required to send images. Please enable your internet and ensure it is connected";
    	}
    	if(type.equalsIgnoreCase("mobile_warning"))
    	{
    		title = "Warning: Mobile data";
    		message = "You are about to send a photo using mobile data connection. This can be expensive. Do you want to continue?";
    	}
    	
    	AlertDialog.Builder builder = new AlertDialog.Builder(context);
    	
		builder.setMessage(message)
		.setTitle(title)
        .setPositiveButton("OK", new DialogInterface.OnClickListener() 
        {
        	/* if the user has clicked "Ok" then the decision is set to true
        	   and the user is redirected to the main / home activity */
            public void onClick(DialogInterface dialog, int id) 
            {
            	decision = true;
            	
            	/* go to home / main */
            	goToMain();
            }
        })
        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() 
        {
        	/* if the user has clicked "Cancel" then the decision is set to true
        	   and the user is redirected to the main / home activity */
            public void onClick(DialogInterface dialog, int id) 
            {
            	decision = false;
            }
        });
		
		/* create the dialog */
		dialog = builder.create();
		
		/* show the connection error / warning message dialog */
		dialog.show();
			
		return decision;
	}
    
    /** 
     * Function: Sends text returned from server to the text display activity 
     * 
     * */
    public void sendTextToDisplay(String text) 
    {
	    Intent myIntent = new Intent(this, DisplayResultTextActivity.class);
	    myIntent.putExtra("text", text);
	    startActivity(myIntent); 
    }
    
    /** 
     * 
     * Function: To initiate the file upload process. 
     * 
     * */
    public void initiateUploading() throws IOException 
    {
    	// Start the check server task on a new thread.
        new CheckServerTask().execute();
		
    }
    
    /** 
     * Function to place the sending image to server on a seperate thread to 
     * the main thread so that the application does not hang. 
     * 
     * */
    private class PostImageTask extends AsyncTask<File, Integer, String>
    {
    	boolean connectionError = false;
    	
    	/* After the Task has been executed */
        protected void onPostExecute(String text) 
        {   
        	// make the loading screen disappear
            linearLay.setVisibility(View.GONE);
        	
		    // if there is no connection error
		    if(!connectionError)
		    {
		    	// send the text to be displayed to the user
		        sendTextToDisplay(text);
		    }
		    else
		    {
		    	// if there is a connection error, then display a message
		        displayConnectionWarning("server");
		    }
        }

        /* Before the Task has been executed */
        @Override
        protected void onPreExecute() 
        {
	     // set the loading screen to be visible
             linearLay.setVisibility(View.VISIBLE);
        }
        
        @Override
        protected void onProgressUpdate(Integer... values) 
        {
        	progressBar.setProgress(values[0]);
        }
        
        /* Do this task in the background */
        @Override
		protected String doInBackground(File... mediaFile) 
		{
		    PostImageClass postImageObj = new PostImageClass();
	    	    
		    /* Send image to the server using the IP address retrieved previously */
	    	try 
		    {
	    	    testString = postImageObj.sendImage("http://"+serverUrl+"/ocrserver/processUpload.php", mediaFile[0]);
		    } 
		    catch (ClientProtocolException e) 
		    {
		    	e.printStackTrace();
		    } 
		    catch (IOException e) 
		    {
				connectionError = true;
				e.printStackTrace();
		    }
	    	    
	        return testString;
		}
    }
    
    /** 
     * Function to place the sending image to server on a seperate thread to 
     * the main thread so that the application does not hang.
     * Modified from source: http://developer.android.com/reference/android/os/AsyncTask.html */
    private class CheckServerTask extends AsyncTask<Void, Void, Boolean>
    {		
    	protected void onPostExecute(Boolean state) 
        {   
    		/* If the server is live and returned a state of true
    		   then begin the image upload task */
        	if(state == true)
        	{
        		new PostImageTask().execute(mediaFile);
        	}
        	/* else display a warning */
        	else
        	{
        		displayConnectionWarning("server");
        	}
        }
    	
    	/* Before the Task has been executed */
        @Override
        protected void onPreExecute() 
        {

        }
        
        @Override
        protected void onProgressUpdate(Void... arg0) 
        {

        }
        
		@Override
		protected Boolean doInBackground(Void... arg0) 
		{
			boolean state = true;
			
			/* get server IP address. If it is a success, true will
			   be returned */
			state = getServerIPAddress();
				
			/* If the server url is not null, then validate
			   the IP address to ensure it is valid	*/
			if(serverUrl != null)
	    	{
	    		if(ipMan.validate(serverUrl))
	            {
	    			/* if the IP address is valid then the state
	    			   is set to true */
	    			state = true;
	            }
	    	}
	    	else
	    	{
	    		state = false;
	    	}
	            	
			return state;
		}
    }
}
