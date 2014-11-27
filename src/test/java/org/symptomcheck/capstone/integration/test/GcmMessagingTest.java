package org.symptomcheck.capstone.integration.test;

import java.util.ArrayList;

import org.junit.Test;
import org.symptomcheck.capstone.client.SymptomGcmMessagingApi;
import org.symptomcheck.capstone.gcm.GcmConstants;
import org.symptomcheck.capstone.gcm.GcmData;
import org.symptomcheck.capstone.gcm.GcmMessage;
import org.symptomcheck.capstone.gcm.GcmResponse;
import org.symptomcheck.capstone.repository.UserType;

import retrofit.ErrorHandler;
import retrofit.RestAdapter;
import retrofit.RestAdapter.LogLevel;
import retrofit.RetrofitError;

public class GcmMessagingTest {

	public class ErrorRecorder implements ErrorHandler {

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
		
	private final static String reg_id_test = "APA91bG7NmFtRHpYEIVvzvUSkI1QHN-pK05CokH0FOn_9F4-yjUUkewRpuhbpj8JhEmP172LcswqhwyaNokhLTcLT0uXpFH4qUwJPh3BHMvazyB-aeP-zmyU-z4VdVRvaF2k8mzFybBQiRD1mA3Q2sXbj_77oxMlFOYrrvo8AaGOfvhMtnYP97g";
	
	private SymptomGcmMessagingApi gcmClient = new RestAdapter.Builder()
		.setEndpoint(GcmConstants.GCM_MESSAGING_URL)
		.setLogLevel(LogLevel.FULL)
		.setErrorHandler(new ErrorRecorder())
		.build()
		.create(SymptomGcmMessagingApi.class);
	
	@Test
	public void sendMessage(){		
		GcmMessage message = new GcmMessage();
		message.addRegistrationId(reg_id_test);
		GcmData dataMsg = new GcmData();
		dataMsg.setAction(GcmConstants.GCM_ACTION_CHECKIN_UPDATE);
		dataMsg.setUserName("patient001");
		dataMsg.setUserType(UserType.PATIENT);
		message.setData(dataMsg);

		GcmResponse response = null;
		try{
			response = gcmClient.sendMessage(GcmConstants.GCM_PROJECT_AUTHORIZATION_KEY, message);
		}catch(RetrofitError error){
			
		}
		if(response != null)
			System.out.print(response.toString());
	}
}
