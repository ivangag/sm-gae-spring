package org.symptomcheck.capstone.gcm;

import java.util.ArrayList;
import java.util.List;

public class GcmMessage {
     
	private List<String> registration_ids = new ArrayList<String>();
          
	private GcmData data;

	public List<String> getRegistration_ids() {
		return registration_ids;
	}

	public void setRegistration_ids(List<String> registration_ids) {
		this.registration_ids = registration_ids;
	}

	public GcmData getData() {
		return data;
	}

	public void setData(GcmData data) {
		this.data = data;
	}

	public void addRegistrationId(String regId) {
		if((registration_ids != null)
				&& (!this.registration_ids.contains(regId))){
			this.registration_ids.add(regId);
		}		
	}
}
