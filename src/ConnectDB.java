import java.sql.*;

public class ConnectDB {
	
	static Connection conn=null;
	
    public static Connection getConnection()
    {
        try
        {
            conn=DriverManager.getConnection("jdbc:oracle:thin:@orca.csc.ncsu.edu:1521/orcl.csc.ncsu.edu", "srjhawar", "200157678");
            //conn=DriverManager.getConnection("jdbc:oracle:thin:@orca.csc.ncsu.edu:1521/orcl.csc.ncsu.edu", "sndesai", "200156206");
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        return conn;        
    }
}