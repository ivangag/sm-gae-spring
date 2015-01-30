package org.symptomcheck.capstone.auth;

import java.io.Serializable;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

//TODO#BPR_1
//TODO#FDAR_1 
@PersistenceCapable
public class User implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5247905988101523432L;

	@PrimaryKey
	@Persistent
	private String username;
	
	@Persistent
	private String password;
	
	@Persistent
	private String role;
	
	@Persistent
	private int isAdmin;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public int getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(int isAdmin) {
		this.isAdmin = isAdmin;
	}
	
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		return sb.append("username:").append(username)
				.append(". ")
				.append("password:").append(password)
				.append(". ")
				.append("role:").append(role)
				.append(". ")
				.append("isAdmin:").append(isAdmin)		
				.toString();
		
	}
	public User(){}
}
