import java.sql.*;
import java.util.logging.Logger;

public class CreateTables {
	
	public static void create_queries() throws SQLException {
	
		Connection conn = ConnectDB.conn;
        Logger logging = Logger.getLogger(CreateTables.class.getName());
		
		// Course Table
		
		Statement stmt = null;

		String query = "CREATE TABLE " + "COURSE" + " " +
						"(" +
							"cID VARCHAR(20) NOT NULL" + ", " +
							"cName VARCHAR(100) NOT NULL" + ", " +
							"PRIMARY KEY (cID)"
                        +")";

		
		try {
			stmt = conn.createStatement();
			stmt.executeUpdate(query);
            System.out.println("\n COURSE Done");
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}



		// Duration Table

		query = "CREATE TABLE " + "DURATION" + " " +
                "(" +
				"startDate DATE NOT NULL" + ", " +
				"endDate DATE NOT NULL" + ", " +
				"PRIMARY KEY (startDate, endDate), " +
                "CONSTRAINT date_check_duration CHECK (startDate < endDate)"
				+")";


		try {
			stmt = conn.createStatement();
			stmt.executeUpdate(query);
            System.out.println("\n DURATION Done");
		} catch (SQLException e) {
            logging.info("\n DURATION Fail");
            System.out.println(e.getMessage());
        }

        // Professor Table

        query = "CREATE TABLE " + "PROFESSOR" + " " +
                "(" +
                "pID VARCHAR(20) NOT NULL" + ", " +
                "pName VARCHAR(100) NOT NULL" + ", " +
                "PRIMARY KEY (pID)"
                +")";


        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(query);
            System.out.println("\n PROFESSOR Done");
        } catch (SQLException e) {
            logging.info("\n PROFESSOR Fail");
            System.out.println(e.getMessage());
        }

        // Taught Table

