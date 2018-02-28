import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.Scanner;

/**
 * Created by Darshak,Saloni,Veenal,Swati on 10/27/17.
 */
public class InstructorPage {

    public static void view() throws SQLException{

        Scanner sc=new Scanner(System.in);
        int option;
        do{
            System.out.println("\n1. View Profile");
            System.out.println("\n2. View/Add Courses");
            System.out.println("\n3. Enroll/Drop a Student ");
            System.out.println("\n4. Search/Add Questions to Question Bank");
            System.out.println("\n5. Setup TA");
            System.out.println("\n6. Add Exercises");
            System.out.println("\n7. View Student Details");
            System.out.println("\n8. View Report");
            System.out.println("\n9. Logout");
            System.out.print("\nYour choice : ");
            option=sc.nextInt();

            switch (option) {
                case 1:
                    viewProfile();
                    break;
                case 2:
                    viewAddCourse();
                    break;
                case 3:
                    enrollDropStudent();
                    break;
                case 4:
                    System.out.println("\n Question list:");
                    try {
                        QueryResult qr = Queries.get_questions();
                        while (qr.rs.next()) {
                            System.out.println(qr.rs.getString("qID")+" "+qr.rs.getString("question"));
                        }
                    }catch (SQLException e) {
                        System.out.print("\n ERROR : ");
                        System.out.println(e.getMessage());
                    }
                    searchAddQuestion();
                    break;
                case 5:
                    setupTA();
                    break;
                case 6:
                    addExercise();
                    break;
                case 7:
                    viewStudentDetails();
                	break;
                case 8:
                    viewReport();
                	break;
                case 9:
                    System.out.println("\n Logged out!");
                    return;
                default:
                    System.out.println("\nInvalid Option");
                    break;
            }
        } while (Boolean.TRUE);

    }

    public static void viewProfile() {
        int goBack= 1;
        do {
            System.out.println("\n View Profile");
            QueryResult qr = new QueryResult();
            try {
                qr = Queries.get_profile(LoginPage.uName);
                String name = "";
                while (qr.rs.next()) {
                    name = qr.rs.getString(1);
                }
                System.out.println("Name : " + name);
                System.out.println("Id :" + LoginPage.uName);
                System.out.println("Press 0 to go back to previous menu");
                Scanner sc = new Scanner(System.in);
                goBack = sc.nextInt();
            }
            catch(SQLException e) {
                System.out.print("\n ERROR : ");
                System.out.println(e.getMessage());
            }
            finally {
                if(qr!= null)
                    qr.cleanup();
            }
        }while(goBack!= 0);
        return;
    }

    public static void viewAddCourse(){
        do {
        System.out.println("\n1.View Course Details");
        System.out.println("\n2.Add Course");
        System.out.println("Press 0 to go back to previous menu");
        System.out.print("\nYour choice : ");
        Scanner sc = new Scanner(System.in);
        int choice = sc.nextInt();

            if (choice == 1) {
                viewCourseDetails();
            } else if (choice == 2) {
                addCourseDetails();
            } else if (choice == 0) {
                return;
            }
        }while(true);
    }
   
