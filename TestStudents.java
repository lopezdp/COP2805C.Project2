/*
 * Copyright (C) David P. Lopez - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited. 
 * Proprietary & Confidential
 * Written by: Lopez <DavidPLopez@Live.com>
 */
package teststudents;

import java.io.*;

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

public class TestStudents {

    /**
     * @param args the command line arguments
     * @throws java.io.FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException {
        // TODO code application logic here
        StudentList studentList = new StudentList();
        
        studentList.readStudents();
        studentList.saveStudentsToDB();
        studentList.writeStudents();
        studentList.writeSortedStudents();
        studentList.findStudent(); 
    }
    
}
