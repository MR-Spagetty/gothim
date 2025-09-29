package ecs.engr302.team14.gothim.entities;

import ecs.engr302.team14.gothim.util.Point;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.Set;

/**
 * Basic player class.
 */
public class Player extends Entity {
    private int accessLevel = 0; // default to "public" (change to an ENUM)
    private Area currentArea;
    private int speed = 4;
    private int size = 32; // for drawing
    private BufferedImage sprite;

    public Player(String name, Point position) {
        super(name, position);
    }

    /**
     * moves this palyer to a new area.
     *
     * @param newArea the new area
     */
    public void moveTo(Area newArea) {
        if (this.canEnter(newArea)) {
            this.currentArea = newArea;
            System.out.println(name + " moved to " + newArea.getName());
        } else {
            System.out.println("Access denied! Need higher clearance");
        }
    }

    protected boolean canEnter(Area area) {
        return this.accessLevel >= area.getRequiredAccess();
    }

    public Area getCurrentArea() {
        return currentArea;
    }

    public void setCurrentArea(Area area) {
        this.currentArea = area;
    }

    public int getAccessLevel() {
        return this.accessLevel;
    }

    public void setAccessLevel(int level) {
        this.accessLevel = level;
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
            newY -= speed;
        }
        if (pressedKeys.contains(KeyEvent.VK_S)) {
            newY += speed;
        }
        if (pressedKeys.contains(KeyEvent.VK_A)) {
            newX -= speed;
        }
        if (pressedKeys.contains(KeyEvent.VK_D)) {
            newX += speed;
        }

        // Clamp inside bounds
        newX = Math.max(0, Math.min(newX, screenWidth - size));
        newY = Math.max(0, Math.min(newY, screenHeight - size));

        // Create a new point instance with updated coords (not sure if this is the best
        // approach)
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
            g.fillRect((int) position.x(), (int) position.y(), size, size);
        }
    }
}
