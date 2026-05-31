package plan;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import app.App;
import custom.Custom;

public class Plan {
    private JFrame frame;
    private ArrayList<String> exerciseList = new ArrayList<>();
    private JTextField planNameField, exerciseField, timeField, restField;

    public Plan() {
        this.frame = new JFrame("Workout Plan Creation");
    }

    /**
     * Assembles and displays the graphical user interface components for routine configuration.
     * Builds input fields, hooks up dynamic form validation mechanics, applies styles via {@link Custom},
     * registers mouse click event bounds on the visual list, and wires up route execution triggers.
     */
    public void showPlan() {
        frame.setSize(500, 600);
        frame.setLayout(new BorderLayout(10, 10));
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        Custom.background(frame);


        JPanel topPanel = new JPanel(new GridLayout(2, 1));
        topPanel.setOpaque(false);
        topPanel.add(new JLabel("Plan name:", SwingConstants.CENTER));
        planNameField = new JTextField();
        topPanel.add(planNameField);


        JPanel inputPanel = new JPanel(new GridLayout(6, 1, 5, 5));
        inputPanel.setOpaque(false);

        exerciseField = new JTextField();
        timeField = new JTextField();
        restField = new JTextField();

        inputPanel.add(new JLabel("Exercise name:"));
        inputPanel.add(exerciseField);
        inputPanel.add(new JLabel("Exercise duration (seconds):"));
        inputPanel.add(timeField);
        inputPanel.add(new JLabel("Rest time (seconds):"));
        inputPanel.add(restField);


        DefaultListModel<String> listModel = new DefaultListModel<>();
        JList<String> jList = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(jList);



        JPanel buttonPanel = new JPanel(new GridLayout(1, 4, 10, 10));
        buttonPanel.setOpaque(false);

        JButton btnAdd = new JButton("Add");
        JButton btnRun = new JButton("Load");
        JButton btnSave = new JButton("Save");
        JButton btnHome = new JButton("Home");

        btnAdd.addActionListener(e -> {
            String exerciseName = exerciseField.getText().trim();
            String timeText = timeField.getText().trim();
            String restText = restField.getText().trim();

             if (exerciseName.isEmpty() || timeText.isEmpty() || restText.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Please fill in all exercise fields.", "Error",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (!timeText.matches("\\d+") || !restText.matches("\\d+")) {
                JOptionPane.showMessageDialog(frame, "Please enter numbers only in the time fields!",
                        "Input error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String line = exerciseName + ";" + timeText + ";" + restText;
            exerciseList.add(line);
            listModel.addElement(exerciseName + " (" + timeText + "s + " + restText + "s)");

            exerciseField.setText("");
            timeField.setText("");
            restField.setText("");
        });

        jList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int index = jList.locationToIndex(e.getPoint());
                    if (index >= 0) {
                        listModel.remove(index);
                        exerciseList.remove(index);
                    }
                }
            }
        });
        btnRun.addActionListener(e -> {
            RunPlan r = new RunPlan();
            r.start();
        });


        btnSave.addActionListener(e -> {
            saveToFile();
            //frame.dispose();
            //new App().showApp();
        });
        btnHome.addActionListener(e -> {
            this.frame.dispose();
            new App().showApp();
        });

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnRun);
        buttonPanel.add(btnSave);
        buttonPanel.add(btnHome);

        Custom.startButton(btnAdd);
        Custom.startButton(btnRun);
        Custom.startButton(btnSave);
        Custom.startButton(btnHome);



        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(inputPanel, BorderLayout.CENTER);
        frame.add(scrollPane, BorderLayout.WEST);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    /**
     * Serializes the configured collection of exercise data elements into a dedicated text file.
     * Targets local directory destinations, resolves naming conflicts gracefully by appending numeric incremental
     * indices (e.g. plan1, plan2) to block metadata loss, and writes data chunks with default system line separation feeds.
     */
    private void saveToFile() {
        if (exerciseList.isEmpty()) {
            JOptionPane.showMessageDialog(frame,
                    "Cannot save an empty plan! Please add some exercises first!", "Saving error",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String title = planNameField.getText().trim();
        if (title.isEmpty()) title = "unnamed_plan";

        File adresar = new File("resources");
        if (!adresar.exists()) {
            adresar.mkdirs();
        }

        File file = new File(adresar, title + ".txt");

        if (file.exists()) {
            int counter = 1;
            while (file.exists()) {
                file = new File(adresar, title + counter + ".txt");
                counter++;
            }
            title = file.getName().replace(".txt", "");
        }

        try (FileWriter writer = new FileWriter(file)) {
            for (String s : exerciseList) {
                writer.write(s + System.lineSeparator());
            }
            JOptionPane.showMessageDialog(frame, "Plan '" + title + "' saved to the resources folder!");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "Error saving: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }



}
