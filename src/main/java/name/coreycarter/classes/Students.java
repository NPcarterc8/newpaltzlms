package name.coreycarter.classes;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import name.coreycarter.Connections.dbconn;

public class Students extends dbconn {
    Connection con;

    public Students() throws SQLException, ClassNotFoundException {
        this.con = getConnection();
    }
    
    public String getAllFromStudentTable() throws SQLException {
        String query = "SELECT * from studtend_table";
        try (Statement stmts = this.con.createStatement();
             ResultSet rs = stmts.executeQuery(query)) {
            StringBuilder result = new StringBuilder();
            while (rs.next()) {
                result.append("names : ").append(rs.getString("name_"))
                      .append(", information : ").append(rs.getInt("stu_id_")).append("\n");
            }
            return result.toString();
        }
    }
    public String sayHi(){
        return "hello world";
    }
    /*
    public String getAllFromStudentTable() throws SQLException {
        String query = "SELECT * from studtend_table";
        try (Statement stmts = this.con.createStatement();
             ResultSet rs = stmts.executeQuery(query)) {
            StringBuilder result = new StringBuilder();
            while (rs.next()) {
                result.append("names : ").append(rs.getString("name_"))
                      .append(", information : ").append(rs.getInt("stu_id_")).append("\n");
            }
            return result.toString();
        }
    }
    */
}
