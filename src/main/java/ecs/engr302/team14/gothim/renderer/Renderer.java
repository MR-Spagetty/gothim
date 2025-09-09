package ecs.engr302.team14.gothim.renderer;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Renderer extends JPanel {

    private static Renderer instance;

    /**
     * Constructs a Renderer instance
     */
    private Renderer() {

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
