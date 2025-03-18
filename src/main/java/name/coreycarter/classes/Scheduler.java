package name.coreycarter.classes;
import java.util.ArrayList;
import java.util.List;

import name.coreycarter.utils.Graph;

public class Scheduler {
    private Graph<Course> graph;

    public Scheduler(Graph<Course> graph) {
        this.graph = graph;
    }
        
    public List<Course> order_squence(Graph<Course> courseGraph){
        List<Course> oldOrder = courseGraph.topologicalSortM();
        List<Course> newOrder = new ArrayList<>();
        List<Course> hold = new ArrayList<>();
        for (int i = 0; i < oldOrder.size(); i++) {
            Course course = oldOrder.get(i);
            newOrder.add(course);
        }
        return newOrder;
        }
        
        public List<String> credits_squence(Students info, Graph<Course> courseGraph) {
        int class_count = 0;
        List<Course> hold = new ArrayList<>();
        List<String> sequence = new ArrayList<>();
        int max = info.get_max_credits_per_semeter();
        int term = 0;
        while (class_count <= Graph_size(courseGraph) - 1) {
            int credits = 0;
            while (credits < max && class_count < Graph_size(courseGraph)) {
            hold.add(courseGraph.topologicalSortM().get(class_count));
            credits += class_credits(class_count, courseGraph);
            class_count++;
            }
            StringBuilder semesterOutput = new StringBuilder("term " + (term) + ": ");
            for (Course course : hold) {
            semesterOutput.append(course.getName()).append("(").append(course.getCredits()).append(") ");
            }
            sequence.add(semesterOutput.toString().trim());
            term++;
        }
        return sequence;
        }
        public void printsemeter(Students info, Graph<Course> courseGraph){

        }
        public int class_credits(int class_order, Graph<Course> courseGraph) {
        Course course = courseGraph.topologicalSortM().get(class_order);
        int cc = course.getCredits();
        return cc;
    }
    public int Graph_size( Graph<Course> courseGraph) {
        int size = courseGraph.topologicalSortM().size();
        return size;
    }

}
