package name.coreycarter;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import name.coreycarter.classes.Course;
import name.coreycarter.utils.TopologicalSort;
public class main {
    public static void main(String args[]) throws SQLException, ClassNotFoundException {
        TopologicalSort<Integer> sorter = new TopologicalSort<>();
        Map<Integer, List<Integer>> graph = new HashMap<>();
        List<Integer> result = sorter.topologicalSort(graph);
        graph.put(1, Arrays.asList(2, 3));
        graph.put(2, Arrays.asList(4));
        graph.put(3, Arrays.asList(4));
        //graph.put(4, Arrays.asList(1)); // Introduce a cycle
        //graph.put(5, Arrays.asList(6));
        //graph.put(6, Arrays.asList(5)); // Another cycle
        
        try {
            Course g = new Course();
            System.err.println(g.csOrder());
                result = sorter.topologicalSort(graph);
                System.out.println("Topological Order: " + result);
        } catch (SQLException | ClassNotFoundException | RuntimeException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        
    }
}
 
