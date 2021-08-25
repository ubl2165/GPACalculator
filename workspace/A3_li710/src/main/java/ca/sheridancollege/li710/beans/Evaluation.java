/*
 * Name: Ji Li
 * Assignment: Assignment 3
 * Program: Computer Programming
 * Date: 08/20/2021

 * Description: This is a java Bean Class called Evaluation.
 * This class uses Lombok to generate methods.
 * This class uses Validation artifact to validate data field.
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

package ca.sheridancollege.li710.beans;

import java.io.Serializable;
import java.time.LocalDate;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

import lombok.*;
 
/**
 * Evaluation models an evaluation (test, assignment, quiz, etc) in a course
 * or class that is graded.
 * Updated June 2021 - Assignment 2 Version
 *
 * @author Wendi Jollymore 
 * @author Ji Li July 2021
 */
@Data
@NoArgsConstructor
public class Evaluation implements Serializable {
 
    private static final long serialVersionUID = 2L;
 
    private int id;
    
    @NotBlank(message = "Title can't be null") 
    private String title = "TBD";
    
    private String course;
    
	@DecimalMin(value = "0.0", message = "Grade must be greater or equal to 0!")
	@DecimalMax(value = "100", message = "Grade must be less than {value}")
    private double grade;
    
    @Positive(message="Max must be greater than zero! ")
	@Max(value = 100, message="Max must be less or equal to 100!")
    private double max;
    
	@Positive(message="Weight must be greater than zero! ")
	@Max(value = 100, message="Weight must be less or equal to 100!")
    private double weight = 10;
	
    private LocalDate dueDate;
    
    /**
     * Constructs a new Evaluation with a specific title, course, grade, max,
     * weight, and due date.
     * 
     * @param title this evaluation't title
     * @param course this evaluation's course code
     * @param grade this evaluation's actual grade
     * @param max what this evaluation is out of
     * @param weight the weight of this evaluation
     * @param dueDate this evaluation's due date
     */
    public Evaluation(String title, String course, double grade, double max, 
            double weight, LocalDate dueDate) {
        setTitle(title);
        setCourse(course);
        setGrade(grade);
        setMax(max);
        setWeight(weight);
        setDueDate(dueDate);
    }
    
    /**
     * Constructs an evaluation with a specific id, title, course, grade, max, 
     * weight, and due date.
     * 
     * @param id this evaluation's unique ID
     * @param title this evaluation't title
     * @param course this evaluation's course code
     * @param grade this evaluation's actual grade
     * @param max what this evaluation is out of
     * @param weight the weight of this evaluation
     * @param dueDate this evaluation's due date
     */
    public Evaluation(int id, String title, String course, double grade, 
            double max, double weight, LocalDate dueDate) {
        this(title, course, grade, max, weight, dueDate);
        setId(id);      
    }
    
    /**
     * Ensure a valid title goes into this evaluation's title member.  Title
     * can't be null or empty.
     * 
     * @param title the specified title
     */
//    public void setTitle(String title) {
//        
//        // title can't be null or empty
//        if (title == null || title.trim().isEmpty())
//            throw new IllegalArgumentException("Evaluation title can't be empty.");
//        else
//            this.title = title;
//    }
    
    /**
     * Ensure this evaluation's grade receives a valid value.  Grade can't
     * be less than 0.  A 0 grade could mean the evaluation is not yet
     * graded.
     * 
     * @param grade the specified actual grade
     */
    public void setGrade(double grade) {
        
        // make sure grade is 0 or more
        if (grade >= 0)
            this.grade = grade;
        else
            throw new IllegalArgumentException("Grade must be greater than 0.");
    }
    
    /**
     * Ensures a valid value is placed into this evaluation's maximum grade.
     * Maximum grade must be 0 to 100.  A 0 maximum is possible for check-off
     * evaluations.
     * 
     * @param max the specified maximum grade
     */
    public void setMax(double max) {
        
        // ensure max is valid
        if (max >= 0 && max <= 100)
            this.max = max;
        else
            throw new IllegalArgumentException("Maximum grade must be between 0 "
                    + "and 100.");
    }
    
    /**
     * Ensure's this evaluation's weight is assigned a valid value. Weight must
     * be more than 0 but no more than 100.
     * 
     * @param weight the specified weight for this evaluation
     */
    public void setWeight(double weight) {
        
        // make sure weight is valid
        if (weight > 0 && weight <= 100)
            this.weight = weight;
        else 
            throw new IllegalArgumentException("Evaluation Weight must be between"
                    + " 0 and 100.");
    }
    
    /**
     * Determines the amount of weight earned for this evaluation item.
     * 
     * @return the weight of this item as grade / max * weight
     */
    public double calcGrade() {
        
        // standard formula for earned weight
        return grade / max * weight;
    }
    
    /**
     * Retrieves this Evaluation as a String, which includes the title,
     * grade, max grade, and the earned weight.
     * 
     * @return this Evaluation as a String
     */
    public String toString() {
        return String.format("id: %d %s %.1f/%.1f Earned %.1f%%", id, title,
                grade, max, calcGrade());
    }
}