package org.symptomcheck.capstone.integration.test;

import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.junit.Test;
import org.magnum.mobilecloud.integration.test.UnsafeHttpsClient;
import org.symptomcheck.capstone.client.SecuredRestBuilder;
import org.symptomcheck.capstone.client.SymptomManagerSvcApi;
import org.symptomcheck.capstone.repository.CheckIn;
import org.symptomcheck.capstone.repository.Doctor;
import org.symptomcheck.capstone.repository.FeedStatus;
import org.symptomcheck.capstone.repository.PainLevel;
import org.symptomcheck.capstone.repository.PainMedication;
import org.symptomcheck.capstone.repository.Patient;
import org.symptomcheck.capstone.repository.Question;
import org.symptomcheck.capstone.repository.QuestionType;
import org.symptomcheck.capstone.repository.UserInfo;
import org.symptomcheck.capstone.symptom.SymptomTestData;

import retrofit.ErrorHandler;
import retrofit.RestAdapter.LogLevel;
import retrofit.RetrofitError;
import retrofit.client.ApacheClient;



public class PopulateDataBaseTest {
	
	private class ErrorRecorder implements ErrorHandler {

		private RetrofitError error;

		@Override
		public Throwable handleError(RetrofitError cause) {
			error = cause;
			return error.getCause();
		}

		public RetrofitError getError() {
			return error;
		}
	}

	//private final String TEST_URL_LOCAL_TRUSTED = "http://localhost:8080";
	//private final String TEST_URL_LOCAL_TRUSTED = "http://localhost:8080/symptom-management-capstone-0.0.4";
	private final String TEST_URL_LOCAL_TRUSTED = "https://spring-mvc-capstone-test.appspot.com";
	private final String TEST_URL_REMOTE_TRUSTED = "https://spring-mvc-capstone-test.appspot.com";
	
	private final String USERNAME = "admin";
	private final String PASSWORD = "pass";
	//private final String CLIENT_PATIENT_ID = "patient";
	private final String CLIENT_PATIENT_ID = "patient";
	private final String CLIENT_DOCTOR_ID = "doctor";
	ErrorRecorder error = new ErrorRecorder();

	/*
	private SymptomManagerSvcApi symptomSvc_TRUSTED = new SecuredRestBuilder()
	.setLoginEndpoint(TEST_URL_LOCAL_TRUSTED + SymptomManagerSvcApi.TOKEN_PATH)
	.setUsername(USERNAME)
	.setPassword(PASSWORD)
	.setClientId(CLIENT_ID)
	.setClient(new ApacheClient(UnsafeHttpsClient.createUnsafeClient()))
	//.setClient(new ApacheClient())
	.setEndpoint(TEST_URL_LOCAL_TRUSTED).setLogLevel(LogLevel.FULL).build()
	.create(SymptomManagerSvcApi.class);
	*/
	private SymptomManagerSvcApi symptomSvcAsPatient1 = new SecuredRestBuilder()
	.setLoginEndpoint(TEST_URL_LOCAL_TRUSTED + SymptomManagerSvcApi.TOKEN_PATH)
	.setUsername(SymptomManagerSvcApi.PATIENT_ID_1)
	.setPassword(PASSWORD)
	.setClientId(CLIENT_PATIENT_ID)
	.setClient(new ApacheClient(UnsafeHttpsClient.createUnsafeClient()))
	.setErrorHandler(error)
	//.setClient(new ApacheClient())
	.setEndpoint(TEST_URL_LOCAL_TRUSTED).setLogLevel(LogLevel.FULL).build()
	.create(SymptomManagerSvcApi.class);	
	
	private SymptomManagerSvcApi symptomSvcAsPatient2 = new SecuredRestBuilder()
	.setLoginEndpoint(TEST_URL_LOCAL_TRUSTED + SymptomManagerSvcApi.TOKEN_PATH)
	.setUsername(SymptomManagerSvcApi.PATIENT_ID_2)
	.setPassword(PASSWORD)
	.setClientId(CLIENT_PATIENT_ID)
	.setClient(new ApacheClient(UnsafeHttpsClient.createUnsafeClient()))
	.setErrorHandler(error)
	//.setClient(new ApacheClient())
	.setEndpoint(TEST_URL_LOCAL_TRUSTED).setLogLevel(LogLevel.FULL).build()
	.create(SymptomManagerSvcApi.class);		

