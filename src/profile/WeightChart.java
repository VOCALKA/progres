package profile;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class WeightChart extends JPanel {
    //private List<Double> weights;
    private List<WeightRecord> weights;
    private int hoveredIndex = -1;
    private double zoomFactor = 1.0;

    //
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d.M.yyyy");

    /**
     * Constructs a new WeightChart container panel.
     * Initializes visual field properties, attaches the mathematical scale zoom engine listeners,
     * and sets up the interactive multi-node lookup bounds parser tracking active hover changes.
     *
     * @param weights The underlying sequence collection utilized as the design source for mapping vectors.
     */
    public WeightChart(List<WeightRecord> weights) {
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


    /**
     * Calculates the dimensional requirements of the viewport, multiplying the base width
     * threshold dynamically by the scale magnification factor.
     *
     * @return A scaled {@link Dimension} object container mapping the drawing width and height limits.
     */
    @Override
    public Dimension getPreferredSize() {

        int width = (int) (450 * zoomFactor);
        return new Dimension(width, 300);
    }

    /**
     * Maps an object collection element position indices to strict physical canvas pixels [X,Y].
     * Scales relative grid steps and structures maximum weight boundaries to align perfectly with drawing margins.
     *
     * @param i The targeting collection node index location.
     * @return A {@link Point} coordinate positioning model detailing canvas layout locations.
     */
    private Point getPointLocation(int i) {
        int padding = 50;
        int currentWidth = getPreferredSize().width;

        double xStep = (weights.size() > 1) ? (double) (currentWidth - 2 * padding) / (weights.size() - 1) : 0;

        double maxVaha = weights.stream().mapToDouble(WeightRecord::weight).max().orElse(100.0);
        maxVaha = Math.max(maxVaha, 100.0);
        double yScale = (double) (getHeight() - 2 * padding) / maxVaha;

        int x = padding + (int) (i * xStep);
        int y = getHeight() - padding - (int) (weights.get(i).weight() * yScale);

        return new Point(x, y);
    }

    /**
     * Replaces the internal weight collection dataset cache and requests an immediate UI redraw.
     *
     * @param weights The updated historical record sequence list.
     */
    public void setWeights(List<WeightRecord> weights) {
        this.weights = weights;
        repaint();
    }

    /**
     * Paints the visual grid, layout lines, and vectors of the tracking profile.
     * Renders background directional paths, connects mathematical node sequences with styled lines,
     * and handles highlighted overlay states whenever a precise node matches cursor hover conditions.
     *
     * @param g The standard systems canvas graphics pipeline driver model.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (weights == null || weights.isEmpty()) {
            g.drawString("No chart data available.", 50, 50);
            return;
        }

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int padding = 50;
        int height = getHeight();
        int width = getWidth();

        g2.setColor(Color.LIGHT_GRAY);
        g2.drawLine(padding, height - padding, padding, padding);
        g2.drawLine(padding, height - padding, (int) (width * zoomFactor), height - padding);

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

                WeightRecord aktualni = weights.get(i);
                String datumText = aktualni.datum().format(formatter);
                String text = aktualni.weight() + " kg | " + datumText;

                g2.setColor(Color.BLACK);
                g2.drawString(text, p1.x - 20, p1.y - 15);
            } else {
                g2.setColor(new Color(41, 128, 185));
                g2.fillOval(p1.x - 4, p1.y - 4, 8, 8);
            }
        }

    }

    /**
     * Safe immutable data record pattern model tracking individual localized weight timeline metrics.
     *
     * @param datum  The specific temporal object log capturing when the event calculation was committed.
     * @param weight The exact physical mass recorded on the timeline measured in kilograms.
     */
    public record WeightRecord(java.time.LocalDate datum, double weight) {
    }

}


