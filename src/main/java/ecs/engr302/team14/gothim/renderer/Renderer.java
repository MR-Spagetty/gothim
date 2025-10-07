package ecs.engr302.team14.gothim.renderer;

import ecs.engr302.team14.gothim.app.LevelManager;
import ecs.engr302.team14.gothim.logic.LevelHolder;
import ecs.engr302.team14.gothim.entities.NPC;
import ecs.engr302.team14.gothim.entities.Player;
import ecs.engr302.team14.gothim.entities.Taskbook;
import ecs.engr302.team14.gothim.map.Board;
import ecs.engr302.team14.gothim.tiles.PrimitiveTile;
import ecs.engr302.team14.gothim.util.Day;
import ecs.engr302.team14.gothim.util.Task;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

/**
 * Central Renderer for rendering the game world, entities, and taskbook.
 */
public class Renderer extends JPanel {

    private static Renderer instance;

    // Game state
    private Player player;
    private List<NPC> npcs;
    private Board board;
    private Day currentDay = Day.ONE;

    // Taskbook
    private boolean showTaskbook = false;
    private final Taskbook taskbook = new Taskbook();
    private BufferedImage openbook;
    private Rectangle taskbookBounds;
    private Rectangle nextButtonBounds;
    private Rectangle prevButtonBounds;

    // Tile images
    private BufferedImage grassTile;
    private BufferedImage fenceTile;
    private BufferedImage houseTile;

    private int tileSize = 32; // pixels per tile
    private int offsetX = 0;   // offsets to render tiles at 0,0
    private int offsetY = 0;

