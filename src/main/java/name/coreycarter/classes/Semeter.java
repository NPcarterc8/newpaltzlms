package name.coreycarter.classes;

import java.util.List;

public class Semeter {
    final int year;
    final Term term;
    final List<Course> courses;
    enum Term {
        Fall,
        Winter,
        Spring,
        Summer 
    }
    public Semeter(int year,Term term,List<Course> courses){
        this.year=year;
        this.term=term;
        this.courses=courses;
    }
    //geters seters
}
