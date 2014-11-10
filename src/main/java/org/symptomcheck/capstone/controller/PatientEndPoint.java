package org.symptomcheck.capstone.controller;

import java.security.Principal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

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
		
		//TEST GCM notification generation
		boolean success = true;
		String resultInfo = "";
		
		//here we should retrieve the doctors of patient and related gcm_reg_id(s)
		List<Doctor> doctorList = (List<Doctor>) doctors.findByPatientMedicalNumber(username);
		List<String> doctors_reg_ids = new ArrayList<String>();
		List<String> doctorsInfo = new ArrayList<String>();
		if(!doctorList.isEmpty()){
			for(Doctor doctor : doctorList){
				for(String regId : doctor.getGcmRegistrationIds()){
					doctors_reg_ids.add(regId.replace("\"", ""));	
				}
				doctorsInfo.add(doctor.toString());
			}
		}
		//for(String regId : doctors_reg_ids){
			GcmMessage message = new GcmMessage();		
			message.setRegistration_ids(doctors_reg_ids);
			//message.addRegistrationId(gcm_reg_id_doctor_test);
			//message.addRegistrationId(regId);
			GcmData dataMsg = new GcmData();
			dataMsg.setAction(GcmConstants.GCM_ACTION_CHECKIN_RX);
			dataMsg.setUserName(username);
			dataMsg.setUserType(UserType.PATIENT);
			message.setData(dataMsg);	
			
			try{
				final GcmResponse gcmResponse = GcmClientRequest.get().getApi().
					 sendMessage(GcmConstants.GCM_PROJECT_AUTHORIZATION_KEY, message);
				resultInfo = gcmResponse.toString();
			}catch(Exception error){
				success = false;
				resultInfo = error.getMessage();
				System.out.print("addCheckIn=>GCMSendError:" + resultInfo);
			}
			DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			Date today = Calendar.getInstance().getTime(); 
			GcmTrack gcmTrack = new GcmTrack();
			gcmTrack.setDate(df.format(today));
			gcmTrack.setGcmDestinationIds(message.getRegistration_ids());
			gcmTrack.setBundleContent(dataMsg.toString() + "\n\n" + doctorsInfo);
			gcmTrack.setResultInfo(resultInfo.substring(0, 
					resultInfo.length() < 495 ? resultInfo.length() : 495));
			gcmTrack.setSuccess(success);	
			gcmTracks.save(gcmTrack);
		//}
		
		//---------------------------------------------------------------------------------------------
		
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
		//Collection<PainMedication> resList = new ArrayList<PainMedication>(){};
		//return (Collection<PainMedication>) medications.findAll();
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