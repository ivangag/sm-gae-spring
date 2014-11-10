package org.symptomcheck.capstone.gcm;

import org.symptomcheck.capstone.client.SymptomGcmMessagingApi;
import retrofit.ErrorHandler;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.RestAdapter.LogLevel;

public class GcmClientRequest {
	
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
}
