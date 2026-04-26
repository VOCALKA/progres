import custom.Custom;
import custom.RoundedButton;
import loading.LoadingScreen;
import profile.Profile;

import javax.swing.*;
import java.awt.*;

public class App {
    private JFrame frame;

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

        this.frame.setVisible(true);
    }
}
