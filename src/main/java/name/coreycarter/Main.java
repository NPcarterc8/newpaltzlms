package name.coreycarter;
import name.coreycarter.utils.Graph;

public class main {
    public static void main(String args[]) {
        Graph<String> courseGraph = new Graph<>();
        
        // Example Courses
        courseGraph.addEdge("Math 101", "Math 102", false); // Math 101 → Math 102
        courseGraph.addEdge("Math 101", "Physics 101", false); // Math 101 → Physics 101
        courseGraph.addEdge("CS 101", "CS 102", false); // CS 101 → CS 102
        courseGraph.addEdge("CS 102", "CS 201", false); // CS 102 → CS 201
        courseGraph.addEdge("CS 102", "Algorithms", false); // CS 102 → Algorithms
        courseGraph.addEdge("Physics 101", "Physics 102", false); // Physics 101 → Physics 102
        
        // Co-requisite: Take "Lab 101" and "Physics 101" together
        courseGraph.addEdge("Physics 101", "Lab 101", true); // Bidirectional → Co-Req
        
        try {
            System.out.println("Course Order: " + courseGraph.topologicalSortM());
        } catch (RuntimeException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }
}