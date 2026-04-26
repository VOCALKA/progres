package loading;

import profile.Profile;

import javax.swing.*;
import java.util.List;

public class LoadingWorker extends SwingWorker<Void, int[]> {
    private JLabel status;
    private JProgressBar progress;
    private JFrame frame;

    public LoadingWorker(JLabel status, JProgressBar progress, JFrame frame) {
        this.status = status;
        this.progress = progress;
        this.frame = frame;
    }

    private String[] steps = {"NACITANI DAT", "AKTUALIZACE PROFILU", "PRIPRAVUJEME PRO VAS", "HOTOVO"};

    /*@Override
    protected Void doInBackground() throws Exception {
        for (int i = 0; i < steps.length; i++) {
            Thread.sleep(3000);

            publish(new int[]{(i + 1) * 25, i});
        }
        return null;
    }*/
    @Override
    protected Void doInBackground() throws Exception {
        int totalSteps = 100;
        for (int i = 0; i <= totalSteps; i++) {
            Thread.sleep(30);


            int stepIndex = Math.min(i / 25, steps.length - 1);

            publish(new int[]{i, stepIndex});
        }
        return null;
    }


    /*@Override
    protected void process(List<int[]> chunks) {
        int[] last = chunks.get(chunks.size() -1);
        progress.setValue(last[0]);
        status.setText(steps[last[1]]);
    }*/
    @Override
    protected void process(List<int[]> chunks) {
        int[] last = chunks.get(chunks.size() - 1);
        progress.setValue(last[0]);

        String newText = steps[last[1]];
        if (!status.getText().equals(newText)) {
            status.setText(newText);
        }
    }


    @Override
    protected void done() {
        frame.dispose();
        new Profile().showApp();
    }
}
