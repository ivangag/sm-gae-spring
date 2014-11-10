package org.symptomcheck.capstone.token;

public class GAETokenRepository {

	 void storeAccessToken(GAEOauth2AccessToken token) throws Exception{
		 throw new Exception("GAETokenRepository storeAccessToken");
	 
	 }
     
	    GAEOauth2AccessToken readAccessToken(String tokenValue) throws Exception{
	    	throw new Exception("GAETokenRepository readAccessToken");
	    }
	  
	     void removeAccessToken(String tokenValue) throws Exception{
	    	 throw new Exception("GAETokenRepository removeAccessToken");
		 }
	  
	     void storeRefreshToken(GAEOauth2RefreshToken token) throws Exception{
	    	 throw new Exception("GAETokenRepository storeRefreshToken");
		 }
	  
	     GAEOauth2RefreshToken readRefreshToken(String tokenValue) throws Exception{
	    	 throw new Exception("GAETokenRepository readRefreshToken");
	    	 /*
	    	 GAEOauth2RefreshToken token = new  GAEOauth2RefreshToken();
		    	return token;*/
		 }  
	  
	     void removeRefreshToken(String tokenValue) throws Exception{
	    	 throw new Exception("GAETokenRepository removeRefreshToken");
		 }  
	  
	     void removeAccessTokenUsingRefreshToken(String refreshToken) throws Exception{
	    	 throw new Exception("GAETokenRepository removeAccessTokenUsingRefreshToken");
		 }
	  
	     GAEOauth2AccessToken getAccessToken(String authentication) throws Exception {
	    	 throw new Exception("GAETokenRepository getAccessToken");
	    	 /*
	    	 GAEOauth2AccessToken token = new  GAEOauth2AccessToken();
		    	return token;
		    	*/
		 } 
}
