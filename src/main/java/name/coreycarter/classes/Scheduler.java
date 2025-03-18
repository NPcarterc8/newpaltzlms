package name.coreycarter.classes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import name.coreycarter.utils.Graph;

public class Scheduler {
    private Graph<Course> graph;
    private Students tax_max;
    private Course tax_class;
    private Map<Course, ArrayList<Course>> map = new HashMap<>();

    public Scheduler(Graph<Course> graph) {
        this.graph = graph;
    }

    public void credits_squence(Students info, Graph<Course> courseGraph) {
        int class_count = 0;
        ArrayList<Course> hold = new ArrayList<>();
        ArrayList<Course> list = new ArrayList<>();
        int max = info.get_max_credits_per_semeter();
        int term = 0;
        list.addAll(courseGraph.topologicalSortM());
        while (class_count <= list.size() - 1) {
            int credits = 0;
            while (credits < max && class_count < list.size()) {
                hold.add(courseGraph.topologicalSortM().get(class_count));
                credits += class_credits(class_count, courseGraph);
                class_count++;
            }
            StringBuilder semesterOutput = new StringBuilder("term " + (term) + ": ");
            for (Course course : hold) {
                semesterOutput.append(course.getName()).append("(").append(course.getCredits()).append(") ");
            }
            System.out.println(semesterOutput.toString().trim());
            hold.clear();
            term++;
        }
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