        query = "CREATE TABLE " + "TAUGHT" + " " +
                "(" +
                "cID VARCHAR(20) NOT NULL" + ", " +
                "pID VARCHAR(20) NOT NULL" + ", " +
                "startDate DATE NOT NULL" + ", " +
                "endDate DATE  NOT NULL" + ", " +
                "FOREIGN KEY (cID) REFERENCES COURSE ON DELETE CASCADE, " +
                "FOREIGN KEY (pID) REFERENCES PROFESSOR ON DELETE CASCADE, " +
                "FOREIGN KEY (startDate, endDate) REFERENCES DURATION ON DELETE CASCADE, " +
                "PRIMARY KEY (cID,startDate,endDate)," +
                "CONSTRAINT date_check_taught CHECK (startDate < endDate)"
                +")";


        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(query);
            System.out.println("\n TAUGHT Done");
        } catch (SQLException e) {
            logging.info("\n TAUGHT Fail");
            System.out.println(e.getMessage());
        }


        // Student Table

        query = "CREATE TABLE " + "STUDENT" + " "+
                "(" +
                "sID VARCHAR(20) NOT NULL" + ", " +
                "name VARCHAR(100) NOT NULL" + ", " +
                "lvl VARCHAR(10) CHECK (lvl IN ('undergrad', 'grad')) NOT NULL" + ", " +
                "PRIMARY KEY (sID)"
                +")";


        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(query);
            System.out.println("\n STUDENT Done");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            logging.info("\n STUDENT Fail");
        }




        // TA Table

        query = "CREATE TABLE " + "TA" + " " +
                "(" +
                "cID VARCHAR(20) NOT NULL" + ", " +
                "sID VARCHAR(20) NOT NULL" + ", " +
                "startDate DATE NOT NULL" + ", " +
                "endDate DATE  NOT NULL" + ", " +
                "FOREIGN KEY (sID) REFERENCES STUDENT ON DELETE CASCADE, " +
                "FOREIGN KEY (cID,startDate, endDate) REFERENCES TAUGHT ON DELETE CASCADE, " +
                "PRIMARY KEY (cID,sID, startDate,endDate)," +
                "CONSTRAINT date_check_ta CHECK (startDate < endDate)"
                +")";


        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(query);
            System.out.println("\n TA Done");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            logging.info("\n TA Fail");
        }


        // Enroll Table

        query = "CREATE TABLE " + "ENROLL" + " " +
                "(" +
                "cID VARCHAR(20) NOT NULL" + ", " +
                "sID VARCHAR(20) NOT NULL" + ", " +
                "startDate DATE NOT NULL" + ", " +
                "endDate DATE  NOT NULL" + ", " +
                "FOREIGN KEY (sID) REFERENCES STUDENT ON DELETE CASCADE, " +
                "FOREIGN KEY (cID,startDate, endDate) REFERENCES TAUGHT ON DELETE CASCADE, " +
                "PRIMARY KEY (cID,sID, startDate,endDate)," +
                "CONSTRAINT date_check_enroll CHECK (startDate < endDate)"
                +")";


        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(query);
            System.out.println("\n ENROLL Done");
        } catch (SQLException e) {
            logging.info("\n ENROLL Fail");
            System.out.println(e.getMessage());
        }

        // Topic Table

        query = "CREATE TABLE " + "TOPIC" + " " +
                "(" +
                "tID INTEGER NOT NULL" + ", " +
                "tName VARCHAR(100) NOT NULL" + ", " +
                "PRIMARY KEY (tID), " +
                "CONSTRAINT unique_topic_name UNIQUE (tName)"
                +")";


        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(query);
            System.out.println("\n TOPIC Done");
        } catch (SQLException e) {
            logging.info("\n TOPIC Fail");
            System.out.println(e.getMessage());
        }

        // Standard Homework Table

        query = "CREATE TABLE " + "HOMEWORK" + " " +
                "(" +
                "hID INTEGER NOT NULL" + ", " +
                "cID VARCHAR(20) NOT NULL" + ", " +
                "hName VARCHAR(100) NOT NULL" + ", " +
                "deadline TIMESTAMP NOT NULL" + ", " +
                "points INTEGER NOT NULL" + ", " +
                "difficulty INTEGER NOT NULL" + ", " +
                "penaltyPoints INTEGER NOT NULL" + ", " +
                "noQs INTEGER NOT NULL" + ", " +
                "noAs INTEGER NOT NULL" + ", " +
                "startDate TIMESTAMP NOT NULL" + ", " +
                "ttype VARCHAR(10) CHECK (ttype IN ('standard', 'adaptive')) NOT NULL" + ", " +
                "scoringPolicy VARCHAR(10) CHECK (scoringPolicy IN ('highest', 'latest', 'average')) NOT NULL" + ", " +
                "tID INTEGER NOT NULL"  + ", " +
                "PRIMARY KEY (hID), " +
                "FOREIGN KEY (cID) REFERENCES COURSE ON DELETE CASCADE" + "," +
                "FOREIGN  KEY (tID) REFERENCES TOPIC ON DELETE CASCADE, " +
                "CONSTRAINT date_check_hw CHECK (startDate < deadline)"
                +")";


        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(query);
            System.out.println("\n HOMEWORK Done");
        } catch (SQLException e) {
            logging.info("\n HOMEWORK Fail");
            System.out.println(e.getMessage());
        }

        // Question table


        query = "CREATE TABLE " + "QUESTION" + " " +
                "(" +
                "qID INTEGER NOT NULL" + ", " +
                "question VARCHAR(1000) NOT NULL" + ", " +
                "difficulty INTEGER NOT NULL" + ", " +
                "hint VARCHAR(1000)" + ", " +
                "solution VARCHAR(1000) NOT NULL" + ", " +  // Detailed solution
                "qType VARCHAR(1) CHECK (qType IN ('F', 'P')) NOT NULL" + ", " +
                "PRIMARY KEY (qID)"
                +")";

        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(query);
            System.out.println("\n QUESTION Done");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            logging.info("\n QUESTION Fail");
        }




        // Answer table


        query = "CREATE TABLE " + "ANSWER" + " " +
                "(" +
                "ansID INTEGER NOT NULL" + ", " +
                "answer VARCHAR(1000) NOT NULL" + ", " +
                "PRIMARY KEY (ansID)"
                +")";


        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(query);
            System.out.println("\n ANSWER Done");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            logging.info("\n ANSWER Fail");
        }




        // Fixed (relation) table


        query = "CREATE TABLE " + "FIXED" + " " +
                "(" +
                "qID INTEGER NOT NULL" + ", " +
                "ansID INTEGER NOT NULL" + ", " +
                "isCorrect VARCHAR(1) CHECK (isCorrect IN ('Y', 'N')) NOT NULL" + ", " +
                "FOREIGN KEY (ansID) REFERENCES ANSWER ON DELETE CASCADE, " +
                "FOREIGN KEY (qID) REFERENCES QUESTION ON DELETE CASCADE, " +
                "PRIMARY KEY (qID, ansID)"
                +")";

        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(query);
            System.out.println("\n FIXED Done");
        } catch (SQLException e) {
            logging.info("\n FIXED Fail");
            System.out.println(e.getMessage());
        }




        // Parameters table


        query = "CREATE TABLE " + "PARAMETERS" + " " +
                "(" +
                "parID INTEGER NOT NULL" + ", " +
                "par1 VARCHAR (100) " + ", " +
                "par2 VARCHAR (100) " + ", " +
                "par3 VARCHAR (100) " + ", " +
                "par4 VARCHAR (100) " + ", " +
                "par5 VARCHAR (100) " + ", " +
                "PRIMARY KEY (parID)"
                +")";

        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(query);
            System.out.println("\n PARAMETERS Done");
        } catch (SQLException e) {
            logging.info("\n PARAMETERS Fail");
            System.out.println(e.getMessage());
        }



        // Parameterized (relation) table


        query = "CREATE TABLE " + "PARAMETERIZED" + " " +
                "(" +
                "qID INTEGER NOT NULL" + ", " +
                "ansID INTEGER NOT NULL" + ", " +
                "parID INTEGER NOT NULL" + ", " +
                "isCorrect VARCHAR(1) CHECK (isCorrect IN ('Y', 'N')) NOT NULL" + ", " +
                "FOREIGN KEY (ansID) REFERENCES ANSWER ON DELETE CASCADE, " +
                "FOREIGN KEY (qID) REFERENCES QUESTION ON DELETE CASCADE," +
                "FOREIGN KEY (parID) REFERENCES PARAMETERS ON DELETE CASCADE, " +
                "PRIMARY KEY (qID, ansID, parID)"
                +")";

        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(query);
            System.out.println("\n PARAMETERIZED Done");
        } catch (SQLException e) {
            logging.info("\n PARAMETERIZED Fail");
            System.out.println(e.getMessage());
        }



        // Belongs to

        query = "CREATE TABLE " + "BELONGS" + " " +
                "(" +
                "cID VARCHAR(20) NOT NULL" + ", " +
                "tID INTEGER NOT NULL" + ", " +
                "FOREIGN KEY (cID) REFERENCES COURSE, " +
                "FOREIGN KEY (tID) REFERENCES TOPIC, " +
                "PRIMARY KEY (cID, tID)"
                +")";

        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(query);
            System.out.println("\n BELONGS Done");
        } catch (SQLException e) {
            logging.info("\n BELONGS Fail");
            System.out.println(e.getMessage());
        }

        // QTC - Question-topic-course

        query = "CREATE TABLE " + "QTC" + " " +
                "(" +
                "cID VARCHAR(20) NOT NULL" + ", " +
                "tID INTEGER NOT NULL" + ", " +
                "qID INTEGER NOT NULL" + ", " +
                "FOREIGN KEY (cID) REFERENCES COURSE, " +
                "FOREIGN KEY (tID) REFERENCES TOPIC, " +
                "FOREIGN KEY (qID) REFERENCES QUESTION, " +
                "PRIMARY KEY (cID, tID, qID)"
                +")";

        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(query);
            System.out.println("\n BELONGS Done");
        } catch (SQLException e) {
            logging.info("\n QTC Fail");
            System.out.println(e.getMessage());
        }




        // Has question

        query = "CREATE TABLE " + "HASQ" + " " +
                "(" +
                "hID INTEGER NOT NULL" + ", " +
                "qID INTEGER NOT NULL" + ", " +
                "FOREIGN KEY (hID) REFERENCES HOMEWORK, " +
                "FOREIGN KEY (qID) REFERENCES QUESTION, " +
                "PRIMARY KEY (hID, qID)"
                +")";

        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(query);
            System.out.println("\n HASQ Done");
        } catch (SQLException e) {
            logging.info("\n HASQ Fail");
            System.out.println(e.getMessage());
        }



        // Has Course //

        /*query = "CREATE TABLE " + "HASC" + " " +
                "(" +
                "hID INTEGER NOT NULL" + ", " +
                "cID VARCHAR(20) NOT NULL" + ", " +
                "FOREIGN KEY (hID) REFERENCES HOMEWORK, " +
                "FOREIGN KEY (cID) REFERENCES COURSE"
                +")";

        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(query);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        logging.info("\n HASC Done");
        System.out.println("\n HASC Done");*/


        // Has EVERYTHING - Submissions

        query = "CREATE TABLE " + "SUBMISSION" + " " +
                "(" +
                "hID INTEGER NOT NULL" + ", " +
                "sID VARCHAR(20) NOT NULL" + ", " +
                "ansID INTEGER NOT NULL" + ", " +
                "attNo INTEGER NOT NULL" + ", " + // Attempt number
                "timeStamp TIMESTAMP NOT NULL" + ", " +
                "selectedAns INTEGER NOT NULL" + ", " +
                "qScore INTEGER NOT NULL" + ", " +
                "FOREIGN KEY (hID) REFERENCES HOMEWORK, " +
                "FOREIGN KEY (sID) REFERENCES STUDENT, " +
                "FOREIGN KEY (ansID) REFERENCES ANSWER, " +
                "PRIMARY KEY (hID, sID, ansID, attNo)"
                +")";

        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(query);
            System.out.println("\n SUBMISSION Done");
        } catch (SQLException e) {
            logging.info("\n SUBMISSION Fail");
            System.out.println(e.getMessage());
        }




        // Login Table
        query = "CREATE TABLE " + "LOGIN" + " " +
                "(" +
                "uName VARCHAR(20) NOT NULL" + ", " +
                "password VARCHAR(20) NOT NULL" + ", " +
                "designation VARCHAR(10) CHECK (designation IN ('student', 'ta', 'instructor')) NOT NULL" + ", " +
                "PRIMARY KEY (uName)"
                +")";

        try {
            stmt = conn.createStatement();
            stmt.executeUpdate(query);
            System.out.println("\n LOGIN Done");
        } catch (SQLException e) {
            logging.info("\n LOGIN Fail");
            System.out.println(e.getMessage());
        }




	}
}
