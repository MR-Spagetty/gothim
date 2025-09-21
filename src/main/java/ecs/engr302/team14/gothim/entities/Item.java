package ecs.engr302.team14.gothim.entities;

import ecs.engr302.team14.gothim.logic.AccessModifier;
import ecs.engr302.team14.gothim.util.Point;

/**
 * Basic item class.
 */
public class Item extends InteractableEntity {

    public Item(String name, Point position, AccessModifier accessModifier, String information) {
        super(name, position);
    }

    public AccessModifier getAccessModifier() {
        return this.getAccessModifier();
    }

    public String getInformation() {
        return this.getInformation();
    }


    // Complete interaction
    public void interact() {
        //TODO
    }

}
