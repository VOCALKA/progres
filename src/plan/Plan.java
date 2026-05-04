package plan;

import javax.swing.*;
import java.awt.*;
import custom.Custom;

public class Plan {
    private JFrame frame;
    public Plan() {
        this.frame = new JFrame("PLAN");
    }
    public void showPlan(){
        this.frame.setSize(500, 500);
        this.frame.setLayout(new BorderLayout());
        this.frame.setLocationRelativeTo(null);
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Custom.background(frame);

        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        buttonPanel.setOpaque(false);

        this.frame.setVisible(true);
    }
}
