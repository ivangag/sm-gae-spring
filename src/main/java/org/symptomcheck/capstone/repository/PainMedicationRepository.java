package org.symptomcheck.capstone.repository;

import java.util.Collection;
import java.util.List;

import javax.jdo.Query;

import org.springframework.stereotype.Service;



@Service
public class PainMedicationRepository extends JDOCrudRepository<PainMedication, String>{

	public PainMedicationRepository() {
		super(PainMedication.class);
	}

	/*
	public String findByMedicationNameOrderByLastTakingDateTimeDesc(
			String medicationName) {
		// TODO Auto-generated method stub
		return null;
	}

	public Collection<PainMedication> findByPatientMedicalNumberOrderByLastTakingDateTimeDesc(
			String medicalRecordNumber) {
		// TODO Auto-generated method stub
		return null;
	}*/
	
	
	@SuppressWarnings("unchecked")
	public Collection<PainMedication> findByPatientMedicalNumber(
			String medicalRecordNumber) {
		Query query = PMF.get().getPersistenceManager().newQuery(PainMedication.class);
		query.setFilter("patientMedicalNumber == n");
		query.declareParameters("String n");
		return (List<PainMedication>)query.execute(medicalRecordNumber);	
	}
}
