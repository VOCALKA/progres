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

    public GymTimer() {
        this.frame = new JFrame("Timer");
    }

    public void showTimer(){
        //setLayout(new BorderLayout(10, 10));
        this.frame.setSize(500, 600);
        this.frame.setLayout(new BorderLayout());
        this.frame.setLocationRelativeTo(null);
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        labelDisplay = new JLabel("00:00", SwingConstants.CENTER);
        labelDisplay.setFont(new Font("Monospaced", Font.BOLD, 60));
        this.frame.add(labelDisplay, BorderLayout.CENTER);

        JPanel pnlQuickSet = new JPanel(new GridLayout(1, 3, 5, 5));
        JButton btnAdd30 = new JButton("+30s");
        JButton btnAdd60 = new JButton("+1m");
        JButton btnClear = new JButton("Clear");

        btnAdd30.addActionListener(e -> addTime(30));
        btnAdd60.addActionListener(e -> addTime(60));
        btnClear.addActionListener(e -> resetTimer());

        pnlQuickSet.add(btnAdd30);
        pnlQuickSet.add(btnAdd60);
        pnlQuickSet.add(btnClear);

        Custom.startButton(btnAdd30);
        Custom.startButton(btnAdd60);
        Custom.startButton(btnClear);
        this.frame.add(pnlQuickSet, BorderLayout.NORTH);

        JPanel pnlControls = new JPanel(new GridLayout(1, 2, 5, 5));
        btnStart = new JButton("START");
        JButton btnStop = new JButton("STOP");
        JButton btnBack = new JButton("BACK");

        btnStart.addActionListener(e -> toggleTimer());
        btnStop.addActionListener(e -> stopTimer());

        btnBack.addActionListener(e -> {
            this.frame.dispose();
            timer.stop();
            new app.App().showApp();
        });

        pnlControls.add(btnStart);
        pnlControls.add(btnStop);
        pnlControls.add(btnBack);
        this.frame.add(pnlControls, BorderLayout.SOUTH);
        Custom.startButton(btnStart);
        Custom.startButton(btnStop);
        Custom.startButton(btnBack);

        this.frame.setVisible(true);

        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (timeLeft > 0) {
                    timeLeft--;
                    updateDisplay();
                } else {
                    timer.stop();
                    btnStart.setText("START");
                    labelDisplay.setForeground(Color.RED);
                    JOptionPane.showMessageDialog(null, "Time's up! Back to work!");
                    labelDisplay.setForeground(Color.BLACK);
                }
            }
        });
    }

    private void addTime(int seconds) {
        timeLeft += seconds;
        updateDisplay();
    }

    private void toggleTimer() {
        if (timer.isRunning()) {
            timer.stop();
            btnStart.setText("START");
        } else if (timeLeft > 0) {
            timer.start();
            btnStart.setText("PAUSE");
        }
    }

    private void stopTimer() {
        timer.stop();
        btnStart.setText("START");
    }

    private void resetTimer() {
        timer.stop();
        timeLeft = 0;
        btnStart.setText("START");
        updateDisplay();
    }

    private void updateDisplay() {
        int mins = timeLeft / 60;
        int secs = timeLeft % 60;
        labelDisplay.setText(String.format("%02d:%02d", mins, secs));
    }
}

