package ecs.engr302.team14.gothim.logic.dialogue.prerequisites;

import ecs.engr302.team14.gothim.entities.Player;
import ecs.engr302.team14.gothim.persistancy.annotations.SerializationExtends;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Dialogue prerequisite with two sub prerequisites that must be met.
 *
 * @author MR-Spagetty
 */
@SerializationExtends(BinaryOperationPrerequisite.class)
public class And extends BinaryOperationPrerequisite {
    /**
     * Gets a minimal Dialogue prerequisite equivalent to the AND of the two provided Prerequisites.
     *
     * @param a prereq a
     * @param b prereq b
     * @return the equivalent of a & b
     */
    public static DialoguePrerequisite of(DialoguePrerequisite a, DialoguePrerequisite b) {
        if (a == b) {
            return a;
        } else if (List.of(a, b).contains(DialoguePrerequisite.False)) {
            return DialoguePrerequisite.False;
        } else if (a == DialoguePrerequisite.True) {
            return b;
        } else if (b == DialoguePrerequisite.True) {
            return a;
        }
        And key = new And(a, b);
        try {
            return cache.get(key, () -> key);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }

    }

    private And(DialoguePrerequisite a, DialoguePrerequisite b) {
        super(a, b);
    }

    @Override
    public boolean met(Player interacting) {
        return opA.met(interacting) && opB.met(interacting);
    }
}
