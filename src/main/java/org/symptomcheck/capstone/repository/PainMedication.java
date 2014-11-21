package org.symptomcheck.capstone.repository;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.appengine.api.datastore.Key;

@PersistenceCapable
@JsonIgnoreProperties("key")
public class PainMedication {

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;

	@Persistent
	private String productId;
	
	@Persistent
	private String medicationName;
	@Persistent
	private String lastTakingDateTime;
	@Persistent
    private String patientMedicalNumber;
    
    public PainMedication(){}
    
    public PainMedication(String medicationName, String lastTakingDateTime,
    		String patientMedicalNumber){
    	this.medicationName = medicationName;
    	this.lastTakingDateTime = lastTakingDateTime;
    	this.patientMedicalNumber = patientMedicalNumber;
    }
    
	public PainMedication(String medicationName) {
		this.medicationName = medicationName;
	}

	public String getMedicationName() {
		return medicationName;
	}
	public void setMedicationName(String medicationName) {
		this.medicationName = medicationName;
	}
	public String getLastTakingDateTime() {
		return lastTakingDateTime;
	}
	public void setLastTakingDateTime(String lastTakingDateTime) {
		this.lastTakingDateTime = lastTakingDateTime;
	}
	public String getPatientMedicalNumber() {
		return patientMedicalNumber;
	}
	public void setPatientMedicalNumber(String patientMedicalNumber) {
		this.patientMedicalNumber = patientMedicalNumber;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}
}
