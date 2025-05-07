package name.coreycarter.classes;

import java.util.List;

public class Course {

    private String name;
    private boolean majorcourse;
    private int credits;
    private int lab_credits;
    private Sect lab;
    private List<Sect> sections;
    private List<Semester.Term> availableTerms;
    private List<String> p_year;

    public Course(String name, boolean majorcourse, int credits, int lab_credits,
            List<Sect> sections, Sect lab, List<Semester.Term> availableTerms, List<String> p_year) {
        this.name = name;
        this.majorcourse = majorcourse;
        this.credits = credits;
        this.lab_credits = lab_credits;
        this.sections = sections;
        this.lab = lab;
        this.availableTerms = availableTerms;
        this.p_year = p_year;

        for (Sect section : sections) {
            section.setcourse(this);
        }
        if (this.lab != null) {
            lab.setcourse(this);
        }
    }

    public Course(String name, boolean majorcourse, int credits, List<Sect> sections, List<Semester.Term> availableTerms) {
        this(name, majorcourse, credits, 0, sections, null, availableTerms, null);
    }

    public List<Semester.Term> getAvailableTerms() {
        return availableTerms;
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
    public List<String> getPYear() {
        return p_year;
    }
    @Override
    public String toString() {
        return name + " (" + credits + " credits" + (majorcourse ? ", Major" : "") + ")";
    }

}
