package ecs.engr302.team14.gothim.logic.dialogue.prerequisites;

import ecs.engr302.team14.gothim.entities.Player;
import ecs.engr302.team14.gothim.persistancy.annotations.DeserializationMethod;
import ecs.engr302.team14.gothim.persistancy.annotations.SerializedField;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

/**
 * Dialogue prerequisite  that requires the sub requisite to be not met.
 *
 * @author MR-Spagetty
 */
public class Not implements DialoguePrerequisite {

    /**
     * Gets a minimal Dialogue prerequisite equivilent to the NOT of the provided Prerequisite.
     *
     * @param value the prereq to NOT
     * @return the equivalent of ! value
     */
    @DeserializationMethod(serialFieldNames = { "value" })
    public static DialoguePrerequisite of(DialoguePrerequisite value) {
        if (value == True) {
            return False;
        } else if (value == False) {
            return True;
        }

        Not key = new Not(value);
        try {
            return cache.get(key, () -> key);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @SerializedField
    private final DialoguePrerequisite value;

    private Not(DialoguePrerequisite value) {
        this.value = Objects.requireNonNull(value);
    }

    @Override
    public boolean met(Player interacting) {
        return !this.value.met(interacting);
    }

}
