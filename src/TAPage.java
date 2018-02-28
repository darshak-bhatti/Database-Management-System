import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

/**
 * Created by Darshak,Saloni,Veenal,Swati on 10/27/17.
 */
public class TAPage {
    public static void view() throws SQLException{

        Scanner sc=new Scanner(System.in);
        int option;

        do{
            System.out.println("\n1. View Profile");
            System.out.println("\n2. View Courses");
            System.out.println("\n3. Enroll/Drop a Student");
            System.out.println("\n4. View Student details");
            // View exercises are part of view courses. Because they are course specific
            //System.out.println("\n4. View Exercises");
            // TA cannot see question bank
            //System.out.println("\n5. View Question bank");
            System.out.println("\n5. View Report");
            System.out.println("\n6. Go to Student's View");
            System.out.println("\n7. Logout");
            System.out.print("\nYour choice : ");
            option=sc.nextInt();

            switch (option) {
                case 1:
                    viewProfile();
                    break;
                case 2:
                    viewCourseDetails();
                    break;
                case 3:
                    enrollDropStudent();
                    break;
                case 4:
                	viewStudentDetails();
                	break;
                case 5:
                	viewReport();
                	break;
                case 6:
                	StudentPage.view();
                	break;
 	
                case 7:
                    System.out.println("\n Logged out!");
                    return;
                default:
                    System.out.println("\nInvalid Option");
                    break;
            }
        } while (Boolean.TRUE);
    }


    public static void viewProfile(){
        System.out.println("\n View Profile");
        int goBack= 1;
        do {
            System.out.println("\n View Profile");
            QueryResult qr = new QueryResult();
            try {
                System.out.println("Got result" + LoginPage.level);
                qr = Queries.get_profile(LoginPage.uName);
                String name = "";
                while (qr.rs.next()) {
                    System.out.println("Got result");
                    name = qr.rs.getString(1);
                }
                System.out.println("Name : " + name);
                System.out.println("Id :" + LoginPage.uName);
                System.out.println("Press 0 to go back to previous menu");
                Scanner sc = new Scanner(System.in);
                goBack = sc.nextInt();
            }
            catch(SQLException e) {
                System.out.println(e.getMessage());
            }
            finally {
                if(qr!= null)
                    qr.cleanup();
            }
        }while(goBack!= 0);
        return;
    }


