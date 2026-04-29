import custom.Custom;
import custom.RoundedButton;

import javax.swing.*;
import java.awt.*;

public class TitleScreen {
    private JFrame frame;
    //private String language;

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

        /*JButton settings = new JButton("Nastaveni");
        CustomButton.settingsButton(button);
        this.frame.add(settings, BorderLayout.EAST);*/

        //
         //
        /*button.addActionListener(e -> {
            this.frame.dispose();
            new App().showApp();
            //new LoadingScreen().show();
        });*/
        //
         //
        button.addActionListener(e -> {
            this.frame.dispose();
            new Login().setVisible(true);
        });


        /*settings.addActionListener(e -> {
            SettingsDialog dialog = new SettingsDialog(this.frame);
            dialog.setVisible(true);
            language = dialog.getLanguage();
            if (language != null){
                JOptionPane.showMessageDialog(this.frame, "Jazyk ulozen " + language,
                        "vyskakovaci okno", JOptionPane.INFORMATION_MESSAGE);
            }
        });*/


        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setVisible(true);
    }
}
