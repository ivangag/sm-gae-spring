package org.symptomcheck.capstone.controller;

import java.security.Principal;
import java.util.Collection;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.symptomcheck.capstone.auth.User;
import org.symptomcheck.capstone.auth.UserRepository;
import org.symptomcheck.capstone.client.SymptomManagerSvcApi;
import org.symptomcheck.capstone.repository.Doctor;
import org.symptomcheck.capstone.repository.DoctorRepository;
import org.symptomcheck.capstone.repository.PMF;
import org.symptomcheck.capstone.repository.Patient;
import org.symptomcheck.capstone.repository.PatientRepository;
import org.symptomcheck.capstone.repository.UserInfo;
import org.symptomcheck.capstone.repository.UserType;


@Controller
public class AdminEndPoint {

	public static int HTTP_OK 			= 200;
	public static int HTTP_BAD_REQUEST 	= 400;
	public static int HTTP_NOT_FOUND 	= 404;
	
	@Autowired
	DoctorRepository doctors;
	
	@Autowired
	PatientRepository patients;	
	
	@Autowired
	UserRepository userRepo;
	
	@Secured({"ROLE_PATIENT", "ROLE_DOCTOR"})
	@RequestMapping(value=SymptomManagerSvcApi.GCM_SVC_PATH, method=RequestMethod.POST)
	public @ResponseBody boolean sendGCMRegistrationId(
			@RequestBody String gcmRegistrationId,
			Principal user, 
			HttpServletRequest request,
			HttpServletResponse response){
		
		boolean status = true;
		if(request.isUserInRole("ROLE_PATIENT")){
			Patient patient = patients.findOne(user.getName());
			if(patient != null){
				patient.addGcmRegistrationId(gcmRegistrationId);
				try{
					//patients.save(patient);
					PMF.get().getPersistenceManager().close();
				}catch(Exception e){
					status = false;
				}				
			}else{
				response.setStatus(HTTP_NOT_FOUND);
			}
		}
		else if(request.isUserInRole("ROLE_DOCTOR")){
			Doctor doctor = doctors.findOne(user.getName());
			if(doctor != null){
				doctor.addGcmRegistrationId(gcmRegistrationId);
				try{
					//doctors.save(doctor);
					PMF.get().getPersistenceManager().close();
				}catch(Exception e){
					status = false;
				}				
			}else{
				response.setStatus(HTTP_NOT_FOUND);
			}
		}
		else{
			status = false;
		}
		return status;
	}
	
	@Secured({"ROLE_PATIENT", "ROLE_DOCTOR"})
	@RequestMapping(value=SymptomManagerSvcApi.GCM_DELETE_PATH, method=RequestMethod.POST)
	public @ResponseBody boolean clearGCMRegistration(
			@RequestBody String gcmRegistrationId,
			Principal userPrincipal,
			HttpServletRequest request){
		boolean status = true;
		if(request.isUserInRole("ROLE_PATIENT")){
			Patient patient = patients.findOne(userPrincipal.getName());
			if(patient != null){				
				//patient.setGcmRegistrationIds(new ArrayList<String>());
				if(gcmRegistrationId == "ALL"){
					Set<String> gcmIds = patient.getGcmRegistrationIds();
					for(String gcmId: gcmIds){
						patient.removeGcmRegistrationIds(gcmId.replace("\"", ""));
					}
				}else{
					patient.removeGcmRegistrationIds(gcmRegistrationId);
				}
				PMF.get().getPersistenceManager().close();
			}
		
		}else if(request.isUserInRole("ROLE_DOCTOR")){
			Doctor doctor = doctors.findOne(userPrincipal.getName());
			if(doctor != null){
				//doctor.setGcmRegistrationIds(new ArrayList<String>());
				if(gcmRegistrationId == "ALL"){
					Set<String> gcmIds = doctor.getGcmRegistrationIds();
					for(String gcmId: gcmIds){
						doctor.removeGcmRegistrationIds(gcmId.replace("\"", ""));
					}
				}else{
					doctor.removeGcmRegistrationIds(gcmRegistrationId);
				}
				PMF.get().getPersistenceManager().close();
			}
		}
		return status;
	}
	