    public static void viewCourseDetails() throws SQLException{
    	int goback = 1;
    	QueryResult qrCoursesOfTA = new QueryResult();
    	String taID = LoginPage.uName;
        /*
    	qrCoursesOfTA = TAQueries.get_TACourses(taID);
        List<String> coursesOfTA = new ArrayList<String>();
        while(qrCoursesOfTA.rs.next()) {
        	coursesOfTA.add(qrCoursesOfTA.rs.getString(1));
        }
        */
    	do {
        System.out.println("\n Enter course id");
        // TA can view courses only if he is a TA of that course
        Scanner sc = new Scanner(System.in);
        String course_id = sc.next();
        QueryResult qr1 = new QueryResult();
        QueryResult qr2 = new QueryResult();
        QueryResult qr3 = new QueryResult();
        QueryResult qr4= new QueryResult();
        if(!Queries.isTA(course_id, taID)) {
        	System.out.println("You cannot view courses for which you are not a TA");
        	continue;
        }
        try {
            
            qr1= Queries.get_courseName(course_id);
            String courseName = "";
            System.out.println("Got Course name");
            if(qr1.rs.next()){
                courseName = qr1.rs.getString(1);
                System.out.println("CourseName : " + courseName);
            }
            else {
            	System.out.println("No course with this id");
            	return;
            }
            
            qr2 = Queries.get_TADuration(course_id);
            String taID1 = "";
            String taName = "";
            Date startDate = new Date();
            Date endDate = new Date();
            while (qr2.rs.next()) {
                System.out.println("Duration and TAs :");
                taID1 = qr2.rs.getString(1);
                taName = qr2.rs.getString(2);
                startDate = qr2.rs.getDate(3);
                endDate = qr2.rs.getDate(4);
                System.out.println("TA id "+ taID1);
                System.out.println("TA name "+ taName);
                System.out.println("Start date "+ startDate);
                System.out.println("End date "+ endDate);
            }
            int hwID = 0;
            String hwName = "";
            String deadline = "";
            qr3 = Queries.get_hwExerciseFromCourse(course_id);
            while (qr3.rs.next()) {
            	System.out.println("Homework information :");
            	hwID = qr3.rs.getInt(1);
            	hwName = qr3.rs.getString(2);
            	deadline = qr3.rs.getString(3).toString();
            	System.out.println("Homework ID "+ hwID);
            	System.out.println("Homework Name "+ hwName);
            	System.out.println("Homework Deadline "+ deadline);
            }
            
            
            qr4 = Queries.get_report(course_id);

            while (qr4.rs.next()) {
               System.out.println("Report : ");
               System.out.println("Student ID "+ qr4.rs.getString("sID"));
               System.out.println("Student Name "+ qr4.rs.getString("name"));
               System.out.println("Homework ID "+ qr4.rs.getString("hID"));
               System.out.println("Homework Attempt "+ qr4.rs.getString("attNo"));
               System.out.println("Score "+ qr4.rs.getString("score"));
            }


            System.out.println("Press 0 to go back to previous menu or 1 to view more courses");
            Scanner sc2 = new Scanner(System.in);
            goback = sc2.nextInt();
        }

        catch(SQLException e) {
            System.out.println(e.getMessage());
        }
        finally {

            if(qr1!= null)
                qr1.cleanup();
            if(qr2!= null)
                qr2.cleanup();
            if(qr3!= null)
                qr3.cleanup();
            if(qr4 != null)
                qr4.cleanup();
               
        }
    }while(goback!= 0);
    return;
}

    public static void enrollDropStudent() throws SQLException{
    	        System.out.println("\n Enroll/Drop a Student");
    	        int goback=1;
    	        do{
    	            System.out.println("\n 1. Enroll a Student");
    	            System.out.println("\n 2. Drop Student");
    	            System.out.println("\n Press 0 to go back to previous menu");
    	            Scanner sc = new Scanner(System.in);
    	            goback = sc.nextInt();
    	            switch(goback)
    	            {
    	                case 0:
    	                    break;
    	                case 1:
    	                    enrollStudent();
    	                    break;
    	                case 2:
    	                    System.out.println("\n Give course id");
    	                    sc = new Scanner(System.in);
    	                    String cid = sc.next();
    	                    System.out.println("\n Give Student ID");
    	                    sc = new Scanner(System.in);
    	                    String studentid = sc.next();
    	                    int ret1=0;
    	                    try {
    	                        ret1=Queries.drop_student(cid, studentid);
    	                    }
    	                    catch (SQLException e) {
    	                        System.out.print("\n ERROR : ");
    	                        System.out.println(e.getMessage());
    	                    }
    	                    if(ret1!=0)
    	                        System.out.println("Student dropped successfully!");

    	                    break;
    	            }
    	        }while(goback!=0);

    	    }
    
    public static void enrollStudent() throws SQLException{
        System.out.println("\n Add course id");
        Scanner sc = new Scanner(System.in);
        String course_id = sc.next();
        String taID = LoginPage.uName;
        if(!Queries.isTA(course_id, taID)){
        	System.out.println("Cannot enroll students for this course as you are not the TA");
        	return;
        }
        System.out.println("\n Add student ids separated by comma");
        sc.nextLine();
        String sids = sc.nextLine();
        System.out.println("\n Add start date in format : yyyy-mm-dd");
        //sc.nextLine();
        String start_date = sc.nextLine();
        System.out.println("\n Add end date in format : : yyyy-mm-dd");
        //sc.nextLine();
        String end_date = sc.nextLine();
    

        try {
            int insertStudent = Queries.insert_enroll(course_id, sids, start_date, end_date);
            
            if(insertStudent == 1)
            	System.out.println("Student enrolled successfully ");
        }
        catch (SQLException e) {
            System.out.print("\n ERROR : ");
            System.out.println(e.getMessage());
        }

    }
    
