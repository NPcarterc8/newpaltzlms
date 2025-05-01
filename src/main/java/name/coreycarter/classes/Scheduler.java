package name.coreycarter.classes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import name.coreycarter.utils.Graph;

public class Scheduler {

    public int class_count = 0;

    public Scheduler(Graph<Course> graph) {
        System.out.println("Scheduler initialized with graph: " + graph);
    }

    // Inside Scheduler.java (added detailed debug print statements for each step)

public List<Semester> sequence(Students info, Graph<Course> courseGraph) {
    System.out.println("Starting sequence method...");
    List<Sect> hold = new ArrayList<>();
    List<Semester> old_semester = new ArrayList<>();
    List<Course> unscheduledCourses = new ArrayList<>();
    List<Semester> sequence = new ArrayList<>();
    Map<String, Integer> failureCounts = new HashMap<>();
    Set<String> failedThisSemester = new HashSet<>();

    int maxCredits = info.get_max_credits_per_semeter();
    int semester = info.start_date();
    int totalCourses = Graph_size(courseGraph);

    System.out.println("Max credits per semester: " + maxCredits);
    System.out.println("Total courses to schedule: " + totalCourses);

    while (class_count < totalCourses || !unscheduledCourses.isEmpty()) {
        System.out.println("\n=== Starting new semester: " + semester + " ===");
        System.out.println("Class count: " + class_count);
        System.out.println("Unscheduled courses: " + courseNames(unscheduledCourses));

        failedThisSemester.clear();
        int credits = 0;
        int initialClassCount = class_count;
        int initialUnscheduledCount = unscheduledCourses.size();

        System.out.println("\nProcessing leftover unscheduled courses...");
        unscheduledCourses = processUnscheduledCourses(unscheduledCourses, hold, courseGraph, old_semester, maxCredits, credits);

        System.out.println("\nProcessing newly available courses...");
        credits = processNewCourses(unscheduledCourses, totalCourses, courseGraph, hold, old_semester, maxCredits, credits);

        System.out.println("\nCurrent hold (pre-validation): " + sectNames(hold));

        if (class_count == initialClassCount && unscheduledCourses.size() == initialUnscheduledCount) {
            System.out.println("\n!!! No progress made this round. Potential deadlock. Breaking loop. !!!");
            System.out.println("Current hold: " + sectNames(hold));
            System.out.println("Remaining unscheduled courses: " + courseNames(unscheduledCourses));
            for (Course c : unscheduledCourses) {
                String cname = c.getName();
                failureCounts.put(cname, failureCounts.getOrDefault(cname, 0) + 1);
                System.out.println("Debug: Checking why course not schedulable: " + cname);
                List<Course> deps = courseGraph.getIncomingEdges(c);
                System.out.println("  Dependencies: " + courseNames(deps));
                for (Course dep : deps) {
                    boolean satisfied = !check(dep, old_semester);
                    System.out.println("    Dependency " + dep.getName() + " satisfied: " + satisfied);
                }
            }
            break;
        }

        System.out.println("\nValidating that lecture/lab pairs do not self-conflict...");
        List<Sect> validatedHold = new ArrayList<>();
        Map<Course, List<Sect>> groupedByCourse = new HashMap<>();
        for (Sect s : hold) {
            groupedByCourse.computeIfAbsent(s.getCourse(), k -> new ArrayList<>()).add(s);
        }

        hold.clear();
        for (Map.Entry<Course, List<Sect>> entry : groupedByCourse.entrySet()) {
            List<Sect> sections = entry.getValue();
            Course course = entry.getKey();
            boolean added = false;
            for (int i = 0; i < sections.size(); i++) {
                for (int j = i + 1; j < sections.size(); j++) {
                    Sect s1 = sections.get(i);
                    Sect s2 = sections.get(j);
                    if (!time_conflict(s1, s2) && !weekday_conflict(s1, s2)) {
                        System.out.println("  Selected valid pair: " + s1.getName() + " + " + s2.getName());
                        validatedHold.add(s1);
                        validatedHold.add(s2);
                        added = true;
                        break;
                    } else {
                        System.out.println("  Skipping invalid pair: " + s1.getName() + " + " + s2.getName());
                    }
                }
                if (added) break;
            }
            if (!added && sections.size() == 1) {
                validatedHold.add(sections.get(0));
                System.out.println("  Accepted solo section: " + sections.get(0).getName());
            } else if (!added && sections.size() > 1) {
                System.out.println("  Could not add any valid pairs for course: " + course.getName());
                for (Sect s : sections) {
                    System.out.println("    Considered: " + s.getName());
                }
            }
        }
        hold.addAll(validatedHold);

        System.out.println("\nHold after validation: " + sectNames(hold));

        System.out.println("\nChecking for conflicts between different courses...");
        List<Sect> tempHold = new ArrayList<>(hold);
        for (int i = 0; i < tempHold.size(); i++) {
            Sect course1 = tempHold.get(i);
            for (int j = i + 1; j < tempHold.size(); j++) {
                Sect course2 = tempHold.get(j);
                if (course1 == course2) continue;

                boolean timeConflict = time_conflict(course1, course2);
                boolean weekdayConflict = weekday_conflict(course1, course2);
                boolean hasConflict = timeConflict || weekdayConflict;

                if (hasConflict && course1.getCourse() == course2.getCourse()) {
                    System.out.println("Self conflict detected: " + course1.getName());
                    if (timeConflict) System.out.println("  Reason: Time conflict");
                    if (weekdayConflict) System.out.println("  Reason: Weekday conflict");
                    hold.removeIf(s -> s.getCourse() == course1.getCourse());
                    if (!unscheduledCourses.contains(course1.getCourse()) && !failedThisSemester.contains(course1.getName())) {
                        unscheduledCourses.add(course1.getCourse());
                        failedThisSemester.add(course1.getName());
                        System.out.println("  Dropped from hold due to self-conflict: " + course1.getCourse().getName());
                    }
                } else if (hasConflict) {
                    System.out.println("Conflict: " + course1.getName() + " vs " + course2.getName());
                    if (timeConflict) System.out.println("  Reason: Time conflict");
                    if (weekdayConflict) System.out.println("  Reason: Weekday conflict");
                    hold.remove(course2);
                    if (!unscheduledCourses.contains(course2.getCourse()) && !failedThisSemester.contains(course2.getName())) {
                        unscheduledCourses.add(course2.getCourse());
                        failedThisSemester.add(course2.getName());
                        System.out.println("  Dropped from hold due to external conflict: " + course2.getCourse().getName());
                    }
                }
            }
        }

        List<Sect> finalizedHold = new ArrayList<>(hold);
        updateOldSemester(old_semester, semester, finalizedHold);
        sequence.add(new Semester(semester, Semester.Term.Fall, finalizedHold));

        System.out.println("Finalized hold for semester " + semester + ": " + sectNames(finalizedHold));

        semester++;
        hold.clear();
        System.out.println("=== End of semester " + (semester - 1) + " ===");
    }

    System.out.println("\nFinished scheduling all courses.");
    System.out.println("Final Schedule:");
    for (Semester sem : sequence) {
        System.out.println(sem);
    }

    System.out.println("\nCourse failure summary:");
    for (Map.Entry<String, Integer> entry : failureCounts.entrySet()) {
        System.out.println(entry.getKey() + " failed to schedule " + entry.getValue() + " time(s)");
    }

    return sequence;
}

    
    private List<String> courseNames(List<Course> courses) {
        List<String> names = new ArrayList<>();
        for (Course c : courses) {
            names.add(c.getName());
        }
        return names;
    }
    