    public static void addCourseDetails(){
        System.out.println("\n Add course id");
        Scanner sc = new Scanner(System.in);
        String course_id = sc.next();
        System.out.println("\n Add course name");
        sc.nextLine();
        String course_name = sc.nextLine();
        System.out.println("\n Add start date in format : yyyy-mm-dd");
        //sc.nextLine();
        String start_date = sc.nextLine();
        System.out.println("\n Add end date in format : : yyyy-mm-dd");
        //sc.nextLine();
        String end_date = sc.nextLine();
        String p_id = LoginPage.uName;
        try {
            int insertCourse = Queries.insert_course(course_id,course_name);
            int insertDuration = Queries.insert_duration(start_date, end_date);
            int insertTaught = Queries.insert_taught(course_id, p_id, start_date, end_date);
            if(insertDuration == 1 && insertCourse == 1 && insertTaught == 1)
            System.out.println("Row inserted successfully ");
        }
        catch (SQLException e) {
            System.out.print("\n ERROR : ");
            System.out.println(e.getMessage());
        }
        System.out.println("\n Press 1 to add TA or press 0 to continue");
        int addTa = sc.nextInt();
        while(addTa==1){
            try{
                System.out.println("\n Add student id");
                sc.nextLine();
                String sid = sc.nextLine();
                int res=Queries.insert_TA(course_id,sid,start_date,end_date);
                if(res==1)
                    System.out.println("TA Added Successfully!");
                System.out.println("\n Press 1 to add  another TA or press 0 to continue");
                addTa = sc.nextInt();
            }
            catch (SQLException e) {
                System.out.print("\n ERROR : ");
                System.out.println(e.getMessage());
            }
        }
        System.out.println("\n Press 1 to enroll students or press 0 to continue");
        int addst = sc.nextInt();
        int flag=0;
        while(addTa==1 && flag!=0){
            try{
                System.out.println("\n Add student id");
                sc.nextLine();
                String sid = sc.nextLine();
                int res=Queries.insert_enroll(course_id,sid,start_date,end_date);
                if(res==1)
                {
                    System.out.println("Student Enrolled Successfully!");
                    System.out.println("\n Press 1 to add  another student or press 0 to continue");
                    addTa = sc.nextInt();
                }
            }
            catch (SQLException e) {
                flag=1;
                System.out.println("Something went wrong. Re-enter the details");
            }
        }

        System.out.println("\n Press 1 to add exercise or press 0 to exit");
        int addExer = sc.nextInt();
        if(addExer==1){
            addExerciseFromAddCourse(course_id);
        }
    }
    /*
    public static void viewReport() {
    	int goback = 1;
    	do {
    		QueryResult qr = new QueryResult();
    		qr = Queries.get_report(course_id);
            
            while (qr.rs.next()) {
               System.out.println("Report : ");
               System.out.println("Student ID "+ qr.rs.getString("sID"));
               System.out.println("Student Name "+ qr.rs.getString("name"));
               System.out.println("Homework ID "+ qr.rs.getString("hID"));
               System.out.println("Homework Attempt "+ qr.rs.getString("attNo"));
               System.out.println("Score "+ qr.rs.getString("score"));
            }
    	}
    }*/
    
