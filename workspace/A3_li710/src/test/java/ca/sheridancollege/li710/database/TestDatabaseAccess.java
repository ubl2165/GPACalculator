package ca.sheridancollege.li710.database;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.junit4.SpringRunner;

import ca.sheridancollege.li710.beans.Course;
import ca.sheridancollege.li710.beans.Evaluation;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureTestDatabase
public class TestDatabaseAccess {
	
	@Autowired
	private DatabaseAccess da;
	
	private List<Evaluation> evalBackUp;
	private List<Course> courBackUp;
	
	/*
	 * If execute it only once before running all tests 
	 * then using @BeforeClass instead of @Before
	 * 
	 * before each test case, back up the original data.
	 * only for small database.
	 */
	@Before
	public void backUp() {
		evalBackUp= da.findAllEvaluations();
		courBackUp = da.findAllCourses();
	}
	
	/*
	 * Restore all the database after each test case 
	 * The id will be randomly, will not be 1,2,3...
	 * will be 105, 106, 107...
	 * because of auto_increment's nature
	 */
	@After
	public void restore() {
		da.deleteAllCourses();
		da.deleteAllEvaluations();
		
		for(Evaluation e : evalBackUp) {
			da.insertEvaluation(e);
		}
		
		for(Course c : courBackUp) {
			da.insertCourse(c);
		}
	}
	
	

	/*
	 * Good test for insertEvaluation() and findAllEvaluations()
	 * 
	 */
	@Test
	public void testInsertEvaluation_findAllEvaluations_Pass() {
		
		Evaluation  testEvaluation = 
				new Evaluation("Test", "PROG10082", 10, 10, 10, 
						LocalDate.of(2021, 7, 31)); 
		
		int oldSize = da.findAllEvaluations().size();
		int result = da.insertEvaluation(testEvaluation);	
		int newSize = da.findAllEvaluations().size();		
		Assert.assertTrue(oldSize + result == newSize);

	}
	
	/*
	 * Bad test for insertEvaluation() and findAllEvaluations()
	 * No such exception (expected=DuplicateKeyException.class)
	 * Because id is auto-increment 
	 * Set one of the not null value to null and 
	 * DataIntegrityViolationException will pop up.
	 */
	@Test(expected=DataIntegrityViolationException.class)
	public void testInsertEvaluation_findAllEvaluations_Fail() {
		
		//set Title(not null) to null for the test evaluation object
		Evaluation  testEvaluation = 
				new Evaluation(null, "PROG10082", 100, 100, 100, 
						LocalDate.of(2021, 7, 31));
		
		int oldSize = da.findAllEvaluations().size();

		da.insertEvaluation(testEvaluation);
		int newSize = da.findAllEvaluations().size();

		//size should not change
		Assert.assertTrue(oldSize == newSize);
	}
	
	/*
	 * Good test for findEvaluationById()
	 * 
	 */
	@Test
	public void testFindEvaluationById_Pass() {
		
		//Retrieve all PROG10082 evaluations
		List<Evaluation> testList = da.findEvaluationsByCourses("PROG10082");
		
		//Get the first one's id
		int testId = testList.get(0).getId();
				
		//assign as argument for findEvaluationById()
		Evaluation test = da.findEvaluationById(testId);

		//Its course name must match PROG10082
		Assert.assertEquals(test.getCourse(), "PROG10082");

	}
	
	/*
	 * Bad test for findEvaluationById()
	 */
	@Test
	public void testFindEvaluationById_Fail() {
		
		//99 is not existing in the database
		Evaluation test = da.findEvaluationById(9999);
		
		//it must not be null
		Assert.assertEquals(test, null);

	}
	
	/*
	 * Good test for findEvaluationsByCourses()
	 */
	@Test
	public void testFindEvaluationsByCourses_Pass() {
		
		//"PROG10082" is known existing in the database, 6 evaluations total
		List<Evaluation> test = da.findEvaluationsByCourses("PROG10082");
		
		//6 evaluations total
		Assert.assertEquals(test.size(), 6);

	}
	
