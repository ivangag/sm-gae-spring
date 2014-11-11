/* 
 **
 ** Copyright 2014, Jules White
 **
 ** 
 */
package org.symptomcheck.capstone;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.servlet.configuration.WebMvcSecurityConfiguration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.symptomcheck.capstone.auth.OAuth2SecurityConfiguration;

@Configuration
@ComponentScan
@EnableWebMvc
@Import(OAuth2SecurityConfiguration.class)
//@EnableGlobalMethodSecurity(securedEnabled=true,an)
public class Application extends WebMvcSecurityConfiguration {

	public static String keyUniqe = "keyUnique";
	// We do not have the typical main method because we need
	// the Maven AppEngine plugin to launch / configure the
	// development server. However, we are still using this
	// class to define configuration information.
	
}
