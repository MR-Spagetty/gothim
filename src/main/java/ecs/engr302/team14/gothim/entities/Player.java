package ecs.engr302.team14.gothim.entities;

import ecs.engr302.team14.gothim.app.LevelManager;
import ecs.engr302.team14.gothim.logic.DisguiseableAs;
import ecs.engr302.team14.gothim.logic.Family;
import ecs.engr302.team14.gothim.persistancy.annotations.SerializedField;
import ecs.engr302.team14.gothim.util.Point;
import java.util.List;
import java.util.Optional;

/**
 * Basic player class.
 */
public class Player extends Entity {
    public static final int INTERACTION_DIST = 1;

    @SerializedField
    private final Family family;
    @SerializedField
    Disguise disguise = null;

    public Player(String name, Point position, Family fam) {
        super(name, position);
        this.family = fam;
    }

    /**
     * Checks whether or not the player would currently be seen as the given
     * entity.
     *
     * @param identity the identity to check against
     * @return whether the player is seen as that identity
     */
    public boolean isSeenAs(DisguiseableAs identity) {
        return identity == null || identity == Family.None || family == identity
                || Optional.ofNullable(this.disguise).map(Disguise::disguise)
                        .map(d -> d == identity).orElse(false);
    }


    /**
     * Gets the visual identity fo this player.
     *
     * @return the visual identity
     */
    public DisguiseableAs getIdentity() {
        return Optional.ofNullable(disguise).map(Disguise::disguise)
                .orElse((DisguiseableAs) family);
    }

    /**
     * Attempt interaction with a single entity in range.
     *
     * <p>If more tan one entity is within interaction range do not interact
     * with any of them
     */
    public void interact() {
        List<InteractableEntity> ies = LevelManager.getLevelData().entities().stream().filter(
                e -> e instanceof InteractableEntity ie && ie.isNear(this, INTERACTION_DIST))
                .map(e -> (InteractableEntity) e).toList();
        if (ies.size() != 1) {
            return;
        }
        ies.getFirst().interact(this);
    }

}
