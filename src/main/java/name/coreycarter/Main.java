package name.coreycarter;

import java.util.Arrays;

import name.coreycarter.classes.Course;
import name.coreycarter.classes.Scheduler;
import name.coreycarter.classes.Sect;
import name.coreycarter.classes.Students;
import name.coreycarter.utils.Graph;

public class Main {

    public static void main(String[] args) {

        Graph<Course> courseGraph = new Graph<>();
        Scheduler test = new Scheduler(courseGraph);
        Students t1 = new Students("test", "computer science", "Student", 15, 2020);
        Sect math101_01 = new Sect("01", "t", "08:00", "09:30", Arrays.asList("Monday", "Wednesday", "Friday"));
        Sect math101_02 = new Sect("02", "t", "09:00", "10:30", Arrays.asList("Tuesday", "Thursday"));
        Sect math102_01 = new Sect("01", "t", "10:00", "11:30", Arrays.asList("Monday", "Wednesday", "Friday"));
        Sect cs101_01 = new Sect("01", "t", "11:00", "12:30", Arrays.asList("Monday", "Wednesday"));
        Sect Algorithms_01 = new Sect("01", "t", "13:00", "14:30", Arrays.asList("Tuesday", "Thursday"));
        Sect cs102_01 = new Sect("01", "t", "14:00", "15:30", Arrays.asList("Monday", "Wednesday"));
        Sect cs201_01 = new Sect("01", "t", "15:00", "16:30", Arrays.asList("Tuesday", "Thursday"));
        Sect physics101_01 = new Sect("01", "t", "16:00", "17:30", Arrays.asList("Monday", "Wednesday"));
        Sect physics102_01 = new Sect("01", "t", "17:00", "18:30", Arrays.asList("Tuesday", "Thursday"));
        Sect physics103_01 = new Sect("01", "t", "18:00", "19:30", Arrays.asList("Tuesday", "Thursday"));
        Sect lab101 = new Sect("01", "t", "09:00", "10:30", Arrays.asList("Monday", "Friday"));
        Sect lab102 = new Sect("01", "t", "11:00", "12:30", Arrays.asList("Tuesday", "Saturday"));
        // Example Courses
        Course Math101 = new Course("Math 101", true, 3, Arrays.asList( math101_01, math101_02));
        Course Math102 = new Course("Math 102", true, 3, Arrays.asList( math102_01));
        Course CS101 = new Course("CS 101", true, 3, Arrays.asList(cs101_01));
        Course CS102 = new Course("CS 102", true, 3, Arrays.asList(cs102_01));
        Course CS201 = new Course("CS 201", true, 3, Arrays.asList(cs201_01));
        Course Algorithms = new Course("Algorithms", true, 3, Arrays.asList(Algorithms_01));
        Course Physics101 = new Course("Physics 101", true, 3,1, Arrays.asList(physics101_01), lab101);
        Course Physics102 = new Course("Physics 102", true, 3,1, Arrays.asList(physics102_01), lab102);
        Course Physics103 = new Course("Physics 103", true, 3, Arrays.asList(physics103_01));

        courseGraph.addEdge(Math101, Math102, false); // Math 101 → Math 102
        courseGraph.addEdge(CS201, Physics101, false); // Math 101 → Math 102
        courseGraph.addEdge(Math101, Physics101, false); // Math 101 → Physics 101
        courseGraph.addEdge(CS101, CS102, false); // CS 101 → CS 102
        courseGraph.addEdge(CS102, CS201, false); // CS 102 → CS 201
        courseGraph.addEdge(CS102, Algorithms, false); // CS 102 → Algorithms
        courseGraph.addEdge(Physics101, Physics102, false); // Physics 101 → Physics 102
        courseGraph.addEdge(Physics102, Physics103, false);


        try {
            System.out.println(test.sequence(t1, courseGraph));
            //System.out.println(test.checkTimeConflicts(test.sequence(t1, courseGraph)));
            //System.out.println(test.time_conflict(Math101, CS101));
        } catch (RuntimeException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }
}
