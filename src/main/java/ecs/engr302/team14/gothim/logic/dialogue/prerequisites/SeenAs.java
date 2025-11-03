package ecs.engr302.team14.gothim.logic.dialogue.prerequisites;

import ecs.engr302.team14.gothim.entities.Player;
import ecs.engr302.team14.gothim.logic.DisguiseableAs;
import ecs.engr302.team14.gothim.persistancy.annotations.DeserializationMethod;
import ecs.engr302.team14.gothim.persistancy.annotations.SerializedField;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

/**
 * Dialogue prereq that requires the player to seem to have a specified identity.
 *
 * @author MR-Spagetty
 */
public final class SeenAs implements DialoguePrerequisite {

    /**
     * gets a Dialogue prerequisite that checks if the player is seen as the given identity.
     *
     * @param identity the identity the player needs to be seen as
     * @return the dialogue prereq
     */
    @DeserializationMethod(serialFieldNames = { "identity" })
    public static DialoguePrerequisite of(DisguiseableAs identity) {
        SeenAs key = new SeenAs(identity);
        try {
            return cache.get(key, () -> key);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @SerializedField
    private final DisguiseableAs identity;

    private SeenAs(DisguiseableAs identity) {
        this.identity = identity;
    }


    @Override
    public boolean met(Player interacting) {
        return interacting.isSeenAs(this.identity);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof SeenAs sa && Objects.equals(sa.identity, this.identity);
    }
}
