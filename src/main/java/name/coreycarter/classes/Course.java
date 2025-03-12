package name.coreycarter.classes;

public class Course {
    private String name;
    private boolean majorcourse;

    public Course(String name, boolean majorcours){
        this.name = name;
        this.majorcourse = majorcourse;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}