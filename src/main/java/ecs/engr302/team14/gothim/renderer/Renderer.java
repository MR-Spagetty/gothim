package ecs.engr302.team14.gothim.renderer;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.UncheckedExecutionException;
import ecs.engr302.team14.gothim.app.LevelManager;
import ecs.engr302.team14.gothim.app.Main;
import ecs.engr302.team14.gothim.entities.Entity;
import ecs.engr302.team14.gothim.entities.NPC;
import ecs.engr302.team14.gothim.entities.Player;
import ecs.engr302.team14.gothim.entities.Taskbook;
import ecs.engr302.team14.gothim.logic.LevelHolder;
import ecs.engr302.team14.gothim.map.Board;
import ecs.engr302.team14.gothim.persistancy.JSONArray;
import ecs.engr302.team14.gothim.persistancy.JSONObject;
import ecs.engr302.team14.gothim.persistancy.Serialization;
import ecs.engr302.team14.gothim.renderer.behaviours.Behaviour;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 * Central Renderer for rendering the game world, entities, and taskbook.
 */
public class Renderer extends JPanel {

    private static final int TILE_SIZE = 64; // pixels per tile
    private static final int VIEW_DIST = 13;

    static final Duration CAHCE_EXPIARY = Duration.ofMinutes(5);

    static final LoadingCache<String, BufferedImage> sprites = CacheBuilder.newBuilder()
            .expireAfterAccess(CAHCE_EXPIARY).softValues().build(new CacheLoader<>() {
                @SuppressWarnings("null")
                public BufferedImage load(String key) throws Exception {
                    System.out.println("Loading + assets/%s.png".formatted(key));
                    return ImageIO.read(Renderer.class.getClassLoader()
                            .getResource("assets/%s.png".formatted(key)));
                }
            });

    static final LoadingCache<String, List<Behaviour>> behaviours = CacheBuilder.newBuilder()
            .expireAfterAccess(CAHCE_EXPIARY).softValues().build(new CacheLoader<>() {
                @SuppressWarnings({ "unchecked", "null" })
                public List<Behaviour> load(String key) throws Exception {
                    String cont = new String(Renderer.class.getClassLoader()
                            .getResourceAsStream("assets/behaviours/%s.json".formatted(key))
                            .readAllBytes());
                    if (JSONArray.isNext(cont)) {
                        return (ArrayList<Behaviour>) Serialization
                                .fromJSON(JSONArray.parse(cont).getKey());
                    }
                    return List.of(
                            (Behaviour) Serialization.fromJSON(JSONObject.parse(cont).getKey()));
                }
            });

    static Set<String> specialStyles;

