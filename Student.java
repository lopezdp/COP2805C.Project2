/*
 * Copyright (C) David P. Lopez - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited. 
 * Proprietary & Confidential
 * Written by: Lopez <DavidPLopez@Live.com>
 */
package teststudents;

/**
 * Student ID: 1001975600
 * COP2805C Java Programming 2
 * Spring - T Th 6:15 PM - 9:30 PM
 * Project 2
 * @author David P. Lopez
 * 
 * Plagiarism Statement: I certify that this assignment is my own work and 
 * that I have not copied in part or whole or otherwise plagiarized the work
 * of other students and/or persons.
 */

public class Student {
    
    // Declare field Variables
    private String firstName, lastName, status;
    private char letterGrade;
    private double grade1, grade2, grade3, average;
    
    // Default Constructor
    public Student(){
        
    }
    
    // Overloaded Constructor
    public Student(String firstName, String lastName){
        this.firstName = firstName;
        this.lastName = lastName;
    }
    
    // Mutator Methods
    public void setFirstName(String firstName){
        this.firstName = firstName;
    }
    
    public void setLastName(String lastName){
        this.lastName = lastName;
    }
    
    public void setGrade1(double grade1){
        this.grade1 = grade1;
    }
    
    public void setGrade2(double grade2){
        this.grade2 = grade2;
    }
    
    public void setGrade3(double grade3){
        this.grade3 = grade3;
    }
    
    // Accessor Methods
    public String getFirstName(){
        return this.firstName;
    }
    
    public String getLastName(){
        return this.lastName;
    }
    
    public double getGrade1(){
        return this.grade1;
    }
    
    public double getGrade2(){
        return this.grade2;
    }
    
    public double getGrade3(){
        return this.grade3;
    }
    
    public double getAverage(){
        return this.average;
    }
    
    public String getStatus(){
        return this.status;
    }
    
    public char getLetterGrade(){
        return this.letterGrade;
    }
    
    // Method to compute the average
    public void computeAverage(Student student, int numGrades){
        double avg = (student.grade1 + student.grade2 + student.grade3) / numGrades;
        this.average = avg;
    }
    
    // Method to compute the status of each student
    public void computeStatus(Student student){
        if(this.getAverage() < 70){
            this.status = "Failing";
        }else {
            this.status = "Passing";
        }
    }
    
    // Method to compute the letter grade of each student
    public void computeLetterGrade(double avg){
        if(this.getAverage() >= 90){
            this.letterGrade = 'A';
        }else if(this.getAverage() >= 80 && this.getAverage() < 90){
            this.letterGrade = 'B';
        }else if(this.getAverage() >= 70 && this.getAverage() < 80){
            this.letterGrade = 'C';
        }else if(this.getAverage() >= 60 && this.getAverage() < 70){
            this.letterGrade = 'D';
        }else{
            this.letterGrade = 'F';
        }
    }   
}
