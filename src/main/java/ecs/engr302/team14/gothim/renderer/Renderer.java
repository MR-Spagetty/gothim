package ecs.engr302.team14.gothim.renderer;

import ecs.engr302.team14.gothim.entities.NPC;
import ecs.engr302.team14.gothim.entities.Player;
import ecs.engr302.team14.gothim.entities.Taskbook;
import ecs.engr302.team14.gothim.map.Board;
import ecs.engr302.team14.gothim.map.MapBuilder;
import ecs.engr302.team14.gothim.tiles.PrimitiveTile;
import ecs.engr302.team14.gothim.util.Day;
import ecs.engr302.team14.gothim.util.Task;
import ecs.engr302.team14.gothim.util.Point;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Central Renderer for rendering the game world, entities, and taskbook.
 * All tiles default to grass.
 */
public class Renderer extends JPanel {
    private static Renderer instance;

    // Game state
    private Player player;
    private List<NPC> npcs;
    private Board board = new MapBuilder()
            .withFilledRect(
                    "floor",
                    Map.of("style", "grass"),
                    new Point(0, 49),   // topLeft.y >= bottomRight.y
                    new Point(49, 0)
            )
            .build();


    private Day currentDay = Day.ONE;

    // Taskbook
    private boolean showTaskbook = false;
    private final Taskbook taskbook = new Taskbook();
    private BufferedImage openbook;
    private Rectangle taskbookBounds;
    private Rectangle nextButtonBounds;
    private Rectangle prevButtonBounds;

    // Tile
    private BufferedImage grassTile;
    private int tileSize = 32; // pixels per tile

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

        // Load default grass tile
        try {
            var url = getClass().getResource("/assets/Grass_Tile.png");
            if (url != null) {
                grassTile = ImageIO.read(url);
            } else {
                System.err.println("Could not find Grass_Tile.png");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

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

    // --- Setters ---
    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setNPCs(List<NPC> npcs) {
        this.npcs = npcs;
    }

    public void setBoard(Board board) {
        this.board = board;
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

    // --- Draw tiles as default grass ---
    private void drawTiles(Graphics g) {
        if (board == null) return;

        for (PrimitiveTile tile : board.getAllTiles()) {
            int x = (int) (tile.pos().x() * tileSize);
            int y = (int) (tile.pos().y() * tileSize); // flip Y if negative

            // Choose tile image based on style
            BufferedImage tileImg = switch (tile.style) {
                case "grass" -> grassTile;      // default
                case "fence" -> loadTileImage("/assets/Fence_Tile.png");
                case "townhouse" -> loadTileImage("/assets/Townhouse_Tile.png");
                default -> grassTile;
            };

            if (tileImg != null) {
                g.drawImage(tileImg, x, y, tileSize, tileSize, this);
            }
        }
    }

    // Utility method to lazily load tile images
    private BufferedImage loadTileImage(String path) {
        try {
            return ImageIO.read(getClass().getResource(path));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void drawPlayer(Graphics g) {
        if (player != null) {
            player.render(g);
        }
    }

    private void drawEntities(Graphics g) {
        if (npcs != null) {
            for (NPC npc : npcs) {
                npc.render(g);
            }
        }
    }

    // --- Page flipping ---
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

    // --- Book rendering ---
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
            for (Task task : tasks) {
                textY = drawWrappedText(g, "- " + task.taskDescription(), leftX, textY, columnWidth);
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
                rightY = drawWrappedText(g, "- (" + clue.modifier() + ") " + clue.description(), rightX, rightY, columnWidth);
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
