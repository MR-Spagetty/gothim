package ecs.engr302.team14.gothim.entities;

import ecs.engr302.team14.gothim.logic.AccessModifier;
import ecs.engr302.team14.gothim.logic.DisguiseableAs;
import ecs.engr302.team14.gothim.persistancy.annotations.SerializedField;
import ecs.engr302.team14.gothim.util.Day;
import ecs.engr302.team14.gothim.util.Point;

/**
 * Disguise item allows the player to disguise themselves for example as a
 * member of one of the 4 families.
 *
 * @author MR-Spagetty
 */
public class Disguise<T extends DisguiseableAs> extends InteractableItem {

    @SerializedField
    private final T disguisesAs;

    public Disguise(String name, Point position, String description, AccessModifier modifier,
            Day discoveredDay, Taskbook taskbook, T disguisesAs) {
        super(name, position, description, modifier, discoveredDay, taskbook);
        this.disguisesAs = disguisesAs;
    }

    public T disguise() {
        return this.disguisesAs;
    }
}
