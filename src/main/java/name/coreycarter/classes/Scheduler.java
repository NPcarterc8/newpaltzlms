package name.coreycarter.classes;

import name.coreycarter.utils.Graph;

public class Scheduler {
    private Graph<Course> graph;
    private int max_credits_per_semeter=15;//move in the frutre

    public Scheduler(Graph<Course> graph) {
        this.graph = graph;
    }

    public void printSemesters() {
        Semeter x= new Semeter(2020,Semeter.Term.Fall,graph.topologicalSortM());
        System.out.print(x);
    }
}
