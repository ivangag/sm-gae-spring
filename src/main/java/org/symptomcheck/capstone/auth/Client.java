package org.symptomcheck.capstone.auth;

import java.io.Serializable;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable
public class Client implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3292429430692046037L;

	@PrimaryKey
	@Persistent
	private String clientId;
	
	@Persistent
	private String clientSecret;
	
	@Persistent
	private String role;

	
	@Persistent
	private int accessTokenValiditySeconds;
	
	
	@Persistent
	private int refreshTokenValiditySeconds;
	
	/**
	 * The access token validity period for this client. Null if not set explicitly (implementations might use that fact
	 * to provide a default value for instance).
	 * 
	 * @return the access token validity period
	 */
	Integer getAccessTokenValiditySeconds() {
		return accessTokenValiditySeconds;
	}

	/**
	 * The refresh token validity period for this client. Zero or negative for default value set by token service.
	 * 
	 * @return the refresh token validity period
	 */
	Integer getRefreshTokenValiditySeconds() {
		return refreshTokenValiditySeconds;
	}	


	void setAccessTokenValiditySeconds(int accessTokenValiditySeconds ) {
		this.accessTokenValiditySeconds = accessTokenValiditySeconds;
	}


	void setRefreshTokenValiditySeconds(int refreshTokenValiditySeconds) {
		this.refreshTokenValiditySeconds = refreshTokenValiditySeconds;
	}	
	
	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getClientSecret() {
		return clientSecret;
	}

	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		return sb.append("ClientId:").append(clientId)
				.append(" ")
				.append("role:").append(role)
				.append(" ")
				.append("accessTokenValiditySeconds:").append(accessTokenValiditySeconds)
				.append(" ")
				.append("refreshTokenValiditySeconds:").append(refreshTokenValiditySeconds)				
				.toString();
		
	}
	
	public Client(){}
}
