package com.robertscally.fypapp;

/**
 * Description: 
 * 		This class is used to post the image to the server
 * 
 * Author: 
 * 		Source: http://stackoverflow.com/questions/4126625/how-to-send-a-file-in-android-from-mobile-to-server-using-http
 * 
 * Date:
 * 		-----------
 */
/**
 *  This entire class was sourced from the following URL:
 *  
 *  Source: http://stackoverflow.com/questions/4126625/how-to-send-a-file-in-android-from-mobile-to-server-using-http
 *	
 *	Comments were added by me
 */

import java.io.File;
import java.io.IOException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import android.util.Log;
	
public class PostImageClass
{
	private String responseString = "Null String! Error";

	private HttpEntity resEntity;
	
	
	public PostImageClass()
    {
		
    }
	
	/** 
	 * Function: To send an image to the server 
	 * 
	 *  */
	public String sendImage(String urlString, File aFile) throws ClientProtocolException, IOException
    {
		Log.d("TEST","Image sending started .....");
		
		// the url of the server
		String url = urlString;
		
		// the image file which needs to be sent to the server
		File file = aFile;
		
		HttpResponse response = null;
		
		
		HttpParams httpParameters = new BasicHttpParams();
	    HttpConnectionParams.setConnectionTimeout(httpParameters, 60000);
	    HttpConnectionParams.setSoTimeout(httpParameters, 60000);
	    
		HttpClient httpclient = new DefaultHttpClient(httpParameters);
	    httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);

	    
	    HttpPost httppost = new HttpPost(url);

	    MultipartEntity mpEntity = new MultipartEntity();
	    ContentBody cbFile = new FileBody(file, "image/jpeg");
	    
	    
	    mpEntity.addPart("file", cbFile);


	    httppost.setEntity(mpEntity);
	    System.out.println("executing request " + httppost.getRequestLine());
	    
	    
	    // the response from the http request
		response = httpclient.execute(httppost);
		
		
		int code = response.getStatusLine().getStatusCode(); 
		String message = response.getStatusLine().getReasonPhrase();
		
		System.out.println(message);
		
		if(response.getEntity() == null)
		{
			Log.d("TEST","Entity is null");
			resEntity = null;
		}
		else
		{
			resEntity = response.getEntity();
		}
		

	    System.out.println(response.getStatusLine());
	    
	    
	    if (resEntity != null) 
	    {
	    	try 
	    	{
	    		responseString = EntityUtils.toString(resEntity);
	    		System.out.println(responseString);
			} 
		    catch (ParseException e) 
		    {
				e.printStackTrace();
			} 
		    catch (IOException e) 
		    {
				e.printStackTrace();
			}
	    }
	    
	    if (resEntity != null) 
	    {
	    	try 
	    	{
	    		resEntity.consumeContent();
			} 
	    	catch (IOException e) 
	    	{
				e.printStackTrace();
			}
	    }

	    /* shutdown the http connection */
	    httpclient.getConnectionManager().shutdown();
	    
	    /* return the string of text to display */
		return responseString;
    }	
}
