/* 
 **
 ** Copyright 2014, Jules White
 **
 ** 
 */
package org.symptomcheck.capstone;

//import org.magnum.mobilecloud.video.auth.OAuth2SecurityConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.servlet.configuration.WebMvcSecurityConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.symptomcheck.capstone.auth.ClientAndUserDetailsService;
import org.symptomcheck.capstone.auth.OAuth2SecurityConfiguration;
import org.symptomcheck.capstone.client.VideoSvcApi;
import org.symptomcheck.capstone.repository.Video;

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
