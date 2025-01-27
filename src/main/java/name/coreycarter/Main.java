package name.coreycarter;
import java.sql.SQLException;

import name.coreycarter.classes.Course;

public class Main {
    public static void main(String args[])
    {
        try {
            Course g = new Course();
            System.err.println(g.csOrder());
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
