package name.coreycarter.classes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import name.coreycarter.utils.Graph;

public class Scheduler {

    public int class_count = 0;
    private static final int MAX_STALLED = 10;
    private static final Semester.Term[] TERM_SEQUENCE = Semester.Term.values();

    public Scheduler(Graph<Course> graph) {
        System.out.println("Scheduler initialized with graph: " + graph);
    }

    public List<List<Semester>> generateAllSchedules(Students info, Graph<Course> courseGraph, int maxSchedules) {
        List<List<Semester>> allSchedules = new ArrayList<>();
        List<Course> originalOrder = courseGraph.topologicalSortM();

        for (int i = 0; i < maxSchedules; i++) {
            Collections.shuffle(originalOrder);
            class_count = 0;
            List<Semester> schedule = sequence(info, courseGraph, new ArrayList<>(originalOrder));
            if (!schedule.isEmpty()) {
                allSchedules.add(schedule);
            }
        }

        return allSchedules;
    }

    public List<Semester> sequence(Students info, Graph<Course> courseGraph, List<Course> courseOrder) {
        List<Sect> hold = new ArrayList<>();
        List<Semester> old_semester = new ArrayList<>();
        List<Course> unscheduledCourses = new ArrayList<>();
        List<Semester> sequence = new ArrayList<>();
        Set<String> failedThisSemester = new HashSet<>();

        int maxCredits = info.get_max_credits_per_semeter();
        int year = info.start_date();
        int termIndex = 0;
        int totalCourses = courseOrder.size();
        int stalledRounds = 0;

        while (class_count < totalCourses || !unscheduledCourses.isEmpty()) {
            Semester.Term currentTerm = TERM_SEQUENCE[termIndex % TERM_SEQUENCE.length];
            failedThisSemester.clear();
            int credits = 0;
            int initialClassCount = class_count;
            int initialUnscheduledCount = unscheduledCourses.size();

            unscheduledCourses = processUnscheduledCourses(unscheduledCourses, hold, courseGraph, old_semester, maxCredits, credits);
            credits = processNewCourses(courseOrder, unscheduledCourses, totalCourses, courseGraph, hold, old_semester, maxCredits, credits);

            hold = validateSectionConflicts(hold, unscheduledCourses, failedThisSemester);

            List<Sect> finalizedHold = new ArrayList<>(hold);
            updateOldSemester(old_semester, year, currentTerm, finalizedHold);
            Semester semesterObj = new Semester(year, currentTerm, finalizedHold);
            sequence.add(semesterObj);

            if (class_count == initialClassCount && unscheduledCourses.size() == initialUnscheduledCount) {
                stalledRounds++;
                if (stalledRounds >= MAX_STALLED) break;
            } else {
                stalledRounds = 0;
            }

            termIndex++;
            if (currentTerm == Semester.Term.Summer) year++;
            hold.clear();
        }

        return sequence;
    }

    private List<Course> processUnscheduledCourses(List<Course> unscheduledCourses, List<Sect> hold,
                                                   Graph<Course> courseGraph, List<Semester> old_semester,
                                                   int maxCredits, int credits) {
        List<Course> tempLeft = new ArrayList<>(unscheduledCourses);
        unscheduledCourses.clear();

        for (Course course : tempLeft) {
            if (credits >= maxCredits) break;

            if (!take_course(course, hold, courseGraph, old_semester)) {
                unscheduledCourses.add(course);
                continue;
            }

            List<Sect> options = new ArrayList<>(course.getSect());
            Collections.shuffle(options);
            boolean added = false;

            for (Sect section : options) {
                if (noConflict(section, hold)) {
                    hold.add(section);
                    credits += course.getCredits();
                    if (course.getLab() != null) {
                        hold.add(course.getLab());
                        credits += course.getlabCredits();
                    }
                    added = true;
                    break;
                }
            }

            if (!added) unscheduledCourses.add(course);
        }

        return unscheduledCourses;
    }

    private int processNewCourses(List<Course> courseOrder, List<Course> unscheduledCourses, int totalCourses,
                                  Graph<Course> courseGraph, List<Sect> hold, List<Semester> old_semester,
                                  int maxCredits, int credits) {

        while (credits < maxCredits && class_count < totalCourses) {
            Course course = courseOrder.get(class_count);
            class_count++;

            if (!take_course(course, hold, courseGraph, old_semester)) {
                unscheduledCourses.add(course);
                continue;
            }

            List<Sect> options = new ArrayList<>(course.getSect());
            Collections.shuffle(options);
            boolean added = false;

            for (Sect section : options) {
                if (noConflict(section, hold)) {
                    hold.add(section);
                    credits += course.getCredits();
                    if (course.getLab() != null) {
                        hold.add(course.getLab());
                        credits += course.getlabCredits();
                    }
                    added = true;
                    break;
                }
            }

            if (!added) unscheduledCourses.add(course);
        }

        return credits;
    }

