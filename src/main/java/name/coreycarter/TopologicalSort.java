package name.coreycarter;

//package name.coreycarter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TopologicalSort<T> {
private List<T> sortedNodes = new ArrayList<>();
    private Set<T> temporaryMarks = new HashSet<>();
    private Set<T> permanentMarks = new HashSet<>();
    
    public List<T> topologicalSort(Map<T, List<T>> graph) {
        sortedNodes.clear();
        temporaryMarks.clear();
        permanentMarks.clear();
        
        for (T node : graph.keySet()) {
            if (!permanentMarks.contains(node)) {
                if (!visit(node, graph)) {
                    throw new RuntimeException("Graph has at least one cycle");
                }
            }
        }
        
        return sortedNodes;
    }

    private boolean visit(T node, Map<T, List<T>> graph) {
        if (permanentMarks.contains(node)) {
            return true;
        }
        if (temporaryMarks.contains(node)) {
            return false; // Cycle detected
        }
        
        temporaryMarks.add(node);
        
        for (T neighbor : graph.getOrDefault(node, Collections.emptyList())) {
            if (!visit(neighbor, graph)) {
                return false;
            }
        }
        
        temporaryMarks.remove(node);
        permanentMarks.add(node);
        sortedNodes.add(0, node);
        
        return true;
    }
}
