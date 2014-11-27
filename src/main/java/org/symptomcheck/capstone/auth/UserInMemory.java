/* 
 **
 ** Copyright 2014, Jules White
 **
 ** 
 */
package org.symptomcheck.capstone.auth;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

public class UserInMemory implements UserDetails {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7679609747845140306L;

	public static UserDetails create(String username, String password,
			String...authorities) {
		return new UserInMemory(username, password, authorities);
	}
	
	private final Collection<GrantedAuthority> authorities_;
	private final String password_;
	private final String username_;

	@SuppressWarnings("unchecked")
	private UserInMemory(String username, String password) {
		this(username, password, Collections.EMPTY_LIST);
	}

	private UserInMemory(String username, String password,
			String...authorities) {
		username_ = username;
		password_ = password;
		authorities_ = AuthorityUtils.createAuthorityList(authorities);
	}

	private UserInMemory(String username, String password,
			Collection<GrantedAuthority> authorities) {
		super();
		username_ = username;
		password_ = password;
		authorities_ = authorities;
	}

	public Collection<GrantedAuthority> getAuthorities() {
		return authorities_;
	}

	public String getPassword() {
		return password_;
	}

	public String getUsername() {
		return username_;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
