package name.coreycarter;
import name.coreycarter.classes.Course;
import name.coreycarter.classes.Scheduler;
import name.coreycarter.utils.Graph;

public class Main {
    public static void main(String[] args) {
        Graph<Course> courseGraph = new Graph<>();
        Scheduler test = new Scheduler(courseGraph);
        
        // Example Courses
        Course Math101 = new Course("Math 101");
        Course Math102 = new Course("Math 102");
        Course Physics101 = new Course("Physics 101");
        Course CS101 = new Course("CS 101");
        Course CS102 = new Course("CS 102");
        Course CS201 = new Course("CS 201");
        Course Algorithms = new Course("Algorithms");
        Course Physics102 = new Course("Physics 102");
        Course Lab101 = new Course("Lab 101");

        courseGraph.addEdge(Math101, Math102, false); // Math 101 → Math 102
        courseGraph.addEdge(Math101, Physics101, false); // Math 101 → Physics 101
        courseGraph.addEdge(CS101, CS102, false); // CS 101 → CS 102
        courseGraph.addEdge(CS102, CS201, false); // CS 102 → CS 201
        courseGraph.addEdge(CS102, Algorithms, false); // CS 102 → Algorithms
        courseGraph.addEdge(Physics101, Physics102, false); // Physics 101 → Physics 102
        
        // Co-requisite: Take "Lab 101" and "Physics 101" together
        courseGraph.addEdge(Physics101, Lab101, true); // Bidirectional → Co-Req
        
        try {
            System.out.println("Course Order: " + courseGraph.topologicalSortM());
            test.printSemesters();
        } catch (RuntimeException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }
}