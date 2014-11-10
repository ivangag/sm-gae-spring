package org.symptomcheck.capstone.controller;

import java.util.ArrayList;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.symptomcheck.capstone.client.SymptomManagerSvcApi;
import org.symptomcheck.capstone.repository.Doctor;
import org.symptomcheck.capstone.repository.DoctorRepository;
import org.symptomcheck.capstone.repository.Patient;
import org.symptomcheck.capstone.repository.PatientRepository;

@Controller
public class DoctorEndPoint {
	

	@Autowired
	DoctorRepository doctors;
	
	@Autowired
	PatientRepository patients;	
		

	/*
	@Secured("ROLE_DOCTOR")
	@RequestMapping(value=SymptomManagerSvcApi.DOCTOR_SEARCH_BY_PATIENT_PATH + "/{medicalRecordNumber}",
			method=RequestMethod.GET)	
	public @ResponseBody Collection<Doctor> findDoctorByPatient(@PathVariable("medicalRecordNumber") String medicalCardNumber) {	
		Collection<Doctor> d1 = new ArrayList<Doctor>();	
		Collection<Patient> p = patients.findByMedicalRecordNumber(medicalCardNumber);
		if(!p.isEmpty()){
			d1 = (Collection<Doctor>) doctors.findByPatients((Patient) p.toArray()[0]);
		}
		return d1;
	}*/

	@Secured("ROLE_DOCTOR")
	@RequestMapping(value=SymptomManagerSvcApi.DOCTOR_SVC_PATH + "/{uniqueDoctorID}/patients/search", 
			method=RequestMethod.GET)
	public @ResponseBody Collection<Patient> findPatientsByDoctor(
			@PathVariable("uniqueDoctorID") String uniqueDoctorID){
		//String userName = User.getName();
		Collection<Patient> patientList = new ArrayList<Patient>();
		Doctor doctor = doctors.findOne(uniqueDoctorID);
		if(doctor != null){
			for(String medicalNumber : doctor.getPatients())
			{
				patientList.add(patients.findOne(medicalNumber));
			}
				
			//patientList = doctor.getPatients(); 
		}
		return patientList;
	}
}
