package org.symptomcheck.capstone.repository;

import java.util.Collection;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;



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
	
	public Collection<PainMedication> findByProductId(String productId) {
		Query query = PMF.get().getPersistenceManager().newQuery(PainMedication.class);
		query.setFilter("productId == n");
		query.declareParameters("String n");
		return (List<PainMedication>)query.execute(productId);	
	}
	
	/**
	 * Deletes the {@link GaeOAuthToken} entities with the given token ID.
	 * @param tokenId Token ID.
	 * @return Number of {@link GaeOAuthToken} entities that were deleted.
	 */
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
	/*
	@SuppressWarnings("unchecked")
	public Collection<PainMedication> findByProductId(String productId) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query query = null;
		try {
			query = pm.newQuery(PainMedication.class);
			query.setFilter("productId == param");
			query.declareParameters("String param");
		    return (Collection<PainMedication>) query.execute(productId);
		} finally {
			if (query != null) {
				query.closeAll();
			}
			pm.close();
		}	
	}*/
}
