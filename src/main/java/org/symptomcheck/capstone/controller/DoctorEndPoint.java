package org.symptomcheck.capstone.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.symptomcheck.capstone.client.SymptomManagerSvcApi;
import org.symptomcheck.capstone.repository.CheckIn;
import org.symptomcheck.capstone.repository.CheckInRepository;
import org.symptomcheck.capstone.repository.Doctor;
import org.symptomcheck.capstone.repository.DoctorRepository;
import org.symptomcheck.capstone.repository.Patient;
import org.symptomcheck.capstone.repository.PatientRepository;

import com.google.common.collect.Lists;

@Controller
public class DoctorEndPoint {
	

	@Autowired
	DoctorRepository doctors;
	
	@Autowired
	PatientRepository patients;	
		
	@Autowired
	CheckInRepository checkIns;
	
	//TODO#BPR_1
	//TODO#BPR_2
	@Secured({"ROLE_PATIENT", "ROLE_DOCTOR"}) 
	@RequestMapping(value= SymptomManagerSvcApi.DOCTOR_SVC_PATH + "/{uniqueDoctorID}/patients/checkins/searchByPatientName", method=RequestMethod.GET)		
	public @ResponseBody Collection<CheckIn> findCheckInsByPatientName(
			@PathVariable("uniqueDoctorID") String medicalRecordNumber,
			@RequestParam(value="firstName", required=true) String patientFirstName,
			@RequestParam(value="lastName", required=true) String patientLastName){	
		
		List<CheckIn> results = Lists.newArrayList();
		List<Patient> patientData =  patients.
				findByFirstNameAndLastName(patientFirstName,patientLastName);
						
		if(!patientData.isEmpty()){
			results = (List<CheckIn>) checkIns.findByPatientMedicalNumber(patientData.get(0).getMedicalRecordNumber());
		}
		return results;
	}
	
	//TODO#BPR_1
	//TODO#BPR_2
	@Secured("ROLE_DOCTOR")
	@RequestMapping(value=SymptomManagerSvcApi.DOCTOR_SVC_PATH + "/{uniqueDoctorID}/patients/search", 
			method=RequestMethod.GET)
	public @ResponseBody Collection<Patient> findPatientsByDoctor(
			@PathVariable("uniqueDoctorID") String uniqueDoctorID){
		Collection<Patient> patientList = new ArrayList<Patient>();
		Doctor doctor = doctors.findOne(uniqueDoctorID);
		if(doctor != null){
			for(String medicalNumber : doctor.getPatients()){
				patientList.add(patients.findOne(medicalNumber));
			}				
		}
		return patientList;
	}
}
