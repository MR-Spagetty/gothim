package ecs.engr302.team14.gothim.logic.dialogue;

import ecs.engr302.team14.gothim.entities.Player;
import java.util.Optional;

/**
 * Abstract base for dialogue options.
 *
 * @author MR-Spagetty
 */
public class AbstractDialogueOption implements DialogueOption {
    protected final String text;
    protected final Dialogue result;

    public AbstractDialogueOption(String text, Dialogue result) {
        this.text = text;
        this.result = result;
    }

    @Override
    public String text() {
        return this.text;
    }

    @Override
    public Optional<Dialogue> result(Player interacting) {
        return Optional.ofNullable(this.result);
    }

}
