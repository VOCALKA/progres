package titleScreen;

import custom.Custom;
import custom.RoundedButton;

import javax.swing.*;
import java.awt.*;

public class TitleScreen {
    private JFrame frame;

    /**
     * Constructs a new TitleScreen.
     * Initializes the main window frame with the title "PROGRES" and
     * automatically triggers the UI creation and visibility.
     */
    public TitleScreen() {
        this.frame = new JFrame("PROGRES");
        showTitleScreen();
    }

    /**
     * Builds and displays the graphical user interface for the title screen.
     * Configures frame sizing, centers it on screen, applies custom background,
     * adds the stylized main logo text, attaches a rounded "LOG IN" button,
     * and sets up the action listener to transition to the {@link Login} window.
     */
    public void showTitleScreen(){
        this.frame.setSize(500, 500);
        this.frame.setLayout(new BorderLayout());
        this.frame.setLocationRelativeTo(null);


        Custom.background(frame);

        JLabel label = new JLabel("PROGRES", JLabel.CENTER);
        Custom.startText(label);
        this.frame.add(label, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 1));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));


        RoundedButton button = new RoundedButton("LOG IN");
        Custom.startButton(button);

        buttonPanel.add(button);
        this.frame.add(buttonPanel, BorderLayout.SOUTH);

        button.addActionListener(e -> {
            this.frame.dispose();
            new Login().setVisible(true);
        });

        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setVisible(true);
    }
}
