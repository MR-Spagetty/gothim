package ecs.engr302.team14.gothim.logic.dialogue;

import ecs.engr302.team14.gothim.entities.Player;
import ecs.engr302.team14.gothim.persistancy.annotations.DeserializationMethod;
import ecs.engr302.team14.gothim.persistancy.annotations.SerializedField;
import java.util.Optional;

/**
 * Abstract base for dialogue options.
 *
 * @author MR-Spagetty
 */
public class GenericDialogueOption implements DialogueOption {
    @SerializedField
    protected final String text;
    @SerializedField
    protected final Dialogue result;

    @DeserializationMethod(serialFieldNames = { "text", "result" })
    public GenericDialogueOption(String text, Dialogue result) {
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
