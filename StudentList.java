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
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

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
                
                // Tracer rounds
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
        // Start writing data to db!!!!
        
        try {
            // Declare variables & save login credentials
            String user = "root";
            String pw = "root";
            String dbName = "Grades";
            String url = "jdbc:mysql://localhost:3306/";

            // Load JDBC connection drivers
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            System.out.println("Driver Loaded...");

            // Establish a connection
            Connection conn =
               DriverManager.getConnection(url, user, pw);
            System.out.println("Database Connection Established...");

            // Do something with the Connection
            // Create Statements
            Statement s = conn.createStatement();

            // Execute a Statement & Create DB if it does not exist
            int myResult = s.executeUpdate("DROP DATABASE IF EXISTS " + dbName);
            System.out.println("db dropped? " + myResult);
            myResult = s.executeUpdate("CREATE DATABASE IF NOT EXISTS " + dbName);
            System.out.println(dbName + " DB Created Successfully.");
            System.out.println("result: " + myResult);
            
            // reset conn to Connect to dbName database
            System.out.println("Connecting to " + dbName + " database...");
            conn =
               DriverManager.getConnection(url + dbName, user, pw);
            // prepare to Create db tables & schema in selected db
            s = conn.createStatement();
            System.out.println("Creating tables in " + dbName);
            // Declare variable to store sql statement to execute
            String sqlCreateTableSchema = "CREATE TABLE StudentsTbl " +
                    "(id INTEGER not NULL, " +
                    "FirstName VARCHAR(255), " +
                    "LastName VARCHAR(255), " +
                    "Grade1 DOUBLE, " +
                    "Grade2 DOUBLE, " +
                    "Grade3 DOUBLE, " +
                    "Average DOUBLE, " +
                    "Status VARCHAR(255), " +
                    "LetterGrade VARCHAR(1), " +
                    "PRIMARY KEY (id))";
            // Execute required statements to create tables
            myResult = s.executeUpdate(sqlCreateTableSchema);
            System.out.println("Tables created in " + dbName);
            System.out.println("Result: " + myResult);
            
            int count = 1;
            
            for(Student student : this.students){
                myResult = s.executeUpdate("INSERT INTO StudentsTbl " +
                        "VALUES ( " + count++ + ", " + 
                        "'" + student.getFirstName() + "', " +
                        "'" + student.getLastName() + "', " +
                        student.getGrade1() + ", " +
                        student.getGrade2() + ", " +
                        student.getGrade3() + ", " +
                        student.getAverage() + ", " +
                        "'" + student.getStatus() + "', " +
                        "'" + student.getLetterGrade() + "')"
                        );
                System.out.println("student" + (count - 1) + " entered: " + myResult);
            }
            System.out.println("Records entered into Students Table Successfully...");
            
            
            
            


        } catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(StudentList.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("SEVERE ERROR: " + Arrays.toString(ex.getStackTrace()));
        } catch (InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(StudentList.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //
        
    }
    
    public void findStudent(){
        
    }
    
    public void writeStudents(){
        
    }
    
    public void writeSortedStudents(){
        
    }
    
    
    
}
