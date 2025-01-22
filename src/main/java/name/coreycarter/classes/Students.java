package name.coreycarter.classes;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import name.coreycarter.Connections.dbconn;

public class Students {
    Connection con;

    public Students() throws SQLException, ClassNotFoundException {
        this.con = new dbconn().getConnection();
    }
    
    public String getAllFromStudentTable() throws SQLException {
        String query = "SELECT * from student_table";
        try (Statement stmts = this.con.createStatement();
             ResultSet rs = stmts.executeQuery(query)) {
            StringBuilder result = new StringBuilder();
            while (rs.next()) {
                result.append("names : ").append(rs.getString("name"))
                      .append(", information : ").append(rs.getInt("stu_id")).append("\n");
            }
            return result.toString();
        }
    }
    
}
