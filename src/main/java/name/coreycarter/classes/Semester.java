package name.coreycarter.classes;

import java.util.List;

public class Semester {
    //single semter(bulid when your done wiht it)
    final int year;
    final Term term;
    final List<Course> courses;
    public enum Term {
        Fall,
        Winter,
        Spring,
        Summer 
    }
    public Semester(int year,Term term,List<Course> courses){
        this.year=year;
        this.term=term;
        this.courses=courses;
    }
    public String toString() {
        return String.valueOf(year) + " " + term.name() + " " + courses;
    }

    
}
