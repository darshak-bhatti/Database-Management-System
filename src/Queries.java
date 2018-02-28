import java.sql.*;
import java.sql.Date;
import java.io.*;
import java.util.*;
import java.util.logging.Logger;


public class Queries {
    static Connection conn = ConnectDB.conn;
    Logger logging = Logger.getLogger(Queries.class.getName());

    public static QueryResult get_courses(String prof_id) throws SQLException {
        QueryResult qr = new QueryResult();

        // Given an instructor we can query a list of courses created by him/her
        String query = "SELECT cName FROM COURSE WHERE cID IN (SELECT cID FROM TAUGHT WHERE pID = ?)";
        try {
            qr.pstmt = conn.prepareStatement(query);
            qr.pstmt.setString(1, prof_id);
            qr.rs = qr.pstmt.executeQuery();
           
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return qr;
    }
    public static QueryResult get_courseName(String course_id) throws SQLException {
        QueryResult qr=new QueryResult();

        // Given an instructor we can query a list of couqres created by him/her

        String query = "SELECT cName FROM COURSE WHERE cID = ?";
        try {
            qr.pstmt = conn.prepareStatement(query);
            qr.pstmt.setString(1, course_id);
            qr.rs = qr.pstmt.executeQuery();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return qr;
    }
    
    public static QueryResult get_report(String course_id) throws SQLException {
    	   QueryResult qr=new QueryResult();

    	   // Get student report

    	   String query = "SELECT S.sID, S.name, V.score, H.hID, V.ATTNO " +
    	   "FROM STUDENT_HOMEWORK_SCORE V, HOMEWORK H, STUDENT S " +
    	   "WHERE V.studentID = S.sID AND " +
    	   "H.cID = ? AND V.hID = H.hID";

    	   try {
    	       qr.pstmt = conn.prepareStatement(query);
    	       qr.pstmt.setString(1, course_id);
    	       qr.rs = qr.pstmt.executeQuery();

    	   } catch (SQLException e) {
    	       System.out.println(e.getMessage());
    	   }

    	   return qr;
    	}

    public static QueryResult get_reportForStudent(String course_id, String sID) throws SQLException {
        QueryResult qr=new QueryResult();

        // Get student report
        
        String query = "SELECT S.sID, S.name, V.score, H.hID, V.ATTNO " +
                "FROM STUDENT_HOMEWORK_SCORE V, HOMEWORK H, STUDENT S " +
                "WHERE V.studentID = S.sID AND " +
                "H.cID = ? AND V.hID = H.hID AND S.sID = ?";

        try {
            qr.pstmt = conn.prepareStatement(query);
            qr.pstmt.setString(1, course_id);
            qr.pstmt.setString(2, sID);
            qr.rs = qr.pstmt.executeQuery();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return qr;
    }

    
    
    public static String get_studentName_sid(String sid) throws SQLException {
        QueryResult qr=new QueryResult();
        String sName="";

        // Given an instructor we can query a list of couqres created by him/her

        String query = "SELECT name FROM STUDENT WHERE sID = ?";
        try {
            qr.pstmt = conn.prepareStatement(query);
            qr.pstmt.setString(1, sid);
            qr.rs = qr.pstmt.executeQuery();
            if(qr.rs.next())
                sName=qr.rs.getString(1);

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return sName;
    }
    public static QueryResult get_TADuration(String course_id) throws SQLException {
        QueryResult qr=new QueryResult();
        // Given an instructor we can query a list of couqres created by him/her
        String query = "SELECT t.sID,s.name,t.startDate,t.endDate FROM TA t, STUDENT s WHERE t.cID = ? And s.sID = t.sID";

        try {
            qr.pstmt = conn.prepareStatement(query);
            qr.pstmt.setString(1, course_id);
            qr.rs = qr.pstmt.executeQuery();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return qr;
    }
    public static QueryResult get_hwExerciseFromCourse(String course_id) throws SQLException {
        QueryResult qr=new QueryResult();

        // Given an instructor we can query a list of coures created by him/her

        String query = "SELECT H.hID, H.hName, H.deadline, H.points, H.penaltyPoints, H.noQs, H.noAs, H.startDate, H.ttype, H.scoringPolicy, H.tID"
        		+ " FROM HOMEWORK H WHERE H.cid = ?";
        try {
            qr.pstmt = conn.prepareStatement(query);
            qr.pstmt.setString(1, course_id);
            qr.rs = qr.pstmt.executeQuery();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return qr;
    }
    
    
    public static QueryResult get_hwExerciseFromHWID(int hwID) throws SQLException {
        QueryResult qr=new QueryResult();

        // Given an instructor we can query a list of coures created by him/her

        String query = "SELECT H.hID, H.hName, H.deadline, H.points, H.penaltyPoints, H.noQs, H.noAs, H.startDate, H.ttype, H.scoringPolicy, H.tID, H.cID FROM HOMEWORK H WHERE H.hID = ?";
        try {
            qr.pstmt = conn.prepareStatement(query);
            qr.pstmt.setInt(1, hwID);
            qr.rs = qr.pstmt.executeQuery();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return qr;
    }
    public static int insert_course(String course_id,String cName) throws SQLException {
        QueryResult qr=new QueryResult();

        // Given an instructor we can query a list of couqres created by him/her
        int insertResult = 0;
        String query = "INSERT INTO COURSE (cID,cName) VALUES(?,?)";
        try {
            qr.pstmt = conn.prepareStatement(query);
            qr.pstmt.setString(1, course_id);
            qr.pstmt.setString(2, cName);
            insertResult = qr.pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        finally {
            qr.pstmt.close();
        }
        return insertResult;
    }

    public static int insert_duration(String startDate,String endDate) throws SQLException {
        QueryResult qr=new QueryResult();

        // Given an instructor we can query a list of courses created by him/her
        int insertResult = 0;
        String query = "INSERT INTO DURATION (startDate,endDate) VALUES(TO_DATE(?,'yyyy-mm-dd'),TO_DATE(?,'yyyy-mm-dd'))";
        try {
            qr.pstmt = conn.prepareStatement(query);
            qr.pstmt.setString(1, startDate);
            qr.pstmt.setString(2, endDate);
            insertResult= qr.pstmt.executeUpdate();

        } catch (SQLException e) {
        	if(e.getMessage().contains("unique constraint")){
                insertResult = 1;
            }
            System.out.println(e.getMessage());
        }
        finally {
            qr.pstmt.close();
        }
        return insertResult;
    }
    public static int insert_taught(String cid,String pid, String startDate,String endDate) throws SQLException {
        QueryResult qr=new QueryResult();
        int insertResult = 0;

        // Given an instructor we can query a list of couqres created by him/her

        String query = "INSERT INTO TAUGHT (cID, pID, startDate, endDate) VALUES(?,?,TO_DATE(?,'yyyy-mm-dd'),TO_DATE(?,'yyyy-mm-dd'))";
        try {
            qr.pstmt = conn.prepareStatement(query);
            qr.pstmt.setString(1, cid);
            qr.pstmt.setString(2, pid);
            qr.pstmt.setString(3, startDate);
            qr.pstmt.setString(4, endDate);
            insertResult = qr.pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        finally {
            qr.pstmt.close();
        }
        return insertResult;

    }
    public static int insert_TA(String cid,String sid, String startDate,String endDate) throws SQLException {
        QueryResult qr=new QueryResult();
        int res=0;
        // Given an instructor we can query a list of couqres created by him/her

        String query = "INSERT INTO TA (cID, sID, startDate, endDate) VALUES(?,?,TO_DATE(?,'yyyy-mm-dd'),TO_DATE(?,'yyyy-mm-dd'))";
        try {
        	qr.pstmt = conn.prepareStatement(query);
            qr.pstmt.setString(1, cid);
            qr.pstmt.setString(2, sid);
            qr.pstmt.setString(3, startDate);
            qr.pstmt.setString(4, endDate);
            res=qr.pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        finally {
            qr.pstmt.close();
        }
    return res;
    }

    public static int insert_enroll(String cid,String sids, String startDate,String endDate) throws SQLException {
        

        // Given an instructor we can query a list of couqres created by him/her
        int insertResult = 0;
        List<String> sidList = Arrays.asList(sids.split(","));
        
        for(String sid: sidList) {
        QueryResult qr=new QueryResult();
        String query = "INSERT INTO ENROLL VALUES(?,?,TO_DATE(?,'yyyy-mm-dd'),TO_DATE(?,'yyyy-mm-dd'))";
        try {

            qr.pstmt = conn.prepareStatement(query);
            qr.pstmt.setString(1, cid);
            qr.pstmt.setString(2, sid);
            qr.pstmt.setString(3, startDate);
            qr.pstmt.setString(4, endDate);
            insertResult = qr.pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        finally {
            qr.pstmt.close();
        }
        }
        
        return insertResult;
        
    }
    
    public static boolean isInstructor(String cid) throws SQLException {
    	QueryResult qr=new QueryResult();

        // Given an instructor we can query a list of couqres created by him/her

        String query = "SELECT pID FROM TAUGHT WHERE cID = ?";
        try {
            qr.pstmt = conn.prepareStatement(query);
            qr.pstmt.setString(1, cid);
            qr.rs = qr.pstmt.executeQuery();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        while(qr.rs.next()) {
        	String pid = qr.rs.getString(1);
        	return true;
        }
    return false;
    }
    
    public static boolean isTA(String cid, String sID) throws SQLException {
    	QueryResult qr=new QueryResult();

        // Given an instructor we can query a list of couqres created by him/her

        String query = "SELECT sID FROM TA WHERE cID = ?";
        try {
            qr.pstmt = conn.prepareStatement(query);
            qr.pstmt.setString(1, cid);
            qr.rs = qr.pstmt.executeQuery();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        while(qr.rs.next()) {
        	String sid = qr.rs.getString(1);
        	if(sid.equals(sID))
        	    return true;
        }
    return false;
    }
    
    public static boolean isInstructor(String cid, String iID) throws SQLException {
    	QueryResult qr=new QueryResult();

        // Given an instructor we can query a list of couqres created by him/her

        String query = "SELECT pID FROM TAUGHT WHERE cID = ?";
        try {
            qr.pstmt = conn.prepareStatement(query);
            qr.pstmt.setString(1, cid);
            qr.rs = qr.pstmt.executeQuery();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        while(qr.rs.next()) {
        	String iid = qr.rs.getString(1);
        	if(iid.equals(iID))
        	    return true;
        }
    return false;
    }
    
    	
    public static void insert_topic(int tid,String tname) throws SQLException {
        QueryResult qr=new QueryResult();

        // Given an instructor we can query a list of courses created by him/her

        String query = "INSERT INTO TOPIC (tID, tName) VALUES(?,?)";
        try {
            qr.pstmt = conn.prepareStatement(query);
            qr.pstmt.setInt(1, tid);
            qr.pstmt.setString(2, tname);
            qr.pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        finally {
            qr.pstmt.close();
        }

    }
    public static void insert_belongsto(String cid,int tid) throws SQLException {
        QueryResult qr=new QueryResult();
        // Given an instructor we can query a list of couqres created by him/her

        String query = "INSERT INTO BELONGS (cID, tID) VALUES(?,?)";
        try {
            qr.pstmt = conn.prepareStatement(query);
            qr.pstmt.setString(1, cid);
            qr.pstmt.setInt(2, tid);
            qr.pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        finally {
            qr.pstmt.close();
        }

    }
    public static boolean check_topicInTopics(int tid) throws SQLException {
        QueryResult qr=new QueryResult();
        boolean answer=true;

        // Given an instructor we can query a list of couqres created by him/her

        String query = "SELECT tName FROM TOPIC WHERE tID = ?";
        try {
            qr.pstmt = conn.prepareStatement(query);
            qr.pstmt.setInt(1, tid);
            qr.rs=qr.pstmt.executeQuery();

            if(!qr.rs.next())
                answer=false;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return answer;
    }
    public static boolean processTopicCourse(String cid, int tid, String tname) throws SQLException {
        if(!check_topicInTopics(tid))
            insert_topic(tid,tname);
        
        return true;
    }


    public static QueryResult get_profile(String id) throws SQLException {
        QueryResult qr=new QueryResult();
        String level = LoginPage.level;
        String query="";
        if(level.equals("student") || level.equals("ta")){
            query = "SELECT s.name FROM STUDENT s WHERE s.sID = ?";
        }
        else if(level.equals("instructor")) {
            query = "SELECT p.pName FROM PROFESSOR p WHERE p.pID = ?";
        }
        else
            System.out.println("Invalid Level");
        // Given an instructor we can query a list of couqres created by him/her

        try {
            qr.pstmt = conn.prepareStatement(query);
            qr.pstmt.setString(1, id);
            qr.rs = qr.pstmt.executeQuery();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return qr;
    }

    public static int drop_student(String cid,String sid) throws SQLException {
        QueryResult qr=new QueryResult();
        int res=0;
        // Given an instructor we can query a list of couqres created by him/her

        String query = "DELETE FROM ENROLL where cID=? AND sID=?";
        try {
            qr.pstmt = conn.prepareStatement(query);
            qr.pstmt.setString(1, cid);
            qr.pstmt.setString(2, sid);
            res=qr.pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        finally {
            qr.pstmt.close();
        }
        return res;
    }
    public static int insert_question(String question,int difficulty,String solution,String hint) throws SQLException {
        int last_qid=0;
        CallableStatement callableStatement = null;
        try {
            String q_check = "{call insert_question(?,?,?,?,?)}";
            callableStatement = conn.prepareCall(q_check);
            callableStatement.setString(1, question);
            callableStatement.setInt(2, difficulty);
            callableStatement.setString(3, hint);
            callableStatement.setString(4, solution);
            callableStatement.registerOutParameter(5, Types.INTEGER);
            int ret= callableStatement.executeUpdate();
            if(ret==1)
                last_qid = callableStatement.getInt(5);
            else
                last_qid=-1;
        } catch (SQLException e) {

            System.out.println(e.getMessage());

        } finally {
            callableStatement.close();
        }
        return last_qid;
    }
    public static int insert_QTC(int qid,int tid,String cid) throws SQLException {
        QueryResult qr = new QueryResult();
        int ret=0;
        // Given an instructor we can query a list of couqres created by him/her
/*
                cID VARCHAR(20) NOT NULL" + ", " +
                "tID INTEGER NOT NULL" + ", " +
                "qID INTEGER NOT NULL" + ", " +
 */
        String query = "INSERT INTO QTC (cID, tID, qID) VALUES(?,?,?)";
        try {
            qr.pstmt = conn.prepareStatement(query);
            qr.pstmt.setString(1, cid);
            qr.pstmt.setInt(2, tid);
            qr.pstmt.setInt(3, qid);
            ret=qr.pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            qr.pstmt.close();
        }
        return ret;
    }
    public static int insert_answer(String answer) throws SQLException {
        QueryResult qr = new QueryResult();
        int ret=0;
        // Given an instructor we can query a list of couqres created by him/her
/*
                cID VARCHAR(20) NOT NULL" + ", " +
                "tID INTEGER NOT NULL" + ", " +
                "qID INTEGER NOT NULL" + ", " +
 */
        int last_aid=0;
        CallableStatement callableStatement = null;
        try {
            String q_check = "{call insert_answer(?,?)}";
            callableStatement = conn.prepareCall(q_check);
            callableStatement.setString(1, answer);
            callableStatement.registerOutParameter(2, Types.INTEGER);
            ret= callableStatement.executeUpdate();
            if(ret==1)
                last_aid = callableStatement.getInt(2);
            else
                last_aid=-1;
        } catch (SQLException e) {

            System.out.println(e.getMessage());

        } finally {
            callableStatement.close();
        }
        return last_aid;
    }
    public static int insert_fixed(int qid, int aid, String iscorrect) throws SQLException {
        QueryResult qr = new QueryResult();
        int ret=0;
        // Given an instructor we can query a list of couqres created by him/her
/*
                cID VARCHAR(20) NOT NULL" + ", " +
                "tID INTEGER NOT NULL" + ", " +
                "qID INTEGER NOT NULL" + ", " +
 */
        String query = "INSERT INTO FIXED (qID, ansID,isCorrect) VALUES(?,?,?)";
        try {
            qr.pstmt = conn.prepareStatement(query);
            qr.pstmt.setInt(1, qid);
            qr.pstmt.setInt(2, aid);
            qr.pstmt.setString(3, iscorrect);
            ret=qr.pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            qr.pstmt.close();
        }
        return ret;
    }
    public static int insert_param(int qid, int aid,int pid, String iscorrect) throws SQLException {
        QueryResult qr = new QueryResult();
        int ret=0;
        // Given an instructor we can query a list of couqres created by him/her
/*
                "qID INTEGER NOT NULL" + ", " +
                "ansID INTEGER NOT NULL" + ", " +
                "parID INTEGER NOT NULL" + ", " +
                "isCorrect CHAR (1) NOT NULL" + ", " +
               */
        String query = "INSERT INTO PARAMETERIZED (qID, ansID,parID,isCorrect) VALUES(?,?,?,?)";
        try {
            qr.pstmt = conn.prepareStatement(query);
            qr.pstmt.setInt(1, qid);
            qr.pstmt.setInt(2, aid);
            qr.pstmt.setInt(3, pid);
            qr.pstmt.setString(4, iscorrect);
            ret=qr.pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            qr.pstmt.close();
        }
        return ret;
    }
    public static int insert_parameters(String[] param) throws SQLException {
        QueryResult qr = new QueryResult();
        int ret=0;
        // Given an instructor we can query a list of couqres created by him/her
/*
                "qID INTEGER NOT NULL" + ", " +
                "ansID INTEGER NOT NULL" + ", " +
                "parID INTEGER NOT NULL" + ", " +
                "isCorrect CHAR (1) NOT NULL" + ", " +
               */
        int last_pid=0;
        CallableStatement callableStatement = null;
        try {
            String q_check = "{call insert_parameters(?,?,?,?,?,?)}";
            callableStatement = conn.prepareCall(q_check);
            for(int i=1;i<6;i++)
                callableStatement.setString(i, param[i-1]);
            callableStatement.registerOutParameter(6, Types.INTEGER);
            ret= callableStatement.executeUpdate();
            if(ret==1)
                last_pid = callableStatement.getInt(6);
            else
                last_pid=-1;
        } catch (SQLException e) {

            System.out.println(e.getMessage());

        } finally {
            callableStatement.close();
        }
        return last_pid;
    }
    public static QueryResult view_question(int id) throws SQLException {
        QueryResult qr=new QueryResult();
        String level = LoginPage.level;
        String query="";
            query = "SELECT * FROM QUESTION WHERE  qID = ?";

        // Given an instructor we can query a list of couqres created by him/her

        try {
            qr.pstmt = conn.prepareStatement(query);
            qr.pstmt.setInt(1, id);
            qr.rs = qr.pstmt.executeQuery();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return qr;
    }
    public static QueryResult view_qbTopic(int tid,String cid) throws SQLException {
        QueryResult qr=new QueryResult();
        String query="";
        query = "SELECT * FROM QUESTION Q, QTC qtc WHERE  qtc.tID = ? AND qtc.cID = ? AND Q.qID = qtc.qID";

        // Given an instructor we can query a list of couqres created by him/her

        try {
            qr.pstmt = conn.prepareStatement(query);
            qr.pstmt.setInt(1, tid);
            qr.pstmt.setString(2, cid);
            qr.rs = qr.pstmt.executeQuery();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return qr;
    }
    public static QueryResult get_questions() throws SQLException {
        QueryResult qr=new QueryResult();
        String query="";
        query = "SELECT * FROM QUESTION";

        // Given an instructor we can query a list of couqres created by him/her

        try {
            qr.pstmt = conn.prepareStatement(query);
            qr.rs = qr.pstmt.executeQuery();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return qr;
    }
    
    public static int insert_hw(String hwName,String cID,String deadline,int points,
    		int penaltyPoints, int noQs, int noAs,String startTimeString,String testType, String scoringPolicy, int tID ) throws SQLException {
        /*
    	QueryResult qr = new QueryResult();
        int ret = 0;
        // Insert HWs

        String query = "INSERT INTO HOMEWORK (hID, cID, hName, deadline, points, penaltyPoints, noQs, noAs, startDate, ttype, scoringPolicy,tID)"+
        				"VALUES(?,?,?,TO_TIMESTAMP(?, 'yyyy-mm-dd hh24:mi:ss'),?,?,?,?,TO_TIMESTAMP(?, 'yyyy-mm-dd hh24:mi:ss'),?,?,?)";
        // TO_TIMESTAMP(?, 'yyyy-mm-dd hh:mm:ss')
        try {
            qr.pstmt = conn.prepareStatement(query);
            qr.pstmt.setInt(1, hID);
            qr.pstmt.setString(2, cID);
            qr.pstmt.setString(3, hwName);
            //qr.pstmt.setTimestamp(4, Timestamp.valueOf(deadline));
            qr.pstmt.setString(4, deadline);
            qr.pstmt.setInt(5, points);
            qr.pstmt.setInt(6, penaltyPoints);
            qr.pstmt.setInt(7, noQs);
            qr.pstmt.setInt(8, noAs);
            //qr.pstmt.setTimestamp(9, Timestamp.valueOf(startTimeString));
            qr.pstmt.setString(9, (startTimeString));
            qr.pstmt.setString(10, testType);
            qr.pstmt.setString(11, scoringPolicy);
            qr.pstmt.setInt(12, tID);
            ret=qr.pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            qr.pstmt.close();
        }
        return ret;
        */
    	CallableStatement callableStatement = null;
        //QueryResult qr = new QueryResult();
        int last_hid = -1;

        String query = "{call insert_homework(?,?,?,?,?,?,?,?,?,?,?,?)}";

        try{
            callableStatement = conn.prepareCall(query);
            callableStatement.setString(1, cID); //cid
            callableStatement.setString(2, hwName); //hname
            callableStatement.setString(3, deadline); //deadline
            callableStatement.setInt(4, points); //points
            callableStatement.setInt(5, penaltyPoints); //penaltypointshwName
            callableStatement.setInt(6, noQs); //noqs
            callableStatement.setInt(7, noAs); //noas
            callableStatement.setString(8, startTimeString); //startdate
            callableStatement.setString(9, testType); //ttype
            callableStatement.setString(10, scoringPolicy); //scoringpolicy
            callableStatement.setInt(11, tID); //tid
            callableStatement.registerOutParameter(12, Types.INTEGER);

            callableStatement.executeUpdate();

            last_hid = callableStatement.getInt(12);

        } catch (SQLException e){

            if(e.getMessage().contains("unique constraint")){
                System.out.println("The given Hw exists.");
            }
            else{
                System.out.println(e.getMessage());
            }

        }finally {
        	/*
            if(qr != null){
                qr.cleanup();
            }
            */

            if(callableStatement != null) {
                try {
                    callableStatement.close();
                }
                catch (SQLException e){
                    System.out.println(e.getMessage());
                }

            }
        }

        return last_hid;

    }
    
    public static int addQuestion(int hID, int qID) throws SQLException{
        QueryResult qr = new QueryResult();
        int res = 0;
        String query = "INSERT INTO HASQ VALUES(?,?)";

        try{
            qr.pstmt = conn.prepareStatement(query);
            qr.pstmt.setString(1, Integer.toString(hID));
            qr.pstmt.setString(2, Integer.toString(qID));
            //qr.rs = qr.pstmt.executeQuery();
            res=qr.pstmt.executeUpdate();

        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return res;
    }

    public static  int removeQuestion(int hID,int qID) throws SQLException {
        QueryResult qr = new QueryResult();
        int res = 0;
        String query = "DELETE FROM HASQ WHERE hID = ? AND qID = ?";

        try{
            qr.pstmt = conn.prepareStatement(query);
            qr.pstmt.setString(1, Integer.toString(hID));
            qr.pstmt.setString(2, Integer.toString(qID));
            res=qr.pstmt.executeUpdate();

        } catch (SQLException e){
            System.out.println(e.getMessage());
        }

        return res;
    }
    
    public static QueryResult get_questions_from_tid(int tID) throws SQLException {
        QueryResult qr=new QueryResult();

        // Given a topic id, get all the questions

        String query = "SELECT Q.qID, Q.question FROM QUESTION Q, QTC qtc WHERE Q.qID = qtc.qID AND qtc.tID = ?";
        try {
            qr.pstmt = conn.prepareStatement(query);
            qr.pstmt.setInt(1, tID);
            qr.rs = qr.pstmt.executeQuery();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return qr;
    }
    
    public static boolean isStudentEnrolled(String cid, String sID) throws SQLException {
    	QueryResult qr=new QueryResult();
    	Set<String> students = new HashSet<String>();

        // Given an instructor we can query a list of couqres created by him/her

        String query = "SELECT sID FROM ENROLL WHERE cID = ?";
        try {
            qr.pstmt = conn.prepareStatement(query);
            qr.pstmt.setString(1, cid);
            qr.rs = qr.pstmt.executeQuery();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        while(qr.rs.next()) {
        	String sid = qr.rs.getString(1);
        	students.add(sid);
        }
        if(students.contains(sID)){
        	return true;
        }
    return false;
    }

}
   