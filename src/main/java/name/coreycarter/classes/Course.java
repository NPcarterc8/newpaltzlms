package name.coreycarter.classes;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import name.coreycarter.Connections.dbconn;
import name.coreycarter.utils.Graph;

public class Course {
    Connection con;

    public Course() throws SQLException, ClassNotFoundException {
        this.con = new dbconn().getConnection();
    }
    
    public String getAllNamesOfClasses() throws SQLException {
        String query = "SELECT name from course_table";
        try (Statement stmts = this.con.createStatement();
             ResultSet rs = stmts.executeQuery(query)) {
            StringBuilder result = new StringBuilder();
            while (rs.next()) {
                result.append("names : ").append(rs.getString("name")).append("\n");
            }
            return result.toString();
        }
    }
    
    public Graph<String> csOrder() throws SQLException {
        Graph<String> computerscience_order = new Graph<String>();
        String query = "SELECT name from course_table";
        try (Statement stmts = this.con.createStatement();
             ResultSet rs = stmts.executeQuery(query)) {
            String previous = null;
            while (rs.next()) {
                String current = rs.getString("name");
                if (previous != null) {
                    computerscience_order.addEdge(previous, current, false, false);
                }
                previous = current;
            }
            return computerscience_order;
        }
    }
}
