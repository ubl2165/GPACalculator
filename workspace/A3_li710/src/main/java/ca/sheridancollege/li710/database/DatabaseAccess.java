/*
 * Name: Ji Li
 * Assignment: Assignment 3
 * Program: Computer Programming
 * Date: 08/20/2021

 * Description: This is a database access class to do CRUD operation on database
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import ca.sheridancollege.li710.beans.Course;
import ca.sheridancollege.li710.beans.Evaluation;


@Repository
public class DatabaseAccess {
	
	@Autowired
	private NamedParameterJdbcTemplate jdbc;
	
	/*
	 * Create with JDBC 
	 * Insert evaluation into evaluations table
	 * Id is auto increment generated, no need to add in the sql.
	 * otherwise only can add once as 0
	 * return affected number of rows
	 */
	public int insertEvaluation(Evaluation evaluation) {
		
		String sql = "INSERT INTO evaluations "
				+ "(title, course, grade, max, weight, dueDate) "
				+ "VALUES(:title, :course, :grade, :max, :weight, :dueDate);";
						
		MapSqlParameterSource params = new MapSqlParameterSource();
		
		//Get values for sql query statement 
		params.addValue("title", evaluation.getTitle())
			  .addValue("course", evaluation.getCourse())
			  .addValue("grade", evaluation.getGrade())
			  .addValue("max", evaluation.getMax())
			  .addValue("weight", evaluation.getWeight())
			  .addValue("dueDate", evaluation.getDueDate())
			  ;
		
		return jdbc.update(sql, params);
		
	}
	
	/*
	 * Read with Jdbc, get all records in a List. 
	 * SELECT column1, column2, ...
	 * FROM table_name 
	 * ORDER BY column1, column2, ... ASC|DESC;
	 */	
	public List<Evaluation> findAllEvaluations() {
		
		/*
		 * The evaluations should be sorted by due date, then by course code.
		 */
		String sql = "SELECT * FROM evaluations ORDER By duedate, course;";
		
		ArrayList<Evaluation> evaluations = (ArrayList<Evaluation>)jdbc.query(sql, 
				new BeanPropertyRowMapper<Evaluation>(Evaluation.class));
		
		return evaluations;
	}
	
	/*
	 * Read with Jdbc, by id or primary key. 
	 */
	public Evaluation findEvaluationById(int id) {
		
		//SQL query
		String sql = "SELECT * FROM evaluations WHERE Id=:id;";
		
		MapSqlParameterSource params = new MapSqlParameterSource();
		
		params.addValue("id", id);
		
		//create a arraylist named evaluations as search result.
		ArrayList<Evaluation> evaluations = (ArrayList<Evaluation>)jdbc.query
						(sql, 
						params, 
						new BeanPropertyRowMapper<Evaluation>(Evaluation.class));
		
		/*
		 * get first one form the list if it is not empty
		 */
		if(evaluations.isEmpty()) {
			return null;
		}else {
			return evaluations.get(0);
		}
	}
	
	
	/*
	 * Read with Jdbc, by course 
	 */
	/**
	 * Method to search evaluation by one or more courses
	 * the for loop and the last bit is for not having the ',' in 
	 * the SQL query String
	 * 
	 * "SELECT * FROM evaluations WHERE course IN (team1, team2, team3);"
	 * @param courses
	 * @return
	 */
	public List<Evaluation> findEvaluationsByCourses(String... courses) {
		
		String sql = "SELECT * FROM evaluations WHERE course IN (";
		
		MapSqlParameterSource params = new MapSqlParameterSource();
		
		String aParam;
		
		for (int i = 0; i < courses.length-1; i++) {
			
			aParam = "course" + i;
			
			//add value to aParam
			params.addValue(aParam, courses[i]);
			
			sql += ":" + aParam + ",";

		}
		
		/*
		 * No comma ',' after the parameter, 
		 * go through all these trouble just because of there should not
		 * be any comma in the end of the SQL query
		 */
		aParam = "course" + (courses.length - 1);
		sql += ":" + aParam + ");";
		
		//add the last one if input > 2 
		params.addValue(aParam, courses[courses.length - 1]);
		
		ArrayList<Evaluation> evaluations = (ArrayList<Evaluation>) jdbc.query(sql, params, 
				new BeanPropertyRowMapper<Evaluation>(Evaluation.class));
		
		return evaluations;
		
	}
	
	/*
	 * Update with Jdbc
	 */
	public int updateEvaluation(Evaluation evaluation) {
		
		String sql= "UPDATE evaluations SET "
				+ "title=:title, "
				+ "course=:course, "
				+ "grade=:grade, "
				+ "max=:max, "
				+ "weight=:weight, "
				+ "dueDate=:dueDate "				
				+ "WHERE id=:id;";
		
		MapSqlParameterSource params = new MapSqlParameterSource();
		
		params.addValue("id", evaluation.getId())
		  .addValue("title", evaluation.getTitle())
		  .addValue("course", evaluation.getCourse())
		  .addValue("grade", evaluation.getGrade())
		  .addValue("max", evaluation.getMax())
		  .addValue("weight", evaluation.getWeight())
		  .addValue("dueDate", evaluation.getDueDate())		  
		  ;

		
		return jdbc.update(sql, params);
	}
	

	
	/*
	 * Delete all evaluations, for J-unit testing
	 */
	public int deleteAllEvaluations() {
		String sql= "DELETE FROM evaluations ;";
		
		return jdbc.update(sql, new HashMap<String, Object>());
		
	}
	
	/*
	 * Delete all courses, for J-unit testing
	 */
	public int deleteAllCourses() {
		String sql= "DELETE FROM courses ;";
		
		return jdbc.update(sql, new HashMap<String, Object>());
		
	}
	
	/*
	 * Delete one course by Code 
	 * @return affected row
	 */
	public int deleteCourseByCode(String code) {
		String sql= "DELETE FROM courses WHERE code=:code;";
		
		
		MapSqlParameterSource params = new MapSqlParameterSource();
		
		params.addValue("code", code);
		
		return jdbc.update(sql, params);
		
	}
	

	
	
	/*
	 * Read with JDBC, get all. 
	 */
	public List<Course> findAllCourses() {
		
		String sql = "SELECT * FROM courses ORDER BY title;";
		
		List<Course> courses = (ArrayList<Course>)jdbc.query(sql, 
				new BeanPropertyRowMapper<Course>(Course.class));
		
		/*
		 * Alternative 
		 * List<Course> courses = new ArrayList<Course>();
		 * 
		 * //List <Map<columnName type, valueType>> List<Map<String, Object>> rows =
		 * jdbc.queryForList(sql, new HashMap());
		 * 
		 * for (Map<String, Object> row : rows) {
		 * 
		 * Course course = new Course();
		 * 
		 * course.setId((String)(row.get("id"))); course.setDescription((String)(row.get("Desc")));
		 * course.setAisle((String)(row.get("aisle")));
		 * 
		 * courses.add(course);
		 * 
		 * }
		 */
		
		return courses;
	}
	
	/*
	 * Update with Jdbc
	 */
	public int updateCourse(Course course) {
		
		String sql= "UPDATE courses SET "
				+ "title=:title, "
				+ "credits=:credits, "
				+ "term=:term, "
				+ "complete=:complete, "
				+ "finalGrade=:finalGrade, "
				+ "code=:code "				
				+ "WHERE code=:code;";
		
		MapSqlParameterSource params = new MapSqlParameterSource();
		
		params.addValue("code", course.getCode())
		  .addValue("title", course.getTitle())
		  .addValue("credits", course.getCredits())
		  .addValue("complete", course.isComplete())
		  .addValue("term", course.getTerm())
		  .addValue("finalGrade", course.getFinalGrade())
		  ;

		
		return jdbc.update(sql, params);
	}
	
	
	/*
	 * Read with JDBC, by Id or primary key. 
	 */
	public Course findCourseByCode(String code) {
		
		String sql = "SELECT * FROM courses WHERE code=:code;";
		
		MapSqlParameterSource params = new MapSqlParameterSource();
		
		params.addValue("code", code);
		
		/*
		 * Course course = jdbc.queryForObject(sql,params, new
		 * BeanPropertyRowMapper<Course>(Course.class));
		 */
		
		ArrayList<Course> courses = (ArrayList<Course>)jdbc.query
				(sql, 
				params, 
				new BeanPropertyRowMapper<Course>(Course.class));

				/*
				 * get first one form the list if it is not empty
				 */
				if (courses.isEmpty()) {
					return null;
				} else {
					return courses.get(0);
				}
		
	}
	
	/*
	 * A method to create a mapping for the Course with code as key, title
	 * as value.
	 */
	public Map<String, String> getCourseMap() {
		
		Map<String, String> courseMap = new HashMap<>();
		
		List<Course> courses = findAllCourses();
		
		for(Course c : courses) {
			
			courseMap.put(c.getCode(), c.getTitle());
			
		}
		
		return courseMap;
	}
	
	/*
	 * A method to create a mapping for the Course with code as key, 
	 * Course object as value.
	 */
	public Map<String, Course> getCourseObjectMap() {
		
		Map<String, Course> courseMap = new HashMap<>();
		
		List<Course> courses = findAllCourses();
		
		for(Course c : courses) {
			
			courseMap.put(c.getCode(), c);
			
		}
		
		return courseMap;
	}
	
	/*
	 * A method to calculate and update cumulative Grade 
	 * for a specific record to the final grade.
	 * 
	 */
	public int updateFinalGradeByCode(String code) {
		
		String sql= "UPDATE courses SET "
				+ "finalGrade=:finalGrade "
				+ "WHERE code=:code;";
		
		/*
		 * 1st, find the all the evaluations of the course
		 */
		List<Evaluation> evals = findEvaluationsByCourses(code);
		
		/*
		 * 2nd, literate through to get the total cumulative grades: cGrade
		 */
		Iterator<Evaluation> it = evals.iterator();
		
		double cGrades = 0; //cumulative grades
		
		while(it.hasNext()) {
			
			Evaluation e = it.next();
			cGrades += e.calcGrade();
			
		}		
		
		/*
		 * Add eGrades and code to SQL parameter finalGrade and code
		 */
		MapSqlParameterSource params = new MapSqlParameterSource();
		
		params.addValue("code", code)
			.addValue("finalGrade", cGrades)		
		  ;
		
		return jdbc.update(sql, params);		
	}
	
	
	/*
	 * A method to update final grade for all completed courses
	 * 
	 */
	public int updateFinalGradeForCompletedCourses() {
		
		List<Course> completedCourses = findAllCompletedCourses(true);
		
		int affectedRecords = 0;
		
		for(Course cc : completedCourses) {
			
			/*
			 * Call updateFinalGradeByCode method and go through every
			 * completed course.
			 */
			affectedRecords += updateFinalGradeByCode(cc.getCode());
			
		}
		
		return affectedRecords;
	}
	
	/*
	 * A method to create a list for the all the completed Courses
	 * 
	 */
	public List<Course> findAllCompletedCourses(boolean complete) {
		
		String sql = "SELECT * FROM courses WHERE complete=:complete "
				+ "ORDER BY term;";
		
		MapSqlParameterSource params = new MapSqlParameterSource();
		
		params.addValue("complete", complete);
		
		List<Course> courses = (ArrayList<Course>)jdbc.query(sql, params,  
				new BeanPropertyRowMapper<Course>(Course.class));
	
		return courses;
	}
	
	
	/*
	 * Search with JDBC, by course title or course code, ignore cases:
	 * SELECT * FROM COURSES 
	 * where upper(title) like '%math%' or upper(code) like upper('%math%') ; 
	 */
	public List<Course> searchCourseByKeyword(String keyword) {
		
		//Database search query 
		String sql = "SELECT * FROM courses WHERE "
				+ "UPPER(title) LIKE UPPER(:keyword) "
				+ "OR "
				+ "UPPER(code) LIKE UPPER(:keyword);"
				;
		
		//Create a SqlParameterSource(interface) instance
		MapSqlParameterSource params = new MapSqlParameterSource();
		
		//binding the name with search string
		params.addValue("keyword", "%" + keyword + "%");
		
		/*call method query(String sql, SqlParameterSource paramSource, 
		RowMapper<T> rowMapper)
		*/
		List<Course> courses = (ArrayList<Course>)jdbc.query(
				sql, 
				params,  
				new BeanPropertyRowMapper<Course>(Course.class));
		
		//return a list of matching evaluations as result
		return courses;
				
	}
	
	/**
	 * 
	 * Create with JDBC 
	 * Insert new course for Courses table
	 * return affected rows integer
	 */
	public int insertCourse(Course course) {
		
		String sql = "INSERT INTO courses "
				+ "(code, title, credits, complete, term, finalgrade) "
				+ "VALUES(:code, :title, :credits, :complete, :term, :finalgrade);";
						
		MapSqlParameterSource params = new MapSqlParameterSource();
		
		/*
		 * Get values for SQL query statement 
		 * The boolean attribute complete's get method in lombok is isComplete()
		 */
		params.addValue("code", course.getCode())
			  .addValue("title", course.getTitle())
			  .addValue("credits", course.getCredits())
			  .addValue("complete", course.isComplete())
			  .addValue("term", course.getTerm())
			  .addValue("finalgrade", course.getFinalGrade())
			  ;
		
		// Get the auto-increment key
//		KeyHolder keyholder = new GeneratedKeyHolder();
		
		return jdbc.update(sql, params);
		
	}
	
	/**
	 * Method to count how many rows in Courses table
	 * @return count long type
	 */
	public Long countAllCourses() {
		String sql = "SELECT count(*) FROM courses;";
		return jdbc.queryForObject(sql, new HashMap<String, Object>(), Long.TYPE);

	}
	
	/**
	 * Save all courses in the table
	 * @param list of course
	 */
	public void saveAllCourses(List<Course> courses) {

		for (Course c : courses) {

			insertCourse(c);

		}
	}
	
	
}