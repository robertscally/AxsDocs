package com.robertscally.fypapp;

/**
 * Description: 
 * 		This is the main / home activity
 * 
 * Author: 
 * 		Source: http://www.mkyong.com/regular-expressions/how-to-validate-ip-address-with-regular-expression/
 * 
 * Date:
 * 		-----------
 */
// 
/**
 * 
 * This class was completely sourced from
 * Source: http://www.mkyong.com/regular-expressions/how-to-validate-ip-address-with-regular-expression/
 *
 */

import java.util.regex.Matcher;
import java.util.regex.Pattern;
 
public class IpAddressValidator 
{
 
    private Pattern pattern;
    private Matcher matcher;
 
    private static final String IPADDRESS_PATTERN = 
		"^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
		"([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
		"([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
		"([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
 
    public IpAddressValidator()
    {
    	pattern = Pattern.compile(IPADDRESS_PATTERN);
    }
 
   /**
    * Validate ip address with regular expression
    * @param ip ip address for validation
    * @return true valid ip address, false invalid ip address
    */
    public boolean validate(final String ip)
    {		  
    	matcher = pattern.matcher(ip);
    	
    	return matcher.matches();	    	    
    }
}