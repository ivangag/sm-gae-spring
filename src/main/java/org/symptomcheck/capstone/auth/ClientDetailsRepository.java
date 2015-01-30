package org.symptomcheck.capstone.auth;

import java.io.Serializable;

import org.springframework.stereotype.Service;
import org.symptomcheck.capstone.repository.JDOCrudRepository;

@Service
public class ClientDetailsRepository  extends JDOCrudRepository<Client, String> implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2352682794901509165L;

	public ClientDetailsRepository() {
		super(Client.class);
	}
	

}
