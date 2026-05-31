package loading;

import profile.Profile;

import javax.swing.*;
import java.util.List;

public class LoadingWorker extends SwingWorker<Void, int[]> {
    private JLabel status;
    private JProgressBar progress;
    private JFrame frame;

    /**
     * Constructs a new LoadingWorker thread coordinator.
     * Binds structural display component resources from the calling initialization screen view container.
     *
     * @param status   The display label rendering description strings.
     * @param progress The tracking progress bar reference.
     * @param frame    The structural frame containing the loading view.
     */
    public LoadingWorker(JLabel status, JProgressBar progress, JFrame frame) {
        this.status = status;
        this.progress = progress;
        this.frame = frame;
    }

    private String[] steps = {"LOADING DATA", "UPDATING PROFILE", "GETTING READY", "DONE"};

    /**
     * Executes asynchronous simulation tasks on a isolated background worker pool thread.
     * Increments internal state index values up to one hundred percent, pauses slightly to mimic
     * operational strain, calculates current phase strings, and publishes state data chunks for UI updates.
     *
     * @return null standard return payload indicator.
     * @throws Exception If thread sleep routines get unexpectedly interrupted.
     */
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

    /**
     * Intercepts transient computation arrays on the safe Event Dispatch Thread (EDT).
     * Extracts the most recent tracking block, updates the visual fill state value of the tracking gauge,
     * and performs conditional checks to refresh string descriptions without redundant layout cycles.
     *
     * @param chunks Collection list containing integer arrays formatted as [progressPercent, stepStatusIndex].
     */
    @Override
    protected void process(List<int[]> chunks) {
        int[] last = chunks.get(chunks.size() - 1);
        progress.setValue(last[0]);

        String newText = steps[last[1]];
        if (!status.getText().equals(newText)) {
            status.setText(newText);
        }
    }


    /**
     * Automatically executed by the core worker ecosystem once the background process task resolves.
     * Safely tears down the splash screen frame context allocations and invokes the target profile view.
     */
    @Override
    protected void done() {
        frame.dispose();
        new Profile().showApp();
    }
}
