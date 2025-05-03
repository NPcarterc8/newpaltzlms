// Cleaned SchedulerUI.java without Add New Course
package name.coreycarter;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import name.coreycarter.classes.*;
import name.coreycarter.utils.Graph;

public class SchedulerUI {

    private static Scheduler scheduler;
    private static Graph<Course> courseGraph;
    private static Students student;

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(SchedulerUI::createAndShowGUI);
    }

    private static void createAndShowGUI() {
        setupSchedulerData();

        JFrame frame = new JFrame("Seamless Learning Scheduler");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 650);
        frame.setLayout(new BorderLayout(10, 10));

        JLabel header = new JLabel("Course Schedule Generator", JLabel.CENTER);
        header.setFont(new Font("SansSerif", Font.BOLD, 24));
        header.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        frame.add(header, BorderLayout.NORTH);

        // ===== Input Panel =====
        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Scheduler Options", TitledBorder.LEFT, TitledBorder.TOP));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel nameLabel = new JLabel("Student Name:");
        JTextField nameField = new JTextField("Corey", 10);

        JLabel majorLabel = new JLabel("Major:");
        JComboBox<String> majorBox = new JComboBox<>(new String[]{"Computer Science", "Mathematics", "Physics", "Engineering"});

        JLabel creditLabel = new JLabel("Max Credits/Semester:");
        JSpinner creditSpinner = new JSpinner(new SpinnerNumberModel(15, 3, 24, 1));

        JLabel yearLabel = new JLabel("Max Years to Graduate:");
        JTextField yearField = new JTextField(5);

        JLabel countLabel = new JLabel("# of Schedules:");
        JSpinner countSpinner = new JSpinner(new SpinnerNumberModel(3, 1, 10, 1));

        JLabel termLabel = new JLabel("Allowed Terms:");
        JCheckBox fallBox = new JCheckBox("Fall", true);
        JCheckBox springBox = new JCheckBox("Spring", true);
        JCheckBox winterBox = new JCheckBox("Winter", true);
        JCheckBox summerBox = new JCheckBox("Summer", true);

        JPanel termPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        termPanel.add(fallBox);
        termPanel.add(springBox);
        termPanel.add(winterBox);
        termPanel.add(summerBox);

        JButton generateBtn = new JButton("Generate Schedule");
        JButton viewGraphBtn = new JButton("View Course Graph");

        int row = 0;
        gbc.gridx = 0;
        gbc.gridy = row;
        inputPanel.add(nameLabel, gbc);
        gbc.gridx = 1;
        inputPanel.add(nameField, gbc);

        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        inputPanel.add(majorLabel, gbc);
        gbc.gridx = 1;
        inputPanel.add(majorBox, gbc);

        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        inputPanel.add(creditLabel, gbc);
        gbc.gridx = 1;
        inputPanel.add(creditSpinner, gbc);

        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        inputPanel.add(yearLabel, gbc);
        gbc.gridx = 1;
        inputPanel.add(yearField, gbc);

        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        inputPanel.add(countLabel, gbc);
        gbc.gridx = 1;
        inputPanel.add(countSpinner, gbc);

        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        inputPanel.add(termLabel, gbc);
        gbc.gridx = 1;
        inputPanel.add(termPanel, gbc);

        row++;
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        inputPanel.add(generateBtn, gbc);
        gbc.gridy++;
        inputPanel.add(viewGraphBtn, gbc);

        viewGraphBtn.addActionListener(e -> {
            JTextArea graphView = new JTextArea(courseGraph.toString());
            graphView.setFont(new Font("Monospaced", Font.PLAIN, 12));
            graphView.setEditable(false);
            JScrollPane scroll = new JScrollPane(graphView);
            scroll.setPreferredSize(new Dimension(600, 400));

            JOptionPane.showMessageDialog(frame, scroll, "Course Dependency Graph", JOptionPane.INFORMATION_MESSAGE);
        });

        JTextArea outputArea = new JTextArea();
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        outputArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Schedules"));

        frame.add(inputPanel, BorderLayout.WEST);
        frame.add(scrollPane, BorderLayout.CENTER);

        generateBtn.addActionListener(e -> {
            outputArea.setText("");
            try {
                int maxYears = Integer.parseInt(yearField.getText().trim());
                int maxCredits = (int) creditSpinner.getValue();
                int scheduleCount = (int) countSpinner.getValue();
                String studentName = nameField.getText().trim();
                String major = (String) majorBox.getSelectedItem();

                List<Semester.Term> allowedTerms = new ArrayList<>();
                if (fallBox.isSelected()) {
                    allowedTerms.add(Semester.Term.Fall);
                }
                if (springBox.isSelected()) {
                    allowedTerms.add(Semester.Term.Spring);
                }
                if (winterBox.isSelected()) {
                    allowedTerms.add(Semester.Term.Winter);
                }
                if (summerBox.isSelected()) {
                    allowedTerms.add(Semester.Term.Summer);
                }

                if (allowedTerms.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Please select at least one term.", "Input Error", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                List<List<Semester>> schedules = scheduler.generateAllSchedules(student, courseGraph, scheduleCount, maxYears, allowedTerms);

                if (schedules.isEmpty()) {
                    outputArea.setText("No valid schedules found. Try adjusting years or terms.");
                } else {
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < schedules.size(); i++) {
                        sb.append("\n====== Schedule #").append(i + 1).append(" ======\n");
                        for (Semester s : schedules.get(i)) {
                            sb.append(s.toString()).append("\n");
                        }
                    }
                    outputArea.setText(sb.toString());
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Please enter a valid number for max years.", "Input Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                outputArea.setText("Error: " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        frame.setVisible(true);
    }

    private static void setupSchedulerData() {
        courseGraph = new Graph<>();
        student = new Students("test", "computer science", "Student", 15, 2020);
        scheduler = new Scheduler(courseGraph);

        // Section definitions
        Sect math101_01 = new Sect("01", "t", "08:00", "09:30", Arrays.asList("Monday", "Wednesday", "Friday"));
        Sect math101_02 = new Sect("02", "t", "11:00", "12:30", Arrays.asList("Monday", "Wednesday"));
        Sect math102_01 = new Sect("01", "t", "10:00", "11:30", Arrays.asList("Monday", "Wednesday", "Friday"));
        Sect cs101_01 = new Sect("01", "t", "11:00", "12:30", Arrays.asList("Tuesday", "Thursday"));
        Sect algorithms_01 = new Sect("01", "t", "13:00", "14:30", Arrays.asList("Tuesday", "Thursday"));
        Sect cs102_01 = new Sect("01", "t", "14:00", "15:30", Arrays.asList("Monday", "Wednesday"));
        Sect cs201_01 = new Sect("01", "t", "15:00", "16:30", Arrays.asList("Tuesday", "Thursday"));

        // // FIX: Separate lecture & lab sections
        Sect physics101_lecture = new Sect("01", "t", "16:00", "17:30", Arrays.asList("Monday", "Wednesday"));
        Sect physics101_lab = new Sect("L1", "t", "09:00", "10:30", Arrays.asList("Friday"));
        Sect physics102_lecture = new Sect("01", "t", "17:00", "18:30", Arrays.asList("Tuesday", "Thursday"));
        Sect physics102_lab = new Sect("L1", "t", "11:00", "12:30", Arrays.asList("Saturday"));
        Sect physics103_01 = new Sect("01", "t", "18:00", "19:30", Arrays.asList("Tuesday", "Thursday"));

        // // Additional sections
        Sect math201_01 = new Sect("01", "t", "09:00", "10:30", Arrays.asList("Tuesday", "Thursday"));
        Sect math201_02 = new Sect("02", "t", "14:00", "15:30", Arrays.asList("Monday", "Wednesday"));
        Sect cs301_01 = new Sect("01", "t", "10:00", "11:30", Arrays.asList("Monday", "Wednesday"));
        Sect cs301_02 = new Sect("02", "t", "13:00", "14:30", Arrays.asList("Tuesday", "Thursday"));
        Sect physics201_lecture = new Sect("01", "t", "15:00", "16:30", Arrays.asList("Monday", "Wednesday"));
        Sect physics201_lab = new Sect("L1", "t", "08:00", "09:30", Arrays.asList("Friday"));

        // Courses
        Course Math101 = new Course("Math 101", true, 3, Arrays.asList(math101_01, math101_02), Arrays.asList(Semester.Term.Fall, Semester.Term.Spring, Semester.Term.Winter, Semester.Term.Summer));
        Course Math102 = new Course("Math 102", true, 3, Arrays.asList(math102_01), Arrays.asList(Semester.Term.Fall, Semester.Term.Spring, Semester.Term.Winter, Semester.Term.Summer));
        Course CS101 = new Course("CS 101", true, 3, Arrays.asList(cs101_01), Arrays.asList(Semester.Term.Fall, Semester.Term.Spring, Semester.Term.Winter, Semester.Term.Summer));
        Course CS102 = new Course("CS 102", true, 3, Arrays.asList(cs102_01), Arrays.asList(Semester.Term.Fall, Semester.Term.Spring, Semester.Term.Winter, Semester.Term.Summer));
        Course CS201 = new Course("CS 201", true, 3, Arrays.asList(cs201_01), Arrays.asList(Semester.Term.Fall, Semester.Term.Spring, Semester.Term.Winter, Semester.Term.Summer));
        Course Algorithms = new Course("Algorithms", true, 3, Arrays.asList(algorithms_01), Arrays.asList(Semester.Term.Fall, Semester.Term.Spring, Semester.Term.Winter, Semester.Term.Summer));
        Course Physics101 = new Course("Physics 101", true, 3, 1, Arrays.asList(physics101_lecture), physics101_lab, Arrays.asList(Semester.Term.Fall, Semester.Term.Spring, Semester.Term.Winter, Semester.Term.Summer));
        Course Physics102 = new Course("Physics 102", true, 3, 1, Arrays.asList(physics102_lecture), physics102_lab, Arrays.asList(Semester.Term.Fall, Semester.Term.Spring, Semester.Term.Winter, Semester.Term.Summer));
        Course Physics103 = new Course("Physics 103", true, 3, Arrays.asList(physics103_01), Arrays.asList(Semester.Term.Fall, Semester.Term.Spring, Semester.Term.Winter, Semester.Term.Summer));
        Course Math201 = new Course("Math 201", true, 3, Arrays.asList(math201_01, math201_02), Arrays.asList(Semester.Term.Fall, Semester.Term.Spring, Semester.Term.Winter, Semester.Term.Summer));
        Course CS301 = new Course("CS 301", true, 3, Arrays.asList(cs301_01, cs301_02), Arrays.asList(Semester.Term.Fall, Semester.Term.Spring, Semester.Term.Winter, Semester.Term.Summer));
        Course Physics201 = new Course("Physics 201", true, 3, 1, Arrays.asList(physics201_lecture), physics201_lab, Arrays.asList(Semester.Term.Fall, Semester.Term.Spring, Semester.Term.Winter, Semester.Term.Summer));
        // // Dependencies
        courseGraph.addEdge(Math101, Math102, false);
        courseGraph.addEdge(CS201, Physics101, false);
        courseGraph.addEdge(Math101, Physics101, false);
        courseGraph.addEdge(CS101, CS102, false);
        courseGraph.addEdge(CS102, CS201, false);
        courseGraph.addEdge(CS102, Algorithms, false);
        courseGraph.addEdge(Physics101, Physics102, false);
        courseGraph.addEdge(Physics102, Physics103, false);
        courseGraph.addEdge(Math102, Math201, false);
        courseGraph.addEdge(CS201, CS301, false);
        courseGraph.addEdge(Algorithms, CS301, false);
        courseGraph.addEdge(Physics101, Physics201, false);
        courseGraph.addEdge(Physics201, Physics102, false);
        courseGraph.addEdge(Math201, Physics201, false);
    }
}
