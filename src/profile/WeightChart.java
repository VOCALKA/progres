package profile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class WeightChart extends JPanel {
    private List<Double> weights;
    private int hoveredIndex = -1;

    public WeightChart(List<Double> weights) {
        this.weights = weights;
        setBackground(Color.WHITE);

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                if (weights == null || weights.isEmpty()) return;

                int padding = 50;
                int width = getWidth();
                int height = getHeight();
                double xStep = (weights.size() > 1) ? (double) (width - 2 * padding) / (weights.size() - 1) : 0;
                double maxVaha = weights.stream().max(Double::compare).orElse(100.0);
                maxVaha = Math.max(maxVaha, 100.0);
                double yScale = (double) (height - 2 * padding) / maxVaha;

                int foundIndex = -1;
                for (int i = 0; i < weights.size(); i++) {
                    int x = padding + (int) (i * xStep);
                    int y = height - padding - (int) (weights.get(i) * yScale);
                    if (e.getPoint().distance(x, y) < 10) {
                        foundIndex = i;
                        break;
                    }
                }
                if (foundIndex != hoveredIndex) {
                    hoveredIndex = foundIndex;
                    repaint();
                }
            }
        });
    }

    public void setWeights(List<Double> weights) {
        this.weights = weights;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (weights == null || weights.isEmpty()) {
            g.drawString("Nejsou k dispozici žádná data pro graf.", 50, 50);
            return;
        }

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int padding = 50;
        int width = getWidth();
        int height = getHeight();

        g2.setColor(Color.LIGHT_GRAY);
        g2.drawLine(padding, height - padding, padding, padding);
        g2.drawLine(padding, height - padding, width - padding, height - padding);

        double xStep = (weights.size() > 1) ? (double) (width - 2 * padding) / (weights.size() - 1) : 0;
        double maxVaha = weights.stream().max(Double::compare).orElse(100.0).doubleValue();
        maxVaha = Math.max(maxVaha, 100.0);
        double yScale = (double) (height - 2 * padding) / maxVaha;

        for (int i = 0; i < weights.size(); i++) {
            int x1 = padding + (int) (i * xStep);
            int y1 = height - padding - (int) (weights.get(i) * yScale);

            if (i < weights.size() - 1) {
                int x2 = padding + (int) ((i + 1) * xStep);
                int y2 = height - padding - (int) (weights.get(i + 1) * yScale);
                g2.setColor(new Color(41, 128, 185));
                g2.drawLine(x1, y1, x2, y2);
            }

            if (i == hoveredIndex) {
                g2.setColor(Color.RED);
                g2.fillOval(x1 - 6, y1 - 6, 12, 12);

                String text = weights.get(i) + " kg";
                g2.setColor(Color.BLACK);
                g2.drawString(text, x1 - 10, y1 - 15);
            } else {
                g2.setColor(new Color(41, 128, 185));
                g2.fillOval(x1 - 4, y1 - 4, 8, 8);
            }
        }
    }
}

