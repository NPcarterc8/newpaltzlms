package name.coreycarter.classes;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import name.coreycarter.utils.Graph; // Ensure Course is imported

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
        List<Course> l = new ArrayList<>();
        List<Semeter> final_semeters = new ArrayList<>();
        List<String> sequence = new ArrayList<>();
        int max = info.get_max_credits_per_semeter();
        int semeter = info.start_date();
        int totalCourses = Graph_size(courseGraph);

        while (class_count < totalCourses || !l.isEmpty()) {
            int credits = 0;
            List<Course> tempLeft = new ArrayList<>(l);
            l.clear();

            for (Course consider : tempLeft) {
                if (credits < max && take_course(consider, hold, courseGraph, final_semeters)) {
                    hold.add(consider);
                    credits += consider.getCredits();
                } else {
                    l.add(consider);
                }
            }
            
            while (credits < max && class_count < totalCourses) {
                Course consider = courseGraph.topologicalSortM().get(class_count);
                if (take_course(consider, hold, courseGraph, final_semeters)) {
                    hold.add(consider);
                    credits += consider.getCredits();
                } else {
                    l.add(consider);
                }
                class_count++;
            }
            Semeter t2 = new Semeter(semeter, Semeter.Term.Fall, hold);
            sequence.add(printsemeter(info, courseGraph, semeter, hold, sequence));
            final_semeters.add(t2);
            semeter++;

            hold.clear();
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
//     let deps = all courses that i depends on
// for dep in deps:
//     if dep is not in one of final_semester:
//          return false

// return tru
// e
    public boolean take_course(Course i, List<Course> hold ,Graph<Course> courseGraph, List<Semeter> final_Semeters){
        List<Course> deps_list = courseGraph.getIncomingEdges(i);
        for (Course dep : deps_list) {
            if(check(dep, final_Semeters)){
                return false;
            }
        }
        return true;
    }
    public boolean check(Course dep,List<Semeter> final_Semeters) {
        for (Semeter semeter : final_Semeters) {
            if (semeter.courses.contains(dep)) {
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
