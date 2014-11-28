package org.symptomcheck.capstone.repository;

import java.util.Collection;
import java.util.List;

import javax.jdo.Query;

import org.springframework.stereotype.Service;


//TODO#FDAR_3 
@Service
public class CheckInRepository extends JDOCrudRepository<CheckIn, String>{

	public CheckInRepository() {
		super(CheckIn.class);
	}

	@SuppressWarnings("unchecked")
	//TODO#FDAR_11
	public Collection<CheckIn> findByPatientMedicalNumber( 
			String medicalRecordNumber) {
		Query query = PMF.get().getPersistenceManager().newQuery(CheckIn.class);
		query.setFilter("patientMedicalNumber == n");
		query.declareParameters("String n");
		return (List<CheckIn>)query.execute(medicalRecordNumber);	
	}
	
}
