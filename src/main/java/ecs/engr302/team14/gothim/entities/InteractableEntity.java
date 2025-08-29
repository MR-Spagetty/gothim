package ecs.engr302.team14.gothim.entities;

import ecs.engr302.team14.gothim.util.Point;

/**
 * Abstract class that adds the additional features an intractable entity
 * should have over a normal entity.
 */
public abstract class InteractableEntity extends Entity {

    public InteractableEntity(String name, Point position) {
        super(name, position);
    }

    // Needs to filled out
    public abstract void interact();
}