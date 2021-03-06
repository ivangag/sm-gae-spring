package org.symptomcheck.capstone.repository;

import java.util.Collection;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;


//TODO#FDAR_12 
@Service
public class PainMedicationRepository extends JDOCrudRepository<PainMedication, String>{

	public PainMedicationRepository() {
		super(PainMedication.class);
	}

	@SuppressWarnings("unchecked")
	public Collection<PainMedication> findByPatientMedicalNumber(
			String medicalRecordNumber) {
		Query query = PMF.get().getPersistenceManager().newQuery(PainMedication.class);
		query.setFilter("patientMedicalNumber == n");
		query.declareParameters("String n");
		return (List<PainMedication>)query.execute(medicalRecordNumber);	
	}
	
	@SuppressWarnings("unchecked")
	public Collection<PainMedication> findByProductId(String productId) {
		Query query = PMF.get().getPersistenceManager().newQuery(PainMedication.class);
		query.setFilter("productId == n");
		query.declareParameters("String n");
		return (List<PainMedication>)query.execute(productId);	
	}
	

	public long deleteByProductId(String productId) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query query = null;
		try {
			query = pm.newQuery(PainMedication.class);
			query.setFilter("productId == param");
			query.declareParameters("String param");
			return query.deletePersistentAll(productId);
		} finally {
			if (query != null) {
				query.closeAll();
			}
			pm.close();
		}
	}	

}
