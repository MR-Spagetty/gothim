package ecs.engr302.team14.gothim.entities;

import ecs.engr302.team14.gothim.util.Point;

/**
 * Player class, contains all the implementation needed for a functional player.
 */
public class Player extends Entity {
    private int accessLevel = 0; // default to "public" (change to an ENUM)
    private Area currentArea;

    public Player(String name, Point position) {
        super(name, position);
    }

    /**
     * Moves this player into a new area.
     *
     * @param newArea the new area to move the player into
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

    public String getName() {
        return this.name;
    }

}
