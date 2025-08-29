package ecs.engr302.team14.gothim.entities;

import ecs.engr302.team14.gothim.util.Point;

/**
 * Abstract class containing the basic features of all entities.
 */
public abstract class Entity {
    protected String name;
    protected Point position;

    public Entity(String name, Point pos) {
        this.name = name;
        this.position = pos;
    }

    public String getName() {
        return name;
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

}