	private SymptomManagerSvcApi symptomSvcAsPatient1Trusted = new SecuredRestBuilder()
	.setLoginEndpoint(TEST_URL_REMOTE_TRUSTED + SymptomManagerSvcApi.TOKEN_PATH)
	.setUsername(SymptomManagerSvcApi.PATIENT_ID_1)
	.setPassword(PASSWORD)
	.setClientId(CLIENT_PATIENT_ID)
	.setClient(new ApacheClient(UnsafeHttpsClient.createUnsafeClient()))
	.setErrorHandler(error)
	//.setClient(new ApacheClient())
	.setEndpoint(TEST_URL_REMOTE_TRUSTED).setLogLevel(LogLevel.FULL).build()
	.create(SymptomManagerSvcApi.class);
	
	private SymptomManagerSvcApi symptomSvcASDoctor1 = new SecuredRestBuilder()
	.setLoginEndpoint(TEST_URL_LOCAL_TRUSTED + SymptomManagerSvcApi.TOKEN_PATH)
	.setUsername(SymptomManagerSvcApi.DOCTOR_ID_1)
	.setPassword(PASSWORD)
	.setClientId(CLIENT_DOCTOR_ID)
	.setClient(new ApacheClient(UnsafeHttpsClient.createUnsafeClient()))
	.setErrorHandler(error)
	//.setClient(new ApacheClient())
	.setEndpoint(TEST_URL_LOCAL_TRUSTED).setLogLevel(LogLevel.FULL).build()
	.create(SymptomManagerSvcApi.class);	
	
	private SymptomManagerSvcApi symptomSvcASDoctor2 = new SecuredRestBuilder()
	.setLoginEndpoint(TEST_URL_LOCAL_TRUSTED + SymptomManagerSvcApi.TOKEN_PATH)
	.setUsername(SymptomManagerSvcApi.DOCTOR_ID_2)
	.setPassword(PASSWORD)
	.setClientId(CLIENT_DOCTOR_ID)
	.setClient(new ApacheClient(UnsafeHttpsClient.createUnsafeClient()))
	.setErrorHandler(error)
	//.setClient(new ApacheClient())
	.setEndpoint(TEST_URL_LOCAL_TRUSTED).setLogLevel(LogLevel.FULL).build()
	.create(SymptomManagerSvcApi.class);		
	
