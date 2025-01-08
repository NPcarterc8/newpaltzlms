package name.coreycarter.Connections;
//import name.coreycarter.password;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class dbconn 
{
    private static final String DB_URL = "jdbc:mysql://localhost:3306/newpaltzlms?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String USER = "root";
    //private static final String conn2 = "DB_URL" + "USER" + "password.PASSWORD";

    public Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection con = DriverManager.getConnection(DB_URL, USER, password.PASSWORD);
        return con;
      }

}
