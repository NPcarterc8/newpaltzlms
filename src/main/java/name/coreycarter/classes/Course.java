package name.coreycarter.classes;

public class Course {
    private String name;
    private boolean majorcourse;
    private Course lab;
    private int credits;

    public Course(String name, boolean majorcours,int credits, Course lab){
        this.name = name;
        this.majorcourse = majorcourse;
        this.lab = lab;
        this.credits=credits;
    }
    public Course(String name, boolean majorcours,int credits){
        this(name, majorcours, credits, null);
    }

    public String getName() {
        return name;
    }
    public Course getLab() {
        return lab;
    }
    
    public int getCredits() {
        return credits;
    }

    public String toString() {
        return name;
    }
    
}