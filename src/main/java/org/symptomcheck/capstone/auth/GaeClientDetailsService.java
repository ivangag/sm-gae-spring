package org.symptomcheck.capstone.auth;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.config.annotation.builders.ClientDetailsServiceBuilder.ClientBuilder;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;

@Service
@Transactional(readOnly = true)
public class GaeClientDetailsService implements ClientDetailsService, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -876872227005211262L;

	/**
	 * Token validity in seconds
	 */
	protected static final Integer DEFAULT_ACCESS_TOKEN_VALIDITY = 60*60*12;//12 hours

	protected static final Integer DEFAULT_REFRESH_TOKEN_VALIDITY = 60*60*24*30; //30 days

	@Autowired
	ClientDetailsRepository clientRepository;
	
	
	public GaeClientDetailsService(){
		
	}
	
	public GaeClientDetailsService(Set<String> scopes,
			Set<String> authorizedGrantTypes, Set<String> resourceIds,Collection<String> authorities ) {
		// 
		this.scopes = scopes;
		this.authorizedGrantTypes = authorizedGrantTypes;
		this.resourceIds = resourceIds;
		this.authorities = authorities;
	}

	@Override
	public ClientDetails loadClientByClientId(String clientId)
			throws ClientRegistrationException {
		
		final Client client = clientRepository.findOne(clientId);
		return new ClientDetails() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 9147682504670142780L;

			@Override
			public boolean isSecretRequired() {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean isScoped() {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean isAutoApprove(String scope) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public Set<String> getScope() {
				// TODO Auto-generated method stub
				return GaeClientDetailsService.this.scopes;
			}
			
			@Override
			public Set<String> getResourceIds() {
				// TODO Auto-generated method stub
				return GaeClientDetailsService.this.resourceIds;
			}
			
			@Override
			public Set<String> getRegisteredRedirectUri() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public Integer getRefreshTokenValiditySeconds() { 
				return (client.getAccessTokenValiditySeconds() == 0 ? DEFAULT_REFRESH_TOKEN_VALIDITY : client.getAccessTokenValiditySeconds());				
			}
			
			@Override
			public String getClientSecret() {
				// TODO Auto-generated method stub
				return client.getClientSecret();
			}
			
			@Override
			public String getClientId() {
				// TODO Auto-generated method stub
				return client.getClientId();
			}
			
			@Override
			public Set<String> getAuthorizedGrantTypes() {
				// TODO Auto-generated method stub
				return GaeClientDetailsService.this.authorizedGrantTypes;
			}
			
			@Override
			public Collection<GrantedAuthority> getAuthorities() {
				return getGrantedAuthorities((LinkedHashSet<String>) GaeClientDetailsService.this.authorities);
			}
			
			@Override
			public Map<String, Object> getAdditionalInformation() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public Integer getAccessTokenValiditySeconds() {							
				return (client.getAccessTokenValiditySeconds() == 0 ? DEFAULT_ACCESS_TOKEN_VALIDITY : client.getAccessTokenValiditySeconds());
			}
			
			@Override
			public String toString(){
				StringBuilder sb = new StringBuilder();
				StringBuilder authorizedGrantTypes = new StringBuilder();
				StringBuilder roles = new StringBuilder();
				for(String item:getAuthorizedGrantTypes()){
					authorizedGrantTypes.append(item).append(";");
				}		
				for(GrantedAuthority item:getAuthorities()){
					roles.append(item).append(";");
				}							
				return sb.append("isSecretRequired:").append(isSecretRequired())
						.append(". ")
						.append("isScoped:").append(isScoped())
						.append(". ")
						.append("RefreshTokenValidity:").append(getRefreshTokenValiditySeconds())
						.append(". ")
						.append("AccessTokenValidity:").append(getRefreshTokenValiditySeconds())
						.append(". ")
						.append("getClientId:").append(getClientId())
						.append(". ")
						.append("getClientSecret:").append(getClientSecret())
						.append(". ")
						.append("authorizedGrantTypes:").append(authorizedGrantTypes.toString())
						.append(". ")
						.append("roles:").append(roles.toString())							
						.toString();
			}
		};
	}

	/**
	 * Retrieves a collection of {@link GrantedAuthority} based on a numerical role
	 * @param role the numerical role
	 * @return a collection of {@link GrantedAuthority
	 */
	public Collection<GrantedAuthority> getAuthorities(Integer isAdmin, String role) {
		List<GrantedAuthority> authList = getGrantedAuthorities(getRoles(isAdmin,role));
		return authList;
	}
	/**
	 * Converts a numerical role to an equivalent list of roles
	 * @param isAdmin the numerical role
	 * @param role
	 * @return list of roles as as a list of {@link String}
	 */
	public LinkedHashSet<String> getRoles(Integer isAdmin, String role) {
		LinkedHashSet<String> roles = new LinkedHashSet<String>();
		
		if (isAdmin.intValue() == 1) {
			roles.add("ROLE_" + role.toUpperCase()); //TODO#FDAR_1 TODO#FDAR_9 
			roles.add("ROLE_ADMIN");
			
		} else /*if (role.intValue() == 2)*/ {
			roles.add("ROLE_" + role.toUpperCase());
		}
		
		return roles;
	}
	
	/**
	 * Wraps {@link String} roles to {@link SimpleGrantedAuthority} objects
	 * @param authorities2 {@link String} of roles
	 * @return list of granted authorities
	 */
	public static List<GrantedAuthority> getGrantedAuthorities(LinkedHashSet<String> authorities) {
		List<GrantedAuthority> authoritiesGranted = new ArrayList<GrantedAuthority>();
		for (String role : authorities) {
			authoritiesGranted.add(new SimpleGrantedAuthority(role));
		}
		return authoritiesGranted;
	}
	
	protected Set<String> resourceIds = new HashSet<String>();
	protected Set<String> scopes = new LinkedHashSet<String>();
	protected Set<String> authorizedGrantTypes = new LinkedHashSet<String>();
	protected Collection<String> authorities = new LinkedHashSet<String>();
	
	public static class Builder {
		private Set<String> resourceIds = new HashSet<String>();
		private Set<String> scopes = new LinkedHashSet<String>();
		private Set<String> authorizedGrantTypes = new LinkedHashSet<String>();
		private Collection<String> authorities = new LinkedHashSet<String>();
		
		public Builder resourceIds(String... resourceIds) {
			for (String resourceId : resourceIds) {
				this.resourceIds.add(resourceId);
			}
			return this;
		}
		public Builder scopes(String... scopes) {
			for (String scope : scopes) {
				this.scopes.add(scope);
			}
			return this;
		}
		public Builder authorizedGrantTypes(String... authorizedGrantTypes) {
			for (String grant : authorizedGrantTypes) {
				this.authorizedGrantTypes.add(grant);
			}
			return this;
		}
		
		public Builder authorities(String... authorities) {
			for (String authority : authorities) {
				this.authorities.add(authority);
			}
			return this;
		}
		
        public GaeClientDetailsService build() {
            return new GaeClientDetailsService(scopes,authorizedGrantTypes,resourceIds,authorities);
        }        

	}
}