	@Secured({"ROLE_PATIENT", "ROLE_DOCTOR"})
	@RequestMapping(value="/userinfo", method=RequestMethod.GET)
	public @ResponseBody UserInfo verifyUser(Principal user, HttpServletRequest request){
		
		UserInfo userInfo = new UserInfo();
		userInfo.setLogged(true);
		userInfo.setUserIdentification(user.getName());
		if(request.isUserInRole("ROLE_PATIENT")){
			userInfo.setUserType(UserType.PATIENT);
			Patient p = patients.findOne(user.getName());
			if(p != null){
				userInfo.setFirstName(p.getFirstName());
				userInfo.setLastName(p.getLastName());
				userInfo.setAnagPresent(true);
			}
			
		}
		else if(request.isUserInRole("ROLE_DOCTOR")){
			userInfo.setUserType(UserType.DOCTOR);
			Doctor p = doctors.findOne(user.getName());
			if(p != null){
				userInfo.setFirstName(p.getFirstName());
				userInfo.setLastName(p.getLastName());
				userInfo.setAnagPresent(true);
			}
		}
		else{
			userInfo.setUserType(UserType.UNKWOWN);
		}
		return userInfo;
	}
	
		
	@RequestMapping(value=SymptomManagerSvcApi.DOCTOR_SVC_PATH + "/{uniqueDoctorID}/patients", method=RequestMethod.POST)
	public @ResponseBody Doctor addPatientToDoctor(
			@PathVariable("uniqueDoctorID") String uniqueDoctorId, 
			@RequestBody Patient patient){	
			Doctor d = doctors.findOne(uniqueDoctorId);
			Patient p = patients.findOne(patient.getMedicalRecordNumber());
			if((d != null)
					&& (p != null)){
				d.addPatient(patient.getMedicalRecordNumber());
				p.addDoctor(d.getUniqueDoctorId());	
				PMF.get().getPersistenceManager().close();
			}
			return d;
	}
	
	@RequestMapping(value=SymptomManagerSvcApi.DOCTOR_SVC_PATH, method=RequestMethod.POST)
	public @ResponseBody Doctor addDoctor(@RequestBody Doctor doctor){	
	    Doctor d = doctors.findOne(doctor.getUniqueDoctorId());	   
	    if((d != null)){
	    	d.setFirstName(doctor.getFirstName());
	    	d.setLastName(doctor.getLastName());
	    	//doctor = doctors.save(d);
	    	PMF.get().getPersistenceManager().close();
	    }
	    else {
	    	doctors.save(doctor);
	    }
	    return doctor;	 
	}
	
	@RequestMapping(value=SymptomManagerSvcApi.DOCTOR_SVC_PATH, method=RequestMethod.GET)	
	public @ResponseBody Collection<Doctor> getDoctorList() {		
		return (Collection<Doctor>) doctors.findAll();
	}
	
	

	@RequestMapping(value=SymptomManagerSvcApi.PATIENT_SVC_PATH, method=RequestMethod.POST)
	public @ResponseBody Patient addPatient(@RequestBody Patient patient){		
		System.out.print("PatientEndPoint=>addPatient" + patient.getMedicalRecordNumber());
	    Patient p = patients.findOne(patient.getMedicalRecordNumber());	   
	    if(p != null){
	    	p.setFirstName(patient.getFirstName());
	    	p.setLastName(patient.getLastName());
	    	//patients.save(p);
	    	PMF.get().getPersistenceManager().close();
	    }
	    else {
	    	patients.save(patient);
	    }
	    return patient;
	}
	
	@Secured({"ROLE_PATIENT", "ROLE_DOCTOR"})
	@RequestMapping(value=SymptomManagerSvcApi.DOCTOR_SVC_PATH + "/{uniqueDoctorID}", method=RequestMethod.GET)	
	public @ResponseBody Doctor findDoctorByUniqueDoctorID(
			@PathVariable("uniqueDoctorID") String uniqueDoctorID) {		
		return doctors.findOne(uniqueDoctorID);
	}	
	
	
	@Secured({"ROLE_PATIENT", "ROLE_DOCTOR"})
	@RequestMapping(value=SymptomManagerSvcApi.PATIENT_SVC_PATH + "/{medicalRecordNumber}", method=RequestMethod.GET)	
	public @ResponseBody Patient findPatientByMedicalRecordNumber(
			@PathVariable("medicalRecordNumber") String medicalRecordNumber) {		
		return patients.findOne(medicalRecordNumber);
	}		

	@Secured({"ROLE_PATIENT", "ROLE_DOCTOR"})
	@RequestMapping(value=SymptomManagerSvcApi.PATIENT_SVC_PATH, method=RequestMethod.GET)	
	public @ResponseBody Collection<Patient> getPatientList() {		
		return (Collection<Patient>) patients.findAll();
	}
	
	@RequestMapping(value = "/createAccount/{username}", method=RequestMethod.GET)	
	public @ResponseBody User initAccounts(
			@PathVariable("username") String username) {		
		Patient patient = null;
		Doctor doctor = null;
		User user = new User();
		User userOut = new User();
		user.setIsAdmin(1);
		if(username.toLowerCase().startsWith("p")){
			user.setRole("PATIENT");
			user.setPassword("pass");
			patient = new Patient(username, "", "");
			
		}else if(username.toLowerCase().startsWith("d")){
			user.setRole("DOCTOR");
			user.setPassword("pass");
			doctor = new Doctor(username, "", "");
			//doctors.save(doctor);			
		}		
		user.setUsername(username);
		if((userOut = userRepo.save(user)) != null){
			if(patient != null){
				patients.save(patient);
			}
			if(doctor != null){
				doctors.save(doctor);
			}			
		}
		
		return userOut;
	}
}
