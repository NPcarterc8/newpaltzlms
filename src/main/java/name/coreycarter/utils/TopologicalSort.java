package name.coreycarter.utils;

//package name.coreycarter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TopologicalSort<T> {

    private List<T> sortedNodes = new ArrayList<>();
    private Set<T> temporaryMarks = new HashSet<>();
    private Set<T> permanentMarks = new HashSet<>();
    private Graph<T> graph;

    public TopologicalSort(Graph<T> graph) {
        this.graph = graph;
        sortedNodes.clear();
        temporaryMarks.clear();
        permanentMarks.clear();
    }

    public List<T> topologicalSort() {
        sortedNodes.clear();
        temporaryMarks.clear();
        permanentMarks.clear();

        for (T node : graph.nodeset()) {
            if (!permanentMarks.contains(node)) {
                if (!visit(node)) {
                    throw new RuntimeException("Graph has at least one cycle");
                }
            }
        }
        return sortedNodes;
    }

    private boolean visit(T node) {
        //System.out.println(node+"  1");
        if (permanentMarks.contains(node)) {
            return true;
        }
        //System.out.println(node+"  2");
        if (temporaryMarks.contains(node)) {
            return false; // Cycle detected
        }

        temporaryMarks.add(node);

        // Visit prerequisites first
        for (T neighbor : graph.Tneighbours(node)) {
            if (!visit(neighbor)) {
                return false;
            }
        }

        temporaryMarks.remove(node);
        permanentMarks.add(node);
        sortedNodes.add(0, node); // Add after dependencies

        return true;
    }

}
