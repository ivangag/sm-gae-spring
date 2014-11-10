package org.symptomcheck.capstone.auth;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.jdo.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.symptomcheck.capstone.repository.JDOCrudRepository;
import org.symptomcheck.capstone.repository.PMF;

@Service
public class RememberMeTokenRepository  extends JDOCrudRepository<RememberMeToken, Long>
	//implements PersistentTokenRepository 
{


	public RememberMeTokenRepository() {
		super(RememberMeToken.class);
	}
	
	public RememberMeToken findBySeries(String series){
		Query query = PMF.get().getPersistenceManager().newQuery(RememberMeToken.class);
		query.setFilter("series == n");
		query.declareParameters("String n");
		return (RememberMeToken)query.execute(series);
	}	

	public List<RememberMeToken> findByUsername(String username){
		Query query = PMF.get().getPersistenceManager().newQuery(RememberMeToken.class);
		query.setFilter("username == n");
		query.declareParameters("String n");
		return (List<RememberMeToken>)query.execute(username);
	}	

	
	
}