    public static void dropStudent() throws SQLException {
    	System.out.println("\n Give course id");
        Scanner sc = new Scanner(System.in);
        String taID = LoginPage.uName;
        
        String cid = sc.next();
        if(!Queries.isTA(cid, taID)){
        	System.out.println("Cannot drop students for this course as you are not the TA");
        }
        System.out.println("\n Give Student ID");
        sc = new Scanner(System.in);
        String studentid = sc.next();
        int ret1=0;
        try {
            ret1=Queries.drop_student(cid, studentid);
        }
        catch (SQLException e) {
            System.out.print("\n ERROR : ");
            System.out.println(e.getMessage());
        }
    }

    public static void viewStudentDetails() throws SQLException {
        int goback = 1;
        QueryResult qrCoursesOfTA = new QueryResult();
        String taID = LoginPage.uName;

        do {
            System.out.println("\n Enter course id");
            // TA can view courses only if he is a TA of that course
            Scanner sc = new Scanner(System.in);
            String course_id = sc.next();
            sc.nextLine();
            QueryResult qr1 = new QueryResult();

            if (!Queries.isTA(course_id, taID)) {
                System.out.println("You cannot view student details since you are not TA for this course");
                continue;
            }
            String sID="";
            try {
            	do{
            		System.out.println("\n Enter student id");
                    sID = sc.nextLine();
            	}while((!Queries.isStudentEnrolled(course_id, sID)));

                qr1 = Queries.get_reportForStudent(course_id, sID);

                while (qr1.rs.next()) {
                    System.out.println("Report : ");
                    System.out.println("Student ID " + qr1.rs.getString("sID"));
                    System.out.println("Student Name " + qr1.rs.getString("name"));
                    System.out.println("Homework ID " + qr1.rs.getString("hID"));
                    System.out.println("Homework Attempt " + qr1.rs.getString("attNo"));
                    System.out.println("Score " + qr1.rs.getString("score"));
                }


                System.out.println("Press 0 to go back to previous menu or 1 to view more courses");
                Scanner sc2 = new Scanner(System.in);
                goback = sc2.nextInt();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            } finally {

                if (qr1 != null)
                    qr1.cleanup();

            }
        } while (goback != 0);
        return;
    }
    
    public static void viewReport() throws SQLException {
        int goback = 1;
        
        String taID = LoginPage.uName;

        do {
            System.out.println("\n Enter course id");
            // TA can view student details only if he is the instructor of that course
            Scanner sc = new Scanner(System.in);
            String course_id = sc.next();
            QueryResult qr1 = new QueryResult();
            

            if (!Queries.isTA(course_id, taID)) {
                System.out.println("You cannot view student details since you are not TA for this course");
                continue;
            }
            
           
            try {
            	
            	
                qr1 = Queries.get_report(course_id);

                while (qr1.rs.next()) {
                    System.out.println("Report : ");
                    System.out.println("Student ID " + qr1.rs.getString("sID"));
                    System.out.println("Student Name " + qr1.rs.getString("name"));
                    System.out.println("Homework ID " + qr1.rs.getString("hID"));
                    System.out.println("Homework Attempt " + qr1.rs.getString("attNo"));
                    System.out.println("Score " + qr1.rs.getString("score"));
                }


                System.out.println("Press 0 to go back to previous menu or 1 to view more courses");
                Scanner sc2 = new Scanner(System.in);
                goback = sc2.nextInt();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            } finally {

                if (qr1 != null)
                    qr1.cleanup();

            }
        } while (goback != 0);
        return;
    }
}
