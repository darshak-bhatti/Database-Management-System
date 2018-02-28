import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import javax.management.Query;
import java.sql.SQLException;
import java.util.Scanner;

/**
 * Created by Darshak,Saloni,Veenal,Swati on 10/27/17.
 */
public class StudentPage {
    static Scanner sc=new Scanner(System.in);

    public static void view(){

        int option;

        do{
            System.out.println("\n1. View/Edit Profile");
            System.out.println("\n2. View Courses");
            System.out.println("\n3. Log Out ");
            System.out.print("\nYour choice : ");
            option=sc.nextInt();

            switch (option) {
                case 1:
                    viewEditProfile();
                    break;
                case 2:
                    viewCourses();
                    break;
                case 3:
                    System.out.println("\n Logged out!");
                    return;
                default:
                    System.out.println("\nInvalid Option");
                    break;
            }
        } while (Boolean.TRUE);

    }

    private static void viewEditProfile(){
        System.out.println("\n View/Edit Profile");
        QueryResult qr = null;

        try{
            qr = StudentQueries.get_profile();
            while(qr.rs.next()){
                System.out.println("Name : " + qr.rs.getString("name"));
                System.out.println("Student ID : " + qr.rs.getString("sID"));
            }
            System.out.println("Press 0 to Go Back To Previous Menu");
            int option;

            do{
                option = sc.nextInt();
                if(option == 0){
                    return;
                }
                System.out.println("Press 0 to Go Back To Previous Menu");
            } while(Boolean.TRUE);

        } catch (SQLException e){
            e.printStackTrace();
        } finally {
            if(qr!= null){
                qr.cleanup();
            }
        }


    }


