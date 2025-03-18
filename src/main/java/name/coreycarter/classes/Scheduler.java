package name.coreycarter.classes;
import java.util.ArrayList;

import name.coreycarter.utils.Graph;

public class Scheduler {
    private Graph<Course> graph;
    private Students tax_max;
    private Course tax_class;
    
    

    public Scheduler(Graph<Course> graph) {
        this.graph = graph;
    }

    public void printSemesters(Students info, Graph<Course> courseGraph) {
        int class_count = 0;
        ArrayList<Course> hold = new ArrayList<>();
        int max = info.get_max_credits_per_semeter();
        int term = 0;
        while (class_count <= Graph_size(courseGraph) - 1) {
            int credits = 0;
            while (credits < max && class_count < Graph_size(courseGraph)) {
                hold.add(courseGraph.topologicalSortM().get(class_count));
                credits += class_credits(class_count, courseGraph);
                class_count++;
            }
            StringBuilder semesterOutput = new StringBuilder("Semester " + (term) + ": ");
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
