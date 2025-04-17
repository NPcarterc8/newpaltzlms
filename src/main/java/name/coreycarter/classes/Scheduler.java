package name.coreycarter.classes;
import java.util.ArrayList;
import java.util.List;

import name.coreycarter.utils.Graph;

public class Scheduler {
    //private Graph<Course> graph;
    //private Map<Course, List<Course>> map = new HashMap<>();

    public Scheduler(Graph<Course> graph) {
        //this.graph = graph;
    }
    // if the cvlass is not listed oit go intop leftovers
    // from thiere the semter is printed
    // then first it goes thor 
    // pouse class count in a ways top keep track of where we are
    //illrater java for this class
    //

    public List<String> credits_sequence(Students info, Graph<Course> courseGraph) {
        int class_count = 0; 
        List<Course> hold = new ArrayList<>();
        List<Course> l = new ArrayList<>();
        List<Semeter> final_semesters = new ArrayList<>();
        List<String> sequence = new ArrayList<>();
        int max = info.get_max_credits_per_semeter();
        int semester = info.start_date();
        int totalCourses = Graph_size(courseGraph);

        while (class_count < totalCourses || (!l.isEmpty() && class_count < totalCourses)) {
            int credits = 0;
            List<Course> tempLeft = new ArrayList<>(l);
            l.clear();

            for (Course consider : tempLeft) {
                if (credits < max && take_course(consider, hold, courseGraph, final_semesters)) {
                    hold.add(consider);
                    credits += consider.getCredits();
                } else {
                    l.add(consider);
                }
            }
            
            while (credits < max && class_count < totalCourses) {
                Course consider = courseGraph.topologicalSortM().get(class_count);
                if (take_course(consider, hold, courseGraph, final_semesters)) {
                    hold.add(consider);
                    credits += consider.getCredits();
                } else {
                    l.add(consider);
                }
                class_count++;
            }
            Semeter t2 = new Semeter(semester, Semeter.Term.Fall, hold);
            sequence.add(printSemester(info, courseGraph, semester, hold, sequence));
            final_semesters.add(t2);
            semester++;

            hold.clear();
        }

        return sequence;
    }
    public List<String> order_sequence(List<String> x, Graph<Course> courseGraph) {
        List<String> sequence = new ArrayList<>();

        return sequence;
    }

    public String printSemester(Students info, Graph<Course> courseGraph, int semester, List<Course> hold, List<String> sequence) {
        StringBuilder semesterOutput = new StringBuilder("semester " + semester + ": ");
        Semeter t2 = new Semeter(semester, Semeter.Term.Fall, hold);
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
    public boolean take_course(Course i, List<Course> hold ,Graph<Course> courseGraph, List<Semeter> final_semesters){
        List<Course> deps_list = courseGraph.getIncomingEdges(i);
        for (Course dep : deps_list) {
            if(check(dep, final_semesters)){
                return false;
            }
        }
        return true;
    }
    public boolean check(Course dep,List<Semeter> final_semesters) {
        for (Semeter semester : final_semesters) {
            if (semester.courses.contains(dep)) {
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
