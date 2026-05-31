package timer;

import custom.Custom;

import javax.swing.*;
import java.awt.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

public class GymTimer {
    private int timeLeft = 0;
    private Timer timer;
    private JLabel labelDisplay;
    private JButton btnStart;
    private JFrame frame;

    /**
     * Constructs a new GymTimer instance.
     * Initializes the core window frame with the title "Timer".
     */
    public GymTimer() {
        this.frame = new JFrame("Timer");
    }

    /**
     * Assembles and displays the graphical user interface for the gym timer.
     * Configures layouts, registers action listeners for quick-add and control buttons,
     * applies styles via {@link Custom}, and defines the background timer execution loop.
     */
    public void showTimer(){
        //setLayout(new BorderLayout(10, 10));
        this.frame.setSize(500, 600);
        this.frame.setLayout(new BorderLayout());
        this.frame.setLocationRelativeTo(null);
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Custom.background(frame);

        labelDisplay = new JLabel("00:00", SwingConstants.CENTER);
        labelDisplay.setFont(new Font("Monospaced", Font.BOLD, 60));
        this.frame.add(labelDisplay, BorderLayout.CENTER);

        JPanel pnlQuickSet = new JPanel(new GridLayout(1, 3, 5, 5));
        pnlQuickSet.setOpaque(false);
        JButton btnAdd05 = new JButton("+5s");
        JButton btnAdd30 = new JButton("+30s");
        JButton btnAdd60 = new JButton("+1m");
        JButton btnAdd600 = new JButton("+10m");

        btnAdd05.addActionListener(e -> addTime(5));
        btnAdd30.addActionListener(e -> addTime(30));
        btnAdd60.addActionListener(e -> addTime(60));
        btnAdd600.addActionListener(e -> addTime(600));

        pnlQuickSet.add(btnAdd05);
        pnlQuickSet.add(btnAdd30);
        pnlQuickSet.add(btnAdd60);
        pnlQuickSet.add(btnAdd600);

        Custom.startButton(btnAdd05);
        Custom.startButton(btnAdd30);
        Custom.startButton(btnAdd60);
        Custom.startButton(btnAdd600);
        this.frame.add(pnlQuickSet, BorderLayout.NORTH);

        JPanel pnlControls = new JPanel(new GridLayout(1, 2, 5, 5));
        pnlControls.setOpaque(false);
        btnStart = new JButton("START");
        JButton btnClear = new JButton("ClEAR");
        JButton btnBack = new JButton("HOME");

        btnClear.addActionListener(e -> resetTimer());
        btnStart.addActionListener(e -> toggleTimer());

        btnBack.addActionListener(e -> {
            this.frame.dispose();
            timer.stop();
            new app.App().showApp();
        });

        pnlControls.add(btnStart);
        pnlControls.add(btnClear);
        pnlControls.add(btnBack);
        this.frame.add(pnlControls, BorderLayout.SOUTH);
        Custom.startButton(btnStart);
        Custom.startButton(btnClear);
        Custom.startButton(btnBack);

        this.frame.setVisible(true);

        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (timeLeft > 0) {
                    timeLeft--;
                    updateDisplay();
                    if (timeLeft <= 5) {

                        labelDisplay.setForeground(Color.RED);

                    } else {

                        labelDisplay.setForeground(Color.BLACK);
                    }
                } else {
                    timer.stop();
                    btnStart.setText("START");
                    labelDisplay.setForeground(Color.RED);
                    JOptionPane.showMessageDialog(null, "Time's up!");
                    labelDisplay.setForeground(Color.BLACK);
                }
            }
        });
    }

    /**
     * Adds a specified amount of seconds to the current remaining time and updates the display.
     *
     * @param seconds The number of seconds to be added to the countdown.
     */
    private void addTime(int seconds) {
        timeLeft += seconds;
        updateDisplay();
    }

    /**
     * Toggles the timer running state.
     * Stops the timer if it is active, or starts it if there is remaining countdown time.
     */
    private void toggleTimer() {
        if (timer.isRunning()) {
            timer.stop();
            btnStart.setText("START");
        } else if (timeLeft > 0) {
            timer.start();
            btnStart.setText("PAUSE");
        }
    }

    /**
     * Forces the background timer thread to stop and reverts the start button label.
     */
    private void stopTimer() {
        timer.stop();
        btnStart.setText("START");
    }

    /**
     * Resets the entire timer sequence by stopping execution, clearing out
     * any remaining duration, and updating the visual display indicators back to zero.
     */
    private void resetTimer() {
        timer.stop();
        timeLeft = 0;
        btnStart.setText("START");
        updateDisplay();
    }

    /**
     * Calculates the individual minutes and seconds components from the total
     * remaining time and formats them into a standardized MM:SS display string.
     */
    private void updateDisplay() {
        int mins = timeLeft / 60;
        int secs = timeLeft % 60;
        labelDisplay.setText(String.format("%02d:%02d", mins, secs));
    }
}

