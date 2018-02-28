import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by Darshak,Saloni,Veenal,Swati on 10/27/17.
 */
public class StudentQueries {
    static Connection conn = ConnectDB.conn;

    public static QueryResult get_courses() throws SQLException {

        QueryResult qr=new QueryResult();
        System.out.println("\n Student id is " + LoginPage.uName);

        String query = "SELECT C.cID,C.cName FROM " +
                        "ENROLL E, COURSE C " +
                        "WHERE E.sID = ? AND E.cID = C.cID AND SYSDATE < E.endDate  AND SYSDATE >= E.startDate";

        try {
            qr.pstmt = conn.prepareStatement(query);
            qr.pstmt.setString(1, LoginPage.uName);
            qr.rs = qr.pstmt.executeQuery();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return qr;
    }

    public static QueryResult course_details(String courseID) throws SQLException {
        QueryResult qr = new QueryResult();

        String query = "Select DISTINCT C.CID, C.CNAME, P.PNAME " +
                        "FROM Course C, Taught T, Professor P " +
                        "WHERE C.CID = ? " +
                        "AND C.CID = T.CID " +
                        "AND T.PID = P.PID " +
                        "AND SYSDATE <= T.ENDDATE " +
                        "AND SYSDATE >= T.STARTDATE";

        try{
            qr.pstmt = conn.prepareStatement(query);
            qr.pstmt.setString(1, courseID);
            qr.rs = qr.pstmt.executeQuery();

        } catch (SQLException e){
            System.out.println(e.getMessage());
        }

        return qr;
    }

    public static QueryResult TAs(String courseID) throws SQLException {
        QueryResult qr = new QueryResult();

        String query = "Select S.NAME " +
                        "FROM Student S, TA T " +
                        "WHERE T.CID = ? " +
                        "AND SYSDATE <= T.ENDDATE " +
                        "AND SYSDATE >= T.STARTDATE " +
                        "AND T.SID = S.SID";
        try{
            qr.pstmt = conn.prepareStatement(query);
            qr.pstmt.setString(1, courseID);
            qr.rs = qr.pstmt.executeQuery();

        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return qr;
    }

    public static QueryResult get_profile() throws SQLException {

        QueryResult qr=new QueryResult();

        String query = "SELECT S.sID, S.name FROM " +
                "STUDENT S " +
                "WHERE S.sID = ?";

        try {
            qr.pstmt = conn.prepareStatement(query);
            qr.pstmt.setString(1, LoginPage.uName);
            qr.rs = qr.pstmt.executeQuery();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return qr;
    }

    public static QueryResult openHWs(String courseID) throws SQLException {

        QueryResult qr = new QueryResult();

        String query = "SELECT * FROM " +
                "HOMEWORK H " +
                "WHERE H.cID = ? AND SYSTIMESTAMP >= H.startDate AND SYSTIMESTAMP <= H.deadline";

        try {
            qr.pstmt = conn.prepareStatement(query);
            qr.pstmt.setString(1,courseID);
            qr.rs = qr.pstmt.executeQuery();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return qr;
    }

    public static QueryResult pastHWs(String courseID) throws SQLException {

        QueryResult qr = new QueryResult();

        String query = "SELECT * FROM " +
                "HOMEWORK H " +
                "WHERE H.cID = ? AND SYSTIMESTAMP > H.deadline";
        try {
            qr.pstmt = conn.prepareStatement(query);
            qr.pstmt.setString(1,courseID);
            qr.rs = qr.pstmt.executeQuery();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return qr;

    }

    public static QueryResult homeworkDetails(int hID) throws SQLException {

        QueryResult qr = new QueryResult();

        String query = "SELECT * FROM " +
                "HOMEWORK H " +
                "WHERE H.hID = ? ";
        try {
            qr.pstmt = conn.prepareStatement(query);
            qr.pstmt.setString(1,Integer.toString(hID));
            qr.rs = qr.pstmt.executeQuery();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return qr;

    }

    public static QueryResult homeworkResult(int hID) throws SQLException {
        QueryResult qr = new QueryResult();

        String query = "SELECT * FROM " +
                "STUDENT_HOMEWORK_SCORE SHS " +
                "WHERE SHS.hID = ?  AND SHS.studentID = ? ORDER BY SHS.attNo";
        try {
            qr.pstmt = conn.prepareStatement(query);
            qr.pstmt.setString(1,Integer.toString(hID));
            qr.pstmt.setString(2,LoginPage.uName);
            qr.rs = qr.pstmt.executeQuery();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return qr;
    }

    public static  QueryResult fixedQuestion(int hID, int attNo) throws SQLException {
        QueryResult qr = new QueryResult();

        String query = "SELECT DISTINCT Q.QID,Q.QUESTION, Q.HINT,Q.SOLUTION,S.ANSID,S.SELECTEDANS,S.QSCORE, A.ANSWER  FROM Question Q, SUBMISSION S,FIXED F1, ANSWER A " +
                        "WHERE Q.QID IN " +
                        "(SELECT F.QID " +
                        "FROM FIXED F, SUBMISSION S1 " +
                        "WHERE F.ANSID = S1.ANSID " +
                        "AND S1.SID = ? " +
                        "AND S1.HID = ? " +
                        "AND S1.ATTNO = ? )" +
                        "AND S.ANSID = F1.ANSID " +
                        "AND F1.QID = Q.QID " +
                        "AND S.ATTNO = ? " +
                        "AND S.HID = ? " +
                        "AND S.SID = ? " +
                        "AND S.SELECTEDANS = A.ANSID";

        //System.out.println("Query: " + query);

        try{
            qr.pstmt = conn.prepareStatement(query);
            qr.pstmt.setString(1,LoginPage.uName);
            qr.pstmt.setString(2,Integer.toString(hID));
            qr.pstmt.setString(3,Integer.toString(attNo));
            qr.pstmt.setString(4,Integer.toString(attNo));
            qr.pstmt.setString(5,Integer.toString(hID));
            qr.pstmt.setString(6,LoginPage.uName);
            qr.rs = qr.pstmt.executeQuery();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return  qr;
    }


    public static  QueryResult parameterizedQuestion(int hID, int attNo) throws SQLException {
        QueryResult qr = new QueryResult();

        String query = "SELECT DISTINCT Q.QID,Q.QUESTION, Q.HINT,Q.SOLUTION, PR.PAR1,PR.PAR2,PR.PAR3,PR.PAR4,PR.PAR5, S.selectedAns, S.ansID, S.qScore, A.ANSWER " +
                        "FROM Question Q, PARAMETERS PR, PARAMETERIZED P1, SUBMISSION S, ANSWER A WHERE Q.QID IN " +
                        "(SELECT P.QID " +
                        "FROM PARAMETERIZED P, SUBMISSION S1 " +
                        "WHERE P.ANSID = S1.ANSID " +
                        "AND S1.SID = ? " +
                        "AND S1.HID = ? " +
                        "AND S1.ATTNO = ? ) " +
                        "AND PR.PARID = P1.PARID " +
                        "AND S.SID = ? " +
                        "AND S.HID = ? " +
                        "AND S.ATTNO = ? " +
                        "AND S.ANSID = P1.ANSID " +
                        "AND S.SELECTEDANS = A.ANSID";

//        System.out.println("Query: " + query);
        try{
            qr.pstmt = conn.prepareStatement(query);
            qr.pstmt.setString(1,LoginPage.uName);
            qr.pstmt.setString(2,Integer.toString(hID));
            qr.pstmt.setString(3,Integer.toString(attNo));
            qr.pstmt.setString(4,LoginPage.uName);
            qr.pstmt.setString(5,Integer.toString(hID));
            qr.pstmt.setString(6,Integer.toString(attNo));
            qr.rs = qr.pstmt.executeQuery();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return  qr;
    }

}
