package name.coreycarter.classes;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class Semester {

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

    public List<Course> getCourses() {
        Set<Course> unique = new LinkedHashSet<>();
        for (Sect section : sections) {
            unique.add(section.getCourse());
        }
        return new ArrayList<>(unique);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(year).append(" ").append(term).append(":\n");
        for (Sect s : sections) {
            sb.append("  ").append(s.toString()).append("\n");
        }
        return sb.toString();
    }
}