	/*
	 * Bad test for findEvaluationById()
	 */
	@Test
	public void testFindEvaluationsByCourses_Fail() {
		
		//"ABCD12345" is not existing in the database
		List<Evaluation> test = da.findEvaluationsByCourses("ABCD12347");
		
		//it must not be 0
		Assert.assertEquals(test.size(), 0);

	}
	
	/*
	 * Good test for updateEvaluation(Evaluation evaluation)
	 */
	@Test
	public void testUpdateEvaluation_Pass() {
		
		//"PROG10082" is known existing in the database, 6 evaluations total
		List<Evaluation> list = da.findEvaluationsByCourses("PROG10082");
		
		//get first one as test piece
		Evaluation test = list.get(0);
		
		//do a little change
		test.setGrade(10);
		test.setTitle("Assignment 99");
		
		
		//updateEvaluation will return 1 when successful.
		Assert.assertEquals(da.updateEvaluation(test), 1);

	}
	
	/*
	 * Bad test for updateEvaluation(Evaluation evaluation)
	 */
	@Test
	public void testUpdateEvaluation_Fail() {
		
		//find first one in database
		Evaluation test = da.findEvaluationById(1);
		
		//do a little change, but set id to 100 non-existing
		test.setCourse("TEST12345");
		test.setGrade(10);
		test.setTitle("Assignment 99");
		test.setId(100);	
		
		//update will return 0
		Assert.assertEquals(da.updateEvaluation(test), 0);

	}
	
	/*
	 * Good test for insertCourse() and findAllCourses()
	 * 
	 */
	@Test
	public void testInsertCourse_findAllCourse_Pass() {
		

		Course  test = 
				new Course("TEST99999", "C Programming", 3.0, false, 2, 99.9); 
		
		int oldSize = da.findAllCourses().size();
		int result = da.insertCourse(test);	//return 1
		int newSize = da.findAllCourses().size();		
		Assert.assertTrue(oldSize + result == newSize);

	}
	
	/*
	 * Bad test for insertCourse() and findAllCourses()
	 */
	@Test(expected=DuplicateKeyException.class)
	public void testInsertCourse_findAllCourse_Fail() {
		
		//add an existing primary key(code) for the test object
		Course  test = 
				new Course("PROG10082", "C Programming", 3.0, false, 2, 99.9); 
		
		
		int oldSize = da.findAllCourses().size();
		int result = da.insertCourse(test);	//exception
		int newSize = da.findAllCourses().size();
		Assert.assertEquals(result, 0);
		Assert.assertEquals(oldSize, newSize);
	}
	
	/*
	 * Good test for updateCourse(Course course)
	 */
	@Test
	public void testUpdateCourse_Pass() {
		
		//"PROG10082" is known existing in the database
		Course test = da.findCourseByCode("PROG10082");
		
	
		//do a little change
		test.setTerm(4);
		test.setTitle("Winds of Winter");
		
		//update will return 1 when successful.
		Assert.assertEquals(da.updateCourse(test), 1);

	}
	
	/*
	 * Bad test for updateCourse(Course course)
	 */
	@Test
	public void testUpdateCourse_Fail() {
		
		//"PROG10082" is known existing in the database
		Course test = da.findCourseByCode("PROG10082");
		
		//do a little change
		test.setTerm(4);
		test.setTitle("Winds of Winter");
		test.setCode("WIND12345");//non existing primary key
		
		//update will return 0
		Assert.assertEquals(da.updateCourse(test), 0);

	}
	
	/*
	 * Good test for findCourseByCode()
	 * 
	 */
	@Test
	public void testFindCourseByCode_Pass() {
		
		
		Course course = 
				new Course("TEST55555", "C Programming", 3.0, false, 2, 99.9); 
		
		da.insertCourse(course);
		//TEST55555 is known existing in the database
		Course test = da.findCourseByCode("TEST55555");
		
		//it must not be null
		Assert.assertTrue(test.getFinalGrade().doubleValue() == 99.9);

	}
	
