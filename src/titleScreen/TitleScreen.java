package titleScreen;

import custom.Custom;
import custom.RoundedButton;

import javax.swing.*;
import java.awt.*;

public class TitleScreen {
    private JFrame frame;

    public TitleScreen() {
        this.frame = new JFrame("PROGRES");
        showTitleScreen();
    }

    public void showTitleScreen(){
        this.frame.setSize(500, 500);
        this.frame.setLayout(new BorderLayout());
        this.frame.setLocationRelativeTo(null);


        Custom.background(frame);

        JLabel label = new JLabel("PROGRES", JLabel.CENTER);
        Custom.startText(label);
        this.frame.add(label, BorderLayout.CENTER);

        RoundedButton button = new RoundedButton("LOG IN");
        Custom.startButton(button);
        this.frame.add(button, BorderLayout.SOUTH);

        button.addActionListener(e -> {
            this.frame.dispose();
            new Login().setVisible(true);
        });

        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setVisible(true);
    }
}
