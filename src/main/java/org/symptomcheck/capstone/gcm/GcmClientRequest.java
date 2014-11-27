package org.symptomcheck.capstone.gcm;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.symptomcheck.capstone.client.SymptomGcmMessagingApi;
import org.symptomcheck.capstone.repository.Doctor;
import org.symptomcheck.capstone.repository.DoctorRepository;
import org.symptomcheck.capstone.repository.GcmTrack;
import org.symptomcheck.capstone.repository.GcmTrackRepository;
import org.symptomcheck.capstone.repository.PMF;
import org.symptomcheck.capstone.repository.Patient;
import org.symptomcheck.capstone.repository.PatientRepository;
import org.symptomcheck.capstone.repository.UserType;

import com.google.api.client.util.Lists;

import retrofit.ErrorHandler;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.RestAdapter.LogLevel;

@Service
public class GcmClientRequest {
	

	@Autowired
	GcmTrackRepository gcmTracks;
	 

	@Autowired
	PatientRepository patients;

	@Autowired
	DoctorRepository doctors;

	public static class ErrorRecorder implements ErrorHandler {

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
	
	ExecutorService threadPool = Executors.newFixedThreadPool(4);
	
	
	//private static GcmClientRequest instance = new GcmClientRequest();
	/*
	public static GcmClientRequest get(){
		
		if(gcmClient == null){
			gcmClient = new RestAdapter.Builder()
			.setEndpoint(GcmConstants.GCM_MESSAGING_URL)
			.setLogLevel(LogLevel.FULL)
			.setErrorHandler(new ErrorRecorder())
			.build()
			.create(SymptomGcmMessagingApi.class);
		
		}
		return instance;
	}*/
	
	public GcmClientRequest()
	{
		init();
	}
	
	private void init() {
		if(gcmClient == null){
			gcmClient = new RestAdapter.Builder()
			.setEndpoint(GcmConstants.GCM_MESSAGING_URL)
			.setLogLevel(LogLevel.FULL)
			.setErrorHandler(new ErrorRecorder())
			.build()
			.create(SymptomGcmMessagingApi.class);
		
		}
	}

	private static SymptomGcmMessagingApi gcmClient = null;
	
	public SymptomGcmMessagingApi getApi(){
		
		return gcmClient;
	}
	
	public GcmResponse sendGcmMessage(String action, String username, UserType sender,
			List<String> regIds, String extraLogInfo){
				
		GcmResponse gcmResponse = postMessage(action, username, sender, regIds, extraLogInfo);
		
		//canonical handling
		if(gcmResponse != null) {
			// try to extract canonical
			if(Integer.valueOf(gcmResponse.canonical_ids) != null){
				int canonical_ids = Integer.valueOf(gcmResponse.canonical_ids);
				System.out.print(String.format("user_reg_ids: %s\n",regIds.toString()));
				System.out.print(String.format("canonical_ids COUNT: %d\n",canonical_ids));
				if(canonical_ids > 0) {						
					for(int idx = 0; idx < gcmResponse.results.size(); idx++){
						final String canonicalId = gcmResponse.results.get(idx).getRegistration_id();
						if((canonicalId != null) && 
								!canonicalId.isEmpty()){
							String gcmIdtoUpdate = regIds.get(idx);
							switch(sender){							
								case PATIENT: //TODO#BPR_1
									Collection<Doctor> doctorList = doctors.findByGcmRegistrationId(gcmIdtoUpdate);
									System.out.print(String.format("doctors.findByGcmRegistrationId: %d-canonical:%s\n",doctorList.size(),gcmResponse.results.get(idx).getRegistration_id()));
									for(Doctor doctor : doctorList){
										List<String> gcmDoctorIds = Lists.newArrayList(doctor.getGcmRegistrationIds());
										gcmDoctorIds.remove(gcmIdtoUpdate);
										gcmDoctorIds.add(gcmResponse.results.get(idx).getRegistration_id());
										doctor.setGcmRegistrationIds(gcmDoctorIds);
										PMF.get().getPersistenceManager().close();
									}
								break;
								case DOCTOR: //TODO#BPR_1
									Collection<Patient> patientList = patients.findByGcmRegistrationId(gcmIdtoUpdate);
									System.out.print(String.format("patients.findByGcmRegistrationId: %d-canonical:%s\n",patientList.size(),gcmResponse.results.get(idx).getRegistration_id()));
									for(Patient patient : patientList){
										List<String> gcmDoctorIds = Lists.newArrayList(patient.getGcmRegistrationIds());
										gcmDoctorIds.remove(gcmIdtoUpdate);
										gcmDoctorIds.add(gcmResponse.results.get(idx).getRegistration_id());
										patient.setGcmRegistrationIds(gcmDoctorIds);
										PMF.get().getPersistenceManager().close();
									}									
									break;
							default:
								break;
							}
						}												
					}						
				}						
			}
		}	
		
		return gcmResponse;
	}
	
	private GcmResponse postMessage(String action, String username, UserType userTrigger,
			List<String> regIds, String extraLogInfo){
		
		GcmResponse gcmResponse = null;
		//------------------------------GCM notification generation----------------------------------------//
		// Need of 
		// 1) GCM Action, Username, UserType,...
		// 2) reg_ids List where deliver message to
		//		
		boolean success = true;
		String resultInfo = "";		
		GcmMessage message = new GcmMessage();		
		message.setRegistration_ids(regIds);
		GcmData dataMsg = new GcmData();
		dataMsg.setAction(action);
		dataMsg.setUserName(username);
		dataMsg.setUserType(userTrigger);
		message.setData(dataMsg);	
		
		try{
			gcmResponse = this.getApi().sendMessage(GcmConstants.GCM_PROJECT_AUTHORIZATION_KEY, message);
			resultInfo = gcmResponse.toString();
		}catch(Exception error){
			success = false;
			resultInfo = error.getMessage();
			System.out.print("addCheckIn=>GCMSendError:" + resultInfo);
		}
		
		// Log gcm sending event
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Date today = Calendar.getInstance().getTime(); 
		GcmTrack gcmTrack = new GcmTrack();
		gcmTrack.setDate(df.format(today));
		gcmTrack.setGcmDestinationIds(message.getRegistration_ids());
		gcmTrack.setBundleContent(dataMsg.toString() + "\n\n" + (extraLogInfo != null ? extraLogInfo : extraLogInfo));
		gcmTrack.setResultInfo(resultInfo.substring(0, 
				resultInfo.length() < 495 
				? resultInfo.length() : 495));
		gcmTrack.setSuccess(success);	
		gcmTracks.save(gcmTrack);
		
		
		return gcmResponse;
		//--------------------------------------------------------------------------------//	
	}

}
