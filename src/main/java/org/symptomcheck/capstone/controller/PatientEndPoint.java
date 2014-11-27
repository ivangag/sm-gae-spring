package org.symptomcheck.capstone.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.junit.runners.Parameterized.Parameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.symptomcheck.capstone.client.SymptomManagerSvcApi;
import org.symptomcheck.capstone.gcm.GcmClientRequest;
import org.symptomcheck.capstone.gcm.GcmConstants;
import org.symptomcheck.capstone.repository.CheckIn;
import org.symptomcheck.capstone.repository.CheckInRepository;
import org.symptomcheck.capstone.repository.Doctor;
import org.symptomcheck.capstone.repository.DoctorRepository;
import org.symptomcheck.capstone.repository.GcmTrackRepository;
import org.symptomcheck.capstone.repository.PMF;
import org.symptomcheck.capstone.repository.PainMedication;
import org.symptomcheck.capstone.repository.PainMedicationRepository;
import org.symptomcheck.capstone.repository.Patient;
import org.symptomcheck.capstone.repository.PatientRepository;
import org.symptomcheck.capstone.repository.UserType;

@Controller
public class PatientEndPoint {

	//private static final Logger log = Logger.getLogger(PatientEndPoint.class.getName()); 
	public PatientEndPoint(){}
	
	@Autowired
	PatientRepository patients;	
	@Autowired
	CheckInRepository checkIns;	
	@Autowired
	PainMedicationRepository medications;	
	@Autowired
	DoctorRepository doctors;	
	@Autowired
	GcmTrackRepository gcmTracks;
	
	@Autowired 
	GcmClientRequest gcmClientRequest;
	
	@Secured("ROLE_DOCTOR")
	@RequestMapping(value= SymptomManagerSvcApi.PATIENT_SVC_PATH + "/{medicalRecordNumber}/medications/delete", method=RequestMethod.DELETE)		
	public @ResponseBody boolean deletePainMedication(
			Principal User,
			@PathVariable("medicalRecordNumber") String medicalRecordNumber, 
			@RequestParam(value="medicineProductId", required=true) String medicineProductId){

		boolean found = false;
		final String username = User.getName();		
		long deleted =  medications.deleteByProductId(medicineProductId);
		if(deleted > 0){			
			//GCM handling
			List<String> patients_reg_ids = new ArrayList<String>();
			StringBuilder patientsInfo = new StringBuilder();			
			Patient patient = patients.findOne(username);
			if((patient != null)
					&& !patient.getGcmRegistrationIds().isEmpty()){
				
				patients_reg_ids.addAll(patient.getGcmRegistrationIds());
			}		
			if(!patients_reg_ids.isEmpty()){
				gcmClientRequest.sendGcmMessage(GcmConstants.GCM_ACTION_MEDICATION_UPDATE, 
						username, UserType.DOCTOR, patients_reg_ids, patientsInfo.toString());							
			}				
		}		
		return deleted > 0;
	}
	
	
	@Secured("ROLE_DOCTOR")
	@RequestMapping(value= SymptomManagerSvcApi.PATIENT_SVC_PATH + "/{medicalRecordNumber}/medications", method=RequestMethod.POST)		
	public @ResponseBody PainMedication addPainMedication(
			Principal User,
			@PathVariable("medicalRecordNumber") String medicalRecordNumber, 
			@RequestBody PainMedication painMedication){

		final String username = User.getName();
		
		PainMedication medicine =  medications.save(painMedication);
		
		List<String> patients_reg_ids = new ArrayList<String>();
		StringBuilder patientsInfo = new StringBuilder();
		
		Patient patient = patients.findOne(username);
		if((patient != null)
				&& !patient.getGcmRegistrationIds().isEmpty()){
			
			patients_reg_ids.addAll(patient.getGcmRegistrationIds());
		}
		//GCM handling
		if(!patients_reg_ids.isEmpty()){
			gcmClientRequest.sendGcmMessage(GcmConstants.GCM_ACTION_MEDICATION_UPDATE, 
					username, UserType.DOCTOR, patients_reg_ids, patientsInfo.toString());							
		}				
		return medicine;
	}
	
	
	//private final static String gcm_reg_id_doctor_test = "APA91bHNoS19Gz2_Z6VROtXy1-Qo5rya4cTOl8YkVI2L35RupyVhU6L2WyhPR7tKuH2eCD1hFRCBiGkJI8VnBEykJSAwIEZv-ijlI7IeO2rGDQwpCTpWe97rGdZ3FfWtjFMQHLv5cWxoXP4B9eJcQnFF2dokcMoYK9nGUoMWtRZ1LM9QCt7urZw";
	
