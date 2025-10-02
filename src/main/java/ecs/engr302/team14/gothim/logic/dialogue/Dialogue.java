package ecs.engr302.team14.gothim.logic.dialogue;

import ecs.engr302.team14.gothim.entities.Player;
import ecs.engr302.team14.gothim.persistancy.annotations.DeserializationMethod;
import ecs.engr302.team14.gothim.persistancy.annotations.SerializedField;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


/**
 * Simple class for representing all dialogue statements.
 *
 * @author MR-Spagetty
 */
public class Dialogue {
    @SerializedField
    protected final String text;
    @SerializedField
    protected final ArrayList<DialogueOption> options;

    /**
     * Creates a new dialogue object.
     *
     * @param text the text for this dialogue statement
     * @param options the non goodbye options to progress through from this
     *      dialogue statement
     */
    @DeserializationMethod(serialFieldNames = { "text", "options" })
    public Dialogue(String text, List<DialogueOption> options) {
        this.text = Objects.requireNonNull(text);
        this.options = new ArrayList<>(Optional.ofNullable(options).orElse(List.of()));
        this.options.removeIf(o -> o == DialogueOption.GoodBye);
        this.options.addFirst(DialogueOption.GoodBye);
    }

    /**
     * Call this statement to be spoken.
     *
     * @return the display text
     */
    public String say() {
        return this.text;
    }

    /**
     * get the available options for this dialogue statement.
     *
     * @param interacting the player interacting with the source of this dialogue
     * @return the available options
     */
    public List<DialogueOption> getOptions(Player interacting) {
        return Collections.unmodifiableList(options);
    }

    /**
     * Progresses this stream of dialogue using the specified option.
     *
     * @param interacting the player interacting with the source of this dialogue
     * @param option the option to progress down.
     * @return the result of the dialogue progression (Empty optional if dialogue ends)
     */
    public Optional<Dialogue> progress(Player interacting, int option) {
        return options.get(option).result(interacting);
    }

}
