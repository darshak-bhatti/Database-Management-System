import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Darshak on 10/20/17.
 */
public class cleanup {

    public static void clean_queries() throws SQLException {

        Connection conn = ConnectDB.getConnection();


        Statement stmt = null;

        String query = "DROP TABLE " + "COURSE" + " CASCADE CONSTRAINTS";


        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }


        List<String> table_names = new ArrayList<String>();
        table_names.add("DURATION");
        table_names.add("PROFESSOR");
        table_names.add("TAUGHT");
        table_names.add("STUDENT");
        table_names.add("TA");
        table_names.add("ENROLL");
        table_names.add("HOMEWORK");
        table_names.add("TOPIC");
        table_names.add("QUESTION");
        table_names.add("ANSWER");
        table_names.add("FIXED");
        table_names.add("PARAMETERS");
        table_names.add("PARAMETERIZED");
        table_names.add("BELONGS");
        table_names.add("HASQ");
        table_names.add("HASC");
        table_names.add("SUBMISSION");
        table_names.add("QTC");

        for(String s : table_names){
            query = "DROP TABLE " + s + " CASCADE CONSTRAINTS";


            try {
                stmt = conn.createStatement();
                stmt.executeUpdate(query);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }





        conn.close();
    }
}
