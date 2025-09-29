package ecs.engr302.team14.gothim.logic.dialogue;

import ecs.engr302.team14.gothim.entities.Player;
import java.util.Objects;
import java.util.Optional;

/**
 * Dialogue option that the player may only use if a prereq is met.
 *
 * @author MR-Spagetty
 */
public class DialogueOptionPresentIf extends AbstractDialogueOption {
    protected final DialoguePrerequisite prereq;

    /**
     * Creates a new dialogue option that is available to the player when the
     * prereq is met.
     *
     * @param text the text for the option (what the player will say)
     * @param result the result of progressing the dialogue down this option
     * @param prereq the prereq that must be met for this option to be available
     *      to the player
     */
    public DialogueOptionPresentIf(String text, Dialogue result, DialoguePrerequisite prereq) {
        super(text, result);
        this.prereq = Objects.requireNonNull(prereq);
    }

    @Override
    public boolean isAvailableTo(Player interacting) {
        return prereq.met(interacting);
    }

    @Override
    public Optional<Dialogue> result(Player interacting) {
        if (!isAvailableTo(interacting)) {
            throw new IllegalAccessError(("Player \"%s\" should only have access to "
                    + "this option if the following is met:\n%s").formatted(interacting.getName(),
                            prereq.toString()));
        }
        return super.result(interacting);
    }
}
