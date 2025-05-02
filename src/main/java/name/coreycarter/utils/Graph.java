package name.coreycarter.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Graph<T> {

    // We use Hashmap to store the edges in the graph
    private Map<T, List<T>> map = new HashMap<>();
    private Map<T, List<T>> coRequisites = new HashMap<>();

    // This function adds a new vertex to the graph
    public void addVertex(T s) {
        //map.put(s, new LinkedList<T>());
        map.putIfAbsent(s, new ArrayList<>());
    }

    public Map<T, List<T>> getCoRequisites() {
        return coRequisites;
    }

    // This function adds the edge
    // between source to destination
    public void addEdge(T source, T destination, boolean isCoReq) {
        if (!map.containsKey(source)) {
            addVertex(source);
        }
        if (!map.containsKey(destination) && isCoReq != true) {
            addVertex(destination);
        }

        if (isCoReq) {
            coRequisites.putIfAbsent(source, new ArrayList<>());
            coRequisites.get(source).add(destination);
        } else {
            map.get(source).add(destination);
        }
    }

    // This function gives the count of vertices
    public void getVertexCount() {
        System.out.println("The graph has " + map.keySet().size() + " vertex");
    }

    // This function gives the count of edges
    public void getEdgesCount(boolean bidirection) {
        int count = 0;
        for (T v : map.keySet()) {
            count += map.get(v).size();
        }
        if (bidirection == true) {
            count = count / 2;
        }
        System.out.println("The graph has " + count + " edges.");
    }

    // This function gives whether
    // a vertex is present or not.
    public void hasVertex(T s) {
        if (map.containsKey(s)) {
            System.out.println("The graph contains " + s + " as a vertex.");
        } else {
            System.out.println("The graph does not contain " + s + " as a vertex.");
        }
    }

    // This function gives whether an edge is present or
    // not.
    public void hasEdge(T s, T d) {
        if (map.get(s).contains(d)) {
            System.out.println("The graph has an edge between " + s + " and " + d + ".");
        } else {
            System.out.println("The graph does not contain an edge between " + s + " and " + d + ".");
        }
    }

    public List<T> printneighbours(T s) {
        if (!map.containsKey(s)) {
            return Collections.emptyList();
        }
        System.out.println("The neighbours of " + s + " are");
        for (T w : map.get(s)) {
            System.out.print(w + ",");
        }
        return map.get(s);
    }

    public List<T> Tneighbours(T s) {
        return map.getOrDefault(s, Collections.emptyList());
    }

    public Set<T> nodeset() {
        return map.keySet();
    }

    public List<T> topologicalSortM() {
        TopologicalSort<T> x = new TopologicalSort<>(this);
        return x.topologicalSort();
    }

    // This function checks if the first node is the source of the second node
    public boolean isSourceOf(T source, T destination) {
        return map.containsKey(source) && map.get(source).contains(destination);
    }

    // This function checks if there is an edge from source to destination
    public boolean isDestinationof(T source, T destination) {
        return map.containsKey(source) && map.get(source).contains(destination);
    }
    // Existing methods in the Graph class

// Method to get incoming edges for a given node
    public List<T> getIncomingEdges(T node) {
        List<T> incomingEdges = new ArrayList<>();
        for (Map.Entry<T, List<T>> entry : map.entrySet()) {
            if (entry.getValue().contains(node)) {
                incomingEdges.add(entry.getKey());
            }
        }
        return incomingEdges;
    }

    // Prints the adjacency list of each vertex.
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("=== Course Dependency Graph ===\n\n");

        for (T course : map.keySet()) {
            builder.append("➤ ").append(course.toString()).append("\n");

            List<T> deps = map.get(course);
            if (deps.isEmpty()) {
                builder.append("    └─ No prerequisites\n");
            } else {
                for (T dep : deps) {
                    builder.append("    └─ ").append(dep.toString()).append("\n");
                }
            }
            builder.append("\n");
        }

        if (!coRequisites.isEmpty()) {
            builder.append("=== Co-Requisites ===\n\n");
            for (T course : coRequisites.keySet()) {
                builder.append("➤ ").append(course.toString()).append(" has co-requisites:\n");
                for (T co : coRequisites.get(course)) {
                    builder.append("    └─ ").append(co.toString()).append("\n");
                }
                builder.append("\n");
            }
        }

        return builder.toString();
    }

    public List<T> getOutgoingEdges(T node) {
        List<T> outgoingEdges = new ArrayList<>();
        if (map.containsKey(node)) {
            outgoingEdges.addAll(map.get(node));
        }
        return outgoingEdges;
    }
}
