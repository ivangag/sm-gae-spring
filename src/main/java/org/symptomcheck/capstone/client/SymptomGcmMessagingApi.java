package org.symptomcheck.capstone.client;

import org.symptomcheck.capstone.gcm.GcmMessage;
import org.symptomcheck.capstone.gcm.GcmResponse;

import retrofit.http.Body;
import retrofit.http.Header;
import retrofit.http.POST;

public interface SymptomGcmMessagingApi {

	public static final String GCM_SEND_SVC_PATH = "/send";
	
	@POST(GCM_SEND_SVC_PATH)
	public GcmResponse sendMessage(@Header("Authorization") String authorization, @Body GcmMessage gcmMessage);
}
