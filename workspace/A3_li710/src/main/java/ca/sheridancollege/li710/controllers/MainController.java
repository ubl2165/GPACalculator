/*
 * Name: Ji Li
 * Assignment: Assignment 3
 * Program: Computer Programming
 * Date: 08/20/2021

 * Description: This is the Main Controller class to act as a manager between
 * Web interface and Database Access Objects
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import ca.sheridancollege.li710.beans.Course;
import ca.sheridancollege.li710.beans.Evaluation;
import ca.sheridancollege.li710.database.DatabaseAccess;

@Controller
public class MainController {

	@Autowired
	private DatabaseAccess da;
	
	
	/*
	 * Permission Denied page
	 */
	@GetMapping("/permissionDenied")
	public String permDenied() {
		return "/errors/permissionDenied.html";
	}
	
	/*
	 * Login page
	 */
	@GetMapping("/login")
	public String login() {
		return "/login.html";
	}
	
	/*
	 * Return index.html when enter index.htm or index.html or "/"	 * 
	 * or 
	 * @GetMapping("/{i:(?:index\\.html?)?}") or @GetMapping(value = {"/",
	 * "index.htm", "index.html"})
	 */
	@GetMapping(value = { "/{index:index\\.html?}", "/"})
	public String loadIndexpage(HttpSession session, Model model) {

		// Get all incomplete courses as a list
		List<Course> incompleteCourses = da.findAllCompletedCourses(false);

		// load all incomplete courses, store in session		
		session.setAttribute("incompleteCourses", incompleteCourses);
		
		// a map for the number of evaluations of course, course code as key.
		Map<String, Integer> evaluationsMap = new HashMap<>();

		// set each course code with number of evaluations
		for (Course c : incompleteCourses) {
			List<Evaluation> evaluations = da.findEvaluationsByCourses(c.getCode());
			evaluationsMap.put(c.getCode(), evaluations.size());
		}

		// store it to session
		session.setAttribute("numberOfEvaluationsMap", evaluationsMap);
		
		// load the index page
		return "/index.html";
	}

	/*
	 * Return Evaluation page with session and model
	 */
	@GetMapping("/evaluations")
	public String loadEvaluationPage(Model model, HttpSession session) {

		// populate the evaluation form with default number through model
		model.addAttribute("evaluation", new Evaluation());

		// Add Courses in the Model for backup if session failed.
		model.addAttribute("courses", da.findAllCourses());

		// Add Categories in the session, only if it is null.
		if (session.getAttribute("courses") == null)
			session.setAttribute("courses", da.findAllCourses());

		/*
		 * set session attribute 'edit' to default value false each time when access
		 * through menu link.
		 */
		session.setAttribute("edit", false);

		return "/evaluations/evaluation.html";
	}

	/*
	 * URL requests for add and edit evaluation
	 */
	@PostMapping("/evaluations/addEvaluation")
	public String displayEvaluationForm(HttpSession session, 
			@Valid @ModelAttribute(value = "evaluation") Evaluation evaluation,
			BindingResult result
	/*
	 * Adding an SessionAttribute annotation to retrieve session.edit is the
	 * alternative way to session.getattribute("edit")
	 */
//			@SessionAttribute(value = "edit") boolean edit
	) {


		if (result.hasErrors()) {

			// If errors exist go back to the form with same data.
			return "/evaluations/evaluation.html";

		} else {

			/*
			 * check if editing or adding, if session.edit is null then it is false if
			 * session.edit is not null then retrieve the latest value
			 */
			boolean edit = (session.getAttribute("edit") == null) ? 
					false : (boolean) session.getAttribute("edit");

			if (edit) {

				da.updateEvaluation(evaluation);

			} else {

				// add new item to database
				da.insertEvaluation(evaluation);

			}

			// update the session since its values changed.
			session.setAttribute("evaluations", da.findAllEvaluations());
			
			//Get a map for all the course objects with code as the key
			session.setAttribute("courseObjectMap", da.getCourseObjectMap());

			// Add Courses in the session, only if it is null.
			if (session.getAttribute("courses") == null)
				session.setAttribute("courses", da.findAllCourses());

			return "/evaluations/evalResults.html";
		}

	}

	/*
	 * A method to choose which evaluation to edit
	 * 
	 */
	@GetMapping("/evaluations/edit/{id}")
	public String editEvaluation(HttpSession session, Model model, 
			@PathVariable int id) {

		Evaluation evaluation = da.findEvaluationById(id);

		// parsing the editing evaluation object and course list to the form
		model.addAttribute("evaluation", evaluation);
		model.addAttribute("courses", da.findAllCourses());

		/*
		 * set boolean edit to true, so it can tell the system this is editing 
		 * not adding
		 */
		session.setAttribute("edit", true);

		return "/evaluations/evaluation.html";
	}

	/*
	 * Mapping for URL request "course"
	 */
	@GetMapping("/courses")
	public String displayCoursePage(Model model, HttpSession session) {
		
		// populate the course form with default value
		model.addAttribute("course", new Course());

		/*
		 * update Courses in the session, because h2 database is volatile, session may
		 * store data that not in the h2 database, e.g. from last operation.
		 */
		
		// First update the all final grades, because h2-database is volatile.
		da.updateFinalGradeForCompletedCourses();
		session.setAttribute("courses", da.findAllCourses());
	
		/*
		 * set session attribute 'edit' to default value false each time when access
		 * through menu link.
		 */
		session.setAttribute("editCourse", false);

		return "/courses/add-course.html";

	}

	/*
	 * A method to either add or edit the course Added a primitive way to check if
	 * they are duplicated key when adding course
	 */
	@PostMapping("/courses/add-edit-course")
	public String displayCourseForm(HttpSession session, Model model, 
			@ModelAttribute Course course) {

		/*
		 * check if editing or adding, if session.edit is null then it is false if
		 * session.edit is not null then retrieve the latest value
		 */
		boolean edit = (session.getAttribute("editCourse") == null) ? false
				: (boolean) session.getAttribute("editCourse");

		if (edit) {

			session.setAttribute("duplicated", false);
			da.updateCourse(course);

		} else {

			// check if the course already exist by check duplicated code.
			if (da.findCourseByCode(course.getCode()) == null) {
				
				//set duplicated value to false
				session.setAttribute("duplicated", false);
				// add to database
				da.insertCourse(course);

			} else {
				
				//if course code exists, duplicated set to true.
				session.setAttribute("duplicated", true);
				session.setAttribute("duplicatedCode", "Duplicated Course Code!");
			}

		}

		// update the session since its values changed.
		session.setAttribute("courses", da.findAllCourses());

		return "redirect:/courses";

	}
	
	/*
	 * Choose which course to edit
	 */
	@GetMapping("/courses/edit-course/{code}")
	public String editCourse(HttpSession session, Model model, 
			@PathVariable String code) {

		session.setAttribute("duplicated", false);
		Course course = da.findCourseByCode(code);

		// parsing the editing course object to the form
		model.addAttribute("course", course);

		/*
		 * set boolean edit to true, so it can tell the system this is editing not
		 * adding
		 */
		session.setAttribute("editCourse", true);
		
		session.setAttribute("courses", da.findAllCourses());

		return "/courses/add-course.html";
	}

	/*
	 * Request handler for "gpa"
	 */
	@GetMapping("/gpa")
	public String displayGPAPage(Model model, HttpSession session) {

		// First update the all final grades, because h2-database is volatile.
		da.updateFinalGradeForCompletedCourses();

		// load all completed courses, parse argument true
		session.setAttribute("completedCourses", da.findAllCompletedCourses(true));

		return "/gpa/gpa-calculator.html";

	}

	/*
	 * Show detail for a completed course /courses/show/}${cc.code}
	 */
	@GetMapping("/courses/show/{code}")
	public String showCourseDetail(HttpSession session, 
			Model model, @PathVariable String code) {

		List<Evaluation> evaluations = da.findEvaluationsByCourses(code);

		session.setAttribute("selectedCourse", da.findCourseByCode(code));

		// selected evaluations "sEvaluations"
		session.setAttribute("sEvaluations", evaluations);
		
		//Get a map for all the course objects with code as the key
		if(session.getAttribute("courseObjectMap") == null) {
			session.setAttribute("courseObjectMap", da.getCourseObjectMap());
		}		

		return "/evaluations/list.html";
	}


}
