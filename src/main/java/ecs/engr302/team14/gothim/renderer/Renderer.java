package ecs.engr302.team14.gothim.renderer;

import ecs.engr302.team14.gothim.app.LevelManager;
import ecs.engr302.team14.gothim.app.Main;
import ecs.engr302.team14.gothim.entities.NPC;
import ecs.engr302.team14.gothim.entities.Player;
import ecs.engr302.team14.gothim.entities.Taskbook;
import ecs.engr302.team14.gothim.logic.LevelHolder;
import ecs.engr302.team14.gothim.map.Board;
import ecs.engr302.team14.gothim.tiles.PrimitiveTile;
import ecs.engr302.team14.gothim.util.Day;
import ecs.engr302.team14.gothim.util.Point;
import ecs.engr302.team14.gothim.util.Task;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

/**
 * Central Renderer for rendering the game world, entities, and taskbook.
 */
public class Renderer extends JPanel {

    static LoadingCache<String, BufferedImage> sprites = CacheBuilder.newBuilder()
            .expireAfterAccess(Duration.ofSeconds(10)).softValues().build(new CacheLoader<>() {
                public BufferedImage load(@SuppressWarnings("null")
                String key) throws Exception {
                    System.out.println("Loading + assets/%s.png".formatted(key));
                    return ImageIO.read(Renderer.class.getClassLoader()
                            .getResource("assets/%s.png".formatted(key)));
                }
            });

    private static Renderer instance;

    // Game state
    private Board board;
    private Day currentDay = Day.ONE;

    // Taskbook
    private boolean showTaskbook = false;
    private final Taskbook taskbook = new Taskbook();
    private BufferedImage openbook;
    private Rectangle taskbookBounds;
    private Rectangle nextButtonBounds;
    private Rectangle prevButtonBounds;

    private static final int TILE_SIZE = 33; // pixels per tile
    private int offsetX = 0; // offsets to render tiles at 0,0
    private int offsetY = 0;

    private Renderer() {
        // Load book UI asset
            try {
                openbook = sprites.get("openbook");
                taskbookBounds = new Rectangle(100, 100, openbook.getWidth(), openbook.getHeight());
            } catch (ExecutionException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        setBackground(new Color(30, 30, 30));

        // Handle page turn clicks
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!showTaskbook) {
                    return;
                }
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

    /** Load board, player, and entities from the current LevelHolder. */
    public void loadFromLevel() {
        LevelHolder level = LevelManager.getLevelData();
        this.board = level.map();

        // compute offsets so tiles start visible at 0,0
        if (board != null && !board.getAllTiles().isEmpty()) {
            int minX = board.getAllTiles().stream().mapToInt(t -> (int) t.pos().x()).min()
                    .orElse(0);
            int minY = board.getAllTiles().stream().mapToInt(t -> (int) t.pos().y()).min()
                    .orElse(0);
            offsetX = -minX * TILE_SIZE;
            offsetY = -minY * TILE_SIZE;
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
        for (Player p : LevelManager.getLevelData().players()) {
            drawPlayers(g, p);
        }
        drawTaskbook(g);
    }

    // --- Draw tiles ---
    private void drawTiles(Graphics g) {
        if (board == null) {
            return;
        }

        // Second pass: draw non-grass tiles on top
        for (PrimitiveTile tile : board.getTiles(LevelManager.getLevelData().getPlayer(Main.playerID).getPosition(), 10)) {
            String style = tile.style.toLowerCase();
            switch (style) {
                case "townhouse" -> {
                    drawTile("fog", tile.pos(), g);
                        try {
                            BufferedImage houseTile = sprites.get("townhouse");
                            double topLeftX = 24;
                            double topLeftY = -10;
                            double bottomRightX = 36;
                            double bottomRightY = -25;

                            // Compute width and height in pixels
                            int width = (int) ((bottomRightX - topLeftX) * TILE_SIZE);
                            int height = (int) ((topLeftY - bottomRightY) * TILE_SIZE);

                            // Convert map coords to pixel position
                            int pixelX = (int) (topLeftX * TILE_SIZE) + offsetX;
                            int pixelY = (int) (bottomRightY * TILE_SIZE) + offsetY;

                            g.drawImage(houseTile, pixelX, pixelY, width, height, this);
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }

                }
                case "fence" -> {
                    drawTile("grass", tile.pos(), g);
                    drawTile(style, tile.pos(), g);
                }
                default -> drawTile(style, tile.pos(), g);
            }
        }
    }

    private void drawTile(String type, Point pos, Graphics g){
            int tileX = (int) (pos.x() * TILE_SIZE) + offsetX;
            int tileY = (int) (pos.y() * TILE_SIZE) + offsetY;
            try {
                BufferedImage tileImg = sprites.get(type);
                g.drawImage(tileImg, tileX, tileY, TILE_SIZE, TILE_SIZE, this);
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
    }

    private void drawPlayers(Graphics g, Player p) {

        Point pos = p.getPosition();

        // Center camera on player
        offsetX = getWidth() / 2 - (int) (pos.x() * TILE_SIZE);
        offsetY = getHeight() / 2 - (int) (pos.y() * TILE_SIZE);

        int px = (int) (pos.x() * TILE_SIZE) + offsetX;
        int py = (int) (pos.y() * TILE_SIZE) + offsetY;
        BufferedImage playerSprite = null;
        try {
            playerSprite = sprites.get("player_"+p.getIdentity().name());
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        if (playerSprite != null) {
            int spriteWidth = playerSprite.getWidth();
            int spriteHeight = playerSprite.getHeight();
            float ratio = ((float) spriteWidth) / spriteHeight;
            int trueWidth = (int) (ratio * TILE_SIZE);
            g.drawImage(playerSprite, px + (TILE_SIZE - trueWidth) / 2, py, trueWidth, TILE_SIZE,
                    this);
        } else {
            g.setColor(Color.RED);
            g.fillRect(px, py, TILE_SIZE, TILE_SIZE);
        }
    }

    private void drawEntities(Graphics g) {
        for (NPC npc : LevelManager.getLevelData().entities().stream().<NPC>mapMulti((e, cons) -> {
            if (e instanceof NPC npcE) {
                cons.accept(npcE);
            }
        }).toList()) {
            npc.render(g);
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
        if (!showTaskbook || openbook == null) {
            return;
        }

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
        nextButtonBounds = new Rectangle(x + width - 70, y + height / 2 - btnSize / 2, btnSize,
                btnSize);

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
            for (Task task : tasks) {
                textY = drawWrappedText(g, "- " + task.taskDescription(), leftX, textY,
                        columnWidth);
            }
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
                rightY = drawWrappedText(g, "- (" + clue.modifier() + ") " + clue.description(),
                        rightX, rightY, columnWidth);
            }
        }

        if (!found) {
            g.drawString("No discoveries yet.", rightX, rightY);
        }
    }

    private int drawWrappedText(Graphics g, String text, int x, int y, int width) {
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
        return y + metrics.getHeight();
    }
}
