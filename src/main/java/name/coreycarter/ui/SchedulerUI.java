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
        Course Math101 = new Course("Math 101", "Dr. Smith", true, 3, "08:00", "09:30", Arrays.asList("Monday", "Wednesday", "Friday"));
        Course Math102 = new Course("Math 102", "Dr. Johnson", true, 3, "10:00", "11:30", Arrays.asList("Tuesday", "Thursday"));
        Course CS101 = new Course("CS 101", "Prof. Brown", true, 3, "08:00", "09:30", Arrays.asList("Monday", "Wednesday", "Friday"));
        Course CS102 = new Course("CS 102", "Prof. Davis", true, 3, "14:00", "15:30", Arrays.asList("Tuesday", "Thursday"));
        Course CS201 = new Course("CS 201", "Dr. Wilson", true, 3, "16:00", "17:30", Arrays.asList("Monday", "Wednesday"));
        Course Algorithms = new Course("Algorithms", "Dr. Taylor", true, 3, "18:00", "19:30", Arrays.asList("Tuesday", "Thursday"));
        Course Lab101 = new Course("Lab 101", "Ms. Anderson", false, 3, "09:00", "10:30", Arrays.asList("Monday", "Wednesday"));
        Course Lab102 = new Course("Lab 102", "Ms. Thomas", false, 1, "11:00", "12:30", Arrays.asList("Tuesday", "Thursday"));
        Course Lab103 = new Course("Lab 103", "Ms. Jackson", false, 1, "13:00", "14:30", Arrays.asList("Monday", "Wednesday"));
        Course Physics101 = new Course("Physics 101", "Dr. White", true, 3, "15:00", "16:30", Arrays.asList("Tuesday", "Thursday"), Lab101);
        Course Physics102 = new Course("Physics 102", "Dr. Harris", true, 3, "17:00", "18:30", Arrays.asList("Monday", "Wednesday"), Lab102);
        Course Physics103 = new Course("Physics 103", "Dr. Martin", true, 3, "19:00", "20:30", Arrays.asList("Tuesday", "Thursday"), Lab103);

        courseGraph = new Graph<>();
        courseGraph.addEdge(Math101, Math102, false); // Math 101 → Math 102
        courseGraph.addEdge(CS201, Physics101, false); // Math 101 → Math 102
        courseGraph.addEdge(Math101, Physics101, false); // Math 101 → Physics 101
        courseGraph.addEdge(CS101, CS102, false); // CS 101 → CS 102
        courseGraph.addEdge(CS102, CS201, false); // CS 102 → CS 201
        courseGraph.addEdge(CS102, Algorithms, false); // CS 102 → Algorithms
        courseGraph.addEdge(Physics101, Physics102, false); // Physics 101 → Physics 102
        courseGraph.addEdge(Physics102, Physics103, false);

        // Co-requisite: Take "Lab 101" and "Physics 101" together
        courseGraph.addEdge(Physics101, Lab101, true); // Bidirectional → Co-Req
        courseGraph.addEdge(Physics102, Lab102, true); // Bidirectional → Co-Req
        courseGraph.addEdge(Physics103, Lab103, true); // Bidirectional → Co-Req

        student = new Students("John Doe", "Computer Science", "Undergrad", 7, 2025);
        scheduler = new Scheduler(courseGraph);

        Graph<Course> courseGraph = new Graph<>();
        Scheduler test = new Scheduler(courseGraph);
        Students t1 = new Students("test", "computer science", "Student", 15, 2020);

        // Example Courses
        
        
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
