package org.symptomcheck.capstone.repository;

import java.util.Collection;
import java.util.List;

import javax.jdo.Query;

import org.springframework.stereotype.Service;



@Service
public class PatientRepository extends JDOCrudRepository<Patient, String>{

	public PatientRepository() {
		super(Patient.class);
	}
	
	@SuppressWarnings("unchecked")
	public Collection<Patient> findByLastName(String lastName){
		Query query = PMF.get().getPersistenceManager().newQuery(Patient.class);
		query.setFilter("lastName == n");
		query.declareParameters("String n");
		return (List<Patient>)query.execute(lastName);	
	}
	
	@SuppressWarnings("unchecked")
	public Collection<Patient> findByFirstName(String firstName){
		Query query = PMF.get().getPersistenceManager().newQuery(Patient.class);
		query.setFilter("firstName == n");
		query.declareParameters("String n");
		return (List<Patient>)query.execute(firstName);	
	}
}
