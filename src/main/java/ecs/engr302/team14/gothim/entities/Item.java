package ecs.engr302.team14.gothim.entities;

import ecs.engr302.team14.gothim.persistancy.annotations.SerializationExtends;
import ecs.engr302.team14.gothim.persistancy.annotations.SerializedField;
import ecs.engr302.team14.gothim.util.Point;

/**
 * Represents an item in the game world.
 *
 * <p>Items could be clues, quest objects, or usable things.
 * On interaction, items can automatically add discovered information to the Taskbook.
 */
@SerializationExtends(Entity.class)
public class Item extends InteractableEntity {

    @SerializedField
    private boolean collected;
    @SerializedField
    private String description;

    /**
     * Creates a new item.
     *
     * @param name        - display name of the item
     * @param position    - grid/world position of the item
     * @param description - short description of the item
     * @param collected whether the item has been collected or not
     */
    public Item(
            String name,
            Point position,
            String description, boolean collected
    ) {
        super(name, position);
        this.description = description;
        this.collected = collected;
    }

    /**
     * Called when the player interacts with the item.
     * Marks as collected and adds a Clue to the Taskbook.
     */
    public void interact(Player p) {
        if (!collected) {
            collected = true;
        }
    }

    public boolean isCollected() {
        return collected;
    }

    public String getDescription() {
        return description;
    }
}