	Patient patient1User = SymptomTestData.createDummyPatient(SymptomManagerSvcApi.PATIENT_ID_1, "patient_1_FirstName", "patient_1_LastName");
	Patient patient2User = SymptomTestData.createDummyPatient(SymptomManagerSvcApi.PATIENT_ID_2, "patient_2_FirstName", "patient_2_LastName");
	Patient patient3User = SymptomTestData.createDummyPatient(SymptomManagerSvcApi.PATIENT_ID_3, "patient_3_FirstName", "patient_3_LastName");
	Patient patient4User = SymptomTestData.createDummyPatient(SymptomManagerSvcApi.PATIENT_ID_4, "patient_34_FirstName", "patient_4_LastName");
	Doctor doctor1User = SymptomTestData.createDummyDoctor(SymptomManagerSvcApi.DOCTOR_ID_1, "doctor_1_Name", "doctor_2_LastName");
	Doctor doctor2User = SymptomTestData.createDummyDoctor(SymptomManagerSvcApi.DOCTOR_ID_2, "Armando", "Della Torre");
	
/*	
	//@Test
	public void addPatient(){
		
		Collection<Patient> patients = symptomSvcAsPatient1.getPatientList();
		Collection<Doctor> doctors = symptomSvcASDoctor1.getDoctorList();
		//patientDummy.setDoctorOwnerId(doctorDummy.getUniqueDoctorId());		
		Calendar calendar = Calendar.getInstance();
		Long timestamp = Calendar.getInstance().getTimeInMillis();
		Timestamp currentTimestamp = new java.sql.Timestamp(calendar.getTime().getTime());		
		CheckIn checkIn = new CheckIn(timestamp.toString(),PainLevel.MODERATE,FeedStatus.SOME);
		Question question1 = new Question("Did you Take Lortan?","YES",QuestionType.Medication,timestamp.toString());
		Question question2 = new Question("Did you Take Oxytocin?","NO",QuestionType.Medication,timestamp.toString());
		Question question3 = new Question("Did you Take Mosticotol?","NO",QuestionType.Medication,timestamp.toString());
		checkIn.addQuestions(question1);
		checkIn.addQuestions(question2);
		checkIn.addQuestions(question3);
		
		//pre-populate
		Patient patient1 = symptomSvcAsPatient1.addPatient(patient1User);
		Patient patient2 = symptomSvcAsPatient1.addPatient(patient2User);
		Doctor doctor = symptomSvcASDoctor1.addDoctor(doctor2User);
		
		//retrieve check-in if present
		List<CheckIn> checkIns;
		try{		
			checkIns = (List<CheckIn>) symptomSvcAsPatient1.
				findCheckInsByPatient(patient1User.getMedicalRecordNumber());
		}catch(Exception e){
		
		}
		//add check-in 
		symptomSvcAsPatient1.addCheckIn(patient1.getMedicalRecordNumber(),checkIn);	
		//retrieve check-in if present
		checkIns = (List<CheckIn>) symptomSvcAsPatient1.
				findCheckInsByPatient(patient1User.getMedicalRecordNumber());
		List<PainMedication> painMedsPatient1,painMedsPatient2;
		//add painMedication
		List<PainMedication> painMeds = (List<PainMedication>) symptomSvcASDoctor1.addPainMedication(patient1.getMedicalRecordNumber(), 
				new PainMedication("LORTAB"));
		painMeds = (List<PainMedication>) symptomSvcASDoctor1.addPainMedication(patient2.getMedicalRecordNumber(), 
				new PainMedication("OXYTON"));	
		painMedsPatient1 = (List<PainMedication>) symptomSvcASDoctor1.findPainMedicationsByPatient(patient1.getMedicalRecordNumber());
		painMedsPatient2 = (List<PainMedication>) symptomSvcASDoctor1.findPainMedicationsByPatient(patient2.getMedicalRecordNumber());
		PainMedication painMed = painMeds.get(0);
		painMed.setLastTakingDateTime(timestamp.toString());
		//painMed = symptomSvcAsPatient1.updatePainMedication(patient1.getMedicalRecordNumber(), painMed);
		
		painMedsPatient1 = (List<PainMedication>) symptomSvcASDoctor1.findPainMedicationsByPatient(patient1.getMedicalRecordNumber());
		painMedsPatient2 = (List<PainMedication>) symptomSvcASDoctor1.findPainMedicationsByPatient(patient2.getMedicalRecordNumber());
				
		//retrieve patients
		//patients = symptomSvcAsPatient1.getPatientList();
		//retrieve patients by doctor
		try{
			patients = symptomSvcASDoctor1.findPatientsByDoctor(doctor1User.getUniqueDoctorId());
			patients = symptomSvcAsPatient1.findPatientsByDoctor(doctor1User.getUniqueDoctorId());
		}catch(Exception e){
			
		}

		try{
		//retrieve doctors by patient
		doctors = symptomSvcAsPatient1.findDoctorsByPatient(patient1.getMedicalRecordNumber());
		doctors = symptomSvcASDoctor1.findDoctorsByPatient(patient1.getMedicalRecordNumber());
		}catch(Exception e){
			
		}
		

		//doctor2User.addPatient(patient);		
		 doctors = symptomSvcASDoctor1.getDoctorList();
		 
		 //Collection<Doctor> doctorsPatient = symptomSvcAsPatient1.findDoctorByPatient(patient1.getMedicalRecordNumber());	
		 //System.out.print(doctorsPatient.toString());
		assert(true);
	}
	
	//@Test
	public void addPatientToDoctor(){
		IUser userInfo = symptomSvcAsPatient1.verifyUser();
		
		Patient patient1 = symptomSvcAsPatient1.addPatient(patient1User);
		Patient patient2 = symptomSvcAsPatient1.addPatient(patient2User);
		Patient patient3 = symptomSvcAsPatient1.addPatient(patient3User);
		Doctor doctor = symptomSvcASDoctor1.addDoctor(doctor2User);
		symptomSvcASDoctor1.addDoctor(doctor1User);
		doctor = symptomSvcASDoctor1.addPatientToDoctor(doctor1User.getUniqueDoctorId(), patient1);
		doctor = symptomSvcASDoctor1.addPatientToDoctor(doctor1User.getUniqueDoctorId(), patient2);
		doctor = symptomSvcASDoctor2.addPatientToDoctor(doctor2User.getUniqueDoctorId(), patient3);
		
		
	}
	
	@Test
	public void addCheckins(){
		Calendar calendar = Calendar.getInstance();
		Long timestamp = Calendar.getInstance().getTimeInMillis();
		Timestamp currentTimestamp = new java.sql.Timestamp(calendar.getTime().getTime());		
		CheckIn checkIn = new CheckIn(timestamp.toString(),PainLevel.MODERATE,FeedStatus.SOME);
		Question question1 = new Question("Did you Take Lortan?","YES",QuestionType.Medication,timestamp.toString());
		Question question2 = new Question("Did you Take Oxytocin?","NO",QuestionType.Medication,timestamp.toString());
		Question question3 = new Question("Did you Take Mosticotol?","NO",QuestionType.Medication,timestamp.toString());
		checkIn.addQuestions(question1);
		checkIn.addQuestions(question2);
		checkIn.addQuestions(question3);
		
		patient1User.addGcmRegistrationId("gcmRegId0001");
		Patient patient1 = symptomSvcAsPatient1.addPatient(patient1User);
		
		
		symptomSvcAsPatient1.addCheckIn(patient1.getMedicalRecordNumber(), checkIn);
	}
*/
	//@Test
	public void addPatient(){
		
		try{		
		Patient patient4 = symptomSvcAsPatient1.addPatient(patient4User);
		Patient patient1 = symptomSvcAsPatient1.addPatient(patient1User);
		Patient patient2 = symptomSvcAsPatient1.addPatient(patient2User);
		Patient patient3 = symptomSvcAsPatient1.addPatient(patient3User);
		
		Collection<Patient> patients= symptomSvcAsPatient1.getPatientList();
		}catch(Exception e){
			
		}
		//Doctor doctor = symptomSvcASDoctor1.addDoctor(doctor2User);
		//symptomSvcASDoctor1.addDoctor(doctor1User);
		//doctor = symptomSvcASDoctor1.addPatientToDoctor(doctor1User.getUniqueDoctorId(), patient1);
		//doctor = symptomSvcASDoctor1.addPatientToDoctor(doctor1User.getUniqueDoctorId(), patient2);
		//doctor = symptomSvcASDoctor2.addPatientToDoctor(doctor2User.getUniqueDoctorId(), patient3);
		
		
	}
	
	
	//@Test
	public void addPatientToRemote(){
		
		patient4User.setFirstName("patient4_New_Name");
		try{		
		Patient patient4 = symptomSvcAsPatient1Trusted.addPatient(patient4User);
		Patient patient1 = symptomSvcAsPatient1Trusted.addPatient(patient1User);
		Patient patient2 = symptomSvcAsPatient1Trusted.addPatient(patient2User);
		Patient patient3 = symptomSvcAsPatient1Trusted.addPatient(patient3User);
		
		Collection<Patient> patients= symptomSvcAsPatient1Trusted.getPatientList();
		}catch(Exception e){
			
		}
		//Doctor doctor = symptomSvcASDoctor1.addDoctor(doctor2User);
		//symptomSvcASDoctor1.addDoctor(doctor1User);
		//doctor = symptomSvcASDoctor1.addPatientToDoctor(doctor1User.getUniqueDoctorId(), patient1);
		//doctor = symptomSvcASDoctor1.addPatientToDoctor(doctor1User.getUniqueDoctorId(), patient2);
		//doctor = symptomSvcASDoctor2.addPatientToDoctor(doctor2User.getUniqueDoctorId(), patient3);
		
	}
	//@Test
	public void addPainMedications(){
		PainMedication painMed = symptomSvcASDoctor1.addPainMedication(patient1User.getMedicalRecordNumber(), 
				new PainMedication("LORTAB"));		
		painMed = symptomSvcASDoctor1.addPainMedication(patient2User.getMedicalRecordNumber(), 
				new PainMedication("OXYTUI"));				
		
		//List<PainMedication> painMeds = (List<PainMedication>) 
		List<PainMedication> painMeds  = (List<PainMedication>) symptomSvcASDoctor1.findPainMedicationsByPatient(patient1User.getMedicalRecordNumber());
	}
	
