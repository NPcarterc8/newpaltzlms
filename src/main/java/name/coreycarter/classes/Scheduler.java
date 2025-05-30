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
    private static final int MAX_STALLED = 100;
    private static final Semester.Term[] TERM_SEQUENCE = Semester.Term.values();

    public Scheduler(Graph<Course> graph) {
        System.out.println("Scheduler initialized with graph: " + graph);
    }

    public List<List<Semester>> generateAllSchedules(Students info, Graph<Course> courseGraph, int maxSchedules, int maxYears,List<Semester.Term> allowedTerms) {
        List<List<Semester>> allSchedules = new ArrayList<>();
        Set<String> seenSchedules = new HashSet<>();  // Track unique schedules as string signatures
        List<Course> originalOrder = courseGraph.topologicalSortM();

        int attempts = 0;
        while (allSchedules.size() < maxSchedules && attempts < maxSchedules * 5) {
            attempts++;  // Prevent infinite loops if too many duplicates
            Collections.shuffle(originalOrder);
            class_count = 0;

            List<Semester> schedule = sequence(info, courseGraph, new ArrayList<>(originalOrder), maxYears, allowedTerms);
            if (!schedule.isEmpty()) {
                String signature = scheduleSignature(schedule);
                if (!seenSchedules.contains(signature)) {
                    seenSchedules.add(signature);
                    allSchedules.add(schedule);
                }
            }
        }
        System.out.println("\n " + allSchedules.size() + " valid schedule(s) found within your " + maxYears + "-year limit.");
        return allSchedules;
    }

    private String scheduleSignature(List<Semester> schedule) {
        StringBuilder sb = new StringBuilder();
        for (Semester s : schedule) {
            sb.append(s.toString().trim()).append("|");
        }
        return sb.toString();
    }

    public List<Semester> sequence(Students info, Graph<Course> courseGraph, List<Course> courseOrder, int maxYears, List<Semester.Term> allowedTerms) {
        List<Sect> hold = new ArrayList<>();
        List<Semester> old_semester = new ArrayList<>();
        List<Course> unscheduledCourses = new ArrayList<>();
        List<Semester> sequence = new ArrayList<>();
        Set<String> failedThisSemester = new HashSet<>();
    
        int maxCredits = info.get_max_credits_per_semeter();
        int year = info.start_date();
        int termIndex = 0;
        int stalledRounds = 0;
        int totalCourses = courseOrder.size();
    
        List<Course> scheduledCourses = new ArrayList<>();
    
        while (class_count < totalCourses || !unscheduledCourses.isEmpty()) {
            Semester.Term currentTerm = allowedTerms.get(termIndex % allowedTerms.size());
            failedThisSemester.clear();
            int credits = 0;
            int initialClassCount = class_count;
            int initialUnscheduledCount = unscheduledCourses.size();
    
            List<Course> dynamicPriority = new ArrayList<>();
            List<Course> regularCourses = new ArrayList<>();
    
            for (Course c : courseOrder) {
                if (scheduledCourses.contains(c)) continue;
                if (c.getPYear() != null && c.getPYear().contains(String.valueOf(year))) {
                    dynamicPriority.add(c);
                } else {
                    regularCourses.add(c);
                }
            }
    
            List<Course> fullCourseOrder = new ArrayList<>();
            fullCourseOrder.addAll(dynamicPriority);
            fullCourseOrder.addAll(regularCourses);
    
            courseOrder = fullCourseOrder;
    
            unscheduledCourses = processUnscheduledCourses(unscheduledCourses, hold, courseGraph, old_semester, maxCredits, credits, currentTerm, year);
            credits = processNewCourses(courseOrder, unscheduledCourses, totalCourses, courseGraph, hold, old_semester, maxCredits, credits, currentTerm, year);
    
            for (Sect s : hold) {
                scheduledCourses.add(s.getCourse());
            }
    
            hold = validateSectionConflicts(hold, unscheduledCourses, failedThisSemester);
    
            List<Sect> finalizedHold = new ArrayList<>(hold);
            updateOldSemester(old_semester, year, currentTerm, finalizedHold);
            Semester semesterObj = new Semester(year, currentTerm, finalizedHold);
            sequence.add(semesterObj);
    
            if (class_count == initialClassCount && unscheduledCourses.size() == initialUnscheduledCount) {
                stalledRounds++;
                if (stalledRounds >= MAX_STALLED) {
                    break;
                }
            } else {
                stalledRounds = 0;
            }
    
            termIndex++;
            if (termIndex % allowedTerms.size() == 0) {
                year++;
            }
    
            if (year - info.start_date() >= maxYears) {
                System.out.println("Schedule exceeds " + maxYears + "-year limit. Discarding..." + year);
                return new ArrayList<>();
            }
    
            hold.clear();
        }
    
        return sequence;
    }
    
    private List<Course> processUnscheduledCourses(List<Course> unscheduledCourses, List<Sect> hold,
            Graph<Course> courseGraph, List<Semester> old_semester,
            int maxCredits, int credits, Semester.Term currentTerm, int currentYear) {
        List<Course> tempLeft = new ArrayList<>(unscheduledCourses);
        unscheduledCourses.clear();
    
        for (Course course : tempLeft) {
            if (credits >= maxCredits) {
                break;
            }
    
            if (course.getPYear() != null && !course.getPYear().contains(String.valueOf(currentYear))) {
                unscheduledCourses.add(course);
                continue;
            }
    
            if (!course.getAvailableTerms().contains(currentTerm)) {
                unscheduledCourses.add(course);
                continue;
            }
    
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
    
            if (!added) {
                unscheduledCourses.add(course);
            }
        }
    
        return unscheduledCourses;
    }
    
    private int processNewCourses(List<Course> courseOrder, List<Course> unscheduledCourses, int totalCourses,
            Graph<Course> courseGraph, List<Sect> hold, List<Semester> old_semester,
            int maxCredits, int credits, Semester.Term currentTerm, int currentYear) {
    
        while (credits < maxCredits && class_count < totalCourses) {
            Course course = courseOrder.get(class_count);
            class_count++;
    
            if (course.getPYear() != null && !course.getPYear().contains(String.valueOf(currentYear))) {
                unscheduledCourses.add(course);
                continue;
            }
    
            if (!course.getAvailableTerms().contains(currentTerm)) {
                unscheduledCourses.add(course);
                continue;
            }
    
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
    
            if (!added) {
                unscheduledCourses.add(course);
            }
        }
    
        return credits;
    } 

    private boolean noConflict(Sect newSect, List<Sect> hold) {
        for (Sect existing : hold) {
            if (weekday_time_conflict(newSect, existing)) {
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
                    if (!weekday_time_conflict(s1, s2)) {
                        validatedHold.add(s1);
                        validatedHold.add(s2);
                        added = true;
                        break;
                    }
                }
                if (added) {
                    break;
                }
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
                if (course1 == course2) {
                    continue;
                }

                boolean Conflict = weekday_time_conflict(course1, course2);

                if ((Conflict) && course1.getCourse() == course2.getCourse()) {
                    hold.removeIf(s -> s.getCourse() == course1.getCourse());
                    if (!unscheduledCourses.contains(course1.getCourse()) && !failedThisSemester.contains(course1.getName())) {
                        unscheduledCourses.add(course1.getCourse());
                        failedThisSemester.add(course1.getName());
                    }
                } else if (Conflict) {
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

    public boolean weekday_time_conflict(Sect course1, Sect course2) {
        String startTime1 = course1.getStartTime();
        String endTime1 = course1.getEndTime();
        String startTime2 = course2.getStartTime();
        String endTime2 = course2.getEndTime();
        List<String> weekdays1 = course1.getWeekdays();
        List<String> weekdays2 = course2.getWeekdays();
        int start1 = Integer.parseInt(startTime1.replace(":", ""));
        int end1 = Integer.parseInt(endTime1.replace(":", ""));
        int start2 = Integer.parseInt(startTime2.replace(":", ""));
        int end2 = Integer.parseInt(endTime2.replace(":", ""));
        for (String day1 : weekdays1) {
            if (weekdays2.contains(day1) && (start1 < end2 && start2 < end1)) {
                return true;
            }
        }
        return false;
    }

    public int Graph_size(Graph<Course> courseGraph) {
        return courseGraph.topologicalSortM().size();
    }

}