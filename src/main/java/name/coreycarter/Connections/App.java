package name.coreycarter.Connections;
//import name.coreycarter.password;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Hello world!
 *
 */
public class App 
{
    private static final String DB_URL = "jdbc:mysql://localhost:3306/newpaltzlms?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USER = "root";

    public static void main( String[] args )
    {
 try {
            // Load the MySQL JDBC driver explicitly
            Class.forName("com.mysql.cj.jdbc.Driver");

            try (Connection conn = DriverManager.getConnection(DB_URL, USER, password.PASSWORD)) {
                System.out.println("Database connected successfully!");
                getTransactionDistribution(conn);
            }
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC Driver not found. Include it in your library path.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Connection failed! Check the console output for details.");
            e.printStackTrace();
        }
        System.out.println( "Hello World!" );
    }
    
    public static void getTransactionDistribution(Connection conn) throws SQLException {
        String query = "SELECT * from studtend_table";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                System.out.println("names : " + rs.getString("name_"));
                System.out.println("information : " + rs.getInt("stu_id_"));
            }
        }
    }
}
