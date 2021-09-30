package ca.sheridancollege.li710.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import ca.sheridancollege.li710.beans.Course;
import ca.sheridancollege.li710.beans.Evaluation;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
/*
 * set up the mock user admin as ADMIN, 
 * otherwise the test will fail because no permission
 */
@WithMockUser(username="admin@foo.com",roles={"ADMIN"})
public class TestMainController {
	
	@Autowired
	private MockMvc mockMvc;

	
	@Test
	public void testLoadIndexPage_Pass() throws Exception {
		mockMvc.perform(get("/index.htm"))
//		.andExpect(model().attributeExists(null))
		.andExpect(status().isOk())
		.andExpect(view().name("/index.html"))
		.andDo(print())
		;
		
		mockMvc.perform(get("/index.html"))
		.andExpect(status().isOk())
		.andExpect(view().name("/index.html"))
		;
		
		mockMvc.perform( get("/index.html")
				.sessionAttr("incompleteCourses", new ArrayList<Course>())
				.sessionAttr("numberOfEvaluationsMap", new HashMap<String, Integer>())
				)
//		.andExpect(model().attributeExists("incompleteCourses"))
//		.andExpect(model().size(2))
		.andExpect(status().isOk())
		.andExpect(view().name("/index.html"))
		;
	}
	
	@Test
	public void testLoadIndexPage_Fail() throws Exception {
		mockMvc.perform(get("/index"))
		.andExpect(status().isNotFound())
		;
	}
	
