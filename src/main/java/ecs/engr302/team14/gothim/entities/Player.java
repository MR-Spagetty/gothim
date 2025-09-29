package ecs.engr302.team14.gothim.entities;

import ecs.engr302.team14.gothim.logic.DisguiseableAs;
import ecs.engr302.team14.gothim.logic.Family;
import ecs.engr302.team14.gothim.persistancy.annotations.SerializedField;
import ecs.engr302.team14.gothim.util.Point;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.Optional;
import java.util.Set;

/**
 * Basic player class.
 */
public class Player extends Entity {
    private BufferedImage sprite;

    @SerializedField
    private final Family family;
    @SerializedField
    private Disguise<?> disguise = null;

    public Player(String name, Point position, Family fam) {
        super(name, position);
        this.family = fam;
    }

    boolean isSeenAs(DisguiseableAs identity) {
        return identity == null || identity == Family.None || family == identity
                || Optional.ofNullable(this.disguise).map(Disguise::disguise)
                        .map(d -> d == identity).orElse(false);
    }

    /**
     * Update the player.
     *
     * @param pressedKeys the keys that hev been pressed
     * @param screenWidth the width of the screen
     * @param screenHeight the height of the screen
     */
    public void update(Set<Integer> pressedKeys, int screenWidth, int screenHeight) {
        double newX = position.x();
        double newY = position.y();

        if (pressedKeys.contains(KeyEvent.VK_W)) {
            newY--;
        }
        if (pressedKeys.contains(KeyEvent.VK_S)) {
            newY++;
        }
        if (pressedKeys.contains(KeyEvent.VK_A)) {
            newX--;
        }
        if (pressedKeys.contains(KeyEvent.VK_D)) {
            newX++;
        }
        position = new Point(newX, newY);
    }

    /**
     * Render this player.
     *
     * @param g the graphics to render it on.
     */
    public void render(Graphics g) {
        if (sprite != null) {
            g.drawImage(sprite, (int) position.x(), (int) position.y(), 60, 96, null);
        } else {
            // fallback: draw a placeholder rectangle
            g.setColor(Color.WHITE);
            g.fillRect((int) position.x(), (int) position.y(), 40, 40);
        }
    }
}
