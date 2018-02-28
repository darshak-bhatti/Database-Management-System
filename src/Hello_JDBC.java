import java.lang.*;
import java.sql.*;
import java.util.*;
import java.sql.CallableStatement;


public class Hello_JDBC {

	public static void main(String[] args) throws SQLException {
		// TODO Auto-generated method stub

		Connection conn = ConnectDB.getConnection();
		// Connection error check TO BE DONE

		//LoginPage.login_view();

		//cleanup.clean_queries();
		//CreateTables.create_queries();

		/*
		CallableStatement callableStatement = null;

		try {

			String topic_check = "{call insert_topic(?,?)}";
			callableStatement = conn.prepareCall(topic_check);

			callableStatement.setString(1, "SHOW OFF CALL");
			callableStatement.registerOutParameter(2, Types.INTEGER);

			callableStatement.executeUpdate();

			int last_tid = callableStatement.getInt(2);

			System.out.println("LAST TID IS : " + last_tid);

		} catch (SQLException e) {

			System.out.println(e.getMessage());

		} finally {

			callableStatement.close();

		}
		*/


		conn.close();

		/*

		System.out.println("Menu:");
		System.out.println("1. Login");
		System.out.println("2. Exit");
		Scanner sc=new Scanner(System.in);
		int option=sc.nextInt();
		switch(option)
		{
			case 1:
				break;
			case 2:
				break;
		}
		Connection conn = ConnectDB.getConnection();
		//dbhatti@//orca.csc.ncsu.edu:1521/orcl.csc.ncsu.edu

		CreateTables.create_queries();

		//cleanup.clean_queries();

		conn.close();

		/*
        
        Statement stmt = conn.createStatement();
        String query = "SELECT * FROM STUDENT";
        ResultSet rset = stmt.executeQuery(query);
        
        String retStr = "";
        
        while (rset.next())
        {
            retStr += rset.getString(2) + ',';
            System.out.println(retStr);
        }

		CreateTables.create_queries();
        
        stmt.close();
        conn.close();
        System.out.println("Firing the query to find course name from course id");
        System.out.println(getCourseName("CSC540"));
        */
	}

	public static String getCourseName(String cid) {
		String  sql = "SELECT cname FROM COURSE WHERE cid = ?";
		String courseName="";
		try{ 
			   Connection conn = ConnectDB.getConnection();
	  		   PreparedStatement pstmt  = conn.prepareStatement(sql);
	  		   pstmt.setString(1, cid);
	  		   ResultSet rs = pstmt.executeQuery();
	  		   
	  		   
	  		   while(rs.next()) {
	  			   System.out.println("Hel222lo");
	  			   courseName = rs.getString("cname");
	  			   System.out.println(courseName);
	  		   }
	  		   rs.close();
				pstmt.close();
	  		   conn.close();
		}
	   catch (SQLException e) {
		   System.out.println(e.getMessage());
	   }
		return courseName;
	}
}
