/*
 * Copyright (C) David P. Lopez - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited. 
 * Proprietary & Confidential
 * Written by: Lopez <DavidPLopez@Live.com>
 */
package teststudents;

/**
 *
 * @author MyDocs
 */
public class TestStudents {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        StudentList studentList = new StudentList();
        
        studentList.readStudents();
        studentList.saveStudentsToDB();
        studentList.writeStudents();
        studentList.writeSortedStudents();
        studentList.findStudent();
        
        
        
    }
    
}
