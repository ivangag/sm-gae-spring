package org.symptomcheck.capstone.repository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.common.base.Objects;

//TODO#BPR_1
@PersistenceCapable/*(identityType =IdentityType.APPLICATION, detachable = "true")*/
public class Patient {

	@PrimaryKey
	@Persistent
	private String medicalRecordNumber;
	
	@Persistent
	private String firstName;
	
	@Persistent
	private String lastName;
	
	@Persistent
	private Set<String> gcmRegistrationIds = new HashSet<String>();
	
	@Persistent
	private String birthDate;
	
	@Persistent
	private String email;

	@Persistent
	private String phoneNumber;

	
	public Patient() {
	}
	
	public Patient(String medicalRecordNumber, String firstName,
			String lastName) {
		super();
		this.medicalRecordNumber = medicalRecordNumber;
		this.lastName = lastName;
		this.firstName = firstName;
	}

	public String getMedicalRecordNumber(){
		return this.medicalRecordNumber;
	}
	
	public String getFirstName(){
		return this.firstName;
	}
	
	public String getLastName(){
		return this.lastName;
	}
	
	public void setMedicalRecordNumber(String medicalRecordNumber){
		this.medicalRecordNumber = medicalRecordNumber;
	}
	
	public void setFirstName(String firstName){
		this.firstName = firstName;
	}
	
	public void setLastName(String lastName){
		this.lastName = lastName;
	}

	
	public Set<String> getDoctors(){
		return this.doctors;
	}
	
	public void setDoctors(List<String> doctors){
		this.doctors = new HashSet<String>(doctors);
	}

	public void addDoctor(String doctor) {	  
		if(!this.doctors.contains(doctor))
			this.doctors.add(doctor);
    }
  	@Persistent
  	private Set<String> doctors = new HashSet<String>();
  	
	public String getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}

	
	public void addGcmRegistrationId(String gcmRegistrationId) {
		gcmRegistrationId = gcmRegistrationId.replace("\"","");
		if(!this.gcmRegistrationIds.contains(gcmRegistrationId))
			this.gcmRegistrationIds.add(gcmRegistrationId);
	}
	

	public Set<String> getGcmRegistrationIds() {
		Set<String> idsLocal = new HashSet<String>();
		for(String gcmId : gcmRegistrationIds){			
			idsLocal.add(gcmId.replace("\"",""));
		}
		return idsLocal;
	}

	public void setGcmRegistrationIds(List<String> gcmRegistrationIds) {
			this.gcmRegistrationIds =  new HashSet<String>(gcmRegistrationIds);	
	}
	
	@Override
	public String toString() {
		return "Patient " + this.firstName + " "+ this.lastName + " " + this.medicalRecordNumber;
	}

	/**
	 * Two Object will generate the same hashcode if they have exactly the same
	 * values for their properties
	 * 
	 */
	@Override
	public int hashCode() {
		// Google Guava provides great utilities for hashing
		return Objects.hashCode(medicalRecordNumber, this.firstName, this.lastName);
	}

	/**
	 * Two Object are considered equal if they have exactly the same values for
	 * their properties
	 * 
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Patient) {
			Patient other = (Patient) obj;
			// Google Guava provides great utilities for equals too!
			return Objects.equal(medicalRecordNumber, other.medicalRecordNumber)
					&& Objects.equal(firstName, other.firstName)
					&& lastName == other.lastName;
		} else {
			return false;
		}
	}

	public void removeGcmRegistrationIds(String gcmRegistrationId) {
		if(this.gcmRegistrationIds.contains(gcmRegistrationId)) {
			this.gcmRegistrationIds.remove(gcmRegistrationId);
		}		
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
}
