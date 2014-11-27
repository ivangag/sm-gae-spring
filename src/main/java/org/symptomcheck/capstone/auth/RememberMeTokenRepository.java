package org.symptomcheck.capstone.auth;

import java.util.List;

import javax.jdo.Query;

import org.springframework.stereotype.Service;
import org.symptomcheck.capstone.repository.JDOCrudRepository;
import org.symptomcheck.capstone.repository.PMF;

@Service
public class RememberMeTokenRepository  extends JDOCrudRepository<RememberMeToken, Long>
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

	@SuppressWarnings("unchecked")
	public List<RememberMeToken> findByUsername(String username){
		Query query = PMF.get().getPersistenceManager().newQuery(RememberMeToken.class);
		query.setFilter("username == n");
		query.declareParameters("String n");
		return (List<RememberMeToken>)query.execute(username);
	}	

	
	
}
