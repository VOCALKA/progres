package loading;
import javax.swing.*;
import java.awt.*;

public class LoadingScreen {
    private JFrame frame;
    public LoadingScreen() {
        frame = new JFrame("loading");

    }
    public void show() {
        frame.setSize(400, 150);
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        JLabel status = new JLabel("nacitani aplikace", JLabel.CENTER);
        JProgressBar progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(progressBar, BorderLayout.CENTER);
        panel.add(status, BorderLayout.NORTH);

        frame.add(panel);
        frame.setVisible(true);

        LoadingWorker loadingWorker = new LoadingWorker(status, progressBar, frame);
        loadingWorker.execute();
    }
}
