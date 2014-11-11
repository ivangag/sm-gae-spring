package org.symptomcheck.capstone.gcm;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.symptomcheck.capstone.client.SymptomGcmMessagingApi;
import org.symptomcheck.capstone.repository.Doctor;
import org.symptomcheck.capstone.repository.GcmTrack;
import org.symptomcheck.capstone.repository.GcmTrackRepository;
import org.symptomcheck.capstone.repository.UserType;

import retrofit.ErrorHandler;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.RestAdapter.LogLevel;


public class GcmClientRequest {
	

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
	
	ExecutorService handlePostRequest = Executors.newFixedThreadPool(4);
	
	private static GcmClientRequest instance = new GcmClientRequest();
	
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
	}

	private static SymptomGcmMessagingApi gcmClient = null;
	
	public SymptomGcmMessagingApi getApi(){
		
		return gcmClient;
	}
	
	public GcmTrack PostGcmMessage(String action, String username, UserType userTrigger,
			List<String> regIds, List<String> extraLogInfo){
		
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
			final GcmResponse gcmResponse = 
					GcmClientRequest.get().getApi().sendMessage(GcmConstants.GCM_PROJECT_AUTHORIZATION_KEY, message);
			resultInfo = gcmResponse.toString();
			
			// try to extract canonical
			if(Integer.valueOf(gcmResponse.canonical_ids) != null){
				int canonical_ids = Integer.valueOf(gcmResponse.canonical_ids);
				
				for(GcmReponseResult responseResult : gcmResponse.results){
					
				}
			}
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
				resultInfo.length() < 495 ? resultInfo.length() : 495));
		gcmTrack.setSuccess(success);	
		//gcmTracks.save(gcmTrack);
		
		//--------------------------------------------------------------------------------//		
		return gcmTrack;
	}

}
