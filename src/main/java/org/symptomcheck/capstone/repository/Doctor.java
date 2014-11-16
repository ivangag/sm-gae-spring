package org.symptomcheck.capstone.repository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable//(identityType = IdentityType.APPLICATION, detachable = "true")
public class Doctor{
	
	@PrimaryKey
	@Persistent
	private String uniqueDoctorId;
	
	@Persistent
	private String firstName;
	@Persistent
	private String lastName;
	
	@Persistent
	//private List<String> gcmRegistrationIds = Lists.newArrayList();
	private Set<String> gcmRegistrationIds = new HashSet<String>();
		
	public Doctor(){}
	public Doctor(String uniqueDoctorId, String firstName, String lastName) {
		super();
		this.firstName = firstName;
		this.uniqueDoctorId = uniqueDoctorId;
		this.lastName=  lastName;
	}

	public String getUniqueDoctorId(){
		return this.uniqueDoctorId;
	}
	
	public String getFirstName(){
		return this.firstName;
	}
	
	public String getLastName(){
		return this.lastName;
	}
	
	public void setUniqueDoctorId(String uniqueDoctorId){
		this.uniqueDoctorId = uniqueDoctorId;
	}
	
	public void setFirstName(String firstName){
		this.firstName = firstName;
	}
	
	public void setLastName(String lastName){
		this.lastName = lastName;
	}
	
	public Set<String> getPatients(){
		return this.patients;
	}
	
	public void setPatients(List<String> patients){
		this.patients = new HashSet<String>(patients);
	}

	public void addPatient(String patient) {	  
		if(!this.patients.contains(patient))
			this.patients.add(patient);		
    }
  	@Persistent
  	//@Unowned
  	//private List<Patient> patients = new ArrayList<Patient>();
  	private Set<String> patients = new HashSet<String>();
  
	@Override
	public String toString() {
		return "Doctor: " + this.firstName + " " + this.lastName + ";" +  this.uniqueDoctorId;
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
		this.gcmRegistrationIds = new HashSet<String>(gcmRegistrationIds);;		
	}
	public void removeGcmRegistrationIds(String gcmRegistrationId) {
		if(this.gcmRegistrationIds.contains(gcmRegistrationId)) {
			this.gcmRegistrationIds.remove(gcmRegistrationId);
		}		
	}
}