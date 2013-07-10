<?php

/**
 * Description:
 * 		Class used to check spelling of provided text
 *
 * Author:
 * 		Robert Scally
 *
 * */

class SpellChecker 
{
	public $converted_text;
	public $spellchecked = "";


	/** 
	*  Function: To check the spelling of the provided text	
	*/
	public function checkSpell($converted_text) 
	{

		$pspell = pspell_new("en");

	    $sentence = $converted_text;
	    
		$words2 = implode("+", preg_split("/[\s]+/", $sentence));
		$words = explode("+", $words2);
	
		/* Loop through each word of the text */
	    foreach($words as $word) 
		{
			/* Variables to store if a comma, fullstop or colon is present */
			$comma = false;
			$fullstop = false;
			$colon = false;
			
			/* If the word contains a comma set comma to be true */
			if(substr_count($word,','))
			{
				$comma = true;
			}
			/* If the word contains a fullstop set comma to be true */
			if(substr_count($word,'.'))
			{
				$fullstop = true;
			}
			/* If the word contains a colon set comma to be true */
			if(substr_count($word,':'))
			{
				$colon = true;
			}
			
			/* Remove the fullstop, comma or colon from the word */
			$word = str_replace('.', '', $word);
			$word = str_replace(',', '', $word);
			$word = str_replace(':', '', $word);
			
			/* Remove all non alphabethical characters */
			$word = preg_replace("/[^A-Za-z]+/", "", $word);
			

		    if(pspell_check($pspell, $word)) 
			{
				//check for single letters which make no sense on their own
				if(strlen($word) == 1)
				{
					if(strcasecmp($word,"a") != 0 && strcasecmp($word,"i") != 0)
					{
						$word = "";
						//echo "<strong><i> {".$word."} </i></strong>";
					}
				}
				else 
				{
	        		// this word is fine; print as-is
	       			//echo $word, " ";
				}
	        } 
			else 
			{
	        	/* Get an array of suggestions for spelling */
				$suggestions = pspell_suggest($pspell, $word);
	
				/* If there are suggestions */
				if(count($suggestions)) 
				{
					/* The word is changed to be the first suggestion provided */
					$word = $suggestions[0];
				} 
				else 
				{
					
				}
	        }
	        
			/* Put back, if present, the colon, comma or fullstop */
	        if($colon)
	        {
	        	$word = $word . ":";
	        }       			
	        if($comma)
	        {
	        	$word = $word . ",";
	        }   
	        if($fullstop)
	        {
	        	$word = $word . ".";
	        }
	        
			/* Add the word which was checked to the sentance containing 
		  	   words already spell checked */
	        $spellchecked = $spellchecked . " ". $word;
	    }
		
		
		return $spellchecked; // return the spellchecked sentance
	}
}
?>
