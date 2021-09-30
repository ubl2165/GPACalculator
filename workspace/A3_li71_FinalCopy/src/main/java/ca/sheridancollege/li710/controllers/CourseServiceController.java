/*
 * Name: Ji Li
 * Assignment: Assignment 3
 * Program: Computer Programming
 * Date: 08/20/2021

 * Description: This is a controller class for course's CRUD service
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

package ca.sheridancollege.li710.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ca.sheridancollege.li710.beans.Course;
import ca.sheridancollege.li710.database.DatabaseAccess;

@RestController
@RequestMapping("/api/course")
public class CourseServiceController {
	
	@Autowired
	private DatabaseAccess da;
	
	/**
	 * A Get Service method to get all courses.
	 * 
	 * Matching URL '/api/course' 
	 * @return A list of course collection.
	 */
	@GetMapping
	public List<Course> getCourseCollection() {
		
		return da.findAllCourses();
	}
	
	/**
	 * A Get Service method to get all completed courses.
	 * 
	 * Matching URL '/api/course/completed' 
	 * @return A list of completed course collection.
	 */
	@GetMapping("/completed")
	public List<Course> getCompletedCourses() {
		
		return da.findAllCompletedCourses(true);
	}
	
	/**
	 * A Get Service method to get all incomplete courses.
	 * 
	 * Matching URL '/api/course/incomplete' 
	 * @return A list of incomplete course collection.
	 */
	@GetMapping(value = "/incomplete")
	public List<Course> getIncompleteCourses() {
		
		return da.findAllCompletedCourses(false);
	}
	
	
	/**
	 * A Get Service method to get a course by code.
	 * 
	 * Matching URL '/api/course/get/{code}'
	 * 
	 * @param code, which is a string primary key in courses database.
	 * @return A course object matching the code
	 */
	@GetMapping(value = "/get/{code}")
	public Course getCourseByCode(@PathVariable String code) {
		
		return da.findCourseByCode(code);
	}
	
	
	/**
	 * 
	 * Matching URL '/api/course/search/{keyword}'
	 * A Get service to retrieve search result by keyword in couser's title 
	 * and code.
	 *  
	 * @param keyword for searching in title and code string
	 * @return list of matching result
	 */
	@GetMapping(value = "/search/{keyword}")
	public List<Course> searchCourseByKeyword(@PathVariable String keyword){
		
		return da.searchCourseByKeyword(keyword);
	}
	
	/**
	 * Post service method to insert a new course
	 * 
	 * Post Matching URL '/api/course/'
	 * @param c a Course object
	 * @return newly inserted primary key
	 */
	@PostMapping(consumes="application/json")
	public String postCourse(@RequestBody Course c) {
		
		if(da.insertCourse(c) == 1) {
			return c.getCode();
		}else {
			return "N/A";
		}

	    
	}
	
	/**
	 * Put service method to update whole table with a new list
	 * @param courses
	 * @return affected rows
	 */
	@PutMapping(consumes="application/json")
	public long putCourses(@RequestBody List<Course> courses) {
		da.deleteAllCourses();
		da.saveAllCourses(courses);
		return da.countAllCourses();		
	}
	
	/**
	 * Put service method to update one record in the table
	 * @param Course course 
	 * @return affected row
	 */	
	@PutMapping(value="/update/", consumes="application/json")
	public long putOneCourse(@RequestBody Course course) {
		
		
		return da.updateCourse(course);
		
	}
	
	/**
	 * Delete service method to delete all records
	 */
	@DeleteMapping
	public void deleteAll() {
		da.deleteAllCourses();
	}
	
	/**
	 * Delete service method to delete one record by id
	 * @param id
	 */
	@DeleteMapping("/{code}")
	public void deleteByID(@PathVariable String code) {
		
		da.deleteCourseByCode(code);
	}

	
	
	
	
	
}