    private static void viewCourses(){
        System.out.println("\n List of your current courses");
        QueryResult qr = null;

        try{
            qr = StudentQueries.get_courses();
            int i = 1;
            //Print the list of courses
            while(qr.rs.next()){
                System.out.println(i +". " + qr.rs.getString("cID") + " " + qr.rs.getString("cName"));
                i++;
            }
            System.out.println("Press 0 to Go Back To Previous Menu");
            System.out.print("Please provide CourseID: ");
            String option = sc.next();

            if(option.equals("0")){
                return;
            }
            else{
                //TO DO: Check for invalid CourseID

                //Display the course details
                displayCourse(option);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            if(qr!= null){
                qr.cleanup();
            }
        }
    }

    private static void courseDetails(String courseID) {
        QueryResult qr = null;
        QueryResult taResult = null;

        try{
            qr = StudentQueries.course_details(courseID);

            while(qr.rs.next()){
                taResult = null;
                System.out.println("Course ID : " + qr.rs.getString("cID"));
                System.out.println("Course name : " + qr.rs.getString("cName"));
                System.out.println("Professor : " + qr.rs.getString("pName"));
                System.out.println("TAs for the course :");

                taResult = StudentQueries.TAs(courseID);

                while (taResult.rs.next()){
                    System.out.println(taResult.rs.getString("name"));
                }

                if(taResult != null){
                    taResult.cleanup();
                }

            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        finally {
            if(qr != null){
                qr.cleanup();
            }
        }
    }

    private static void displayCourse(String courseID){
        //Call Queries.get_courses(courseID) for Course Details
        System.out.println("Course Details");
        courseDetails(courseID);

        System.out.println("1. Current Open HWs");
        System.out.println("2. Past HWs");
        int option = sc.nextInt();

        switch (option){
            case 1:
                openHWs(courseID);
                break;
            case 2:
                pastHWs(courseID);
                break;
            default:
                System.out.println("Invalid option");
                return;
        }
    }

    private static void openHWs(String courseID){
        System.out.println("Current Open HWs");

        QueryResult qr = null;

        try{
            qr = StudentQueries.openHWs(courseID);

            while(qr.rs.next()){
                System.out.println(qr.rs.getString("hName"));
            }
            System.out.println("Press 0 to Go Back To Previous Menu");
            System.out.print("Please provide a homework ID :");
            int option = sc.nextInt();

            if(option == 0){
                return;
            }

            homeworkDetails(option, "current");

        } catch (SQLException e){
            System.out.println(e.getMessage());
        }finally {
            if(qr != null){
                qr.cleanup();
            }
        }
    }

    private static void pastHWs(String courseID){
        System.out.println("Past HWs");

        QueryResult qr = null;

        try{
            qr = StudentQueries.pastHWs(courseID);

            while(qr.rs.next()){
                System.out.println( qr.rs.getString("hID") + " " + qr.rs.getString("hName"));
            }
            System.out.println("Press 0 to Go Back To Previous Menu");
            System.out.print("Please provide a homework ID :");
            int option = sc.nextInt();

            if(option == 0){
                return;
            }

            homeworkDetails(option,"past");


        } catch (SQLException e){
            System.out.println(e.getMessage());
        }finally {
            if(qr != null){
                qr.cleanup();
            }
        }
    }

    private static void homeworkDetails(int hID, String hw){

        QueryResult qr = null;
        QueryResult hwResult = null;

        try {
            qr = StudentQueries.homeworkDetails(hID);
            hwResult = StudentQueries.homeworkResult(hID);
            int attempts = 0;
            String scoringPolicy = null;

            while (qr.rs.next()){
                System.out.println(qr.rs.getString("hName"));
                System.out.println("StartDateTime : " + qr.rs.getString("startDate"));
                System.out.println("Deadline : " + qr.rs.getString("deadline"));
                System.out.println("Points per Question : " + qr.rs.getString("points"));
                System.out.println("Penalty Points per Question : "+ qr.rs.getString("penaltyPoints"));
                System.out.println("Number of Questions : " + qr.rs.getString("noQs"));
                System.out.println("Total Points for this homework : " + qr.rs.getInt("points") * qr.rs.getInt("noQs"));
                attempts = qr.rs.getInt("noAs");
                System.out.println("Number of Attempts : " + qr.rs.getString("noAs"));
                System.out.println("Type of Homework : " + qr.rs.getString("ttype"));
                scoringPolicy = qr.rs.getString("scoringPolicy");
                System.out.println("Scoring Policy : " + qr.rs.getString("scoringPolicy"));
            }

            int alreadyAttempted = 0;

            int finalAttemptConsidered = 0;
            int finalScore = 0;

            while (hwResult.rs.next()){
                System.out.println("Attempt no : " +  hwResult.rs.getString("attNo") + " Score : " + hwResult.rs.getString("score"));
                int currentAttempt = hwResult.rs.getInt("attNo");
                QueryResult fQ = null;
                QueryResult pQ = null;
                fQ = StudentQueries.fixedQuestion(hID,currentAttempt);
                pQ = StudentQueries.parameterizedQuestion(hID,currentAttempt);

                if((fQ.rs != null)) {
                    System.out.println("Fixed Questions");
                    while (fQ.rs.next()) {
                        System.out.println("Question : " + fQ.rs.getString("question"));
                        System.out.println("Solution : " + fQ.rs.getString("solution"));
                        System.out.println("Answer Selected : " + fQ.rs.getString("answer"));

                        String selectedAnswer = fQ.rs.getString("selectedAns");
                        String correctAnswer = fQ.rs.getString("ansID");

                        if (selectedAnswer.equals(correctAnswer)) {
                            System.out.println("Your selected answer is correct");
                        }

                        System.out.println("Score for this question : " + fQ.rs.getString("qscore"));
                    }
                }

                if((pQ.rs != null)) {

                    System.out.println("Parameterized Questions");
                    while (pQ.rs.next()) {
                        System.out.println("Question : " + pQ.rs.getString("question"));
                        System.out.println("Parameters :");
                        if (!pQ.rs.getString("par1").equals("null")) {
                            System.out.println("Parameter 1 : " + pQ.rs.getString("par1"));
                        }
                        if (!pQ.rs.getString("par2").equals("null")) {
                            System.out.println("Parameter 2 : " + pQ.rs.getString("par2"));
                        }
                        if (!pQ.rs.getString("par3").equals("null")) {
                            System.out.println("Parameter 3 : " + pQ.rs.getString("par3"));
                        }
                        if (!pQ.rs.getString("par4").equals("null")) {
                            System.out.println("Parameter 4 : " + pQ.rs.getString("par4"));
                        }
                        if (!pQ.rs.getString("par5").equals("null")) {
                            System.out.println("Parameter 5 : " + pQ.rs.getString("par5"));
                        }
                        System.out.println("Solution : " + pQ.rs.getString("solution"));
                        System.out.println("Answer Selected : " + pQ.rs.getString("answer"));

                        String selectedAnswer = pQ.rs.getString("selectedAns");
                        String correctAnswer = pQ.rs.getString("ansID");

                        if (selectedAnswer.equals(correctAnswer)) {
                            System.out.println("Your selected answer is correct");
                        }

                        System.out.println("Score for this question : " + pQ.rs.getString("qscore"));
                    }
                }

                if(fQ!= null) {
                    fQ.cleanup();
                }
                if(pQ!= null){
                    pQ.cleanup();
                }

                if(scoringPolicy.equals("latest")){
                    finalAttemptConsidered = hwResult.rs.getInt("attNo");
                    finalScore = hwResult.rs.getInt("score");
                }
                else if(scoringPolicy.equals("highest")){
                    int currentScore = hwResult.rs.getInt("score");
                    if(finalScore <= currentScore){
                        finalScore = currentScore;
                        finalAttemptConsidered = hwResult.rs.getInt("attNo");
                    }
                }
                else if(scoringPolicy.equals("average")){
                    finalAttemptConsidered = hwResult.rs.getInt("attNo");
                    finalScore += hwResult.rs.getInt("score");
                }

                alreadyAttempted++;
            }

            if(hw.equals("current") && (alreadyAttempted < attempts)){
                System.out.print("As of now ");
            }

            if(scoringPolicy.equals("average")){
                float finalAvgScore = (float) finalScore/ (float) alreadyAttempted;
                System.out.println("Your final score is " + finalAvgScore + " from "+ alreadyAttempted + " attempts.");
            }
            else{
                System.out.println("Your final score is " + finalScore + " from " + finalAttemptConsidered + " attempt.");
            }

            System.out.println("Attempts already by student : " + alreadyAttempted);
            System.out.println("Available attempts : "+ attempts);
            if(hw.equals("current")){

                if(alreadyAttempted < attempts){
                    System.out.println("ATTEMPT NOW!");

                }
            }

        } catch (SQLException e){
            System.out.println(e.getMessage());
        }finally {
            if(qr != null){
                qr.cleanup();
            }

            if(hwResult != null){
                hwResult.cleanup();
            }
        }

    }
}
