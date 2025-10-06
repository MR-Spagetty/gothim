package ecs.engr302.team14.gothim.entities;

import ecs.engr302.team14.gothim.logic.dialogue.Dialogue;
import ecs.engr302.team14.gothim.persistancy.annotations.DeserializationMethod;
import ecs.engr302.team14.gothim.persistancy.annotations.SerializationExtends;
import ecs.engr302.team14.gothim.persistancy.annotations.SerializedField;
import ecs.engr302.team14.gothim.util.Point;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 * Basic NPC class.
 */
@SerializationExtends(Entity.class)
public class NPC extends InteractableEntity {

    @SerializedField
    private Dialogue dialogue;
    private BufferedImage sprite;
    private int size = 40;

    @DeserializationMethod(serialFieldNames = { "name", "dialogue", "pos" })
    public NPC(String name, Dialogue dialogue, Point position) {
        super(name, position);
        this.dialogue = dialogue;
    }

    //Complete interaction
    public void interact(Player p) {
        System.out.println(name + ": " + dialogue);
    }

    /**
     * Renders the NPC on the given graphics.
     *
     * @param g the graphics to render the NPC on
     */
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