    private Renderer() {
        // Load book UI asset
        try {
            var url = getClass().getResource("/assets/Openbook.png");
            if (url != null) {
                openbook = ImageIO.read(url);
                taskbookBounds = new Rectangle(100, 100, openbook.getWidth(), openbook.getHeight());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Load tiles
        grassTile = loadTileImage("/assets/Grass_Tile.png");
        fenceTile = loadTileImage("/assets/Fence_Tile.png");
        houseTile = loadTileImage("/assets/Townhouse_Tile.png");

        setBackground(new Color(30, 30, 30));

        // Handle page turn clicks
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!showTaskbook) return;
                if (nextButtonBounds != null && nextButtonBounds.contains(e.getPoint())) {
                    goToNextDay();
                } else if (prevButtonBounds != null && prevButtonBounds.contains(e.getPoint())) {
                    goToPreviousDay();
                }
            }
        });
    }

    /** Singleton accessor. */
    public static synchronized Renderer getInstance() {
        if (instance == null) {
            instance = new Renderer();
        }
        return instance;
    }

    /** Load board, player, and entities from the current LevelHolder */
    public void loadFromLevel() {
        LevelHolder level = LevelManager.getLevelData();
        this.board = level.map();
        this.player = level.players().isEmpty() ? null : level.players().get(0);
        this.npcs = level.entities().stream()
                .filter(e -> e instanceof NPC)
                .map(e -> (NPC) e)
                .toList();

        // compute offsets so tiles start visible at 0,0
        if (board != null && !board.getAllTiles().isEmpty()) {
            int minX = board.getAllTiles().stream()
                    .mapToInt(t -> (int) t.pos().x())
                    .min().orElse(0);
            int minY = board.getAllTiles().stream()
                    .mapToInt(t -> (int) t.pos().y())
                    .min().orElse(0);
            offsetX = -minX * tileSize;
            offsetY = -minY * tileSize;
        }
    }

    public void toggleTaskbook() {
        showTaskbook = !showTaskbook;
        repaint();
    }

    // --- Core painting ---
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawTiles(g);
        drawEntities(g);
        drawPlayer(g);
        drawTaskbook(g);
    }

    // --- Draw tiles ---
    private void drawTiles(Graphics g) {
        if (board == null) return;

        // First pass: draw grass on every tile
        for (PrimitiveTile tile : board.getAllTiles()) {
            int tileX = (int) (tile.pos().x() * tileSize) + offsetX;
            int tileY = (int) (tile.pos().y() * tileSize) + offsetY;
            g.drawImage(grassTile, tileX, tileY, tileSize, tileSize, this);
        }

        // Second pass: draw non-grass tiles on top
        for (PrimitiveTile tile : board.getAllTiles()) {
            int tileX = (int) (tile.pos().x() * tileSize) + offsetX;
            int tileY = (int) (tile.pos().y() * tileSize) + offsetY;

            switch (tile.style) {
                case "fence" -> g.drawImage(fenceTile, tileX, tileY, tileSize, tileSize, this);
                case "townhouse" -> {
                    if (houseTile != null) {
                        // Map coordinates from your JSON
                        double topLeftX = 24;
                        double topLeftY = -10;
                        double bottomRightX = 36;
                        double bottomRightY = -25;

                        // Compute width and height in pixels
                        int width = (int) ((bottomRightX - topLeftX) * tileSize);
                        int height = (int) ((topLeftY - bottomRightY) * tileSize);

                        // Convert map coords to pixel position
                        int pixelX = (int) (topLeftX * tileSize) + offsetX;
                        int pixelY = (int) (bottomRightY * tileSize) + offsetY;

                        g.drawImage(houseTile, pixelX, pixelY, width, height, this);
                    }
                }
            }
        }
    }


    private BufferedImage loadTileImage(String path) {
        try {
            var url = getClass().getResource(path);
            if (url != null) return ImageIO.read(url);
            else System.err.println("Missing asset: " + path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void drawPlayer(Graphics g) {
        if (player != null) player.render(g);
    }

    private void drawEntities(Graphics g) {
        if (npcs != null) {
            for (NPC npc : npcs) npc.render(g);
        }
    }

    private void goToNextDay() {
        Day[] days = Day.values();
        int idx = currentDay.ordinal();
        if (idx < days.length - 1) {
            currentDay = days[idx + 1];
            repaint();
        }
    }

    private void goToPreviousDay() {
        Day[] days = Day.values();
        int idx = currentDay.ordinal();
        if (idx > 0) {
            currentDay = days[idx - 1];
            repaint();
        }
    }

    private void drawTaskbook(Graphics g) {
        if (!showTaskbook || openbook == null) return;

        int targetWidth = 700;
        double aspectRatio = (double) openbook.getHeight() / openbook.getWidth();
        int targetHeight = (int) (targetWidth * aspectRatio);

        int x = (getWidth() - targetWidth) / 2;
        int y = (getHeight() - targetHeight) / 2;

        g.drawImage(openbook, x, y, targetWidth, targetHeight, this);
        taskbookBounds.setBounds(x, y, targetWidth, targetHeight);

        drawPageButtons(g, x, y, targetWidth, targetHeight);

        g.setFont(new Font("Serif", Font.PLAIN, 20));
        g.setColor(Color.BLACK);
        drawTasksPage(g, x, y, targetWidth);
        drawDiscoveriesPage(g, x, y, targetWidth);
    }

    private void drawPageButtons(Graphics g, int x, int y, int width, int height) {
        int btnSize = 40;
        prevButtonBounds = new Rectangle(x + 30, y + height / 2 - btnSize / 2, btnSize, btnSize);
        nextButtonBounds = new Rectangle(x + width - 70, y + height / 2 - btnSize / 2, btnSize, btnSize);

        g.setColor(new Color(200, 200, 200, 180));
        g.fillRect(prevButtonBounds.x, prevButtonBounds.y, btnSize, btnSize);
        g.fillRect(nextButtonBounds.x, nextButtonBounds.y, btnSize, btnSize);

        g.setColor(Color.BLACK);
        g.setFont(new Font("SansSerif", Font.BOLD, 20));
        g.drawString("<", prevButtonBounds.x + 12, prevButtonBounds.y + 25);
        g.drawString(">", nextButtonBounds.x + 12, nextButtonBounds.y + 25);
    }

    private void drawTasksPage(Graphics g, int x, int y, int width) {
        int leftX = x + 100;
        int textY = y + 80;
        int columnWidth = width / 2 - 100;

        g.drawString("Day " + currentDay + " To-Do:", leftX, textY);
        textY += 40;

        List<Task> tasks = taskbook.getTasks().get(currentDay);
        if (tasks != null && !tasks.isEmpty()) {
            for (Task task : tasks) textY = drawWrappedText(g, "- " + task.taskDescription(), leftX, textY, columnWidth);
        } else {
            g.drawString("No tasks for this day.", leftX, textY);
        }
    }

    private void drawDiscoveriesPage(Graphics g, int x, int y, int width) {
        int rightX = x + width / 2 + 15;
        int rightY = y + 80;
        int columnWidth = width / 2 - 100;

        g.drawString("Discoveries:", rightX, rightY);
        rightY += 40;

        var clues = taskbook.getDiscoveredInformation();
        boolean found = false;
        for (var clue : clues) {
            if (clue.id().startsWith("Day" + currentDay.ordinal() + "_")) {
                found = true;
                rightY = drawWrappedText(g, "- (" + clue.modifier() + ") " + clue.description(), rightX, rightY, columnWidth);
            }
        }

        if (!found) g.drawString("No discoveries yet.", rightX, rightY);
    }

    private int drawWrappedText(Graphics g, String text, int x, int y, int width) {
        FontMetrics metrics = g.getFontMetrics();
        String[] words = text.split(" ");
        StringBuilder line = new StringBuilder();

        for (String word : words) {
            if (metrics.stringWidth(line + word) <= width) line.append(word).append(" ");
            else {
                g.drawString(line.toString(), x, y);
                line = new StringBuilder(word + " ");
                y += metrics.getHeight();
            }
        }
        g.drawString(line.toString(), x, y);
        return y + metrics.getHeight();
    }
}
