package org.symptomcheck.capstone.client;

import java.util.Collection;

import org.symptomcheck.capstone.repository.CheckIn;
import org.symptomcheck.capstone.repository.Doctor;
import org.symptomcheck.capstone.repository.PainMedication;
//import org.symptomcheck.capstone.repository.CheckIn;
//import org.symptomcheck.capstone.repository.Doctor;
//import org.symptomcheck.capstone.repository.IUser;
//import org.symptomcheck.capstone.repository.PainMedication;
import org.symptomcheck.capstone.repository.Patient;
import org.symptomcheck.capstone.repository.UserInfo;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

public interface SymptomManagerSvcApi {
	
	public static final String PATIENT_ID_1 = "patient001";
	public static final String PATIENT_ID_2 = "patient002";
	public static final String PATIENT_ID_3 = "patient003";
	public static final String PATIENT_ID_4= "patient004";
	
	public static final String DOCTOR_ID_1 = "doctor001";
	public static final String DOCTOR_ID_2 = "doctor002";
	
	public static final String DOCTOR_SEARCH_BY_PATIENT_PATH = SymptomManagerSvcApi.DOCTOR_SVC_PATH + "/search/findByPatient";
	
	public static final String PATIENT_SVC_PATH = "/patient";
	
	public static final String DOCTOR_SVC_PATH = "/doctor";

	public static final String TOKEN_PATH = "/oauth/token";
	public static final String CHECKIN_SVC_PATH = "/checkin";
	
	public static final String GCM_SVC_PATH = "/userinfo/gcm";
	
	public static final String GCM_DELETE_PATH = GCM_SVC_PATH + "/clear";
	
	

	//----------------- ADMIN methods ----------------- //
	@POST(DOCTOR_SVC_PATH + "/{uniqueDoctorID}/patients")
	public Doctor addPatientToDoctor(@Path("uniqueDoctorID") String uniqueDoctorID, @Body Patient patient);
	
	@POST(PATIENT_SVC_PATH)
	public Patient addPatient(@Body Patient patient);
	
	@POST(DOCTOR_SVC_PATH)
	public Doctor addDoctor(@Body Doctor d);
	
	@GET(value="/userinfo")
	public UserInfo verifyUser();
	
	@GET(DOCTOR_SVC_PATH)
	public Collection<Doctor> getDoctorList();	
	
	@GET(PATIENT_SVC_PATH)
	public Collection<Patient> getPatientList();
	
	//----------------- PATIENT methods ----------------- //
	@GET(PATIENT_SVC_PATH + "/{medicalRecordNumber}")
	public Patient findPatientByMedicalRecordNumber(@Path("medicalRecordNumber") String medicalCardNumber);
	
	@GET(PATIENT_SVC_PATH + "/{medicalRecordNumber}/doctors/search")
	public Collection<Doctor> findDoctorsByPatient(@Path("medicalRecordNumber") String medicalCardNumber);
	
	@POST(PATIENT_SVC_PATH + "/{medicalRecordNumber}/checkins")
	public CheckIn addCheckIn(@Path("medicalRecordNumber") String medicalCardNumber, @Body CheckIn checkIn);
	
	@POST(PATIENT_SVC_PATH + "/{medicalRecordNumber}/medications")
	public PainMedication addPainMedication(
			@Path("medicalRecordNumber") String medicalCardNumber,
			@Body PainMedication painMedication);

	@GET(PATIENT_SVC_PATH + "/{medicalRecordNumber}/medications/search")
	public Collection<PainMedication> findPainMedicationsByPatient(
			@Path("medicalRecordNumber") String medicalCardNumber);	
	
	//----------------- DOCTOR methods ----------------- //	
	@GET(DOCTOR_SVC_PATH + "/{uniqueDoctorID}")
	public Doctor findDoctorByUniqueDoctorID(@Path("uniqueDoctorID") String uniqueDoctorID);
	
	@GET(DOCTOR_SVC_PATH + "/{uniqueDoctorID}/patients/search")
	public Collection<Patient> findPatientsByDoctor(@Path("uniqueDoctorID") String uniqueDoctorID);		
	
	@GET(PATIENT_SVC_PATH + "/{medicalRecordNumber}/checkins/search")
	public Collection<CheckIn> findCheckInsByPatient(@Path("medicalRecordNumber") String medicalCardNumber);
	
	//----------------- GCM methods ----------------- //
	@POST(GCM_SVC_PATH)
	public boolean sendGCMRegistrationId(@Body String gcmRegistrationId);

    @POST(GCM_DELETE_PATH)
    public boolean clearGCMRegistration(@Body String gcmRegistrationId);
}
