package org.symptomcheck.capstone.auth;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;


@PersistenceCapable
public class RememberMeToken {
		
		@PrimaryKey
		@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
		Long id;
	
		@Persistent
	 	private String username;

		@Persistent
	    private String series;

		@Persistent
	    private String tokenValue;

		@Persistent
	    private Date date;
	    
	    public RememberMeToken(PersistentRememberMeToken token){
	        this.series = token.getSeries();
	        this.setUsername(token.getUsername());
	        this.setTokenValue(token.getTokenValue());
	        this.setDate(token.getDate());
	    }

		public String getTokenValue() {
			return tokenValue;
		}

		public void setTokenValue(String tokenValue) {
			this.tokenValue = tokenValue;
		}

		public Date getDate() {
			return date;
		}

		public void setDate(Date date) {
			this.date = date;
		}

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}	    
		
		public String getSeries() {
			return username;
		}

		public void setSeries(String series) {
			this.series = series;
		}	
}
