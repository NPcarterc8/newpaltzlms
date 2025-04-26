package name.coreycarter.ui;

import name.coreycarter.classes.*;
import name.coreycarter.utils.Graph;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;

public class SchedulerUI extends JFrame {

    private JTextArea outputArea;
    private JButton generateButton;
    private Scheduler scheduler;
    private Graph<Course> courseGraph;
    private Students student;

    public SchedulerUI() {
        setTitle("Course Scheduler");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);

        generateButton = new JButton("Generate Schedule");
        generateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateSchedule();
            }
        });

        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
        add(generateButton, BorderLayout.SOUTH);

        initializeData();
    }

    private void initializeData() {
        // Create sample courses
        Course math101 = new Course("Math 101", "Dr. Smith", true, 3, "09:00", "10:15", Arrays.asList("Monday", "Wednesday", "Friday"));
        Course cs101 = new Course("CS 101", "Prof. Allen", true, 4, "10:30", "11:45", Arrays.asList("Tuesday", "Thursday"));
        Course eng201 = new Course("English 201", "Dr. Brown", false, 3, "13:00", "14:15", Arrays.asList("Monday", "Wednesday"));
        Course phy101 = new Course("Physics 101", "Dr. White", true, 4, "14:30", "15:45", Arrays.asList("Tuesday", "Thursday"));

        courseGraph = new Graph<>();
        courseGraph.addVertex(math101);
        courseGraph.addVertex(cs101);
        courseGraph.addVertex(eng201);
        courseGraph.addVertex(phy101);

        courseGraph.addEdge(math101, cs101, false); // Math is prerequisite for CS

        student = new Students("John Doe", "Computer Science", "Undergrad", 7, 2025);
        scheduler = new Scheduler(courseGraph);
    }

    private void generateSchedule() {
        List<Semester> semesters = scheduler.sequence(student, courseGraph);
        outputArea.setText("");

        for (Semester sem : semesters) {
            outputArea.append(prettyPrintSemester(sem));
        }
    }

    private String prettyPrintSemester(Semester semester) {
        StringBuilder sb = new StringBuilder();
        sb.append("====================================\n");
        sb.append(" Semester: ").append(semester.toString().split(":")[0]).append("\n");
        sb.append("====================================\n");

        for (Course course : semester.getCourses()) {
            sb.append("- ").append(course.getName()).append("\n")
              .append("    Credits: ").append(course.getCredits()).append("\n")
              .append("    Days: ").append(course.getWeekdays() != null ? String.join(", ", course.getWeekdays()) : "N/A").append("\n")
              .append("    Time: ").append(course.getStartTime()).append(" - ").append(course.getEndTime()).append("\n\n");
        }
        return sb.toString();
    }
    
    public void showUI() {
        SwingUtilities.invokeLater(() -> {
            this.setVisible(true);
        });
    }
}
