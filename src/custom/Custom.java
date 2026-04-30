package custom;

import javax.swing.*;
import java.awt.*;

public class Custom {
    public static void startButton(JButton button){
        button.setBackground(new Color(0, 0, 54));
        button.setForeground(Color.BLUE);
        button.setFont(new Font("SansSerif", Font.BOLD, 20));
        button.setForeground(new Color(255, 255, 255));

        button.setFocusPainted(false);
        button.setBorderPainted(false);
    }
    /*public static void settingsButton(JButton button){
        button.setBackground(new Color(149, 204, 152));
        button.setForeground(Color.CYAN);
        button.setFont(new Font("Arial", Font.BOLD, 20));

        button.setFocusPainted(false);
        button.setBorderPainted(false);
    }*/
    public static void startText(JLabel label){
        label.setForeground(new Color(0,0, 54));
        label.setFont(new Font("Arial", Font.BOLD, 30));
    }
    public static void background(JFrame frame){
        frame.getContentPane().setBackground(new Color(169, 224, 255));
    }


}
