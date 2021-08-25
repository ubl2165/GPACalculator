/*
 * Name: Ji Li
 * Assignment: Assignment 3
 * Program: Computer Programming
 * Date: 08/20/2021

 * Description: This is a class for configuring security details.
 * 
 * Other Files in this Project:
 * 	LogAccessDeniedHandler.java
 * 	SecurityConfig.java
 * 	CourseServiceController.java
 *  MyErrorController.java 
 *  MainController.java
 *  ErrorMessages.java
 *  EvaluationRepository.java
 *  Evaluations.java  
 *  Course.java
 *  DataConfig.java
 *  DatabaseAccess.java
 *  Main class: A3Li710Application.java 
 *  SecurityWebApplicationInitializer.java
 */
package ca.sheridancollege.li710.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private LogAccessDeniedHandler accessDeniedHandler;

	//In-Memory user, no need to access database
//	@Autowired
//	private UserDetailsServiceImpl userDetailsService;
	
	/*
	 * To enable the custom login page, override configure method
	 */
	@Override
	public void configure(HttpSecurity http) throws Exception {
		
		
		//TEMPORARY!! - REMOVE AFTER LAUNCH.
//		http.csrf().disable();
//		http.headers().frameOptions().disable();
		
		/*
		 * Note that the matchers are considered in order. 
		 * Therefore, the following will be invalid if the first matcher matches
		 *  every request and will never get to the second mapping.
		 *  
		 *  The antMatchers will follow most specific match. the following user 
		 *  can not access /exercises, only Guest can.
		 *  But I add foo@foo.com with guest and user roles to solve the problem
		 *  .withUser("foo@foo.com").password("4444").roles("USER", "GUEST")
		 *  
		 *  Other solution is add Guest and User at same time to "/exercises/**"
		 *  as following:
		 *  .antMatchers("/exercises/**").hasAnyRole("GUEST", "USER")
		 *  
		 */
		http.
			authorizeRequests()
			.antMatchers("/evaluations/**", "/gpa/**", "/courses/show/**").hasAnyRole("USER", "ADMIN")
			.antMatchers("/courses/**").hasRole("ADMIN")
			//for post request only
//			.antMatchers(HttpMethod.POST, "processForm").permitAll()
			//by default, the antMathcer pattern will include get and post request.
			.antMatchers("/",  "/js/**", "/css/**", "/images/**", "/layouts/**", "/errors/**").permitAll()
//			.antMatchers("/h2-console/**").permitAll() //TEMPORARY!
			.anyRequest().authenticated()
		.and()
			.formLogin()
			.loginPage("/login")
			/*
			 * System will direct to last requesting url by default, sometime 
			 * leading to unexpected return. 
			 * Set a default URL for successful log in to minimize the surprise. 
			 */
			.defaultSuccessUrl("/", true) 
			.permitAll()
		.and()
			.logout().invalidateHttpSession(true)
			.clearAuthentication(true)
			.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
//			.logoutSuccessUrl("/login?logout").permitAll()
			//if I want to logout to different page
			.logoutSuccessUrl("/")
		.and()
			.exceptionHandling().accessDeniedHandler(accessDeniedHandler);
	}
	
	/*@Bean This will automatically ready and injected in 
	 * sping's inversion of control container.
	 * */
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		
		return new BCryptPasswordEncoder();
	}
	
	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication().passwordEncoder(passwordEncoder())
			.withUser("foo@foo.com").password("$2a$10$tdATpp.MeMAVZ1SMDdG60e6IAriAR5nwvF1w6rvafA.33N/12IV52").roles("USER")
			.and()
			.withUser("admin@foo.com").password("$2a$10$dmXoHb08q7CCC8oluHL20eq4NbhQCCQvAxYp3bEB13idohk2kgVHa").roles("ADMIN")
			;
//		.withUser("foo@foo.com").password("4444").roles("USER", "GUEST");
//		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}
}