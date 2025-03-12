package name.coreycarter.classes;

import java.util.LinkedList;

public class Semeter {
    final int year;
    final Term term;
    final LinkedList<Course> courses;
    enum Term {
        Fall,
        Winter,
        Spring,
        Summer 
    }
    public Semeter(int year,Term term,LinkedList<Course> courses){
        this.year=year;
        this.term=term;
        this.courses=courses;
    }
    //geters seters
}
