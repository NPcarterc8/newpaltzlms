package name.coreycarter.classes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import name.coreycarter.utils.Graph;

public class Scheduler {
    private Graph<Course> graph;
    private Map<Course, List<Course>> map = new HashMap<>();

    public Scheduler(Graph<Course> graph) {
        this.graph = graph;
    }
        
    // public List<Course> order_squence(Graph<Course> courseGraph){
    //     List<Course> oldOrder = courseGraph.topologicalSortM();
    //     List<Course> newOrder = new ArrayList<>();
    //     for (int i = 0; i < oldOrder.size() - 1; i++) {
    //         Course course = oldOrder.get(i);
    //         Course courseNext = oldOrder.get(i + 1);
    //         if (!isSourceOf(course, courseNext)) {
    //             newOrder.add(course);
    //         }else{
    //             //hold.add(course);
    //         }
    //     }
    //     // Add the last course if it was not added in the loop
    //     if (!oldOrder.isEmpty()) {
    //         newOrder.add(oldOrder.get(oldOrder.size() - 1));
    //     }
    //     return newOrder;
    // }

    public List<String> credits_squence(Students info, Graph<Course> courseGraph) {
        int class_count = 0;
        List<Course> hold = new ArrayList<>();
        List<String> sequence = new ArrayList<>();
        //List<>
        int max = info.get_max_credits_per_semeter();
        int semeter = 0;
        //int credits = 0;
        while (class_count <= Graph_size(courseGraph) - 1) {
            int credits = 0;
            while (credits < max && class_count < Graph_size(courseGraph)) {
                Course consider = courseGraph.topologicalSortM().get(class_count);
                if(take_course(consider, hold, courseGraph)){
                    hold.add(consider);
                }
                credits += class_credits(class_count, courseGraph);
                class_count++;
            }
            StringBuilder semesterOutput = new StringBuilder("semeter " + (semeter) + ": ");
            Semeter t2= new Semeter(2020, Semeter.Term.Fall, hold);
            for (Course course : hold) {
                semesterOutput.append(course.getName()).append("(").append(course.getCredits()).append(") ");
            }
            sequence.add(semesterOutput.toString().trim());
            semeter++;
        }
        return sequence;
    }
    public boolean take_course(Course i, List<Course> hold ,Graph<Course> courseGraph){
        //check if you take the precourse
        for (Course x : hold) {
            if(courseGraph.isSourceOf(x,i)){
                System.err.println(x+"  "+i+"  "+ courseGraph.isSourceOf(x,i));
                return false;
            }
        }
        return true;
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
