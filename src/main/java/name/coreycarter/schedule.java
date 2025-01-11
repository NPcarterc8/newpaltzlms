package name.coreycarter;
import java.sql.SQLException;

import name.coreycarter.classes.Course;
import name.coreycarter.classes.Students;

public class schedule {
        public static void main(String[] args) {
          try {
            Students myObj = new Students();
            Course nmCourse = new Course();
            System.out.print(myObj.getAllFromStudentTable());
            System.out.print(nmCourse.csOrder());
          } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
          }
        }
}
