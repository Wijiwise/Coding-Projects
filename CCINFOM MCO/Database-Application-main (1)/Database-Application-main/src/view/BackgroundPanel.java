package view;

import javax.swing.JPanel;
import javax.swing.ImageIcon;
import java.awt.Image;
import java.awt.Graphics;

/**
 * The {@code BackgroundPanel} class is part of VIEW.
 * 
 * It extends {@code JPanel} to provide a panel with a custom background image.
 * 
 * This panel can be used as a container for other components while displaying
 * an image as the background.
 */
public class BackgroundPanel extends JPanel {
    private static final long serialVersionUID = 1L; // fixes serial warning
    private transient Image background;             // fixes non-serializable warning

    private BackgroundPanel(Image background) {
        this.background = background;
        setLayout(null); // keep null layout
    }

    /**
     * Factory method to create a BackgroundPanel from a file path.
     * This avoids the "this-escape" warning.
     */
    public static BackgroundPanel create(String path) {
        ImageIcon icon = new ImageIcon(path);
        BackgroundPanel panel = new BackgroundPanel(icon.getImage());
        panel.setOpaque(false); // now called safely after constructor
        return panel;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (background != null) {
            g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
