package name.coreycarter;
import java.sql.SQLException;
import java.util.*;
import name.coreycarter.classes.Course;
import name.coreycarter.classes.Students;
import name.coreycarter.classes.Graph;



// Driver Code
public class Main {

    public static void main(String args[])
    {

        // Object of graph is created.
        Graph g = new Graph();

        // edges are added.
        // Since the graph is bidirectional,
        // so boolean bidirectional is passed as true.
        g.addEdge(0, 1, true);
        g.addEdge(0, 4, true);
        g.addEdge(1, 2, true);
        g.addEdge(1, 3, true);
        g.addEdge(1, 4, true);
        g.addEdge(2, 3, true);
        g.addEdge(3, 4, true);

        // Printing the graph
        System.out.println("Graph:\n" + g.toString());

        // Gives the no of vertices in the graph.
        g.getVertexCount();

        // Gives the no of edges in the graph.
        g.getEdgesCount(true);

        // Tells whether the edge is present or not.
        g.hasEdge(3, 4);

        // Tells whether vertex is present or not
        g.hasVertex(5);
               g.neighbours(1);

    }
}
