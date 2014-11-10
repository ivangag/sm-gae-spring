/* 
 **
 ** Copyright 2014, Jules White
 **
 ** 
 */
package org.symptomcheck.capstone.auth;

import java.io.Serializable;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.client.ClientDetailsUserDetailsService;

/**
 * A class that combines a UserDetailsService and ClientDetailsService
 * into a single object.
 * 
 * @author jules
 *
 */
public class ClientAndUserDetailsService implements UserDetailsService,
		ClientDetailsService, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -546422165059882760L;

	private ClientDetailsService clients_;

	private UserDetailsService users_;
	
	private ClientDetailsUserDetailsService clientDetailsWrapper_;
	
	public ClientAndUserDetailsService(ClientDetailsService clients,
			UserDetailsService users) {
		super();
		clients_ = clients;
		users_ = users;
		clientDetailsWrapper_ = new ClientDetailsUserDetailsService(clients_);
	}

	@Override
	public ClientDetails loadClientByClientId(String clientId)
			throws ClientRegistrationException {
		System.out.println("!!!ClientDetails loadClientByClientId(String clientId)" + clientId + " PRINCIPAL: " + getPrincipal());
		return clients_.loadClientByClientId(clientId);
	}
	
	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		System.out.println("!!!ClientDetails loadUserByUsername(String username)" + username + " PRINCIPAL: " + getPrincipal());	
		UserDetails user = null;
		try{
			user = users_.loadUserByUsername(username);
			if(user == null){
				System.out.println("!!!loadUserByUsername USER NULL!!!");
			}else{
			System.out.println("!!!loadUserByUsername ClientDetails loadUserByUsername(String username)" + user.getPassword() + user.getUsername() + user.getAuthorities());
			}
		}catch(UsernameNotFoundException e){
			user = clientDetailsWrapper_.loadUserByUsername(username);
			System.out.println("!!!catch(UsernameNotFoundException e) ClientDetails loadUserByUsername(String username)" + username + e.getMessage());	
		}
		return user;
	}
	
	
    public static String getPrincipal() {
    	if(SecurityContextHolder.getContext().getAuthentication() != null){
        Object obj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
 
        if (obj instanceof UserDetails) {
            return ((UserDetails) obj).getUsername();
        } else {
            return "";
        }
    	} else{
    		return "";
    	}
    	}    

}
