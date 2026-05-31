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
    public static void startText(JLabel label){
        label.setForeground(new Color(0,0, 54));
        label.setFont(new Font("Arial", Font.BOLD, 30));
    }
    public static void background(JFrame frame){
        frame.getContentPane().setBackground(new Color(
                169,
                224,
                255));
    }

    public static void streakStyle(JLabel label, int streakHodnota) {
        label.setFont(new Font("Arial", Font.BOLD, 22));
        if (streakHodnota > 0) {
            label.setForeground(new Color(255, 102, 0));
        } else {
            label.setForeground(Color.GRAY);
        }
    }
    public static void exerciseStyle(JLabel label) {
        label.setFont(new Font("Arial", Font.PLAIN, 28));
        label.setForeground(new Color(0, 0, 54));
    }
    public static void timeStyle(JLabel label) {
        label.setFont(new Font("Arial", Font.BOLD, 70));
        label.setForeground(Color.BLACK);
    }
    public static void stavStyle(JLabel label, Color barvaTextu) {
        label.setFont(new Font("Arial", Font.BOLD, 40));
        label.setForeground(barvaTextu);
    }

    public static void bmiNumberStyle(JLabel label) {
        label.setFont(new Font("Arial", Font.BOLD, 60));
    }

    public static void bmiTextStyle(JLabel label) {
        label.setFont(new Font("Arial", Font.BOLD, 22));
    }

    public static void bmiWeightUsedStyle(JLabel label) {
        label.setFont(new Font("Arial", Font.PLAIN, 16));
        label.setForeground(Color.GRAY);
    }

    public static void calorieNumberStyle(JLabel label) {
        label.setFont(new Font("Arial", Font.BOLD, 45));
        label.setForeground(new Color(154, 17, 34));
    }

    public static void macroStyle(JLabel label, String type) {
        label.setFont(new Font("Arial", Font.BOLD, 16));
        switch (type.toLowerCase()) {
            case "protein":
                label.setForeground(new Color(70, 130, 180));
                break;
            case "carbs":
                label.setForeground(new Color(218, 165, 32));
                break;
            case "fats":
                label.setForeground(new Color(46, 139, 87));
                break;
        }
    }

    public static void styleTab(JTabbedPane tabbedPane, int tabIndex) {
        tabbedPane.setBackgroundAt(tabIndex, new Color(154, 17, 34));
        tabbedPane.setForegroundAt(tabIndex, Color.WHITE);
    }
}
