import java.sql.Connection;
import java.sql.SQLException;

public class homeworkQuestionQueries {
    static Connection conn = ConnectDB.conn;

    public static QueryResult addQuestion(int hID, int qID) throws SQLException{
        QueryResult qr = new QueryResult();

        String query = "INSERT INTO HASQ VALUES(?,?)";

        try{
            qr.pstmt = conn.prepareStatement(query);
            qr.pstmt.setString(1, Integer.toString(hID));
            qr.pstmt.setString(2, Integer.toString(qID));
            qr.rs = qr.pstmt.executeQuery();

        } catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return qr;
    }

    public static QueryResult removeQuestion(int hID,int qID) throws SQLException {
        QueryResult qr = new QueryResult();

        String query = "DELETE FROM HASQ WHERE hID = ? AND qID = ?";

        try{
            qr.pstmt = conn.prepareStatement(query);
            qr.pstmt.setString(1, Integer.toString(hID));
            qr.pstmt.setString(2, Integer.toString(qID));
            qr.rs = qr.pstmt.executeQuery();

        } catch (SQLException e){
            System.out.println(e.getMessage());
        }

        return qr;
    }
}
