package org.symptomcheck.capstone.repository;

public class UserInfo {
	
	private UserType userType = UserType.UNKWOWN;
	
	private boolean logged;
	
	private boolean anagPresent;
	
	private String userIdentification;
		
	private String firstName;
	
	private String lastName;

	public UserType getUserType() {
		return userType;
	}

	public void setUserType(UserType userType) {
		this.userType = userType;
	}
	
	public boolean getLogged() {
		return logged;
	}

	public void setLogged(boolean logged) {
		this.logged = logged;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}


	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	@Override
	public String toString(){
		StringBuilder sb = new  StringBuilder();
		return sb.append(this.firstName).append("-")
				.append(this.lastName).append("-")
				.append(this.getUserIdentification()).append("-")
				.append(this.logged)
				.toString();
	}

	public String getUserIdentification() {
		return userIdentification;
	}

	public void setUserIdentification(String userIdentification) {
		this.userIdentification = userIdentification;
	}

	public boolean getAnagPresent() {
		return anagPresent;
	}

	public void setAnagPresent(boolean anagPresent) {
		this.anagPresent = anagPresent;
	}

}
