package name.coreycarter;
import java.sql.SQLException;

import name.coreycarter.utils.Graph;
public class main {
    public static void main(String args[]) throws SQLException, ClassNotFoundException {
        Graph<Integer> graph = new Graph<>();
        graph.addEdge(1, 2, false);
        graph.addEdge(1, 3, false);
        graph.addEdge(2, 4, false);
        graph.addEdge(3, 4, false);
        try {
            System.err.println(graph.topologicalSortM());
        } catch (RuntimeException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        
    }
}
 
