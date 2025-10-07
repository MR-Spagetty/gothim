package ecs.engr302.team14.gothim.logic.dialogue;

import ecs.engr302.team14.gothim.entities.Player;
import ecs.engr302.team14.gothim.logic.dialogue.prerequisites.And;
import ecs.engr302.team14.gothim.logic.dialogue.prerequisites.DialoguePrerequisite;
import ecs.engr302.team14.gothim.persistancy.annotations.DeserializationMethod;
import ecs.engr302.team14.gothim.persistancy.annotations.SerializedField;
import java.util.Objects;
import java.util.Optional;

/**
 * Dialogue option that the player may only use if a prereq is met.
 *
 * @author MR-Spagetty
 */
public final class DialogueOptionPresentIf implements DialogueOption {

    @SerializedField
    private final DialogueOption inner;
    @SerializedField
    private final DialoguePrerequisite prereq;

    /**
     * Creates a new dialogue option that is available to the player when the
     * prereq is met.
     *
     * @param inner the underlying DialogueOption
     * @param prereq the prereq that must be met for this option to be available
     *      to the player
     */
    @DeserializationMethod(serialFieldNames = { "inner", "prereq" })
    public DialogueOptionPresentIf(DialogueOption inner, DialoguePrerequisite prereq) {
        Objects.requireNonNull(inner);
        while (inner instanceof DialogueOptionPresentIf presIf) {
            prereq = And.of(prereq, presIf.prereq);
            inner = presIf.inner;
        }
        this.prereq = Objects.requireNonNull(prereq);
        this.inner = inner;
    }

    @Override
    public boolean isAvailableTo(Player interacting) {
        return this.prereq.met(interacting);
    }

    @Override
    public Optional<Dialogue> result(Player interacting) {
        if (!isAvailableTo(interacting)) {
            throw new IllegalAccessError(("Player \"%s\" should only have access to "
                    + "this option if the following is met:\n%s").formatted(interacting.getName(),
                            prereq.toString()));
        }
        return this.inner.result(interacting);
    }

    @Override
    public String text() {
        return this.inner.text();
    }
}
