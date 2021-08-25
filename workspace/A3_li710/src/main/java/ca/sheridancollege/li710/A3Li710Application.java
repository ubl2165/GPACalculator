/*
 * Name: Ji Li
 * Assignment: Assignment 3
 * Program: Computer Programming
 * Date: 08/20/2021

 * Description: This is the entry point
 * 
 * Description: This is a java Bean Class called Course.
 * This class uses Lombok to generate methods.
 * This class uses Validation artifact to validate data field.
 * It has one comparator method and one overriding compareTo method to 
 * sorting the objects. 
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

package ca.sheridancollege.li710;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class A3Li710Application {

	public static void main(String[] args) {
		SpringApplication.run(A3Li710Application.class, args);
	}

}
