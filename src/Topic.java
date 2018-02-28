import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

public class Topic {

    static Connection conn = ConnectDB.conn;
    public static void insertTopic(String topic, String courseID) {

        CallableStatement callableStatement = null;
        QueryResult qr = new QueryResult();

        String query = "{call insert_topic(?,?)}";

        try{

            callableStatement = conn.prepareCall(query);
            callableStatement.setString(1, topic);
            callableStatement.registerOutParameter(2, Types.INTEGER);
            callableStatement.executeUpdate();

            int last_tid = callableStatement.getInt(2);

            query = "INSERT INTO BELONGS VALUES( ? , ? )";

            qr.pstmt = conn.prepareStatement(query);
            qr.pstmt.setString(1, courseID);
            qr.pstmt.setString(2, Integer.toString(last_tid));
            qr.rs = qr.pstmt.executeQuery();

        } catch (SQLException e){

            if(e.getMessage().contains("unique constraint")){
                System.out.println("The given topic name exists.");
            }
            else{
                System.out.println(e.getMessage());
            }

        }finally {

            if(qr != null){
                qr.cleanup();
            }

            if(callableStatement != null) {
                try {
                    callableStatement.close();
                }
                catch (SQLException e){
                    System.out.println(e.getMessage());
                }

            }
        }
    }
}
