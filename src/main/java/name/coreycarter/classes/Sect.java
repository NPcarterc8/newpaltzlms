package name.coreycarter.classes;

import java.util.List;

public class Sect {

    private String sec_id;
    private Course course;
    private String teacher;
    private String start_time;
    private String end_time;
    private List<String> weekdays;

    public Sect(String sec_id, String teacher, String start_time, String end_time, List<String> weekdays) {
        this.sec_id = sec_id;
        this.teacher = teacher;
        this.start_time = start_time;
        this.end_time = end_time;
        this.weekdays = weekdays;
    }

    public void setcourse(Course course) {
        this.course = course;
    }

    public String getStartTime() {
        return start_time;
    }

    public String getEndTime() {
        return end_time;
    }

    public List<String> getWeekdays() {
        return weekdays;
    }

    public Course getCourse() {
        return course;
    }

    public String getName() {
        return course.getName();
    }

    public int getCredits() {
        return course.getCredits();
    }

    @Override
    public String toString() {
        String name = course.getName();
        if (course.getLab() != null && course.getLab() == this) {
            name += "_Lab";
        }
        return name + " (" + start_time + " - " + end_time + ", " + String.join(", ", weekdays) + ")";
    }

}
