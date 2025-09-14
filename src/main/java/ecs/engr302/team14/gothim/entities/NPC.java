package ecs.engr302.team14.gothim.entities;
import ecs.engr302.team14.gothim.util.Point;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class NPC extends InteractableEntity{

    private String dialogue;
    private BufferedImage sprite;
    private int size = 40;

    public NPC(String name, String dialogue, Point position){
        super(name, position);
        this.dialogue = dialogue;

        try {
            sprite = ImageIO.read(getClass().getResource("/assets/npc.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Complete interaction
    public void interact() {
        System.out.println(name+": " + dialogue);
    }

    public boolean isNear(Player player) {
        int dx = (int) (position.x() - player.getPosition().x());
        int dy = (int) (position.y() - player.getPosition().y());
        int distanceSquared = dx * dx + dy * dy;
        int interactionRange = 40; // pixels
        return distanceSquared < interactionRange * interactionRange;
    }

    public void render(Graphics g) {
        if (sprite != null) {
            g.drawImage(sprite, (int) position.x(), (int) position.y(), 50, 50, null);
        } else {
            // fallback: draw a placeholder rectangle
            g.setColor(Color.WHITE);
            g.fillRect((int) position.x(), (int) position.y(), size, size);
        }
    }
}