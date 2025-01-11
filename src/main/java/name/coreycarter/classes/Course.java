package name.coreycarter.classes;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;

import name.coreycarter.Connections.dbconn;

public class Course extends dbconn {
    Connection con;

    public Course() throws SQLException, ClassNotFoundException {
        this.con = getConnection();
    }
    
    public String getAllNamesOfClasses() throws SQLException {
        String query = "SELECT * from course_table";
        try (Statement stmts = this.con.createStatement();
             ResultSet rs = stmts.executeQuery(query)) {
            StringBuilder result = new StringBuilder();
            while (rs.next()) {
                result.append("names : ").append(rs.getString("name")).append("\n");
            }
            return result.toString();
        }
    }
    public LinkedList<String> csOrder() throws SQLException {
        LinkedList<String> computerscience_order = new LinkedList<String>();
        String query = "SELECT * from course_table";
        try (Statement stmts = this.con.createStatement();
             ResultSet rs = stmts.executeQuery(query)) {
            StringBuilder result = new StringBuilder();
            while (rs.next()) {
                computerscience_order.add(rs.getString("name"));
            }
            return computerscience_order;
        }
    }

    
}
