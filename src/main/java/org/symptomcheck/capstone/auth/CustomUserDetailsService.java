package org.symptomcheck.capstone.auth;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Service
@Transactional(readOnly = true)
@JsonIgnoreProperties(value="userRepository")
public class CustomUserDetailsService implements UserDetailsService,Serializable{//TODO#FDAR_1 

	private static final long serialVersionUID = 7884479538365706760L;
	@Autowired
	UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(final String username)
			throws UsernameNotFoundException {
		
		final User user = userRepository.findOne(username);
		return new UserDetails() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = -273801257212281477L;

			@Override
			public boolean isEnabled() {
				return true;
			}
			
			@Override
			public boolean isCredentialsNonExpired() {
				return true;
			}
			
			@Override
			public boolean isAccountNonLocked() {
				return true;
			}
			
			@Override
			public boolean isAccountNonExpired() {
				return true;
			}
			
			@Override
			public String getUsername() {
				return user.getUsername().toString();
			}
			
			@Override
			public String getPassword() {
				return user.getPassword().toLowerCase();
			}
			
			@Override
			public Collection<? extends GrantedAuthority> getAuthorities() {
				return CustomUserDetailsService.this.getAuthorities(user.getIsAdmin(),user.getRole());
			}
		};
	}
	
	/**
	 * Retrieves a collection of {@link GrantedAuthority} based on a numerical role
	 * @param role the numerical role
	 * @return a collection of {@link GrantedAuthority
	 */
	public Collection<? extends GrantedAuthority> getAuthorities(Integer isAdmin, String role) {
		List<GrantedAuthority> authList = getGrantedAuthorities(getRoles(isAdmin,role));
		return authList;
	}
	
	/**
	 * Converts a numerical role to an equivalent list of roles
	 * @param isAdmin the numerical role
	 * @param role
	 * @return list of roles as as a list of {@link String}
	 */
	public List<String> getRoles(Integer isAdmin, String role) {
		List<String> roles = new ArrayList<String>();
		
		if (isAdmin.intValue() == 1) {
			roles.add("ROLE_" + role.toUpperCase()); //TODO#FDAR_1 
			roles.add("ROLE_ADMIN");
			
		} else /*if (role.intValue() == 2)*/ {
			roles.add("ROLE_" + role.toUpperCase());
		}
		
		return roles;
	}
	
	/**
	 * Wraps {@link String} roles to {@link SimpleGrantedAuthority} objects
	 * @param roles {@link String} of roles
	 * @return list of granted authorities
	 */
	public static List<GrantedAuthority> getGrantedAuthorities(List<String> roles) {
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		for (String role : roles) {
			authorities.add(new SimpleGrantedAuthority(role));
		}
		return authorities;
	}
	
	public CustomUserDetailsService(){}
}
