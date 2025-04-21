package name.coreycarter.classes;
import java.util.ArrayList;
import java.util.List;

import name.coreycarter.utils.Graph;

public class Scheduler {

    public Scheduler(Graph<Course> graph) {
        // Constructor
    }

    public List<String> credits_sequence(Students info, Graph<Course> courseGraph) {
        int class_count = 0; 
        List<Course> hold = new ArrayList<>();
        List<Course> l = new ArrayList<>();
        List<Semester> final_semesters = new ArrayList<>();
        List<String> sequence = new ArrayList<>();
        int max = info.get_max_credits_per_semeter();
        int semester = info.start_date();
        int totalCourses = Graph_size(courseGraph);

        System.out.println("Starting credits_sequence method...");
        System.out.println("Max credits per semester: " + max);
        System.out.println("Total courses to schedule: " + totalCourses);

        while (class_count < totalCourses || !l.isEmpty()) { // Process until all courses are scheduled
            System.out.println("Starting new semester...");
            System.out.println("Current class count: " + class_count);
            System.out.println("Courses left to process: " + l);

            int credits = 0;
            List<Course> tempLeft = new ArrayList<>(l);
            l.clear();
            boolean progressMade = false;

            // Process courses left from the previous iteration
            for (Course consider : tempLeft) {
                System.out.println("Considering leftover course: " + consider.getName());
                if (credits < max && take_course(consider, hold, courseGraph, final_semesters)) {
                    hold.add(consider);
                    credits += consider.getCredits();
                    progressMade = true;
                    System.out.println("Added course to hold: " + consider.getName());
                } else {
                    l.add(consider); // Add back if it can't be scheduled
                    System.out.println("Course cannot be scheduled yet: " + consider.getName());
                }
            }

            // Process new courses from the topological sort
            while (credits < max && class_count < totalCourses) {
                Course consider = courseGraph.topologicalSortM().get(class_count);
                System.out.println("Considering new course: " + consider.getName());
                if (take_course(consider, hold, courseGraph, final_semesters)) {
                    hold.add(consider);
                    credits += consider.getCredits();
                    progressMade = true;
                    System.out.println("Added course to hold: " + consider.getName());
                } else {
                    l.add(consider); // Add to the list of unscheduled courses
                    System.out.println("Course cannot be scheduled yet: " + consider.getName());
                }
                class_count++;
            }

            if (!progressMade) {
                System.err.println("Infinite loop detected: Unable to schedule remaining courses.");
                throw new IllegalStateException("Infinite loop detected: Unable to schedule remaining courses.");
            }

            Semester t2 = new Semester(semester, Semester.Term.Fall, hold);
            System.out.println("Scheduled semester: " + semester + " with courses: " + hold);
            sequence.add(printSemester(info, courseGraph, semester, hold, sequence));
            final_semesters.add(t2);
            semester++;

            hold.clear();
        }
        System.out.println("Finished scheduling all courses.");
        return sequence;
    }

    public String printSemester(Students info, Graph<Course> courseGraph, int semester, List<Course> hold, List<String> sequence) {
        StringBuilder semesterOutput = new StringBuilder("semester " + semester + ": ");
        Semester t2 = new Semester(semester, Semester.Term.Fall, hold);
        System.out.println("Printing semester: " + t2);
        for (Course course : hold) {
            semesterOutput.append(course.getName()).append("(").append(course.getCredits()).append(") ");
        }
        return semesterOutput.toString();
    }

    public boolean take_course(Course i, List<Course> hold, Graph<Course> courseGraph, List<Semester> final_semesters) {
        System.out.println("Checking if course can be taken: " + i.getName());
        List<Course> deps_list = courseGraph.getIncomingEdges(i);
        for (Course dep : deps_list) {
            if (check(dep, final_semesters)) {
                System.out.println("Dependency not satisfied for course: " + i.getName() + ", dependency: " + dep.getName());
                return false;
            }
        }
        System.out.println("Course can be taken: " + i.getName());
        // Ensure this return statement is inside a valid method or block
        // Example: If it belongs to the `take_course` method, ensure proper placement
                return true;
    }

    public boolean check(Course dep, List<Semester> final_semesters) {
        for (Semester semester : final_semesters) {
            if (semester.courses.contains(dep)) {
                System.out.println("Dependency satisfied: " + dep.getName());
                return false;
            }
        }
        System.out.println("Dependency not found in any semester: " + dep.getName());
        return true;
    }

    public int class_credits(int class_order, Graph<Course> courseGraph) {
        Course course = courseGraph.topologicalSortM().get(class_order);
        int cc = course.getCredits();
        System.out.println("Class credits for course " + course.getName() + ": " + cc);
        return cc;
    }

    public int Graph_size(Graph<Course> courseGraph) {
        int size = courseGraph.topologicalSortM().size();
        System.out.println("Graph size: " + size);
        return size;
    }
}
