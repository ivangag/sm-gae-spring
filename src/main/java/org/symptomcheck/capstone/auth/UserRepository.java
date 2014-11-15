package org.symptomcheck.capstone.auth;

import java.io.Serializable;

import org.springframework.stereotype.Service;
import org.symptomcheck.capstone.repository.JDOCrudRepository;
import org.symptomcheck.capstone.repository.Patient;

@Service
public class UserRepository  extends JDOCrudRepository<User, String> implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4795130174805829152L;

	public UserRepository() {
		super(User.class);
	}
	

}
