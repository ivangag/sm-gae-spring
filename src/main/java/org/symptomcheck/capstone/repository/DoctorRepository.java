package org.symptomcheck.capstone.repository;

import java.util.Collection;
import java.util.List;

import javax.jdo.Query;

import org.springframework.stereotype.Service;



@Service
public class DoctorRepository extends JDOCrudRepository<Doctor, String>{

	public DoctorRepository() {
		super(Doctor.class);
	}
	
	@SuppressWarnings("unchecked")
	public Collection<Doctor> findByLastName(String lastName){
		Query query = PMF.get().getPersistenceManager().newQuery(Doctor.class);
		query.setFilter("lastName == n");
		query.declareParameters("String n");
		return (List<Doctor>)query.execute(lastName);	
	}
	
	@SuppressWarnings("unchecked")
	public Collection<Doctor> findByFirstName(String firstName){
		Query query = PMF.get().getPersistenceManager().newQuery(Doctor.class);
		query.setFilter("firstName == n");
		query.declareParameters("String n");
		return (List<Doctor>)query.execute(firstName);	
	}

	@SuppressWarnings("unchecked")
	public Collection<Doctor> findByPatientMedicalNumber(String medicalNumber){
		Query query = PMF.get().getPersistenceManager().newQuery(Doctor.class);
		query.setFilter("patients.contains(n)");
		query.declareParameters("String n");
		return (List<Doctor>)query.execute(medicalNumber);	
	}
	
	@SuppressWarnings("unchecked")
	public Collection<Doctor> findByGcmRegistrationId(String regId){
		Query query = PMF.get().getPersistenceManager().newQuery(Doctor.class);
		query.setFilter("gcmRegistrationIds.contains(n)");
		query.declareParameters("String n");
		return (List<Doctor>)query.execute(regId);	
	}	
	/*
	public Collection<Doctor> findByPatient(Patient patient) {
		// TODO Auto-generated method stub
		//PMF.get().getPersistenceManager().getObjectById(Patient.class, patient.getMedicalRecordNumber());	
		Query q = PMF.get().getPersistenceManager().newQuery(Doctor.class, "patients == parentKey");
		q.declareParameters("String parentKey");
		Key key = KeyFactory.createKey(Patient.class.getSimpleName(), patient.getMedicalRecordNumber());
		return (List<Doctor>) q.execute(key);		
	}*/
}
