package profile;

import custom.Custom;
import custom.RoundedButton;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;



public class Profile {
    private JFrame frame;
    private JLabel casLabel;

    public Profile() {
        this.frame = new JFrame("PROFILE");
    }

    public void showApp(){
        this.frame.setSize(500, 500);
        this.frame.setLayout(new BorderLayout());
        this.frame.setLocationRelativeTo(null);
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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

