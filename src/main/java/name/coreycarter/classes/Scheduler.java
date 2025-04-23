package name.coreycarter.classes;
import java.util.ArrayList;
import java.util.List;

import name.coreycarter.utils.Graph;

public class Scheduler {
    public int class_count = 0;
    public Scheduler(Graph<Course> graph) {
        // Constructor
    }

    public List<Semester> credits_sequence(Students info, Graph<Course> courseGraph) {
        List<Course> hold = new ArrayList<>();
        List<Semester> old_semester = new ArrayList<>();
        List<Course> unscheduledCourses = new ArrayList<>();
        List<Semester> sequence = new ArrayList<>();
        int maxCredits = info.get_max_credits_per_semeter();
        int semester = info.start_date();
        int totalCourses = Graph_size(courseGraph);

        System.out.println("Starting credits_sequence method...");
        System.out.println("Max credits per semester: " + maxCredits);
        System.out.println("Total courses to schedule: " + totalCourses);

        while (class_count < totalCourses || !unscheduledCourses.isEmpty()) {
            System.out.println("Starting new semester...");
            System.out.println("Current class count: " + class_count);
            System.out.println("Courses left to process: " + unscheduledCourses);

            int credits = 0;
            unscheduledCourses = processUnscheduledCourses(unscheduledCourses, hold, courseGraph, old_semester, maxCredits, credits);

            credits = processNewCourses(unscheduledCourses,totalCourses, courseGraph, hold, old_semester, maxCredits, credits);
            //class_count += hold.size();

            // if (credits == 0 && unscheduledCourses.isEmpty()) {
            //     System.err.println("Infinite loop detected: Unable to schedule remaining courses.");
            //     throw new IllegalStateException("Infinite loop detected: Unable to schedule remaining courses.");
            // }

            updateOldSemester(old_semester, semester, hold);
            sequence.add(new Semester(semester, Semester.Term.Fall, new ArrayList<>(hold)));
            semester++;
            hold.clear();
            System.out.println("ECurrent class count: " + class_count);
            System.out.println("ECourses left to process: " + unscheduledCourses);
        }
        
        System.out.println("Finished scheduling all courses.");
        return sequence;
    }

    private List<Course> processUnscheduledCourses(List<Course> unscheduledCourses, List<Course> hold, Graph<Course> courseGraph, List<Semester> old_semester, int maxCredits, int credits) {
        List<Course> tempLeft = new ArrayList<>(unscheduledCourses);
        unscheduledCourses.clear();

        for (Course course : tempLeft) {
            System.out.println("Considering leftover course: " + course.getName());
            if (credits < maxCredits && take_course(course, hold, courseGraph, old_semester)) {
                hold.add(course);
                credits += course.getCredits();
                System.out.println("Added course to hold: " + course.getName());
                if(course.getLab()!=null){
                    hold.add(course.getLab());
                    credits += course.getLab().getCredits();
                    System.out.println("LAdded course to hold: " + course.getLab().getName());
                }
            } else {
                unscheduledCourses.add(course);
                System.out.println("Course cannot be scheduled yet: " + course.getName());
            }
        }
        return unscheduledCourses;
    }

    private int processNewCourses(List<Course> unscheduledCourses ,int totalCourses, Graph<Course> courseGraph, List<Course> hold, List<Semester> old_semester, int maxCredits, int credits) {
        
        while (credits < maxCredits && class_count < totalCourses) {
            Course course = courseGraph.topologicalSortM().get(class_count);
            System.out.println("Considering new course: " + course.getName());
            if (take_course(course, hold, courseGraph, old_semester)) {
                hold.add(course);
                credits += course.getCredits();
                System.out.println("Added course to hold: " + course.getName());
                if(course.getLab()!=null){
                    hold.add(course.getLab());
                    credits += course.getLab().getCredits();
                    System.out.println("xAdded course to hold: " + course.getLab().getName());
                }
            } else {
                unscheduledCourses.add(course);
                System.out.println("Course cannot be scheduled yet: " + course.getName());
            }
            class_count++;
        }
        return credits;
    }

    private void updateOldSemester(List<Semester> old_semester, int semester, List<Course> hold) {
        old_semester.add(new Semester(semester, Semester.Term.Fall, new ArrayList<>(hold)));
        System.out.println("Old semester contents updated with current hold: " + old_semester);
    }

    public String printSemester(Students info, Graph<Course> courseGraph, int semester, List<Course> hold, List<Semester> sequence) {
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
        return true;
    }

    public boolean check(Course dep, List<Semester> final_semesters) {
        System.out.println("Checking dependency: " + dep.getName());
        for (Semester semester : final_semesters) {
            System.out.println("Checking in semester: " + semester);
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

    public boolean hasBidirectionalDependency(Course course, Graph<Course> courseGraph) {
        System.out.println("Checking for bidirectional dependencies for course: " + course.getName());
        List<Course> outgoingEdges = courseGraph.getOutgoingEdges(course);
        for (Course outgoing : outgoingEdges) {
            List<Course> incomingEdges = courseGraph.getIncomingEdges(outgoing);
            if (incomingEdges.contains(course)) {
                System.out.println("Bidirectional dependency found between " + course.getName() + " and " + outgoing.getName());
                return true;
            }
        }
        System.out.println("No bidirectional dependencies found for course: " + course.getName());
        return false;
    }
}