	@Test
	public void addCheckIns(){
		//symptomSvcASDoctor1.clearGCMRegistration("ALL");
		/*
		Doctor doctor = symptomSvcASDoctor1.findDoctorByUniqueDoctorID(doctor1User.getUniqueDoctorId());
		for(String gcmId : doctor.getGcmRegistrationIds()){
			//String gcmId2 = gcmId.replace("\"", "");
			symptomSvcASDoctor1.clearGCMRegistration(gcmId.replace("\"", ""));
		}*/
		Map<String,String> meds = new HashMap<String,String>();
		
		
		meds.put("LOXAN","YES");
		meds.put("ORTYCIN","YES");
		meds.put("MOSTICON","NO");
		
		Calendar calendar = Calendar.getInstance();
		Long timestamp = Calendar.getInstance().getTimeInMillis();
		CheckIn checkIn = SymptomTestData.createDummyCheckIn(timestamp, PainLevel.MODERATE, FeedStatus.SOME, meds);
		//add check-in 
		CheckIn checkInRes =symptomSvcAsPatient1.addCheckIn(patient1User.getMedicalRecordNumber(),checkIn);	
		List<CheckIn> checkInsPatient1 = (List<CheckIn>) symptomSvcAsPatient1.findCheckInsByPatient(patient1User.getMedicalRecordNumber());
		List<CheckIn> checkInsPatient2 = (List<CheckIn>) symptomSvcAsPatient1.findCheckInsByPatient(patient2User.getMedicalRecordNumber());
		meds.clear();
		meds.put("LOXAN","NO");
		checkIn = SymptomTestData.createDummyCheckIn(timestamp, PainLevel.SEVERE, FeedStatus.CANNOT_EAT, meds);	
		symptomSvcAsPatient1.addCheckIn(patient1User.getMedicalRecordNumber(),checkIn);
		checkIn = SymptomTestData.createDummyCheckIn(timestamp, PainLevel.WELL_CONTROLLED, FeedStatus.NO, meds);
		symptomSvcAsPatient1.addCheckIn(patient1User.getMedicalRecordNumber(),checkIn);
		
		meds.clear();
		meds.put("LOXAN","YES");
		meds.put("KOLAX","YES");
		checkIn = SymptomTestData.createDummyCheckIn(timestamp, PainLevel.MODERATE, FeedStatus.SOME, meds);
		symptomSvcAsPatient1.addCheckIn(patient2User.getMedicalRecordNumber(),checkIn);
				
		meds.clear();
		meds.put("MIOS","YES");
		meds.put("RAMOX","NO");
		checkIn = SymptomTestData.createDummyCheckIn(timestamp, PainLevel.WELL_CONTROLLED, FeedStatus.NO, meds);
		symptomSvcAsPatient1.addCheckIn(patient3User.getMedicalRecordNumber(),checkIn);
	}
	