	@Secured("ROLE_PATIENT")
	@RequestMapping(value= SymptomManagerSvcApi.PATIENT_SVC_PATH + "/{medicalRecordNumber}/checkins", method=RequestMethod.POST)		
	public @ResponseBody CheckIn addCheckIn(Principal User, @RequestBody CheckIn checkIn){

		
		final String username = User.getName();
		checkIn.setPatientMedicalNumber(username); 		
		//checkIn.image = new Blob(Base64.decodeBase64(checkIn.getThroatImageEncoded()));
		CheckIn check = checkIns.save(checkIn);
		
		//here we should retrieve the doctors of patient and related gcm_reg_id(s)
		List<Doctor> doctorList = (List<Doctor>) doctors.findByPatientMedicalNumber(username);
		
		
		List<String> doctors_reg_ids = new ArrayList<String>();
		StringBuilder doctorsInfo = new StringBuilder();
		if(!doctorList.isEmpty()){
			for(Doctor doctor : doctorList){
				for(String regId : doctor.getGcmRegistrationIds()){
					doctors_reg_ids.add(regId);
				}
				doctorsInfo.append(doctor.toString()).append(" - ");
			}
		}
		
		if(!doctors_reg_ids.isEmpty()){
			gcmClientRequest.sendGcmMessage(GcmConstants.GCM_ACTION_CHECKIN_UPDATE, 
					username, UserType.PATIENT, doctors_reg_ids, doctorsInfo.toString());						
		}
		return check;
	}
	
	
	@Secured({"ROLE_PATIENT", "ROLE_DOCTOR"}) 
	@RequestMapping(value= SymptomManagerSvcApi.PATIENT_SVC_PATH + "/{medicalRecordNumber}/checkins/search", method=RequestMethod.GET)		
	public @ResponseBody Collection<CheckIn> findCheckInsByPatient(
			@PathVariable("medicalRecordNumber") String medicalRecordNumber){		
		List<CheckIn> checks = (List<CheckIn>) checkIns.findByPatientMedicalNumber(medicalRecordNumber);
		return checks;
	}
	

	@Secured({"ROLE_PATIENT", "ROLE_DOCTOR"})
	@RequestMapping(value= SymptomManagerSvcApi.PATIENT_SVC_PATH + "/{medicalRecordNumber}/medications/search",
		method=RequestMethod.GET)		
	public @ResponseBody Collection<PainMedication> findPainMedicationsByPatient(
			@PathVariable("medicalRecordNumber") String medicalRecordNumber){		
		return medications.findByPatientMedicalNumber(medicalRecordNumber);
	}
	
	@Secured("ROLE_PATIENT")
	@RequestMapping(value=SymptomManagerSvcApi.PATIENT_SVC_PATH + "/{medicalRecordNumber}/doctors/search", 
			method=RequestMethod.GET)
	public @ResponseBody Collection<Doctor> findDoctorsByPatient(
			@PathVariable("medicalRecordNumber") String medicalRecordNumber,
			HttpServletRequest request){		
		Collection<Doctor> doctorsList = new ArrayList<Doctor>();
		Patient patient = patients.findOne(medicalRecordNumber);
		if(patient != null){
			doctorsList = doctors.findByPatientMedicalNumber(patient.getMedicalRecordNumber());
		}
		return doctorsList;
	}

	@SuppressWarnings("unused")
	private List<String> getAuthorities(HttpServletRequest request){
		List<String> roles = new ArrayList<String>();
		SecurityContext context = SecurityContextHolder.getContext();
		Authentication authentication = context.getAuthentication();
		for (GrantedAuthority auth : authentication.getAuthorities()) {
			roles.add(auth.getAuthority());
        }		
		if(request != null){
			boolean isPatient = request.isUserInRole("ROLE_PATIENT");
			boolean isDoctor = request.isUserInRole("ROLE_DOCTOR");
		}
		return roles;
	}
}
