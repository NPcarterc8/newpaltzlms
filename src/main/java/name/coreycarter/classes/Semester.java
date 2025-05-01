package name.coreycarter.classes;

import java.util.ArrayList;
import java.util.List;

public class Semester {

    //single semter(bulid when your done wiht it)
    final int year;
    final Term term;
    final List<Sect> sections;

    public enum Term {
        Fall,
        Winter,
        Spring,
        Summer
    }

    public Semester(int year, Term term, List<Sect> sections) {
        this.year = year;
        this.term = term;
        this.sections = sections;
    }

    public List<Sect> getSections() {
        return sections;
    }
    public List<Course> getCourses(){
        List<Course> x= new ArrayList<>();
        for (Sect section: sections) {
            x.add(section.getCourse());
        }
        return x;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(year).append(" ").append(term.name()).append(":\n");
        for (Sect section: sections) {
            sb.append(section.getName())
                    .append(" (")
                    .append(section.getStartTime())
                    .append(" - ")
                    .append(section.getEndTime())
                    .append(", ")
                    .append(section.getWeekdays())
                    .append(")\n");
        }
        return sb.toString();
    }

}
