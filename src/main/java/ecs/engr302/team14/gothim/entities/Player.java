package ecs.engr302.team14.gothim.entities;

import ecs.engr302.team14.gothim.logic.DisguiseableAs;
import ecs.engr302.team14.gothim.logic.Family;
import ecs.engr302.team14.gothim.persistancy.annotations.SerializedField;
import ecs.engr302.team14.gothim.util.Point;
import java.util.Optional;

/**
 * Basic player class.
 */
public class Player extends Entity {
    @SerializedField
    private final Family family;
    @SerializedField
    Disguise<?> disguise = null;

    public Player(String name, Point position, Family fam) {
        super(name, position);
        this.family = fam;
    }

    /**
     * Checks whether or not the player would currently be seen as the given entity.
     *
     * @param identity the identity to check against
     * @return whether the player is seen as that identity
     */
    public boolean isSeenAs(DisguiseableAs identity) {
        return identity == null || identity == Family.None || family == identity
                || Optional.ofNullable(this.disguise).map(Disguise::disguise)
                        .map(d -> d == identity).orElse(false);
    }

}
