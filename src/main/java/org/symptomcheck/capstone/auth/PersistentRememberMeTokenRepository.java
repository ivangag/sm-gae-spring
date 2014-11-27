package org.symptomcheck.capstone.auth;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Component;


@Component
public class PersistentRememberMeTokenRepository  implements PersistentTokenRepository  {

	@Autowired 
	RememberMeTokenRepository rememberMeTokenRepositoryImpl;
	
	@Override
	public void createNewToken(PersistentRememberMeToken token) {
		System.out.println("!!!createNewToken" + token);
        RememberMeToken newToken = new RememberMeToken(token);
        rememberMeTokenRepositoryImpl.save(newToken);
		
	}

	@Override
	public PersistentRememberMeToken getTokenForSeries(String seriesId) {
		System.out.println("!!!getTokenForSeries" + seriesId);
        RememberMeToken token = rememberMeTokenRepositoryImpl.findBySeries(seriesId);
        return new PersistentRememberMeToken(token.getUsername(), token.getSeries(), token.getTokenValue(), token.getDate());
	}

	@Override
	public void removeUserTokens(String username) {
		System.out.println("!!!removeUserTokens" + username);
        List<RememberMeToken> tokens = rememberMeTokenRepositoryImpl.findByUsername(username);
        for(RememberMeToken token : tokens){
        	rememberMeTokenRepositoryImpl.delete(token);
        }
		
	}

	@Override
	public void updateToken(String series, String tokenValue, Date lastUsed) {
	
        RememberMeToken token = rememberMeTokenRepositoryImpl.findBySeries(series);
        if (token != null){
            token.setTokenValue(tokenValue);
            token.setDate(lastUsed);
            rememberMeTokenRepositoryImpl.save(token);
        }		
		
	}

}
