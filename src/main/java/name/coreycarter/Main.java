package name.coreycarter;

import java.util.Arrays;
import java.util.List;

import name.coreycarter.classes.Course;
import name.coreycarter.classes.Scheduler;
import name.coreycarter.classes.Sect;
import name.coreycarter.classes.Semester;
import name.coreycarter.classes.Students;
import name.coreycarter.utils.Graph;

public class Main {

    public static void main(String[] args) {

        Graph<Course> courseGraph = new Graph<>();
        Scheduler test = new Scheduler(courseGraph);
        Students t1 = new Students("test", "computer science", "Student", 15, 2020);

        // Section definitions
        Sect math101_01 = new Sect("01", "t", "08:00", "09:30", Arrays.asList("Monday", "Wednesday", "Friday"));
        Sect math101_02 = new Sect("02", "t", "11:00", "12:30", Arrays.asList("Monday", "Wednesday"));
        Sect math102_01 = new Sect("01", "t", "10:00", "11:30", Arrays.asList("Monday", "Wednesday", "Friday"));
        Sect cs101_01 = new Sect("01", "t", "11:00", "12:30", Arrays.asList("Monday", "Wednesday"));
        Sect algorithms_01 = new Sect("01", "t", "13:00", "14:30", Arrays.asList("Tuesday", "Thursday"));
        Sect cs102_01 = new Sect("01", "t", "14:00", "15:30", Arrays.asList("Monday", "Wednesday"));
        Sect cs201_01 = new Sect("01", "t", "15:00", "16:30", Arrays.asList("Tuesday", "Thursday"));

        // FIX: Separate lecture & lab sections
        Sect physics101_lecture = new Sect("01", "t", "16:00", "17:30", Arrays.asList("Monday", "Wednesday"));
        Sect physics101_lab = new Sect("L1", "t", "09:00", "10:30", Arrays.asList("Friday"));
        Sect physics102_lecture = new Sect("01", "t", "17:00", "18:30", Arrays.asList("Tuesday", "Thursday"));
        Sect physics102_lab = new Sect("L1", "t", "11:00", "12:30", Arrays.asList("Saturday"));
        Sect physics103_01 = new Sect("01", "t", "18:00", "19:30", Arrays.asList("Tuesday", "Thursday"));

        // Courses
        Course Math101 = new Course("Math 101", true, 3, Arrays.asList(math101_01, math101_02));
        Course Math102 = new Course("Math 102", true, 3, Arrays.asList(math102_01));
        Course CS101 = new Course("CS 101", true, 3, Arrays.asList(cs101_01));
        Course CS102 = new Course("CS 102", true, 3, Arrays.asList(cs102_01));
        Course CS201 = new Course("CS 201", true, 3, Arrays.asList(cs201_01));
        Course Algorithms = new Course("Algorithms", true, 3, Arrays.asList(algorithms_01));
        Course Physics101 = new Course("Physics 101", true, 3, 1, Arrays.asList(physics101_lecture), physics101_lab);
        Course Physics102 = new Course("Physics 102", true, 3, 1, Arrays.asList(physics102_lecture), physics102_lab);
        Course Physics103 = new Course("Physics 103", true, 3, Arrays.asList(physics103_01));

        // Dependencies
        courseGraph.addEdge(Math101, Math102, false);
        courseGraph.addEdge(CS201, Physics101, false);
        courseGraph.addEdge(Math101, Physics101, false);
        courseGraph.addEdge(CS101, CS102, false);
        courseGraph.addEdge(CS102, CS201, false);
        courseGraph.addEdge(CS102, Algorithms, false);
        courseGraph.addEdge(Physics101, Physics102, false);
        courseGraph.addEdge(Physics102, Physics103, false);

        try {
            List<Course> allCourses = Arrays.asList(Math101, Math102, CS101, CS102, CS201, Algorithms, Physics101, Physics102, Physics103);
            List<List<Semester>> allSchedules = test.generateAllSchedules(t1, courseGraph, 3);
            for (int i = 0; i < allSchedules.size(); i++) {
                System.out.println("\n=================== SCHEDULE #" + (i + 1) + " ===================");
                for (Semester sem : allSchedules.get(i)) {
                    System.out.println(sem);
                }
            }

        } catch (RuntimeException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }
}
