/*
 * Name: Ji Li
 * Assignment: Assignment 3
 * Program: Computer Programming
 * Date: 08/20/2021

 * Description: This is a Database configuration class.
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

package ca.sheridancollege.li710.database;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@Configuration
public class DataConfig {
	
	@Bean
	public NamedParameterJdbcTemplate namedParameterJdbcTemplate(DataSource dataSource) {
		
		return new NamedParameterJdbcTemplate(dataSource);
	}
	
}