	@Test
	public void testLoadEvaluationPage_Pass() throws Exception {

		mockMvc.perform( get("/evaluations")
				.sessionAttr("courses", new ArrayList<Course>())
				.sessionAttr("edit", false)
				)
		.andExpect(model().attributeExists("evaluation"))
		.andExpect(model().attributeExists("courses"))
		.andExpect(status().isOk())
		.andExpect(view().name("/evaluations/evaluation.html"))
		;
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testLoadEvaluationPage_Fail() throws Exception {

		mockMvc.perform( get("/evaluations")
				.sessionAttr("courses", null)
				.sessionAttr("edit", false)
				)
		.andExpect(model().attributeExists("evaluation"))
		.andExpect(model().attributeExists("courses"))
		.andExpect(status().is5xxServerError())
		.andExpect(view().name("/evaluations/evaluation.html"))
		;
	}
	
	@Test
	public void testDisplayEvaluationForm_Pass() throws Exception {
		Evaluation testEvaluation = new Evaluation("Test", "PROG10082", 10, 10, 10, 
				LocalDate.of(2021, 7, 31));
		mockMvc.perform( post("/evaluations/addEvaluation")
				.sessionAttr("courses", new ArrayList<Course>())
				.sessionAttr("edit", false)
				.flashAttr("evaluation", testEvaluation)
				)
		.andExpect(status().is3xxRedirection())
//		.andExpect(view().name("/evaluations/evalResults.html"))
		;
		
		//set edit to true.
		testEvaluation.setTitle("Test2");
		testEvaluation.setId(19);
		mockMvc.perform( post("/evaluations/addEvaluation")
				.sessionAttr("courses", new ArrayList<Course>())
				.sessionAttr("edit", true)
				.flashAttr("evaluation", testEvaluation)
				)
		.andExpect(status().is3xxRedirection())
//		.andExpect(view().name("/evaluations/evalResults.html"))
		;
	}
	
	@Test
	public void testDisplayEvaluationForm_Fail() throws Exception {
		
		/*
		 * add a null value, database operation will fail
		 * form binding will fail and will stay at
		 * evaluations/evaluation.html displaying error message on the page
		 */
		Evaluation testEvaluation = new Evaluation(null, "PROG10082", 10, 10, 10, 
				LocalDate.of(2021, 7, 31));
		mockMvc.perform( post("/evaluations/addEvaluation")
				.sessionAttr("courses", new ArrayList<Course>())
				.sessionAttr("edit", false)
				.flashAttr("evaluation", testEvaluation)
				)
		.andExpect(status().is3xxRedirection())
//		.andExpect(view().name("/errors/error-500.html"))
		;
		
	}
		

	
	@Test
	public void testEditEvaluation_Pass() throws Exception {
		mockMvc.perform( get("/evaluations/edit/{id}", "9")
				.sessionAttr("courses", new ArrayList<Course>())
				.sessionAttr("edit", true)
				)
		.andExpect(model().attributeExists("evaluation"))
		.andExpect(model().attributeExists("courses"))
		.andExpect(status().isOk())
		.andExpect(view().name("/evaluations/evaluation.html"))
		;
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testEditEvaluation_Fail() throws Exception {
		
		//Set edit to null
		mockMvc.perform( get("/evaluations/edit/{id}", "9")
				.sessionAttr("courses", new ArrayList<Course>())
				.sessionAttr("edit", null)
				)
		.andExpect(model().attributeExists("evaluation"))
		.andExpect(model().attributeExists("courses"))
		.andExpect(status().isOk())
		.andExpect(view().name("/evaluations/evaluation.html"))
		;
	}
	
	@Test
	public void testDisplayCoursePage_Pass() throws Exception {
		mockMvc.perform( get("/courses")
				.sessionAttr("courses", new ArrayList<Course>())
				.sessionAttr("editCourse", false)
				)
		.andExpect(model().attributeExists("course"))
		.andExpect(status().isOk())
		.andExpect(view().name("/courses/add-course.html"))
		;
	}
	
	@Test
	public void testDisplayCoursePage_Fail() throws Exception {
		mockMvc.perform( get("/course3/")
				.sessionAttr("courses", new ArrayList<Course>())
				.sessionAttr("editCourse", false)
				)
//		.andExpect(model().attributeHasErrors("course"))
		.andExpect(status().isNotFound())
//		.andExpect(view().name("/courses/add-course.html"))
		;

	}
	
	@Test
	public void testDisplayCourseForm_Pass() throws Exception {
		
		Course testCourse = new Course("TEST99999", "C Programming", 
				3.0, false, 2, 99.9); 
		mockMvc.perform( post("/courses/add-edit-course")
				.sessionAttr("courses", new ArrayList<Course>())
				.sessionAttr("duplicated", false)
				.sessionAttr("editCourse", false)
				.flashAttr("course", testCourse)
				)
		.andExpect(status().is3xxRedirection())
//		.andExpect(view().name("/courses/add-course.html"))
		;
		
	}
	
	@Test
	public void testDisplayCourseForm_Fail() throws Exception {
		
		//insert a course with existing code
		Course testCourse = new Course("PROG10082", "C Programming", 
				3.0, false, 2, 99.9); 
		mockMvc.perform( post("/courses/add-edit-course")
				.sessionAttr("courses", new ArrayList<Course>())
				.sessionAttr("duplicated", false)
				.sessionAttr("editCourse", false)
				.flashAttr("course", testCourse)
				)
		/*
		 * session attribute duplicated becomes true
		 */
//		.andExpect(request().sessionAttribute("duplicated", true))
		.andExpect(status().isFound())
//		.andExpect(view().name("/courses/add-course.html"))
		;
		

	}
	
	@Test
	public void testEditCourse_Pass() throws Exception {
		mockMvc.perform( get("/courses/edit-course/{code}", "PROG10082")
				.sessionAttr("editCourse", true)
				.sessionAttr("duplicated", false)
				.sessionAttr("courses",  new ArrayList<Course>())
				)
		.andExpect(model().attributeExists("course"))
		.andExpect(status().isOk())
		.andExpect(view().name("/courses/add-course.html"))
		;
	}
	
	//(expected=NestedServletException.class)
	@Test
	public void testEditCourse_Fail() throws Exception {
		
		/*
		 * there is no "WIND10081" in the database
		 * so model attribute course will return a null value
		 * but exception got if first.
		 */
		mockMvc.perform( get("/courses/edit-course/{code}", "PROG10082")
				.sessionAttr("editCourse", true)
				.sessionAttr("duplicated", false)
//				.sessionAttr("courses",  new ArrayList<Course>())
				)
//		.andExpect(model().attribute("course", is(nullValue())))
//		.andExpect(request().sessionAttribute("duplicated", true))
		.andExpect(status().isOk())
//		.andExpect(view().name("/courses/add-course.html"))
		;
	}
	
	@Test
	public void testDisplayGPAPage_Pass() throws Exception {
		
		
		mockMvc.perform( get("/gpa")
				.sessionAttr("completedCourses", new ArrayList<Course>())
				)
		.andExpect(status().isOk())
		.andExpect(view().name("/gpa/gpa-calculator.html"))
		;
	}
	
	@Test
	public void testDisplayGPAPage_Fail() throws Exception {
		
		mockMvc.perform( get("/gpaa")
				.sessionAttr("completedCourses", new ArrayList<Course>())
				)
		.andExpect(status().isNotFound())
//		.andExpect(view().name("/gpa/gpa-calculator.html"))
		;
	}
	
	@Test
	public void testShowCourseDetail_Pass() throws Exception {
		mockMvc.perform( get("/courses/show/{code}", "PROG10082")
				.sessionAttr("sEvaluations", new ArrayList<Evaluation>())
				)
		.andExpect(status().isOk())
		.andExpect(view().name("/evaluations/list.html"))
		;
	}
	
	@Test
	public void testShowCourseDetail_Fail() throws Exception {
		
		//random course code will return 0 result, show the error message.
		mockMvc.perform( get("/courses/show/{code}", "foobar")
				.sessionAttr("sEvaluations", new ArrayList<Evaluation>())
				)
		.andExpect(status().isOk())
		.andExpect(view().name("/evaluations/list.html"))
		;
	}

}
