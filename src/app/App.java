package app;

import calorieTracker.CalorieTracker;
import custom.Custom;
import custom.RoundedButton;
import loading.LoadingScreen;
import plan.RunPlan;
import timer.GymTimer;
import plan.Plan;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;


public class App {
    private JFrame frame;
    private JLabel casLabel;
    private JLabel streakLabel;

    /**
     * The App class serves as the central navigation dashboard and main hub of the entire application.
     * It coordinates real-time localized clock feeds, displays user engagement streak telemetry fetched
     * from the {@link StreakManager}, and populates custom rounded button arrays providing routing pathways
     * to secondary sub-modules (Profile, GymTimer, Plan, and CalorieTracker).
     */
    public App() {
        this.frame = new JFrame("APP!");
    }

    /**
     * Assembles and presents the main application landing navigation menu workspace layout.
     * Pulls logging consistency stats, formats layout alignment vectors using a {@link GridBagLayout},
     * registers navigation listener triggers, styles elements via {@link Custom}, and renders sub-views.
     */
    public void showApp(){
        this.frame.setSize(500, 500);
        this.frame.setLayout(new BorderLayout());
        this.frame.setLocationRelativeTo(null);
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Custom.background(frame);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);

        int currentStreak = StreakManager.getWeightStreak();

        streakLabel = new JLabel("<html>&#128293; " + currentStreak + " DAY STREAK</html>", SwingConstants.CENTER);
        Custom.streakStyle(streakLabel, currentStreak);

        centerPanel.add(streakLabel, new GridBagConstraints());
        this.frame.add(centerPanel, BorderLayout.CENTER);


        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        buttonPanel.setOpaque(false);

        RoundedButton button = new RoundedButton("PROFILE");
        Custom.startButton(button);


        button.addActionListener(e -> {
            this.frame.dispose();
            new LoadingScreen().show();

        });

        RoundedButton button2 = new RoundedButton("TIMER");
        Custom.startButton(button2);


        button2.addActionListener(e -> {
            this.frame.dispose();
            new GymTimer().showTimer();
        });

        RoundedButton button3 = new RoundedButton("PLAN");
        Custom.startButton(button3);

        button3.addActionListener(e -> {
            this.frame.dispose();
            new Plan().showPlan();
        });

        RoundedButton buttonCalories = new RoundedButton("CALORIES");
        Custom.startButton(buttonCalories);

        buttonCalories.addActionListener(e -> {
            this.frame.dispose();
            new CalorieTracker().showTracker();
        });

        clock();

        buttonPanel.add(buttonCalories);
        buttonPanel.add(button3);
        buttonPanel.add(button2);
        buttonPanel.add(button);
        this.frame.add(buttonPanel, BorderLayout.SOUTH);

        this.frame.setVisible(true);
    }

    /**
     * Initializes and fires a background tracking thread clock loop attached to the layout header.
     * Hooks a repeating Swing {@link Timer} executing once every 1000ms and applies
     * full Czech language locale structural string format patterns to render local time (e.g., "Sunday, 31. May 2026...").
     */
    public void clock(){
        casLabel = new JLabel("", SwingConstants.CENTER);
        Custom.startText(casLabel);

        this.frame.add(casLabel, BorderLayout.NORTH);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, d. MMMM yyyy HH:mm:ss",
                new Locale("en", "US"));

        Timer timer = new Timer(1000, e -> {
            casLabel.setText(LocalDateTime.now().format(formatter));
        });

        timer.start();

        casLabel.setText(LocalDateTime.now().format(formatter));
    }
}
