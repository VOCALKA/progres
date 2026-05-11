package profile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.List;

public class WeightChart extends JPanel {
    private List<Double> weights;
    private int hoveredIndex = -1;
    private double zoomFactor = 1.0;

    public WeightChart(List<Double> weights) {
        this.weights = weights;
        setBackground(Color.WHITE);

        addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                if (e.getWheelRotation() < 0) {
                    zoomFactor *= 1.1;
                } else {
                    zoomFactor /= 1.1;
                }

                zoomFactor = Math.max(0.5, Math.min(zoomFactor, 10.0));

                revalidate();
                repaint();
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                if (weights == null || weights.isEmpty()) return;

                int foundIndex = -1;
                for (int i = 0; i < weights.size(); i++) {
                    Point p = getPointLocation(i);
                    if (e.getPoint().distance(p) < 10) {
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

    private Point getPointLocation(int i) {
        int padding = 50;
        double xStep = (weights.size() > 1) ? (double) (getWidth() - 2 * padding) / (weights.size() - 1) : 0;

        double maxVaha = weights.stream().max(Double::compare).orElse(100.0);
        maxVaha = Math.max(maxVaha, 100.0);
        double yScale = (double) (getHeight() - 2 * padding) / maxVaha;

        int x = padding + (int) (i * xStep * zoomFactor);
        int y = getHeight() - padding - (int) (weights.get(i) * yScale);

        return new Point(x, y);
    }

    @Override
    public Dimension getPreferredSize() {
        int baseWidth = 400;
        return new Dimension((int) (baseWidth * zoomFactor), 300);
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
        int height = getHeight();
        int width = getWidth();

        g2.setColor(Color.LIGHT_GRAY);
        g2.drawLine(padding, height - padding, padding, padding);
        g2.drawLine(padding, height - padding, (int)(width * zoomFactor), height - padding);

        for (int i = 0; i < weights.size(); i++) {
            Point p1 = getPointLocation(i);


            if (i < weights.size() - 1) {
                Point p2 = getPointLocation(i + 1);
                g2.setColor(new Color(41, 128, 185));
                g2.drawLine(p1.x, p1.y, p2.x, p2.y);
            }


            if (i == hoveredIndex) {
                g2.setColor(Color.RED);
                g2.fillOval(p1.x - 6, p1.y - 6, 12, 12);

                String text = weights.get(i) + " kg";
                g2.setColor(Color.BLACK);
                g2.drawString(text, p1.x - 10, p1.y - 15);
            } else {
                g2.setColor(new Color(41, 128, 185));
                g2.fillOval(p1.x - 4, p1.y - 4, 8, 8);
            }
        }
    }
}


