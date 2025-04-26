package name.coreycarter.classes;

import java.util.ArrayList;
import java.util.List;

import name.coreycarter.utils.Graph;

public class Scheduler {

    public int class_count = 0;

    public Scheduler(Graph<Course> graph) {
        // Constructor
    }

    public List<Semester> sequence(Students info, Graph<Course> courseGraph) {
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

            credits = processNewCourses(unscheduledCourses, totalCourses, courseGraph, hold, old_semester, maxCredits, credits);

            // Check for time conflicts before finalizing the semester
            List<Course> tempHold = new ArrayList<>(hold);
            for (int i = 0; i < tempHold.size(); i++) {
                Course course1 = tempHold.get(i);
                for (int j = i + 1; j < tempHold.size(); j++) {
                    Course course2 = tempHold.get(j);
                    System.out.println("Checking time conflict between " + course1.getName() + " (Start: " + course1.getstart_time() + ", End: " + course1.getend_time() + ") and " + course2.getName() + " (Start: " + course2.getstart_time() + ", End: " + course2.getend_time() + ").");
                    if (time_conflict(course1, course2)) {
                        System.out.println("Time conflict detected between " + course1.getName() + " and " + course2.getName() + ". Removing " + course2.getName() + " from hold.");
                        hold.remove(course2);
                        unscheduledCourses.add(course2);
                    }
                }
            }

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
                if (course.getLab() != null) {
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

    private int processNewCourses(List<Course> unscheduledCourses, int totalCourses, Graph<Course> courseGraph, List<Course> hold, List<Semester> old_semester, int maxCredits, int credits) {

        while (credits < maxCredits && class_count < totalCourses) {
            Course course = courseGraph.topologicalSortM().get(class_count);
            System.out.println("Considering new course: " + course.getName());
            if (take_course(course, hold, courseGraph, old_semester)) {
                hold.add(course);
                credits += course.getCredits();
                System.out.println("Added course to hold: " + course.getName());
                if (course.getLab() != null) {
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
            String time = class_time(course);
            String startTime = course.getstart_time();
            String endTime = course.getend_time();
            semesterOutput.append(course.getName())
                          .append("(").append(course.getCredits()).append(" credits, ")
                          .append("Start: ").append(startTime).append(", ")
                          .append("End: ").append(endTime).append(", ")
                          .append(time).append(") ");
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

    public String class_time(Course course) {
        System.out.println("Calculating class time for course: " + course.getName());
        try {
            String startTimeStr = course.getstart_time();
            String endTimeStr = course.getend_time();

            if (!isValidTimeFormat(startTimeStr) || !isValidTimeFormat(endTimeStr)) {
                System.out.println("Invalid time format for course: " + course.getName());
                return "Invalid time format";
            }

            int totalMinutes = calculateTotalMinutes(startTimeStr, endTimeStr);

            if (totalMinutes < 0) {
                System.out.println("Invalid time range for course: " + course.getName());
                return "Invalid time range";
            }

            String result = formatDuration(totalMinutes);
            System.out.println("Class time for course " + course.getName() + ": " + result);
            return result;
        } catch (NumberFormatException e) {
            System.out.println("Invalid time format for course: " + course.getName());
            return "Invalid time format";
        }
    }

    private boolean isValidTimeFormat(String time) {
        return time != null && time.matches("\\d{2}:\\d{2}");
    }

    private int calculateTotalMinutes(String startTimeStr, String endTimeStr) {
        int startTime = Integer.parseInt(startTimeStr.replace(":", ""));
        int endTime = Integer.parseInt(endTimeStr.replace(":", ""));

        int startHours = startTime / 100;
        int startMinutes = startTime % 100;
        int endHours = endTime / 100;
        int endMinutes = endTime % 100;

        int totalStartMinutes = startHours * 60 + startMinutes;
        int totalEndMinutes = endHours * 60 + endMinutes;

        return totalEndMinutes - totalStartMinutes;
    }

    private String formatDuration(int totalMinutes) {
        int hours = totalMinutes / 60;
        int minutes = totalMinutes % 60;
        return hours + " hours and " + minutes + " minutes";
    }

    public boolean time_conflict(Course course1, Course course2) {
        System.out.println("Checking time conflict between " + course1.getName() + " and " + course2.getName());
        String startTime1 = course1.getstart_time();
        String endTime1 = course1.getend_time();
        String startTime2 = course2.getstart_time();
        String endTime2 = course2.getend_time();

        if (!isValidTimeFormat(startTime1) || !isValidTimeFormat(endTime1)
                || !isValidTimeFormat(startTime2) || !isValidTimeFormat(endTime2)) {
            System.out.println("Invalid time format for one or both courses.");
            return false;
        }

        int start1 = Integer.parseInt(startTime1.replace(":", ""));
        int end1 = Integer.parseInt(endTime1.replace(":", ""));
        int start2 = Integer.parseInt(startTime2.replace(":", ""));
        int end2 = Integer.parseInt(endTime2.replace(":", ""));

        boolean conflict = (start1 < end2 && start2 < end1);
        System.out.println("Time conflict: " + conflict);
        return conflict;
    }

    public boolean checkTimeConflicts(List<Semester> sequence) {
        System.out.println("Checking for time conflicts in the sequence...");
        for (Semester semester : sequence) {
            System.out.println("Checking semester: " + semester);
            List<Course> courses = semester.getCourses();
            for (int i = 0; i < courses.size(); i++) {
                Course course1 = courses.get(i);
                System.out.println("Checking course: " + course1.getName());
                for (int j = i + 1; j < courses.size(); j++) {
                    Course course2 = courses.get(j);
                    System.out.println("Comparing with course: " + course2.getName());
                    if (time_conflict(course1, course2)) {
                        System.out.println("Time conflict detected between " + course1.getName() + " and " + course2.getName() + " in semester " + semester);
                        return true; // Conflict found
                    } else {
                        System.out.println("No conflict between " + course1.getName() + " and " + course2.getName());
                    }
                }
            }
        }
        System.out.println("Finished checking for time conflicts. No conflicts found.");
        return false; // No conflicts found
    }

}
