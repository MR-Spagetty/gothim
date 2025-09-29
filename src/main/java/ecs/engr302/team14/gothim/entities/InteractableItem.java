package ecs.engr302.team14.gothim.entities;

import ecs.engr302.team14.gothim.logic.AccessModifier;
import ecs.engr302.team14.gothim.logic.Clue;
import ecs.engr302.team14.gothim.util.Day;
import ecs.engr302.team14.gothim.util.Point;

/**
 * Represents an item in the game world that can be interacted with.
 *
 * Items could be clues, quest objects, or usable things.
 * On interaction, items can automatically add discovered information to the Taskbook.
 */
public class InteractableItem extends InteractableEntity {

    private boolean collected = false;
    private String description;
    private AccessModifier modifier;
    private Day discoveredDay;
    private Taskbook taskbook;

    /**
     * Creates a new interactable item.
     *
     * @param name        - display name of the item
     * @param position    - grid/world position of the item
     * @param description - short description of the item
     * @param modifier    - clue access modifier
     * @param discoveredDay - which in-game day the item is tied to
     * @param taskbook    - reference to the player's taskbook
     */
    public InteractableItem(
            String name,
            Point position,
            String description,
            AccessModifier modifier,
            Day discoveredDay,
            Taskbook taskbook
    ) {
        super(name, position);
        this.description = description;
        this.modifier = modifier;
        this.discoveredDay = discoveredDay;
        this.taskbook = taskbook;
    }

    /**
     * Called when the player interacts with the item.
     * Marks as collected and adds a Clue to the Taskbook.
     */
    public void interact() {
        if (!collected) {
            collected = true;

            // Create a unique ID, e.g. "Day1_ItemName"
            String clueId = "Day" + discoveredDay.ordinal() + "_" + getName();

            Clue clue = new Clue(
                    modifier,
                    clueId,
                    description
            );

            taskbook.addDiscoveredInformation(clue);

            System.out.println("You discovered: " + getName() + " -> added clue to taskbook.");
        } else {
            System.out.println(getName() + " has already been collected.");
        }
    }

    public boolean isCollected() {
        return collected;
    }

    public String getDescription() {
        return description;
    }
}
