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

        this.frame.setVisible(true);
    }
}
