package name.coreycarter;

import name.coreycarter.classes.Course;
import name.coreycarter.classes.Scheduler;
import name.coreycarter.utils.Graph;

public class Main {

    public static void main(String[] args) {
        Graph<Course> courseGraph = new Graph<>();
        Scheduler test = new Scheduler(courseGraph);

        // Example Courses
        Course Math101 = new Course("Math 101",true,3);
        Course Math102 = new Course("Math 102",true,3);
        Course Physics101 = new Course("Physics 101",true,3);
        Course CS101 = new Course("CS 101",true,3);
        Course CS102 = new Course("CS 102",true,3);
        Course CS201 = new Course("CS 201",true,3);
        Course Algorithms = new Course("Algorithms",true,3);
        Course Physics102 = new Course("Physics 102",true,3);
        Course Physics103 = new Course("Physics 103",true,3);
        Course Lab101 = new Course("Lab 101",false,1);
        Course Lab102 = new Course("Lab 102",false,1);
        Course Lab103 = new Course("Lab 103",false,1);

        courseGraph.addEdge(Math101, Math102, false); // Math 101 → Math 102
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
            System.out.println("Course Order: " + courseGraph.topologicalSortM());
            test.printSemesters();
        } catch (RuntimeException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }
}