    private List<String> sectNames(List<Sect> sections) {
        List<String> names = new ArrayList<>();
        for (Sect s : sections) {
            names.add(s.getName());
        }
        return names;
    }

    private boolean weekday_conflict(Sect course1, Sect course2) {
        System.out.println("Checking weekday conflict between " + course1.getName() + " and " + course2.getName());
        List<String> weekdays1 = course1.getWeekdays();
        List<String> weekdays2 = course2.getWeekdays();

        for (String day1 : weekdays1) {
            if (weekdays2.contains(day1)) {
                System.out.println("Conflict found on " + day1);
                return true;
            }
        }

        System.out.println("No weekday conflict found.");
        return false;
    }

    private List<Course> processUnscheduledCourses(List<Course> unscheduledCourses, List<Sect> hold, Graph<Course> courseGraph, List<Semester> old_semester, int maxCredits, int credits) {
        System.out.println("Processing unscheduled courses...");
        List<Course> tempLeft = new ArrayList<>(unscheduledCourses);
        unscheduledCourses.clear();

        for (Course course : tempLeft) {
            System.out.println("Considering leftover course: " + course.getName());
            if (credits < maxCredits && take_course(course, hold, courseGraph, old_semester)) {
                hold.add(course.getSect().get(0));
                credits += course.getCredits();
                System.out.println("Added course to hold: " + course.getName());
                if (course.getLab() != null) {
                    hold.add(course.getLab());
                    credits += course.getlabCredits();
                    System.out.println("Added lab to hold: " + course.getLab().getName());
                }
            } else {
                unscheduledCourses.add(course);
                System.out.println("Course cannot be scheduled yet: " + course.getName());
            }
        }
        return unscheduledCourses;
    }