	/*
	 * Bad test for findCourseByCode()
	 */
	@Test
	public void testFindCourseByCode_Fail() {
		
		//"WIND11111" is not existing in the database
		Course test = da.findCourseByCode("WIND11111");
		
		//it must not be null
		Assert.assertEquals(test, null);
	}
	
	/*
	 * Good test for getCourseMap()
	 * 
	 */
	@Test
	public void testGetCourseMap_Pass() {
		
		
		Course course = 
				new Course("TEST22222", "Dream Of Spring", 3.0, false, 2, 99.9); 
		
		da.insertCourse(course);
		
		Map<String, String> test = da.getCourseMap();
		
		//TEST22222 is known existing in the database
		Assert.assertEquals(test.get("TEST22222"), "Dream Of Spring");

	}
	
	/*
	 * Bad test for getCourseMap()
	 */
	@Test
	public void testGetCourseMap_Fail() {
		
		Map<String, String> test = da.getCourseMap();
		
		//TEST98765 IS NOT EXIST
		Assert.assertEquals(test.get("TEST98765"), null);
	}
	
	/*
	 * Good test for  updateFinalGradeByCode(String code)
	 * 
	 */
	@Test
	public void testUpdateFinalGradeByCode_Pass() {
		
		//Insert a course with 99.9 final grade
		Course course = 
				new Course("TEST23456", "Dream Of Spring", 3.0, false, 2, 99.9); 
		
		da.insertCourse(course);		
		Course beforeUpdate = da.findCourseByCode("TEST23456");		
		Assert.assertTrue(beforeUpdate.getFinalGrade() == 99.9);	
		
		//update the same course, there is no evaluations, it should become 0.
		da.updateFinalGradeByCode("TEST23456");		
		Course afterUpdate = da.findCourseByCode("TEST23456");
		Assert.assertTrue(afterUpdate.getFinalGrade() == 0);

	}
	
	/*
	 * Bad test for updateFinalGradeForCompletedCourses()
	 * AND updateFinalGradeByCode(String code)
	 */
	@Test
	public void testUpdateFinalGradeByCode_Fail() {
		
		//"NNNN11111" not exist, method return 0
		Assert.assertEquals(da.updateFinalGradeByCode("NNNN11111"), 0);
	}
	
	
	/*
	 * Good test for  updateFinalGradeForCompletedCourses()
	 * 
	 */
	@Test	
	public void testUpdateFinalGradeForCompletedCourses_Pass() {
		
		//Find the size of completed course
		int completeNum = da.findAllCompletedCourses(true).size();
		
		//update return value should match number of completed courses
		Assert.assertEquals(da.updateFinalGradeForCompletedCourses(), 
				completeNum);

	}
	
	/*
	 * Bad test for updateFinalGradeForCompletedCourses()
	 * 
	 */
	@Test
	public void testUpdateFinalGradeForCompletedCourses_Fail() {
		
		//find completed course list
		List<Course> list = da.findAllCompletedCourses(true);
		
		//Set the complete to false and update database
		for (Course c : list) {
			c.setComplete(false);
			da.updateCourse(c);
		}
		
		//update final grade return 0 record, all records are incomplete
		Assert.assertEquals(da.updateFinalGradeForCompletedCourses(), 0);
	}
	
	/*
	 * Good test for findAllCompletedCourses(boolean complete)
	 * 
	 */
	@Test	
	public void testFindAllCompletedCourses_Pass() {
		
		int total = da.findAllCourses().size();
		int complete = da.findAllCompletedCourses(true).size();
		int  incomplete = da.findAllCompletedCourses(false).size();
		
		Assert.assertTrue(total == complete  + incomplete);
	}
	
	/*
	 * Bad test for findAllCompletedCourses(boolean complete)
	 * 
	 */
	@Test
	public void testFindAllCompletedCourses_Fail() {
		
		//set all completed course to incomplete
		List<Course> test = da.findAllCompletedCourses(true);
		for(Course c : test){
			c.setComplete(false);
			da.updateCourse(c);
		}
		
		//no completed course
		Assert.assertEquals(da.findAllCompletedCourses(true).size(), 0);
		
	}
	
	

}
