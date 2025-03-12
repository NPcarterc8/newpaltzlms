package name.coreycarter.classes;

public class Course {
    private String name;
    private boolean majorcourse;
    private int credits;

    public Course(String name, boolean majorcours,int credits){
        this.name = name;
        this.majorcourse = majorcourse;
        this.credits=credits;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}