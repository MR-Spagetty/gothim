package ecs.engr302.team14.gothim.logic.dialogue.prerequisites;

import ecs.engr302.team14.gothim.entities.Player;
import ecs.engr302.team14.gothim.persistancy.annotations.DeserializationMethod;
import ecs.engr302.team14.gothim.persistancy.annotations.SerializationExtends;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;

/**
 * Dialogue prerequisite with two sub prerequisites atleast one of must be met.
 *
 * @author MR-Spagetty
 */
@SerializationExtends(BinaryOperationPrerequisite.class)
public final class Or extends BinaryOperationPrerequisite {
    /**
     * Gets a minimal Dialogue prerequisite equivalent to the OR of the two provided Prerequisites.
     *
     * @param a prereq a
     * @param b prereq b
     * @return the equivalent of a | b
     */
    @DeserializationMethod(serialFieldNames = {"a", "b"})
    public static DialoguePrerequisite of(DialoguePrerequisite a, DialoguePrerequisite b) {
        if (a == b) {
            return a;
        } else if (List.of(a, b).contains(True)) {
            return True;
        } else if (a == False) {
            return b;
        } else if (b == False) {
            return a;
        }
        Or key = new Or(a, b);
        try {
            return cache.get(key, () -> key);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }

    }

    private Or(DialoguePrerequisite a, DialoguePrerequisite b) {
        super(a, b);
    }

    @Override
    public boolean met(Player interacting) {
        return opA.met(interacting) || opB.met(interacting);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Or other && Set.of(opA, opB).equals(Set.of(other.opA, other.opB));
    }
}
