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
    public List<Course> getCourses() {
        return courses;
    }
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(year).append(" ").append(term.name()).append(":\n");
        for (Course course : courses) {
            sb.append(course.getName())
              .append(" (")
              .append(course.getstart_time())
              .append(" - ")
              .append(course.getend_time())
              .append(")\n");
        }
        return sb.toString();
    }

    
}
