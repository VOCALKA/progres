package custom;

import javax.swing.*;
import java.awt.*;

public class RoundedButton extends JButton {
    /**
     * Constructs a new RoundedButton with the specified text label.
     * Disables the default operating system background fills, border painting, and focus indicators
     * to prevent native UI layers from clipping or interfering with the custom vector geometry.
     *
     * @param label The text to be displayed on the button surface.
     */
    public RoundedButton(String label) {
        super(label);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
    }

    /**
     * Paints the visual surface of the component using custom rounded geometry.
     * Creates a isolated graphic context snapshot, activates vector anti-aliasing for ultra-smooth edges,
     * renders a filled rounded rectangle using the currently active component background color,
     * and forwards execution to the superclass to overlay the button text safely.
     *
     * @param g The standard systems canvas graphics pipeline driver model.
     */
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);


        g2.setColor(getBackground());

        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);

        super.paintComponent(g);
        g2.dispose();
    }
}
