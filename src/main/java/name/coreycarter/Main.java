package name.coreycarter;

import java.util.Arrays;

import name.coreycarter.classes.Course;
import name.coreycarter.classes.Scheduler;
import name.coreycarter.classes.Students;
import name.coreycarter.utils.Graph;

public class Main {

    public static void main(String[] args) {

        Graph<Course> courseGraph = new Graph<>();
        Scheduler test = new Scheduler(courseGraph);
        Students t1 = new Students("test", "computer science", "Student", 15, 2020);

        // Example Courses
        Course Math101 = new Course("Math 101", "Dr. Smith", true, 3, "08:00", "09:30", Arrays.asList("Monday", "Wednesday", "Friday"));
        Course Math102 = new Course("Math 102", "Dr. Johnson", true, 3, "10:00", "11:30", Arrays.asList("Tuesday", "Thursday"));
        Course CS101 = new Course("CS 101", "Prof. Brown", true, 3, "08:00", "09:30", Arrays.asList("Monday", "Wednesday", "Friday"));
        Course CS102 = new Course("CS 102", "Prof. Davis", true, 3, "14:00", "15:30", Arrays.asList("Tuesday", "Thursday"));
        Course CS201 = new Course("CS 201", "Dr. Wilson", true, 3, "16:00", "17:30", Arrays.asList("Monday", "Wednesday"));
        Course Algorithms = new Course("Algorithms", "Dr. Taylor", true, 3, "18:00", "19:30", Arrays.asList("Tuesday", "Thursday"));
        Course Lab101 = new Course("Lab 101", "Ms. Anderson", false, 3, "09:00", "10:30", Arrays.asList("Monday", "Wednesday"));
        Course Lab102 = new Course("Lab 102", "Ms. Thomas", false, 1, "11:00", "12:30", Arrays.asList("Tuesday", "Thursday"));
        Course Lab103 = new Course("Lab 103", "Ms. Jackson", false, 1, "13:00", "14:30", Arrays.asList("Monday", "Wednesday"));
        Course Physics101 = new Course("Physics 101", "Dr. White", true, 3, "15:00", "16:30", Arrays.asList("Tuesday", "Thursday"), Lab101);
        Course Physics102 = new Course("Physics 102", "Dr. Harris", true, 3, "17:00", "18:30", Arrays.asList("Monday", "Wednesday"), Lab102);
        Course Physics103 = new Course("Physics 103", "Dr. Martin", true, 3, "19:00", "20:30", Arrays.asList("Tuesday", "Thursday"), Lab103);

        courseGraph.addEdge(Math101, Math102, false); // Math 101 → Math 102
        courseGraph.addEdge(CS201, Physics101, false); // Math 101 → Math 102
        courseGraph.addEdge(Math101, Physics101, false); // Math 101 → Physics 101
        courseGraph.addEdge(CS101, CS102, false); // CS 101 → CS 102
        courseGraph.addEdge(CS102, CS201, false); // CS 102 → CS 201
        courseGraph.addEdge(CS102, Algorithms, false); // CS 102 → Algorithms
        courseGraph.addEdge(Physics101, Physics102, false); // Physics 101 → Physics 102
        courseGraph.addEdge(Physics102, Physics103, false);

        // Co-requisite: Take "Lab 101" and "Physics 101" together
        courseGraph.addEdge(Physics101, Lab101, true); // Bidirectional → Co-Req
        courseGraph.addEdge(Physics102, Lab102, true); // Bidirectional → Co-Req
        courseGraph.addEdge(Physics103, Lab103, true); // Bidirectional → Co-Req

        try {
            System.out.println(test.sequence(t1, courseGraph));
            //System.out.println(test.checkTimeConflicts(test.sequence(t1, courseGraph)));
            //System.out.println(test.getDays(Arrays.asList(Math101)));
        } catch (RuntimeException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }
}
