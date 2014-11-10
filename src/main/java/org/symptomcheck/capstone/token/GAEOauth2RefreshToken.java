package org.symptomcheck.capstone.token;

import java.io.Serializable;

public class GAEOauth2RefreshToken implements Serializable {  
    private static final long serialVersionUID = 238497479380096784L;  
  
    private String tokenId;  
    private byte[] token;  
    private byte[] authentication;  
  
    public String getTokenId() {  
        return tokenId;  
    }  
  
    public void setTokenId(String tokenId) {  
        this.tokenId = tokenId;  
    }  
  
    public byte[] getToken() {  
        return token;  
    }  
  
    public void setToken(byte[] token) {  
        this.token = token;  
    }  
  
    public byte[] getAuthentication() {  
        return authentication;  
    }  
  
    public void setAuthentication(byte[] authentication) {  
        this.authentication = authentication;  
    }  

}
