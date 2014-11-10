package org.symptomcheck.capstone.gcm;

public class GcmConstants {

	public final static String GCM_PROJECT_AUTHORIZATION_KEY = "key=AIzaSyCACHOdod_qk6e1uiBtJT_MFufGroa45AA";
	
	public final static String GCM_MESSAGING_URL = "https://android.googleapis.com/gcm";
	
	public final static String GCM_EXTRAS_KEY_ACTION = "action";
	public final static String GCM_EXTRAS_KEY_USERNAME = "userName";
	public final static String GCM_EXTRAS_KEY_USERTYPE = "userType";
	// A Patient uploaded a CheckIn
	public final static String GCM_ACTION_CHECKIN_RX = "org.symptomcheck.CHECKIN_RX";	
	// A Doctor updated medicines' list
	public final static String GCM_ACTION_MEDICATION_RX = "org.symptomcheck.MEDICATION_RX";
}
