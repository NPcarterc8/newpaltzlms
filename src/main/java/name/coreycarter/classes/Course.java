package name.coreycarter.classes;

public class Course {

    private String name;
    private boolean majorcourse;
    private Course lab;
    private int credits;
    private String start_time;
    private String end_time;

    public Course(String name, boolean majorcourse, int credits, String start_time, String end_time, Course lab) {
        this.name = name;
        this.majorcourse = majorcourse;
        this.lab = lab;
        this.credits = credits;
        this.start_time = start_time;
        this.end_time = end_time;
    }

    public Course(String name, boolean majorcourse, int credits, String start_time, String end_time) {
        this(name, majorcourse, credits, start_time, end_time, null);
    }

    public String getName() {
        return name;
    }

    public Course getLab() {
        return lab;
    }

    public String getstart_time() {
        return start_time;
    }

    public String getend_time() {
        return end_time;
    }

    public int getCredits() {
        return credits;
    }

    public String toString() {
        return name;
    }

}
