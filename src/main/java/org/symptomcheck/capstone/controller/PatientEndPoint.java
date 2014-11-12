package org.symptomcheck.capstone.controller;

import java.security.Principal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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
import org.springframework.web.bind.annotation.ResponseBody;
import org.symptomcheck.capstone.client.SymptomManagerSvcApi;
import org.symptomcheck.capstone.gcm.GcmClientRequest;
import org.symptomcheck.capstone.gcm.GcmConstants;
import org.symptomcheck.capstone.gcm.GcmData;
import org.symptomcheck.capstone.gcm.GcmMessage;
import org.symptomcheck.capstone.gcm.GcmResponse;
import org.symptomcheck.capstone.repository.CheckIn;
import org.symptomcheck.capstone.repository.CheckInRepository;
import org.symptomcheck.capstone.repository.Doctor;
import org.symptomcheck.capstone.repository.DoctorRepository;
import org.symptomcheck.capstone.repository.GcmTrack;
import org.symptomcheck.capstone.repository.GcmTrackRepository;
import org.symptomcheck.capstone.repository.PMF;
import org.symptomcheck.capstone.repository.PainMedication;
import org.symptomcheck.capstone.repository.PainMedicationRepository;
import org.symptomcheck.capstone.repository.Patient;
import org.symptomcheck.capstone.repository.PatientRepository;
import org.symptomcheck.capstone.repository.UserType;

import com.google.api.client.util.Lists;

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
	@RequestMapping(value= SymptomManagerSvcApi.PATIENT_SVC_PATH + "/{medicalRecordNumber}/medications", method=RequestMethod.POST)		
	public @ResponseBody PainMedication addPainMedication(
			@PathVariable("medicalRecordNumber") String medicalRecordNumber, 
			@RequestBody PainMedication painMedication){

		painMedication.setPatientMedicalNumber(medicalRecordNumber);
		return medications.save(painMedication);		
	}
	
	
	private final static String gcm_reg_id_doctor_test = "APA91bHNoS19Gz2_Z6VROtXy1-Qo5rya4cTOl8YkVI2L35RupyVhU6L2WyhPR7tKuH2eCD1hFRCBiGkJI8VnBEykJSAwIEZv-ijlI7IeO2rGDQwpCTpWe97rGdZ3FfWtjFMQHLv5cWxoXP4B9eJcQnFF2dokcMoYK9nGUoMWtRZ1LM9QCt7urZw";
	
	@Secured("ROLE_PATIENT")
	@RequestMapping(value= SymptomManagerSvcApi.PATIENT_SVC_PATH + "/{medicalRecordNumber}/checkins", method=RequestMethod.POST)		
	public @ResponseBody CheckIn addCheckIn(Principal User, @RequestBody CheckIn checkIn){
		final String username = User.getName();
		checkIn.setPatientMedicalNumber(username); 
		CheckIn check = checkIns.save(checkIn);
		
		//here we should retrieve the doctors of patient and related gcm_reg_id(s)
		List<Doctor> doctorList = (List<Doctor>) doctors.findByPatientMedicalNumber(username);
		List<String> doctors_reg_ids = new ArrayList<String>();
		//Map<String,String> doctors_reg_ids = new HashMap<String, String>();
		List<String> doctorsInfo = new ArrayList<String>();
		if(!doctorList.isEmpty()){
			for(Doctor doctor : doctorList){
				for(String regId : doctor.getGcmRegistrationIds()){
					//doctors_reg_ids.add(regId.replace("\"", ""));
					doctors_reg_ids.add(regId);
					//doctors_reg_ids.put(doctor.getUniqueDoctorId(), regId.replace("\"", ""));
				}
				doctorsInfo.add(doctor.toString());
			}
		}
		if(!doctors_reg_ids.isEmpty()){
			GcmResponse gcmResponse = gcmClientRequest.sendGcmMessage(GcmConstants.GCM_ACTION_CHECKIN_RX, username, UserType.PATIENT,
					doctors_reg_ids, doctorsInfo);
			
			if(gcmResponse != null) {
				// try to extract canonical
				if(Integer.valueOf(gcmResponse.canonical_ids) != null){
					int canonical_ids = Integer.valueOf(gcmResponse.canonical_ids);
					System.out.print(String.format("doctors_reg_ids: %s\n",doctors_reg_ids.toString()));
					System.out.print(String.format("canonical_ids COUNT: %d\n",canonical_ids));
					if(canonical_ids > 0) {						
						for(int idx = 0; idx < gcmResponse.results.size(); idx++){
							final String canonicalId = gcmResponse.results.get(idx).getRegistration_id();
							if(!canonicalId.isEmpty()){
								String gcmIdtoUpdate = doctors_reg_ids.get(idx);
								Collection<Doctor> doctorList1 = doctors.findByGcmRegistrationId(gcmIdtoUpdate);
								System.out.print(String.format("doctors.findByGcmRegistrationId: %d-canonical:%s\n",doctorList1.size(),gcmResponse.results.get(idx).getRegistration_id()));
								for(Doctor doctor : doctorList1){
									List<String> gcmDoctorIds = Lists.newArrayList(doctor.getGcmRegistrationIds());
									gcmDoctorIds.remove(gcmIdtoUpdate);
									gcmDoctorIds.add(gcmResponse.results.get(idx).getRegistration_id());
									doctor.setGcmRegistrationIds(gcmDoctorIds);
									PMF.get().getPersistenceManager().close();
								}
							}												
						}						
					}						
				}
			}						
		}
		return check;
	}
	
	@Secured({"ROLE_PATIENT", "ROLE_DOCTOR"}) 
	@RequestMapping(value= SymptomManagerSvcApi.PATIENT_SVC_PATH + "/{medicalRecordNumber}/checkins/search", method=RequestMethod.GET)		
	public @ResponseBody Collection<CheckIn> findCheckInsByPatient(
			@PathVariable("medicalRecordNumber") String medicalRecordNumber){		
		return checkIns.findByPatientMedicalNumber(medicalRecordNumber);
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
