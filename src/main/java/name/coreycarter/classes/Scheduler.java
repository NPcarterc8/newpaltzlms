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
    // if the cvlass is not listed oit go intop leftovers
    // from thiere the semter is printed
    // then first it goes thor 
    // pouse class count in a ways top keep track of where we are
    //illrater java for this class
    //

    public List<String> credits_squence(Students info, Graph<Course> courseGraph) {
        int class_count = 0;
        List<Course> hold = new ArrayList<>();
        List<Course> left = new ArrayList<>();
        List<String> sequence = new ArrayList<>();
        int max = info.get_max_credits_per_semeter();
        int semeter = info.start_date();
        int totalCourses = Graph_size(courseGraph);

        while (class_count < totalCourses || !left.isEmpty()) {
            int credits = 0;
            List<Course> tempLeft = new ArrayList<>(left);
            left.clear();

            for (Course consider : tempLeft) {
                if (credits < max && take_course(consider, hold, courseGraph)) {
                    hold.add(consider);
                    credits += consider.getCredits();
                } else {
                    left.add(consider);
                }
            }

            // Process remaining courses if credits allow
            while (credits < max && class_count < totalCourses) {
                Course consider = courseGraph.topologicalSortM().get(class_count);
                if (take_course(consider, hold, courseGraph)) {
                    hold.add(consider);
                    credits += consider.getCredits();
                } else {
                    left.add(consider);
                }
                class_count++;
            }

            // Call the printsemeter method to handle semester output
            sequence.add(printsemeter(info, courseGraph, semeter, hold, sequence));
            semeter++;

            // Clear the hold list for the next semester
            hold.clear();

            // Break the loop if no progress is made to avoid infinite loop
            if (credits == 0 && left.isEmpty()) {
                break;
            }
        }

        return sequence;
    }
    public List<String> order_squence(List<String> x, Graph<Course> courseGraph) {
        List<String> sequence = new ArrayList<>();

        return sequence;
    }

    public String printsemeter(Students info, Graph<Course> courseGraph, int semeter, List<Course> hold, List<String> sequence) {
        StringBuilder semesterOutput = new StringBuilder("semeter " + semeter + ": ");
        Semeter t2 = new Semeter(semeter, Semeter.Term.Fall, hold);
        System.out.println("this is " + t2);
        System.out.println(t2.toString());
        for (Course course : hold) {
            semesterOutput.append(course.getName()).append("(").append(course.getCredits()).append(") ");
        }
        return semesterOutput.toString();
    }
    public boolean take_course(Course i, List<Course> hold ,Graph<Course> courseGraph){
        //check if you take the precourse
        //get a list of old semeret
        //make sure all the dependes in i are in the old semeters
        //live when it alive and when it not
        for (Course x : hold) {
            if(courseGraph.isSourceOf(x,i)){
                System.err.println(x+"  "+i+"  "+ courseGraph.isSourceOf(x,i));
                return false;
            }
        }
        return true;
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
