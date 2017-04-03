/*
 * Copyright (C) David P. Lopez - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited. 
 * Proprietary & Confidential
 * Written by: Lopez <DavidPLopez@Live.com>
 */
package teststudents;

import java.util.*;
import javax.swing.*;
import javax.swing.filechooser.*;
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author MyDocs
 */
public class StudentList {
    
    // Declare fields & variables
    List<Student> students;
    
    // Constructor
    public StudentList(){
        this.students = new ArrayList<>();
    }
    
    // method that prompts the user for an input file name
    // and it reads the contents of the input file into students
    // Assume students have between 0 & 3 grades
    // Expected File Input:
    // FirstName | LastName | Grade1 | Grade2
    // FirstName | LastName | Grade1 | Grade2 | Grade3
    // FirstName | LastName | Grade1 
    // FirstName | LastName 
    
    public void readStudents() throws FileNotFoundException{
        // Create a new instance of a file chooser
        JFileChooser chooser = new JFileChooser();
        
        // Allow user to select only .txt files
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Select Only (.txt) files: ", "txt");
        chooser.setFileFilter(filter);
        
        // Display chooser dialog menu
        int returnVal = chooser.showOpenDialog(null);
        
        // Notify the user of the selected file & parse the file
        if(returnVal == JFileChooser.APPROVE_OPTION){
            // Store path to file
            String path = chooser.getSelectedFile().getPath();
            
            // Store filename
            File file = new File(path);
            
            // Notify user of selected file
            System.out.println("You have chosen to open the following file: " + file.toString());
            
            // Use Scanner to access file contents
            Scanner input = new Scanner(file);
            
            // Declare temp variables to hold data from file
            String firstName, lastName;
            double grade1, grade2, grade3;
            
            // Loop control count
            int studentElement = 0;
            
            // Loop through file contents and save each object as an element in ArrayList
            while(input.hasNext()){
                String line = input.nextLine();
                String[] lineArr = line.split("\\|");
                
                if(lineArr.length == 5){
                    firstName = lineArr[0];
                    lastName = lineArr[1];
                    grade1 = Double.parseDouble(lineArr[2]);
                    grade2 = Double.parseDouble(lineArr[3]);
                    grade3 = Double.parseDouble(lineArr[4]);
                    
                    this.students.add(new Student(firstName, lastName));
                    this.students.get(studentElement).setGrade1(grade1);
                    this.students.get(studentElement).setGrade2(grade2);
                    this.students.get(studentElement).setGrade3(grade3);

                    this.students.get(studentElement).computeAverage(this.students.get(studentElement), 3);
                    this.students.get(studentElement).computeStatus(this.students.get(studentElement));
                    this.students.get(studentElement).computeLetterGrade(this.students.get(studentElement).getAverage());
                    
                    studentElement++;

                } else if(lineArr.length == 4){
                    firstName = lineArr[0];
                    lastName = lineArr[1];
                    grade1 = Double.parseDouble(lineArr[2]);
                    grade2 = Double.parseDouble(lineArr[3]);
                    
                    this.students.add(new Student(firstName, lastName));
                    this.students.get(studentElement).setGrade1(grade1);
                    this.students.get(studentElement).setGrade2(grade2);

                    this.students.get(studentElement).computeAverage(this.students.get(studentElement), 2);
                    this.students.get(studentElement).computeStatus(this.students.get(studentElement));
                    this.students.get(studentElement).computeLetterGrade(this.students.get(studentElement).getAverage());
                    
                    studentElement++;

                } else if(lineArr.length == 3){
                    firstName = lineArr[0];
                    lastName = lineArr[1];
                    grade1 = Double.parseDouble(lineArr[2]);
                    
                    this.students.add(new Student(firstName, lastName));
                    this.students.get(studentElement).setGrade1(grade1);

                    this.students.get(studentElement).computeAverage(this.students.get(studentElement), 1);
                    this.students.get(studentElement).computeStatus(this.students.get(studentElement));
                    this.students.get(studentElement).computeLetterGrade(this.students.get(studentElement).getAverage());
                    
                    studentElement++;

                } else if(lineArr.length == 2){
                    firstName = lineArr[0];
                    lastName = lineArr[1];

                    this.students.add(new Student(firstName, lastName));
                    
                    studentElement++;

                }
                
                System.out.println("firstName: " + this.students.get(studentElement - 1).getFirstName());
                System.out.println("lastName: " + this.students.get(studentElement - 1).getLastName());
                System.out.println("grade1: " + this.students.get(studentElement - 1).getGrade1());
                System.out.println("grade2: " + this.students.get(studentElement - 1).getGrade2());
                System.out.println("grade3: " + this.students.get(studentElement - 1).getGrade3());
                System.out.println("average: " + this.students.get(studentElement - 1).getAverage());
                System.out.println("status: " + this.students.get(studentElement - 1).getStatus());
                System.out.println("letterGrade: " + this.students.get(studentElement - 1).getLetterGrade());
                
            }  
        }
    }
    
    public void saveStudentsToDB(){
        // Create a new instance of a file chooser
        JFileChooser dbChooser = new JFileChooser();
        
        // Allow user to select only .txt files
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Select Only (.sql) files: ", "sql");
        dbChooser.setFileFilter(filter);
        
        // Display chooser dialog menu
        int returnVal = dbChooser.showOpenDialog(null);
        
        // Notify the user of the selected file & parse the file
        if(returnVal == JFileChooser.APPROVE_OPTION){
            // Store path to file
            String path = dbChooser.getSelectedFile().getPath();
            
            // Store filename use file to write to disk
            File file = new File(path);
            
            // Notify user of selected file
            System.out.println("You have chosen to write data to the following DB: " + file.toString());
            
            // Start writing data to tables!!!!
            Connection conn = null;
            
            //Class.forName("com.mysql.jdbc.Driver").newInstance();
            
            try {
                String user = "root";
                String pw = "root";
                String url = "jdbc:mysql://localhost:3306/sakila";
                conn =
                   DriverManager.getConnection(user, pw, url);

                // Do something with the Connection
                // ...
                
                
            } catch (SQLException ex) {
                // handle any errors
                System.out.println("SQLException: " + ex.getMessage());
                System.out.println("SQLState: " + ex.getSQLState());
                System.out.println("VendorError: " + ex.getErrorCode());
            }
            
            
            
            
        }
    }
    
}