    private int processNewCourses(List<Course> unscheduledCourses, int totalCourses, Graph<Course> courseGraph, List<Sect> hold, List<Semester> old_semester, int maxCredits, int credits) {
        System.out.println("Processing new courses...");
        while (credits < maxCredits && class_count < totalCourses) {
            Course course = courseGraph.topologicalSortM().get(class_count);
            System.out.println("Considering new course: " + course.getName());
            if (take_course(course, hold, courseGraph, old_semester)) {
                hold.add(course.getSect().get(0));
                credits += course.getCredits();
                System.out.println("Added course to hold: " + course.getName());
                if (course.getLab() != null) {
                    hold.add(course.getLab());
                    credits += course.getlabCredits();
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

    private void updateOldSemester(List<Semester> old_semester, int semester, List<Sect> hold) {
        Semester newSemester = new Semester(semester, Semester.Term.Fall, new ArrayList<>(hold));
        old_semester.add(newSemester);
        System.out.println("Old semester contents updated with current hold:");
        for (Semester s : old_semester) {
            System.out.println(s);
        }
    }

    public String printSemester(Students info, Graph<Course> courseGraph, int semester, List<Sect> hold, List<Semester> sequence) {
        StringBuilder semesterOutput = new StringBuilder("semester " + semester + ": ");
        Semester t2 = new Semester(semester, Semester.Term.Fall, hold);
        System.out.println("Printing semester: " + t2);
        for (Sect course : hold) {
            String time = class_time(course);
            String startTime = course.getStartTime();
            String endTime = course.getEndTime();
            semesterOutput.append(course.getName())
                    .append("(").append(course.getCredits()).append(" credits, ")
                    .append("Start: ").append(startTime).append(", ")
                    .append("End: ").append(endTime).append(", ")
                    .append(time).append(") ");
        }
        return semesterOutput.toString();
    }

    public boolean take_course(Course i, List<Sect> hold, Graph<Course> courseGraph, List<Semester> final_semesters) {
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
            if (semester.getCourses().contains(dep)) {
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

    public String class_time(Sect course) {
        System.out.println("Calculating class time for course: " + course.getName());
        try {
            String startTimeStr = course.getStartTime();
            String endTimeStr = course.getEndTime();

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

    public boolean time_conflict(Sect course1, Sect course2) {
        System.out.println("Checking time conflict between " + course1.getName() + " and " + course2.getName());
        String startTime1 = course1.getStartTime();
        String endTime1 = course1.getEndTime();
        String startTime2 = course2.getStartTime();
        String endTime2 = course2.getEndTime();

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
            List<Sect> courses = semester.getSections();
            for (int i = 0; i < courses.size(); i++) {
                Sect course1 = courses.get(i);
                System.out.println("Checking course: " + course1.getName());
                for (int j = i + 1; j < courses.size(); j++) {
                    Sect course2 = courses.get(j);
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

    public List<String> getDays(List<Sect> courses) {
        System.out.println("Getting days for the list of courses...");
        List<String> days = new ArrayList<>();
        for (Sect course : courses) {
            System.out.println("Processing course: " + course.getName());
            days.addAll(course.getWeekdays());
        }
        System.out.println("Days retrieved: " + days);
        return days;
    }

}