    public static void viewCourseDetails(){
    	int goback = 1;
    	do {
        System.out.println("\n Enter course id");
        Scanner sc = new Scanner(System.in);
        String course_id = sc.next();
        QueryResult qr1 = new QueryResult();
        QueryResult qr2 = new QueryResult();
        QueryResult qr3 = new QueryResult();
        QueryResult qr4 = new QueryResult();
        QueryResult qr5 = new QueryResult();
        try {
            
            qr1= Queries.get_courseName(course_id);
            String courseName = "";
           
            if(qr1.rs.next()){
                courseName = qr1.rs.getString(1);
                System.out.println("CourseName : " + courseName);
            }
            else {
            	System.out.println("No course with this id");
            	return;
            }
            
            qr2 = Queries.get_TADuration(course_id);
            String taID = "";
            String taName = "";
            Date startDate = new Date();
            Date endDate = new Date();
            while (qr2.rs.next()) {
                System.out.println("Duration and TAs :");
                taID = qr2.rs.getString(1);
                taName = qr2.rs.getString(2);
                startDate = qr2.rs.getDate(3);
                endDate = qr2.rs.getDate(4);
                System.out.println("TA id "+ taID);
                System.out.println("TA name "+ taName);
                System.out.println("Start date "+ startDate);
                System.out.println("End date "+ endDate);
            }
            int hwID = 0;
            String hwName = "";
            String deadline = "";
            int points=0;
            int penaltyPoints=0;
            //H.noQs, H.noAs, H.startDate, H.ttype, H.scoringPolicy, H.tID
            int noQs =0;
            int noAs = 0;
            String startDateHW = "";
            String ttype ="";
            String scoringPolicy="";
            int tID = 0;
            qr3 = Queries.get_hwExerciseFromCourse(course_id);
            while (qr3.rs.next()) {
            	System.out.println("Homework information :");
            	hwID = qr3.rs.getInt(1);
            	hwName = qr3.rs.getString(2);
            	System.out.println("Homework ID "+ hwID);
            	System.out.println("Homework Name "+ hwName);
            	
            }
            int continueViewCourse = 1;
            do {
            System.out.println("Enter the homework id for which you wish to see the detailed information");
            int input_hwid = sc.nextInt();
            qr5 = Queries.get_hwExerciseFromHWID(input_hwid);
            while (qr5.rs.next()) {
            	System.out.println("Homework information :");
            	hwID = qr5.rs.getInt(1);
            	hwName = qr5.rs.getString(2);
            	deadline = qr5.rs.getString(3).toString();
            	points = qr5.rs.getInt(4);
            	penaltyPoints = qr5.rs.getInt(5);
            	noQs =  qr5.rs.getInt(6);
            	noAs =  qr5.rs.getInt(7);
            	startDateHW = qr5.rs.getString(8).toString();
            	ttype = qr5.rs.getString(9);
            	scoringPolicy = qr5.rs.getString(10);
            	tID = qr5.rs.getInt(11);
            	
            	
            	
            	System.out.println("Homework ID "+ hwID);
            	System.out.println("Homework Name "+ hwName);
            	System.out.println("Homework Deadline "+ deadline);
            	System.out.println("Homework points "+ points);
            	System.out.println("Homework penalty points "+ penaltyPoints);
            	System.out.println("Number of Questions "+ noQs);
            	System.out.println("Number of Attempts "+ noAs);
            	System.out.println("Start date time "+ startDateHW);
            	System.out.println("Type of homework "+ ttype);
            	System.out.println("Scoring Policy "+ scoringPolicy);
            	System.out.println("Topic id "+ tID);
            	
            	System.out.println("To go back to the rest of course details, press 0");
            	System.out.println("To view more homeworks, press 1");
            	if(ttype.equals("standard"))
            	{
            		System.out.println("To add question to the homework, press 2");
                	System.out.println("To remove questions from the homework, press 3");	
            	}
            	
            	
            	continueViewCourse = sc.nextInt();
            	switch(continueViewCourse) {
            		case 0:break;
            		case 1:break;
            		case 2: if(ttype.equals("adaptive"))
            				{
            					System.out.println("Invalid option");
            					break;
            				}
            				else
            				{
            					addQuestionToHw(hwID, tID);
                				break;	
            				}
            				
            		case 3: if(ttype.equals("adaptive"))
    						{
    							System.out.println("Invalid option");
    							break;
    						}
    						else
    						{
    							deleteQuestionFromHw(hwID, tID);
                				break;	
    						}
            				
            	}
	
            }
            }while(continueViewCourse ==1 );

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
            if(qr4!= null)
            	qr4.cleanup();
            if(qr5!= null)
            	qr5.cleanup();
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
                    if(!Queries.isInstructor(cid)){
                    	System.out.println("Cannot enroll students for this course as you are not the instructor");
                    	break;
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
                    if(ret1!=0)
                        System.out.println("Student dropped successfully!");

                    break;
            }
        }while(goback!=0);

    }
    public static void addquest(){
        String hint="";
        int qid=0;
        System.out.println("\n Adding a Question:");
        System.out.println("\n Enter Question Text");
        Scanner sc = new Scanner(System.in);
        String question = sc.nextLine();
        System.out.println("\n Enter Topic ID");
        sc = new Scanner(System.in);
        int topic = sc.nextInt();
        System.out.println("\n Enter Course ID");
        sc = new Scanner(System.in);
        String cid = sc.nextLine();
        System.out.println("\n Enter Difficulty");
        sc = new Scanner(System.in);
        int difficulty = sc.nextInt();
        System.out.println("\n Enter Solution");
        sc.nextLine();
        String solution = sc.nextLine();
        System.out.println("\n Press 1 if you want to add a Hint");
        int ch = sc.nextInt();
        if(ch==1){
            sc.nextLine();
            hint = sc.nextLine();
        }
        int retq=0;
        try {
            qid=Queries.insert_question(question, difficulty, solution, hint);
            retq=Queries.insert_QTC(qid,topic,cid);
        }
        catch (SQLException e) {
            System.out.print("\n ERROR : ");
            System.out.println(e.getMessage());
        }
        if(retq==1)
        { System.out.println("Question inserted successfully!");
        addans(qid);}
        else
            System.out.println("Failed to enter question!");
    }
    public static void addans(int qid){
        int ch=0,a=0;
        do {
            System.out.println("\n1. Question is fixed");
            System.out.println("\n2. Question is parametrized");
            System.out.print("\nYour choice : ");
            Scanner sc = new Scanner(System.in);
            ch=sc.nextInt();
        }while(ch!=1 && ch!=2);
        if(ch==1) {
            insertAllAnswer(qid,0,1);
        }
        if(ch==2){
            do{
                int pid=0;
                System.out.println("\n Enter number of parameters");
                Scanner sc = new Scanner(System.in);
                int np=sc.nextInt();
                String param[]=new String[5];
                int i=0;
                for(i=0;i<np;i++){
                    System.out.println("\n Parameter "+(i+1)+": ");
                    sc = new Scanner(System.in);
                    param[i] = sc.nextLine();
                }
                while(i<5)
                {
                    param[i]="";
                    i++;
                }
                try {
                   pid= Queries.insert_parameters(param);
                } catch (SQLException e) {
                    System.out.print("\n ERROR : ");
                    System.out.println(e.getMessage());
                }
                insertAllAnswer(qid,pid,ch);
                System.out.println("\n0. Go back to previous menu");
                System.out.println("\n1. Insert another parameter set or answer");
                System.out.print("\nYour choice : ");
                sc = new Scanner(System.in);
                ch=sc.nextInt();
            }while(ch==1);
        }
    }
    public static void insertAllAnswer(int qid,int pid,int fixed){
        int a=0;
        do {
            System.out.println("\n Insert correct answer for question inserted");
            int aid=insertSingleAnswer();
            try {
                if(fixed==1)
                Queries.insert_fixed(qid, aid, "Y");
                if(fixed==2)
                    Queries.insert_param(qid, aid, pid,"Y");
            } catch (SQLException e) {
                System.out.print("\n ERROR : ");
                System.out.println(e.getMessage());
            }
            System.out.print("\nPress 1 to insert another correct answer : ");
            Scanner sc = new Scanner(System.in);
            a=sc.nextInt();
        }while(a==1);
        do {
            System.out.println("\n Insert incorrect answer for question inserted");
            int aid=insertSingleAnswer();
            try {
                if(fixed==1)
                    Queries.insert_fixed(qid, aid, "N");
                if(fixed==2)
                    Queries.insert_param(qid, aid, pid,"N");
            }
            catch (SQLException e) {
                System.out.print("\n ERROR : ");
                System.out.println(e.getMessage());
            }
            System.out.print("\nPress 1 to insert another incorrect answer : ");
            Scanner sc = new Scanner(System.in);
            a=sc.nextInt();
        }while(a==1);
    }
    public static int insertSingleAnswer(){
        int aid=0;
        System.out.println("\n Enter Answer text");
        Scanner sc = new Scanner(System.in);
        String ans = sc.nextLine();
        try {
            aid=Queries.insert_answer(ans);
        } catch (SQLException e) {
            System.out.print("\n ERROR : ");
            System.out.println(e.getMessage());
        }
        return aid;
    }

    
    public static void enrollStudent() throws SQLException{
        System.out.println("\n Add course id");
        Scanner sc = new Scanner(System.in);
        String course_id = sc.next();
        if(!Queries.isInstructor(course_id)){
        	System.out.println("Cannot enroll students for this course as you are not the instructor");
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
        
    public static void searchAddQuestion(){

        System.out.println("\n Search/Add Questions to Question Bank");
        int goback=1;
        do{
            System.out.println("\n 1. Search Question");
            System.out.println("\n 2. Add Question");
            System.out.println("\n Press 0 to go back to previous menu");
            Scanner sc = new Scanner(System.in);
            goback = sc.nextInt();
            switch(goback)
            {
                case 2:
                   addquest();
                    break;
                case 1:
                    int opt=0;
                    do {
                            System.out.println("\n 1. Search By Question ID");
                            System.out.println("\n 2. Search By Topic ID");
                            System.out.println("\n 3. Add new question");
                            System.out.println("\n Press 0 to go back to previous menu");
                            System.out.print("\nYour choice : ");
                            sc = new Scanner(System.in);
                            opt=sc.nextInt();
                            switch(opt) {
                                case 1:
                                System.out.println("\n Give question id");
                                sc = new Scanner(System.in);
                                int qid = sc.nextInt();
                                QueryResult qr;
                                try {
                                    qr = Queries.view_question(qid);
                                    while(qr.rs.next()) {
                                        String question = qr.rs.getString(2);
                                        System.out.println("Question text: " + question);
                                        System.out.println("Difficulty level: " + qr.rs.getString(3));
                                        System.out.println("Hint(optional): " + qr.rs.getString(4));
                                        System.out.println("Solution: " + qr.rs.getString(5));
                                    }
                                } catch (SQLException e) {
                                    System.out.print("\n ERROR : ");
                                    System.out.println(e.getMessage());
                                }
                                break;
                                case 2:
                                    System.out.println("\n Give Topic ID");
                                    sc = new Scanner(System.in);
                                    int tid = sc.nextInt();
                                    System.out.println("\n Give Course ID");
                                    sc = new Scanner(System.in);
                                    String cid = sc.next();
                                    try {
                                        qr = Queries.view_qbTopic(tid,cid);
                                        if(qr != null) {
                                            System.out.println("\n------Question Details:-------");
                                            if(qr.rs != null) {
                                                while (qr.rs.next()) {
                                                    String question = qr.rs.getString(2);
                                                    System.out.println("Question text: " + question);
                                                    System.out.println("Difficulty level: " + qr.rs.getString(3));
                                                    System.out.println("Hint(optional): " + qr.rs.getString(4));
                                                    System.out.println("Solution: " + qr.rs.getString(5));
                                                }
                                            }
                                        }
                                    } catch (SQLException e) {
                                        System.out.print("\n ERROR : ");
                                        System.out.println(e.getMessage());
                                    }
                                    break;
                                case 3:
                                    addquest();
                                    break;
                            }
                        }while(opt!=0);
                    break;
            }
        }while(goback!=0);
    }
    public static void setupTA(){
        System.out.println("\n Setup TA");
        System.out.println("\n Add Course id");
        Scanner sc = new Scanner(System.in);
        String course_id = sc.next();
        System.out.println("\n Add Student id");
        sc.nextLine();
        String sid = sc.nextLine();
        System.out.println("\n Add start date in format : yyyy-mm-dd");
        //sc.nextLine();
        String start_date = sc.nextLine();
        System.out.println("\n Add end date in format : : yyyy-mm-dd");
        //sc.nextLine();
        String end_date = sc.nextLine();
        try{
            int res=Queries.insert_TA(course_id,sid,start_date,end_date);
        }
        catch (SQLException e) {
            System.out.print("\n ERROR : ");
            System.out.println(e.getMessage());
        }

    }
    public static void addExercise(){
        int goback=1;
        do{
        	int rethw = 0;
            
            System.out.println("\n Adding a Homework Exercise :");
            Scanner sc = new Scanner(System.in);
            /*
            System.out.println("\n Enter Homework ID");
            
            int hID = sc.nextInt();
            sc.nextLine();
            */
            System.out.println("\n Enter Homework Name");
            String hwName = sc.nextLine();
            System.out.println("\n Enter Course id");
            String cID = sc.nextLine();
            System.out.println("\n Enter Deadline in format : yyyy-mm-dd hh:mm:ss");
            String deadlineString = sc.nextLine();
            System.out.println("Enter points for each question");
            int points = sc.nextInt();
            System.out.println("Enter penalty points for each question (negative)");
            int penaltyPoints = sc.nextInt(); 
            System.out.println("Enter number of questions");
            int noQs = sc.nextInt();
            System.out.println("Enter number of attempts allowed");
            int noAs = sc.nextInt();
            sc.nextLine();
            System.out.println("\n Enter Start date in format : yyyy-mm-dd hh:mm:ss");
            String startTimeString = sc.nextLine();
           //sc.nextLine();
            System.out.println("\n Enter Test type : standard/adaptive");
            String testType = sc.nextLine();
            System.out.println("\n Enter Scoring policy : highest/latest/average");
            String scoringPolicy = sc.nextLine();
            System.out.println("\n Enter topic id");
            int tID = sc.nextInt();
            try {
                rethw=Queries.insert_hw(hwName, cID, deadlineString, points, penaltyPoints, noQs,noAs, startTimeString, testType, scoringPolicy, tID);
                
            }
            catch (SQLException e) {
                System.out.print("\n ERROR : ");
                System.out.println(e.getMessage());
            }
            if(rethw!= 0) {
                System.out.println("Homework inserted successfully with id !"+ rethw);
            	
            }
            else {
            	System.out.println("Homework insert unsuccessful!");
            }
            System.out.print("\nPress 0 to go back to previous menu ");
        	sc = new Scanner(System.in);
        	goback = sc.nextInt();
            
           
        }while(goback!=0);
        return;
    }
    
    
    public static void addExerciseFromAddCourse(String cID){
        int goback=1;
        do{
        	int rethw = 0;
            
            System.out.println("\n Adding a Homework Exercise :");
           // System.out.println("\n Enter Homework ID");
            Scanner sc = new Scanner(System.in);
            
            System.out.println("\n Enter Homework Name");
            String hwName = sc.nextLine();
            
            System.out.println("\n Enter Deadline in format : yyyy-mm-dd hh:mm:ss");
            String deadlineString = sc.nextLine();
            System.out.println("Enter points for each question");
            int points = sc.nextInt();
            System.out.println("Enter penalty points for each question (negative)");
            int penaltyPoints = sc.nextInt();
            
            System.out.println("Enter number of questions");
            int noQs = sc.nextInt();
            System.out.println("Enter number of attempts allowed");
            int noAs = sc.nextInt();
            sc.nextLine();
            System.out.println("\n Enter Start date in format : yyyy-mm-dd hh:mm:ss");
            String startTimeString = sc.nextLine();
            System.out.println("\n Enter Test type : standard/adaptive");
            String testType = sc.nextLine();
            System.out.println("\n Enter Scoring policy : highes/latest/average");
            String scoringPolicy = sc.nextLine();
            System.out.println("\n Enter topic id");
            int tID = sc.nextInt();
            try {
                rethw=Queries.insert_hw(hwName, cID, deadlineString, points, penaltyPoints, noQs,noAs, startTimeString, testType, scoringPolicy, tID);
                
            }
            catch (SQLException e) {
                System.out.print("\n ERROR : ");
                System.out.println(e.getMessage());
            }
            if(rethw!= 0) {
                System.out.println("Homework inserted successfully!");
            	System.out.print("\nPress 0 to go back to previous menu ");
            	sc = new Scanner(System.in);
            }
            else {
            	System.out.println("Homework insert unsuccessful!");
            }
            
           
        }while(goback!=0);
        return;
    }
    
  
    public static void viewHomework() {
    	/*
    }
    	int goback = 1;
    	do {
        System.out.println("\n Enter homework id");
        Scanner sc = new Scanner(System.in);
        String course_id = sc.next();
        QueryResult qr1 = new QueryResult();
        QueryResult qr2 = new QueryResult();
        QueryResult qr3 = new QueryResult();
        QueryResult qr4 = new QueryResult();
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
    	
    }*/
    	}
    
    
    public static void addQuestionToHw(int hwID, int tID) {
    	int goback=1;
    	Scanner sc = new Scanner(System.in);
    	System.out.println("\n Displaying questions related to the topic id of this homework :");
    	QueryResult qr = new QueryResult();
        try {
            qr = Queries.get_questions_from_tid(tID);
           
            int questionID = 0;
            String questionName ="";
            while (qr.rs.next()) {
                 questionID = qr.rs.getInt(1);
                 questionName = qr.rs.getString(2);
                 System.out.println("Question id :"+ questionID + "Question :"+ questionName);
            }
        }
            catch (SQLException e) {
                System.out.print("\n ERROR : ");
                System.out.println(e.getMessage());
            }
    	
        do{
        	int retq = 0;
            System.out.println("Enter the question id you wish to add in this homework");
            int qID = sc.nextInt();
            try {
                retq=Queries.addQuestion(hwID, qID);
                
            }
            catch (SQLException e) {
                System.out.print("\n ERROR : ");
                System.out.println(e.getMessage());
            }
            if(retq==1) {
                System.out.println("Question inserted successfully in the exercise!");
            	System.out.print("\nPress 0 to go back to previous menu ");
            	goback = sc.nextInt();
            }
            else {
            	System.out.println("Question insert unsuccessful!");
            }
            
           
        }while(goback!=0);
        return;
    }
    
    
    public static void deleteQuestionFromHw(int hwID, int tID) {
    	int goback=1;
    	Scanner sc = new Scanner(System.in);
    	System.out.println("\n Displaying questions related to the topic id of this homework :");
    	QueryResult qr = new QueryResult();
        try {
            qr = Queries.get_questions_from_tid(tID);
           
            int questionID = 0;
            String questionName ="";
            while (qr.rs.next()) {
                 questionID = qr.rs.getInt(1);
                 questionName = qr.rs.getString(2);
                 System.out.println("Question id :"+ questionID + "Question :"+ questionName);
            }
        }
            catch (SQLException e) {
                System.out.print("\n ERROR : ");
                System.out.println(e.getMessage());
            }
    	
        do{
        	int retq = 0;
            System.out.println("Enter the question id you wish delete this homework");
            int qID = sc.nextInt();
            try {
                retq=Queries.removeQuestion(hwID, qID);
                
            }
            catch (SQLException e) {
                System.out.print("\n ERROR : ");
                System.out.println(e.getMessage());
            }
            if(retq==1) {
                System.out.println("Question removed from the exercise!");
            	System.out.print("\nPress 0 to go back to previous menu ");
            	goback = sc.nextInt();
            }
            else {
            	System.out.println("Question delete unsuccessful!");
            }
            
           
        }while(goback!=0);
        return;
    }
    
    public static void viewStudentDetails() throws SQLException {
        int goback = 1;
        
        String iID = LoginPage.uName;

        do {
            System.out.println("\n Enter course id");
            // Instructor can view student details only if he is the instructor of that course
            Scanner sc = new Scanner(System.in);
            String course_id = sc.next();
            sc.nextLine();
            QueryResult qr1 = new QueryResult();
            QueryResult qr2 = new QueryResult();

            if (!Queries.isInstructor(course_id, iID)) {
                System.out.println("You cannot view student details since you are not Prof for this course");
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
        
        String iID = LoginPage.uName;

        do {
            System.out.println("\n Enter course id");
            // Instructor can view student details only if he is the instructor of that course
            Scanner sc = new Scanner(System.in);
            String course_id = sc.next();
            QueryResult qr1 = new QueryResult();
            

            if (!Queries.isInstructor(course_id, iID)) {
                System.out.println("You cannot view student details since you are not Prof for this course");
                continue;
            }
            
            String sID="";
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
        
