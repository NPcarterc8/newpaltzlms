package name.coreycarter;
import java.sql.SQLException;

import name.coreycarter.classes.Students;

public class schedule {
        public static void main(String[] args) {
          try {
            Students myObj = new Students();
            //System.out.print(myObj.getAllFromStudentTable());
            System.out.print(myObj.sayHi());
          } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
          }
        }
}