    private boolean noConflict(Sect newSect, List<Sect> hold) {
        for (Sect existing : hold) {
            if (time_conflict(newSect, existing) || weekday_conflict(newSect, existing)) {
                return false;
            }
        }
        return true;
    }

    private void updateOldSemester(List<Semester> old_semester, int year, Semester.Term term, List<Sect> hold) {
        Semester newSemester = new Semester(year, term, new ArrayList<>(hold));
        old_semester.add(newSemester);
    }

    private List<Sect> validateSectionConflicts(List<Sect> hold, List<Course> unscheduledCourses, Set<String> failedThisSemester) {
        List<Sect> validatedHold = new ArrayList<>();
        Map<Course, List<Sect>> groupedByCourse = new HashMap<>();
        for (Sect s : hold) groupedByCourse.computeIfAbsent(s.getCourse(), k -> new ArrayList<>()).add(s);
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
                        validatedHold.add(s1);
                        validatedHold.add(s2);
                        added = true;
                        break;
                    }
                }
                if (added) break;
            }
            if (!added && sections.size() == 1) {
                validatedHold.add(sections.get(0));
            }
        }

        hold.addAll(validatedHold);

        List<Sect> tempHold = new ArrayList<>(hold);
        for (int i = 0; i < tempHold.size(); i++) {
            Sect course1 = tempHold.get(i);
            for (int j = i + 1; j < tempHold.size(); j++) {
                Sect course2 = tempHold.get(j);
                if (course1 == course2) continue;

                boolean timeConflict = time_conflict(course1, course2);
                boolean weekdayConflict = weekday_conflict(course1, course2);

                if ((timeConflict || weekdayConflict) && course1.getCourse() == course2.getCourse()) {
                    hold.removeIf(s -> s.getCourse() == course1.getCourse());
                    if (!unscheduledCourses.contains(course1.getCourse()) && !failedThisSemester.contains(course1.getName())) {
                        unscheduledCourses.add(course1.getCourse());
                        failedThisSemester.add(course1.getName());
                    }
                } else if (timeConflict || weekdayConflict) {
                    hold.remove(course2);
                    if (!unscheduledCourses.contains(course2.getCourse()) && !failedThisSemester.contains(course2.getName())) {
                        unscheduledCourses.add(course2.getCourse());
                        failedThisSemester.add(course2.getName());
                    }
                }
            }
        }

        return hold;
    }

    public boolean take_course(Course i, List<Sect> hold, Graph<Course> courseGraph, List<Semester> final_semesters) {
        List<Course> deps_list = courseGraph.getIncomingEdges(i);
        for (Course dep : deps_list) {
            if (check(dep, final_semesters)) {
                return false;
            }
        }
        return true;
    }

    public boolean check(Course dep, List<Semester> final_semesters) {
        for (Semester semester : final_semesters) {
            if (semester.getCourses().contains(dep)) {
                return false;
            }
        }
        return true;
    }

    public boolean time_conflict(Sect course1, Sect course2) {
        String startTime1 = course1.getStartTime();
        String endTime1 = course1.getEndTime();
        String startTime2 = course2.getStartTime();
        String endTime2 = course2.getEndTime();

        if (!isValidTimeFormat(startTime1) || !isValidTimeFormat(endTime1)
                || !isValidTimeFormat(startTime2) || !isValidTimeFormat(endTime2)) {
            return false;
        }

        int start1 = Integer.parseInt(startTime1.replace(":", ""));
        int end1 = Integer.parseInt(endTime1.replace(":", ""));
        int start2 = Integer.parseInt(startTime2.replace(":", ""));
        int end2 = Integer.parseInt(endTime2.replace(":", ""));

        return (start1 < end2 && start2 < end1);
    }

    public boolean weekday_conflict(Sect course1, Sect course2) {
        List<String> weekdays1 = course1.getWeekdays();
        List<String> weekdays2 = course2.getWeekdays();

        for (String day1 : weekdays1) {
            if (weekdays2.contains(day1)) {
                return true;
            }
        }

        return false;
    }

    private boolean isValidTimeFormat(String time) {
        return time != null && time.matches("\\d{2}:\\d{2}");
    }

    public int Graph_size(Graph<Course> courseGraph) {
        return courseGraph.topologicalSortM().size();
    }
    
}