	//@Test
	public void addDoctor(){
		/*
		symptomSvcAsPatient2.addPatient(patient2User);
		symptomSvcAsPatient2.addPatient(patient3User);
		symptomSvcAsPatient2.addDoctor(doctor1User);
		symptomSvcAsPatient2.addDoctor(doctor2User);*/
		
		UserInfo user = symptomSvcAsPatient2.verifyUser();
		String userId = user.getUserIdentification();
		Collection<Patient> patientsOfDoctor;
		Collection<Doctor> doctorsOfPatient;
		switch(user.getUserType()){
		
		case DOCTOR:
			if(!user.getAnagPresent()){
				
			}
			//get patient list
			patientsOfDoctor =  symptomSvcAsPatient2.findPatientsByDoctor(userId);
			break;
		case PATIENT:
			if(!user.getAnagPresent()){
				
			}
			//get doctor list
			doctorsOfPatient = symptomSvcAsPatient2.findDoctorsByPatient(userId);
			break;
			default:
				break;
		}
		
		patient1User.setFirstName("patient1UserNEWNAME");
		Patient patient1 = symptomSvcAsPatient1.addPatient(patient1User);
		Doctor doctor2 = symptomSvcASDoctor1.addDoctor(doctor2User);
		symptomSvcASDoctor1.addDoctor(doctor1User);

		symptomSvcASDoctor1.sendGCMRegistrationId("gcmRegistrationForDoctor1");
		//doctor = symptomSvcASDoctor1.addPatientToDoctor(doctor1User.getUniqueDoctorId(), patient2);
		//doctor = symptomSvcASDoctor2.addPatientToDoctor(doctor2User.getUniqueDoctorId(), patient3);
		
	}	
	
