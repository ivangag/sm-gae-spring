package org.symptomcheck.capstone.repository;

import java.util.Collection;
import java.util.List;

import javax.jdo.Query;

import org.springframework.stereotype.Service;


//TODO#BPR_1
//TODO#FDAR_1
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
	
	@SuppressWarnings("unchecked")
	public Collection<Patient> findByDoctorUniqueId(String uniqueId){
		Query query = PMF.get().getPersistenceManager().newQuery(Patient.class);
		query.setFilter("doctors.contains(n)");
		query.declareParameters("String n");
		return (List<Patient>)query.execute(uniqueId);	
	}	
	@SuppressWarnings("unchecked")
	public Collection<Patient> findByGcmRegistrationId(String regId){
		Query query = PMF.get().getPersistenceManager().newQuery(Patient.class);
		query.setFilter("gcmRegistrationIds.contains(n)");
		query.declareParameters("String n");
		return (List<Patient>)query.execute(regId);	
	}

	
	public List<Patient> findByFirstNameAndLastName(String patientFirstName, String patientLastName) {
		Query query = PMF.get().getPersistenceManager().newQuery(Patient.class);
		query.setFilter("firstName == n1 && lastName == n2");
		query.declareParameters("String n1,String n2");
		return (List<Patient>) query.execute(patientFirstName,patientLastName);	
	}		
}
