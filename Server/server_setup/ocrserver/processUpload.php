<?php

/** 
 * Description:
 * 		Class used to process the uploaded file
 * 
 * Author:
 * 		Robert Scally unless where otherwise stated
 * 
 * */

/* Include the SpellChecker class */
include('spellcheck.php');

/* Create a SpellChecker class object */
$spellObj = new SpellChecker();

/* Get the root of the website */
define ('SITE_ROOT', realpath(dirname(__FILE__))); // source: http://www.w3schools.com/php/php_file_upload.asp

/* Creates a file name with the current time and appends a jpg file extension */
$filename = time() . ".jpg";


$allowedExts = array("jpg", "JPG", "JPEG","jpeg", "gif", "png","tif");

$extension = explode(".", $filename);

/* Error variable used to store if there is an error, 1, or not , 0 */
$error = 0;

/* Check if valid file type */
if($_FILES["file"]["type"] != "image/jpeg")
{
	/* If file type is not a jpeg image then set error */
	if($_FILES["file"]["type"] != "image/jpg")
	{
		echo "Not a valid file type";
		$error = 1;
	}
}

/* Check if valid file size, i.e. less than or equal to 10 MB */
if($_FILES["file"]["size"] > 10000000)
{
	/* Set error if file is bigger than 10 MB */
	echo "File size greater than 10 MB";
	$error = 1;
}

/* Check for valid file extension */
if($extension[1] != "jpg")
{
	/* Set error if file extension is not valid */
	echo "File extension not valid";
	$error = 1;
}
/* If any error has not been set based on previous validations, continue to processing */
if($error == 0)
{
	/* Check if there is an error in the file 
	   This if has been sourced from // source: http://www.w3schools.com/php/php_file_upload.asp */
	if ($_FILES["file"]["error"] > 0)
  	{
    	echo "Return Code: " . $_FILES["file"]["error"] . "<br>";
    }
  	else
    {
    	if (file_exists("upload/" . $filename))
    	{
    		  echo $filename . " already exists. ";
    	}
    	else
    	{
    		  /* Move the uploaded file from the Temp directory to the
    		     upload folder */
    		  // Following line source: http://www.w3schools.com/php/php_file_upload.asp
		      move_uploaded_file($_FILES["file"]["tmp_name"], SITE_ROOT . "/upload/" . $filename);	      
		      
		      /* Create tempory TIF file name which will be used
		         in the conversting image step */
		      $temp_file = "tmp.tif";
		      
		      /* Start the output buffer */
		      ob_start();
		      /* Use system call to access 3rd party software, ImageMagick 
		         which uses the convert command. This changes the file to greyscale
		         and the density to 300 dpi */
		      system("convert ". SITE_ROOT . "/upload/". $filename . " -type grayscale -density 300 ". SITE_ROOT . "/upload/" . $temp_file);
		     
              /* Use system call to access 3rd party software, Tesseract-ocr
                 which uses the tesseract command. This extracts the text from 
                 the provided image and outputs the result to a text file */
		      system("tesseract ". SITE_ROOT . "/upload/". $temp_file . " ". SITE_ROOT . "/upload/" . $filename . "_output -l eng");
		      /* Clean the output buffer */
		      ob_clean(); 


		      /* Get the file contents and store them in a variable */
			  try 
			  {
    				$file_content = file_get_contents(SITE_ROOT . "/upload/" . $filename . "_output.txt");
			  } 
			  /* Catch any exceptions which may arise */
			  catch (Exception $e) 
			  {
    				echo 'Caught exception: ',  $e->getMessage(), "\n";
			  }

			  /* If the file contains no text, display error message */
			  if($file_content == "")
			  {
					echo "Server error!";
			  }		
			  
			  else
			  {
			  		/* Send the text to the SpellChecker to check for spelling */
					$spellcheckedtext = $spellObj->checkSpell($file_content);
					
					/* If no text is returned, then display an error message */
					if($spellcheckedtext == "")
					{
						echo "No text could be processed. Are you sure the image contained text?";
					}
					else 
					{
						/* If text is returned from the spell checker then display it on the screen */
						echo $spellcheckedtext;
					}
			  }
      	}
    }
}
?>
