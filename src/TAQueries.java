import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Logger;

/**
 * Created by Darshak,Saloni,Veenal,Swati on 10/27/17.
 */
public class TAQueries {
	static Connection conn = ConnectDB.conn;

	public static QueryResult get_TACourses(String sID) throws SQLException {
        QueryResult qr=new QueryResult();
        // Given an instructor we can query a list of courses created by him/her
        String query = "SELECT t.cID FROM TA t WHERE t.sID = ?";
        try {
            qr.pstmt = conn.prepareStatement(query);
            qr.pstmt.setString(1, sID);
            qr.rs = qr.pstmt.executeQuery();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return qr;
    }
}
