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
import java.sql.ResultSet;
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
    // Declare variables & save login credentials
    private final String user = "root";
    private final String pw = "root";
    private final String dbName = "Grades";
    private final String url = "jdbc:mysql://localhost:3306/";
    private final String driver = "com.mysql.jdbc.Driver";
    private JFileChooser chooser = new JFileChooser(); // Instantiate new chooser
    private FileNameExtensionFilter filter;
    private Connection conn;
    private ResultSet resultSet;
    private List<Student> students;
    
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
        // Create a new instance of a file chooser and 
        // Allow user to select only .txt files
        this.filter = new FileNameExtensionFilter("Select Only (.txt) files: ", "txt");
        this.chooser.setFileFilter(this.filter);
        
        // Display chooser dialog menu
        int returnVal = this.chooser.showOpenDialog(null);
        
        // Notify the user of the selected file & parse the file
        if(returnVal == JFileChooser.APPROVE_OPTION){
            // Store path to file
            String path = this.chooser.getSelectedFile().getPath();
            
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
                
                // store contents of line and split contents into 
                // array at delimiter \|
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
            }
            // Confirmation that data has been read from file and is now ready to be stored into db
            System.out.println("Contents of file are ready to be saved to database.");
            
        }
    }
    
    public void saveStudentsToDB(){
        // Start writing data in students array to db!!!!
        try {
            // Call function to connect DB
            connectDB(0);

            // Do something with the Connection
            // Create Statements
            Statement s = this.conn.createStatement();

            // Execute a Statement & Create DB if it does not exist
            int myResult = s.executeUpdate("DROP DATABASE IF EXISTS " + this.dbName);
            System.out.println("db dropped? " + myResult);
            myResult = s.executeUpdate("CREATE DATABASE IF NOT EXISTS " + this.dbName);
            System.out.println(this.dbName + " DB Created Successfully.");
            System.out.println("db created?: " + myResult);
            
            // reset conn to Connect to dbName database
            System.out.println("Connecting to " + this.dbName + " database...");
            
            connectDB(1);
            
            // prepare to Create db tables & schema in selected db
            s = this.conn.createStatement();
            System.out.println("Creating tables in " + this.dbName);
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
            s.executeUpdate(sqlCreateTableSchema);
            System.out.println("Tables created in " + this.dbName);
            
            // iteration count
            int count = 1;
            // loop through each element in students array 
            // and insert data into students table
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
                System.out.println("student" + (count - 1) + " records entered: " + myResult);
            }
            System.out.println("Records entered into Students Table Successfully...");
            
            // Close connection to DB
            this.conn.close();
        } catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
    }
    
    public void findStudent(){
        // Prompt User for Student Name (Last, First)
        // Show message indicating if student found in db
        // Use JOptionPane with text fields & OK & Cancel Buttons
        //  Continue asking the user until user presses cancel
        
        // Declare variable to store name entered
        String name = "";
        String [] n = new String[2];
        String first = "";
        String last = "";
        String msg = "";
        
        // create event loop to keep asking user for a name
        // when student name found in db then display to user
        // when user cancels dialog then exit loop
        while(name != null){
            // create an instance of JOptionPane to accept user input
            name = JOptionPane.showInputDialog(null, "Enter First & Last Name of Student to Find.", "Find Student in DB", JOptionPane.QUESTION_MESSAGE);
            System.out.println("name entered: " + name);
            
            // Parse input and find first & last name
            if(name != null){
                n = name.split("\\s");
                first = n[0];
                if(n.length < 2)
                    // Continue loop if input is not first && last name
                    continue;
                last = n[1];
            }
            else if(name == null){
                n = null;
                first = null;
                last = null;
            }
            
            try {
                // Connect to DB
                connectDB(1);

                // Do something with the Connection
                // Create Statements
                Statement s = this.conn.createStatement();
                
                // Query Grades DB for Student name to find
                this.resultSet = s.executeQuery("SELECT * FROM " + this.dbName + ".StudentsTbl "
                    + "WHERE FirstName = '" + first + "' && LastName = '" + last + "';");
                
                // Loop through result set if name found in DB
                while(this.resultSet.next()){
                    // Create dialog message to display to user when name found
                    msg = "NAME: " + this.resultSet.getString("FirstName") 
                            + " " + this.resultSet.getString("LastName") + " " 
                            + "AVG: " + String.format("%1$,.2f", this.resultSet.getDouble("Average"))
                            + " STATUS: " + this.resultSet.getString("Status");
                    
                    // Create an instance of a message dialog and display results to user when student found in db
                    JOptionPane.showMessageDialog(null, msg, "Student Found in Grades DB", JOptionPane.INFORMATION_MESSAGE);
                }
                // Close connection to DB
                this.conn.close();
                
            } catch (SQLException ex) {
                // handle any errors
                System.out.println("SQLException: " + ex.getMessage());
                System.out.println("SQLState: " + ex.getSQLState());
                System.out.println("VendorError: " + ex.getErrorCode());
            }   
        }
        System.out.println("Database Connection Closed.");
    }
    
    // Write contents of StudentsTbl in DB to a file (txt)
    // Name    Grade1    Grade2    Grade3    Avg    LetterGrade    Status
    public void writeStudents(){
        // Instantiate new bufferedWriter with an outputstreamWriter
        try (Writer w = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("output.txt"), "utf-8"));){
            
            // instantiate and show save dialog window
            int returnVal = this.chooser.showSaveDialog(null);
            
            if(returnVal == JFileChooser.APPROVE_OPTION){
                // Store path to file
                String path = this.chooser.getSelectedFile().getPath();
                
                // Store filename using path
                File file = new File(path);

                // Notify user of selected file
                System.out.println("You have chosen to Create the following file: " + file);
                
                // Write header to output.txt file
                w.write("Name \t \t Grade1  Grade2  Grade3  Average  Letter  Status");
                w.write("\t \t \t \t \t \t \t \t \t " + " Grade \n");
                
                // connect to db to create a connect to execute statements
                connectDB(1);
                
                // Do something with the Connection
                // Create Statements
                Statement s = this.conn.createStatement();
                String sql;
                
                // Query Grades DB for Student name to find
                this.resultSet = s.executeQuery("SELECT * FROM " + this.dbName + ".StudentsTbl ");
                
                while(resultSet.next()){
                    sql = (resultSet.getString("FirstName") + " " + resultSet.getString("LastName") 
                            + " " + resultSet.getDouble("Grade1") + " \t " + resultSet.getDouble("Grade2") + " \t " + resultSet.getDouble("Grade3") 
                            + " \t " + String.format("%1$,.2f", resultSet.getDouble("Average")) 
                            + " \t " + resultSet.getString("LetterGrade") + " \t " + resultSet.getString("Status") + "\n");
                    
                    w.write(sql);
                }
                // Close db connection
                this.conn.close(); 
            }
        } catch (FileNotFoundException | UnsupportedEncodingException ex) {
            Logger.getLogger(StudentList.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException | SQLException ex) {
            Logger.getLogger(StudentList.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void writeSortedStudents(){
        // Instantiate new bufferedWriter with an outputstreamWriter
        try (Writer w = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("sortedOutput.txt"), "utf-8"));){
            
            // instantiate and show save dialog window
            int returnVal = this.chooser.showSaveDialog(null);
            
            if(returnVal == JFileChooser.APPROVE_OPTION){
                // Store path to file
                String path = this.chooser.getSelectedFile().getPath();
                
                // Store filename using path
                File file = new File(path);

                // Notify user of selected file
                System.out.println("You have chosen to Create the following file: " + file);
                
                // Write header to output.txt file
                w.write("Name \t \t Grade1  Grade2  Grade3  Average  Letter  Status");
                w.write("\t \t \t \t \t \t \t \t \t " + " Grade \n");
                
                // connect to db to create a connect to execute statements
                connectDB(1);
                
                // Do something with the Connection
                // Create Statements
                Statement s = this.conn.createStatement();
                String sql;
                
                // Query Grades DB for Student name to find
                this.resultSet = s.executeQuery("SELECT * FROM " + this.dbName + ".StudentsTbl ORDER BY Average DESC;");
                
                while(resultSet.next()){
                    sql = (resultSet.getString("FirstName") + " " + resultSet.getString("LastName") 
                            + " " + resultSet.getDouble("Grade1") + " \t " + resultSet.getDouble("Grade2") + " \t " + resultSet.getDouble("Grade3") 
                            + " \t " + String.format("%1$,.2f", resultSet.getDouble("Average")) 
                            + " \t " + resultSet.getString("LetterGrade") + " \t " + resultSet.getString("Status") + "\n");
                    
                    w.write(sql);
                }
                // Close db connection
                this.conn.close(); 
            }
        } catch (FileNotFoundException | UnsupportedEncodingException ex) {
            Logger.getLogger(StudentList.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException | SQLException ex) {
            Logger.getLogger(StudentList.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public Connection connectDB(int n){
        try {
            // Load JDBC connection drivers
            Class.forName(this.driver).newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(StudentList.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Driver Loaded...");

        // Establish a connection
        try {
            if(n > 0)
                this.conn = DriverManager.getConnection(this.url + this.dbName, this.user, this.pw);
            else
                this.conn = DriverManager.getConnection(this.url, this.user, this.pw);
            
            
        } catch (SQLException ex) {
            Logger.getLogger(StudentList.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Database Connection Established...");
        
        return this.conn;
    }   
}
