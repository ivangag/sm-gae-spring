package org.symptomcheck.capstone.repository;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.repackaged.org.codehaus.jackson.annotate.JsonIgnoreProperties;

@PersistenceCapable
@JsonIgnoreProperties("key")
public class PainMedication {

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;

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
}
