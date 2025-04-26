package name.coreycarter.classes;

import java.util.List;

public class Course {

    private String name;
    private String teacher;
    private boolean majorcourse;
    private int credits;
    private String start_time;
    private String end_time;
    private Course lab;
    private List<String> weekdays;

    public Course(String name, String teacher, boolean majorcourse, int credits, String start_time, String end_time, List<String> weekdays, Course lab) {
        this.name = name;
        this.teacher = teacher;
        this.majorcourse = majorcourse;
        this.credits = credits;
        this.start_time = start_time;
        this.end_time = end_time;
        this.weekdays = weekdays;
        this.lab = lab;
    }

    public Course(String name, String teacher, boolean majorcourse, int credits, String start_time, String end_time, List<String> weekdays) {
        this(name, teacher, majorcourse, credits, start_time, end_time, weekdays, null);
    }

    public String getName() {
        return name;
    }

    public Course getLab() {
        return lab;
    }

    public String getStartTime() {
        return start_time;
    }

    public String getEndTime() {
        return end_time;
    }

    public int getCredits() {
        return credits;
    }

    public List<String> getWeekdays() {
        return weekdays;
    }

    @Override
    public String toString() {
        return name + (weekdays != null ? " (" + String.join(", ", weekdays) + ")" : "");
    }
}