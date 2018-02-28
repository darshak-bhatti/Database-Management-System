import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Darshak on 10/27/17.
 */
public class QueryResult {
    public PreparedStatement pstmt;
    public ResultSet rs;

    public void cleanup(){
        try{
            if(this.rs != null){
                this.rs.close();
            }
            if(this.pstmt != null){
                this.pstmt.close();
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
}
