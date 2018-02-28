import java.sql.*;
import java.util.*;

public class LoginPage {

    static String uName;
    static String level;

    public static void login_view() throws SQLException {
        do{
            System.out.println("\nHello World\n");

            Scanner sc=new Scanner(System.in);
            int option;

            do{
                System.out.println("\n1.Login");
                System.out.println("\n2.Exit");
                System.out.print("\nYour choice : ");
                option=sc.nextInt();

                switch (option) {
                    case 1:
                        System.out.println("\nLogin :: ");
                        break;
                    case 2:
                        System.out.println("\nGood Bye!");
                        return;
                    default:
                        System.out.println("\nInvalid option");
                        break;
                }
            } while (option != 1 && option != 2);

            String password;
            Boolean login_success;
            System.out.print("\nEnter Username : ");
            //sc.nextLine();
            uName = sc.next();

            System.out.print("\nEnter Password : ");
            password = sc.next();

            System.out.println("\n Username is : " + uName);
            System.out.println("\n Username is : " + password);
            // Admin Check

            if (uName == "admin"){

                do{
                    if(password == "admin") {
                        System.out.println("\n Hello Admin");

                        // Do admin stuff

                        login_success = Boolean.TRUE;
                    } else {
                        System.out.println("\nLogin Incorrect");
                        System.out.println("\nEnter Password : ");
                        password = sc.next();
                        login_success = Boolean.FALSE;
                    }
                } while(!login_success);

            }

            // User things
            login_success = Boolean.FALSE;



            do{
                String  sql = "SELECT * FROM LOGIN WHERE uName = ? AND password = ?";
                try {

                    PreparedStatement pstmt  = ConnectDB.conn.prepareStatement(sql);
                    pstmt.setString(1, uName);
                    pstmt.setString(2, password);

                    ResultSet rs = pstmt.executeQuery();

                    if(rs.next()){
                        System.out.println("\nLogin successful");
                        level = rs.getString("designation");
                        login_success = Boolean.TRUE;

                    } else {
                        System.out.println("\nLogin Incorrect");

                        System.out.print("\nEnter Username : ");
                        uName = sc.next();

                        System.out.print("\nEnter Password : ");
                        password = sc.next();

                        login_success = Boolean.FALSE;
                    }
                    rs.close();
                    pstmt.close();




                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }

            } while (!login_success);


            if(level != null){

                if(level.equals("student")){
                    System.out.println("Student view");
                    StudentPage.view();
                }
                else if(level.equals("ta")){
                    System.out.println("TA view");
                    TAPage.view();
                }
                else if(level.equals("instructor")){
                    System.out.println("Instructor view");
                    InstructorPage.view();
                }
            }
            else{
                System.out.println("Invalid level");
                System.exit(0);
            }

        }while(Boolean.TRUE);

    }
}