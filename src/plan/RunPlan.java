package plan;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.util.*;
import custom.Custom;
import javax.swing.Timer;

public class RunPlan {
    private JFrame frame;
    private JLabel exerciseLabel, timeLabel, labelStatus;
    private ArrayList<String[]> planData = new ArrayList<>();
    private int currentIndex = 0;
    private int remainingTime;
    private boolean isRest = false;
    private Timer timer;

    /**
     * Initiates the routine runtime cycle. Establishes path roots to the local storage folder,
     * triggers file lookup dialogs, builds layout view grids, binds safety windows closure interceptors,
     * loads baseline text values, and forces background clock threads into active states.
     */
        public void start() {

            String projectDirectory = System.getProperty("user.dir");
            File defaultFolder = new File(projectDirectory, "resources");


            if (!defaultFolder.exists()) {
                defaultFolder.mkdirs();
            }

            JFileChooser fileChooser = new JFileChooser(defaultFolder);
            fileChooser.setDialogTitle("Select a workout plan");

            fileChooser.setCurrentDirectory(defaultFolder);

            int result = fileChooser.showOpenDialog(null);

            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                loadData(selectedFile.getAbsolutePath());
            } else {
                return;
            }

            if (planData.isEmpty()) return;

            frame = new JFrame("Workout running");
            frame.setSize(400, 400);
            frame.setLayout(new GridLayout(3, 1));
            frame.setLocationRelativeTo(null);
            Custom.background(frame);

            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    if (timer != null) {
                        timer.stop();
                    }
                }
            });

            labelStatus = new JLabel("Get ready", SwingConstants.CENTER);
            exerciseLabel = new JLabel(planData.get(0)[0], SwingConstants.CENTER);
            timeLabel = new JLabel("0", SwingConstants.CENTER);
            //labelCas.setFont(new Font("Arial", Font.BOLD, 50));

            Custom.exerciseStyle(exerciseLabel);
            Custom.timeStyle(timeLabel);

            frame.add(labelStatus);
            frame.add(exerciseLabel);
            frame.add(timeLabel);

            prepareNext(0);
            startTimer();


            frame.setAlwaysOnTop(true);
            frame.setVisible(true);
        }



    /**
     * Streams workout rows from the storage disk into memory using file readers.
     * Splits entries into sub-arrays using default delimiters to segregate properties.
     *
     * @param cesta The exact absolute path location string mapping to the target routine text file.
     */
            private void loadData(String cesta) {
        try (BufferedReader br = new BufferedReader(new FileReader(cesta))) {
            String line;
            while ((line = br.readLine()) != null) {
                planData.add(line.split(";"));
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error loading file!");
        }
    }

    /**
     * Initializes and fires the internal repetitive countdown sequence.
     * Subtracts integers from remaining intervals every second and handles layout synchronization.
     */
    private void startTimer() {
        timer = new Timer(1000, e -> {
            remainingTime--;
            timeLabel.setText(String.valueOf(remainingTime));

            if (remainingTime <= 0) {
                switchStatus();
            }
        });
        timer.start();
    }

    /**
     * Evaluates clock timeouts and flips app state attributes.
     * Routes tracking flags to rest phases or steps pointers up to index upcoming routines,
     * triggering pop-up completions alerts when arrays reach termination constraints.
     */
    private void switchStatus() {
        if (!isRest) {
            isRest = true;

            String[] currentExercise = planData.get(currentIndex);

            if (currentExercise.length > 2 && !currentExercise[2].trim().isEmpty()) {
                remainingTime = Integer.parseInt(currentExercise[2].trim());
            } else {
                remainingTime = 0;
            }

            labelStatus.setText("Rest time");
            //labelStatus.setForeground(Color.BLUE);
            Custom.stavStyle(labelStatus, Color.BLUE);
        } else {
            isRest = false;
            currentIndex++;
            if (currentIndex < planData.size()) {
                prepareNext(currentIndex);
            } else {
                timer.stop();
                labelStatus.setText("Completed!");
                JOptionPane.showMessageDialog(frame, "Workout completed!");
                frame.dispose();
            }
        }
    }

    /**
     * Refreshes dashboard rendering contexts with metrics belonging to the upcoming workout element index.
     * Pulls array elements, sets countdown timers, updates phase messages to "Work!", and alters visual alerts.
     *
     * @param index The target position array pointer inside the root data tracker cache.
     */
    private void prepareNext(int index) {
        String[] line = planData.get(index);
        exerciseLabel.setText(line[0]);

        if (line.length > 1 && !line[1].trim().isEmpty()) {
            remainingTime = Integer.parseInt(line[1].trim());
        } else {
            remainingTime = 0;
        }

        labelStatus.setText("Work!");
        //labelStatus.setForeground(Color.RED);
        Custom.stavStyle(labelStatus, Color.RED);
    }

}

