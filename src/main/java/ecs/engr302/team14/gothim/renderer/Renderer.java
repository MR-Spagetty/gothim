package ecs.engr302.team14.gothim.renderer;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class Renderer extends JPanel {

    private static Renderer instance;
    private boolean showTaskbook = false;
    private BufferedImage taskbook;
    private Rectangle taskbookBounds;


    /**
     * Constructs a Renderer instance
     */
    private Renderer() {
        // Load the book image once
        try {
            var url = getClass().getResource("/assets/Openbook.png");
            if (url == null) {
                System.err.println("Taskbook image not found in resources!");
            } else {
                taskbook = ImageIO.read(url);
                System.out.println("Loaded taskbook from " + url);
                taskbookBounds = new Rectangle(100, 100, taskbook.getWidth(), taskbook.getHeight());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Mouse listener for interaction
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (showTaskbook && taskbookBounds.contains(e.getPoint())) {
                    System.out.println("Taskbook clicked!");
                    // Add your interaction logic here
                }
            }
        });
    }

    /**
     * Singleton method to get the instance of Renderer.
     *
     * @return instance - the Renderer singleton
     */
    public static synchronized Renderer getInstance() {
        if (instance == null) {
            instance = new Renderer();
        }
        return instance;
    }

    public void toggleTaskbook() {
        showTaskbook = !showTaskbook;
        repaint(); // Force repaint when state changes
        System.out.println("paint called");
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (showTaskbook && taskbook != null) {
            int targetWidth = 700;

            double aspectRatio = (double) taskbook.getHeight() / taskbook.getWidth();
            int targetHeight = (int) (targetWidth * aspectRatio);

            int x = (getWidth() - targetWidth) / 2;
            int y = (getHeight() - targetHeight) / 2;

            g.drawImage(taskbook, x, y, targetWidth, targetHeight, this);

            taskbookBounds.setBounds(x, y, targetWidth, targetHeight);
            g.setFont(new Font("Serif", Font.PLAIN, 24)); // choose your font and size
            g.setColor(Color.BLACK);

            // Text to draw
            String text = "Taskbook.";

            // Draw text inside the book with some padding
            drawWrappedText(g, text, x + 100, y + 50, targetWidth - 2 * 50);
        }
    }

    /**
     * Draws the taskbook
     *
     * @param g - the graphics object used to draw the message box
     * @param tileSize - the size of the game board tiles
     */
//    private void drawInfoMessage(Graphics g, int tileSize) {
//        int boxWidth = tileSize * 8;
//        int boxHeight = tileSize * 2;
//
//        g.setColor(new Color(0, 0, 0, 200));
//        g.fillRect(50, 50, boxWidth, boxHeight);
//        g.setColor(Color.WHITE);
//
//        try {
//            Path fontPath = Paths.get(System.getProperty(""), "", "");
//            try (InputStream fontStream = Files.newInputStream(fontPath)) {
//                customFont = Font.createFont(Font.TRUETYPE_FONT, fontStream).deriveFont(28f);
//            }
//        } catch (FontFormatException | IOException e) {
//            e.printStackTrace();
//        }
//        g.setFont(customFont);
//        drawWrappedText(g, infoMessage, 60, 80, boxWidth - 20);
//    }

    /**
     * Draws the info string and ensures text wraps within the message box
     *
     * @param g - the graphics object used to draw the string
     * @param text - text to be drawn
     * @param x - x coord of where text starts
     * @param y - y coord of where text starts
     * @param width - max width before wrapping onto next line
     */
    private void drawWrappedText(Graphics g, String text, int x, int y, int width) {
        FontMetrics metrics = g.getFontMetrics();
        String[] words = text.split(" ");
        StringBuilder line = new StringBuilder();

        for (String word : words) {
            if (metrics.stringWidth(line + word) <= width) {
                line.append(word).append(" ");
            } else {
                g.drawString(line.toString(), x, y);
                line = new StringBuilder(word + " ");
                y += metrics.getHeight();
            }
        }
        g.drawString(line.toString(), x, y);
    }
}
