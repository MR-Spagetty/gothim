package ecs.engr302.team14.gothim.entities;

import ecs.engr302.team14.gothim.util.Point;

/**
 * Abstract class that covers all intractable entities.
 */
public abstract class InteractableEntity extends Entity {

    public InteractableEntity(String name, Point position) {
        super(name, position);
    }

    /**
     * Checks if the player is within 1 tile of this NPC.
     *
     * @param player the player to check against
     * @return if the player is within interaction range
     */
    public boolean isNear(Player player, int nearDist) {
        int dx = (int) (position.x() - player.getPosition().x());
        int dy = (int) (position.y() - player.getPosition().y());
        return Math.abs(dx) <= nearDist && Math.abs(dy) <= nearDist;
    }

    public abstract void interact(Player p);
}