    static {
        try {
            specialStyles = new String(Renderer.class.getClassLoader()
                    .getResourceAsStream("assets/hasBehaviours").readAllBytes()).lines()
                            .collect(Collectors.toSet());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Renderer instance;

    // Game state
    private Day currentDay = Day.ONE;

    // Taskbook
    private boolean showTaskbook = false;
    private final Taskbook taskbook = new Taskbook();
    private Rectangle taskbookBounds;
    private Rectangle nextButtonBounds;
    private Rectangle prevButtonBounds;

    private int offsetX = 0; // offsets to render tiles at 0,0
    private int offsetY = 0;

    private Renderer() {
        // Load book UI asset
        try {
            BufferedImage openbook = sprites.get("openbook");
            taskbookBounds = new Rectangle(100, 100, openbook.getWidth(), openbook.getHeight());
        } catch (UncheckedExecutionException | ExecutionException e) {
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

    public void toggleTaskbook() {
        showTaskbook = !showTaskbook;
        repaint();
    }

    // --- Core painting ---
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Point offset = LevelManager.getLevelData().getPlayer(Main.playerID).getPosition().mul(-1);
        offsetX = (int) offset.x();
        offsetY = (int) offset.y();
        drawTiles(g);
        for (Player p : LevelManager.getLevelData().players()) {
            drawPlayer(g, p);
        }
        drawTaskbook(g);
    }

    // --- Draw tiles ---
    private void drawTiles(Graphics g) {

        Point cameraPos = LevelManager.getLevelData().getPlayer(Main.playerID).getPosition();
        // Second pass: draw non-grass tiles on top
        Board board = LevelManager.getLevelData().map();
        for (PrimitiveTile tile : board.getTiles(cameraPos, VIEW_DIST)) {
            String style = tile.style.toLowerCase();
            if (specialStyles.contains(style)) {
                for (Behaviour behaviour : behaviours.getUnchecked(style)) {
                    if (behaviour.cond().applies(tile, board.getTiles(
                            tile.pos().add(new Point(-1, 1)), tile.pos().add(new Point(1, -1))))) {
                        drawTile(behaviour.assetName(), tile.pos(), g);
                    }
                }
            } else {
                drawTile(style, tile.pos(), g);
            }
            tile.getOccupant().ifPresent((e) -> drawEntity(e, g));
        }
    }

    private void drawTile(String type, Point pos, Graphics g) {
        int tileX = (int) (pos.x() + offsetX) * TILE_SIZE + getWidth() / 2;
        int tileY = (int) (pos.y() + offsetY) * TILE_SIZE + getHeight() / 2;
        try {
            BufferedImage tileImg = sprites.get(type);
            g.drawImage(tileImg, tileX, tileY, TILE_SIZE, TILE_SIZE, this);
        } catch (UncheckedExecutionException | ExecutionException e) {
            System.err.println("Could not find texture for tile: " + type);
            drawUnknown(pos, g);
        }
    }

    private void drawPlayer(Graphics g, Player p) {

        Point pos = p.getPosition();

        // Center camera on player
        int px = (int) (pos.x() + offsetX) * TILE_SIZE + getWidth() / 2;
        int py = (int) (pos.y() + offsetY) * TILE_SIZE + getHeight() / 2;
        BufferedImage playerSprite = null;
        try {
            playerSprite = sprites.get("player_" + p.getIdentity().name());
        } catch (UncheckedExecutionException | ExecutionException e) {
            System.err.println("Could not find Texture for player_" + p.getIdentity().name());
            drawUnknown(pos, g);
            return;
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

    private void drawEntity(Entity e, Graphics g) {
        switch (e) {
            case Player p -> drawPlayer(g, p);
            case NPC npc -> drawNPC(npc, g);
            default -> drawUnknown(e.getPosition(), g);
        }
    }

    private void drawNPC(NPC npc, Graphics g) {
        BufferedImage img = null;
        try {
            switch (npc.getName()) {
                case "newspaper" -> img = sprites.get("newspaper");
                case "Notice Board" -> img = sprites.get("noticeboard");

                default -> img = sprites.get("NPC");
            }
        } catch (UncheckedExecutionException | ExecutionException e) {
            System.err.println("Could not find Texture for npc with name " + npc.getName());
            drawUnknown(npc.getPosition(), g);
            return;
        }
        int x = (int) (npc.getPosition().x() + offsetX) * TILE_SIZE + getWidth() / 2;
        int y = (int) (npc.getPosition().y() + offsetY) * TILE_SIZE + getHeight() / 2;
        int spriteWidth = img.getWidth();
        int spriteHeight = img.getHeight();
        float ratio = ((float) spriteWidth) / spriteHeight;
        int trueWidth = (int) (ratio * TILE_SIZE);
        g.drawImage(img, x + (TILE_SIZE - trueWidth) / 2, y, trueWidth, TILE_SIZE, this);
    }

    private void drawUnknown(Point pos, Graphics g) {
        try {
            BufferedImage img = sprites.get("unknown");
            int x = (int) (pos.x() + offsetX) * TILE_SIZE + getWidth() / 2;
            int y = (int) (pos.y() + offsetY) * TILE_SIZE + getHeight() / 2;
            g.drawImage(img, x, y, TILE_SIZE, TILE_SIZE, this);
        } catch (Exception e) {
            System.err.println("Could not find unknown texture sprite");
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
        BufferedImage openbook = null;
        try {
            openbook = sprites.get("openbook");
        } catch (UncheckedExecutionException | ExecutionException e) {
            try {
                openbook = sprites.get("unknown");
            } catch (UncheckedExecutionException | ExecutionException e1) {
                e1.printStackTrace();
            }
        }
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
