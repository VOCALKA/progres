import custom.Custom;
import custom.RoundedButton;
import loading.LoadingScreen;
import profile.Profile;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;


public class App {
    private JFrame frame;
    private JLabel casLabel;

    public App() {
        this.frame = new JFrame("APP!");
    }

    public void showApp(){
        this.frame.setSize(500, 500);
        this.frame.setLayout(new BorderLayout());
        this.frame.setLocationRelativeTo(null);
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        RoundedButton button = new RoundedButton("PROFILE");
        Custom.startButton(button);
        this.frame.add(button, BorderLayout.SOUTH);

        button.addActionListener(e -> {
            this.frame.dispose();
            new LoadingScreen().show();
            //new LoadingScreen().show();
        });
        clock();

        this.frame.setVisible(true);
    }

    public void clock(){
        casLabel = new JLabel("", SwingConstants.CENTER);
        casLabel.setFont(new Font("Arial", Font.BOLD, 16));
        this.frame.add(casLabel, BorderLayout.NORTH);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, d. MMMM yyyy HH:mm:ss",
                new Locale("cs", "CZ"));

        Timer timer = new Timer(1000, e -> {
            casLabel.setText(LocalDateTime.now().format(formatter));
        });

        timer.start();

        casLabel.setText(LocalDateTime.now().format(formatter));
    }
}