	//@Test
	public void addPatientToDoctor(){
		Doctor doctor2 = symptomSvcASDoctor1.addDoctor(doctor2User);
		Doctor doctor1 = symptomSvcASDoctor1.addDoctor(doctor1User);
		patient1User.setFirstName("Mario");
		patient1User.setLastName("Milone");
		symptomSvcAsPatient1.addPatient(patient1User);
		patient2User.setFirstName("Alfredo");
		patient2User.setLastName("Acquasanta");
		symptomSvcAsPatient1.addPatient(patient2User);
		doctor2 = symptomSvcASDoctor1.addPatientToDoctor(doctor1User.getUniqueDoctorId(), patient1User);
		doctor2 = symptomSvcASDoctor1.addPatientToDoctor(doctor1User.getUniqueDoctorId(), patient2User);
		doctor2 = symptomSvcASDoctor1.addPatientToDoctor(doctor2User.getUniqueDoctorId(), patient2User);
		doctor2 = symptomSvcASDoctor1.addPatientToDoctor(doctor2User.getUniqueDoctorId(), patient2User);
	}
	
	//@Test
	public void sendGCMRegId(){

		String gcmRegistrationId = "Doctor1GCMId_" + UUID.randomUUID();
		symptomSvcASDoctor1.sendGCMRegistrationId(gcmRegistrationId);
		
	}
	
	//@Test
	public void loginAndSynchronizationFlow(){
		
		//**********PATIENT*****************///
		UserInfo user= null;
		//login
		try{
			user = symptomSvcAsPatient1.verifyUser();
		}catch(RetrofitError error){
			
		}
		if((user != null && user.getLogged())){
			final String userName =	user.getUserIdentification();
			//retrieve and save info...
			//1.Patient detail
			Patient patient = symptomSvcAsPatient1.findPatientByMedicalRecordNumber(userName);		
			//2.Check-Ins
			List<CheckIn> checkIns = (List<CheckIn>) symptomSvcAsPatient1.findCheckInsByPatient(userName);		
			//3.Medications
			List<PainMedication> medicines = (List<PainMedication>) symptomSvcAsPatient1.findPainMedicationsByPatient(userName);
		}			
		
		//**********DOCTOR******************///
		 user= null;
		//login
		try{
			user = symptomSvcASDoctor1.verifyUser();
		}catch(RetrofitError error){
			
		}
		if((user != null && user.getLogged())){
			final String userName =	user.getUserIdentification();
			//retrieve and save info...
			//1.Patient detail
			Doctor doctor = symptomSvcASDoctor1.findDoctorByUniqueDoctorID(userName);		
			//2.Patients list
			List<Patient> patients = (List<Patient>) symptomSvcASDoctor1.findPatientsByDoctor(userName);
			
			for(Patient patient : patients){
				//2.1 Patients' Check-Ins
				List<CheckIn> checkIns = (List<CheckIn>) symptomSvcASDoctor1.findCheckInsByPatient(patient.getMedicalRecordNumber());
				//2.2.Medications
				List<PainMedication> medicines = (List<PainMedication>) symptomSvcASDoctor1.findPainMedicationsByPatient(patient.getMedicalRecordNumber());				
			}			
		}			 
		 
	}
}
