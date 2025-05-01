package name.coreycarter.classes;

import java.util.List;

public class Course {

    private String name;
    private boolean majorcourse;
    private int credits;
    private int lab_credits;
    private Sect lab;
    private List<Sect> sections;

    public Course(String name, boolean majorcourse, int credits, int lab_credits, List<Sect> sections, Sect lab) {
        this.name = name;
        this.majorcourse = majorcourse;
        this.credits = credits;
        this.lab_credits = lab_credits;
        this.sections = sections;
        this.lab = lab;
        for (Sect section: sections) {
            section.setcourse(this);
        }
        if(this.lab!=null){
            lab.setcourse(this);
        }

    }

    public Course(String name, boolean majorcourse, int credits,List<Sect> sections) {
        this(name, majorcourse, credits, 0, sections, null);
    }

    public String getName() {
        return name;
    }
    public List<Sect> getSect() {
        return sections;
    }

    public Sect getLab() {
        return lab;
    }


    public int getCredits() {
        return credits;
    }
    public int getlabCredits() {
        return lab_credits;
    }

    
}