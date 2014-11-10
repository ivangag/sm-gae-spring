package org.symptomcheck.capstone.gcm;

import org.symptomcheck.capstone.repository.UserType;

public class GcmData {

    private String action;
    //[DataMember]
    //public GCMUser user;
    private String userName;

    private UserType userType = UserType.UNKWOWN;

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String username) {
		this.userName = username;
	}

	public UserType getUserType() {
		return userType;
	}

	public void setUserType(UserType userType) {
		this.userType = userType;
	}
	
	@Override
	public String toString(){
	      StringBuilder sb = new StringBuilder();
	        sb.append("action:").append(this.action).append(";")
	          .append("userName:").append(this.userName).append(";")
	          .append("userType:").append(this.userType.toString())
	          ;
	        return sb.toString();
	}
}
