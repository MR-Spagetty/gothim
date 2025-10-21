package ecs.engr302.team14.gothim.logic.dialogue;

import ecs.engr302.team14.gothim.app.LevelManager;
import ecs.engr302.team14.gothim.logic.ClueHolder;
import ecs.engr302.team14.gothim.persistancy.annotations.DeserializationMethod;
import ecs.engr302.team14.gothim.persistancy.annotations.SerializationExtends;
import ecs.engr302.team14.gothim.persistancy.annotations.SerializedField;
import java.util.List;

/**
 * Special Dialogue statement that grants a specified clue to the player(s).
 *
 * @author MR-Spagetty
 */
@SerializationExtends(Dialogue.class)
public class DialogueClue extends Dialogue {

    @SerializedField
    private final String clueID;

    /**
     * Creates a new dialogue statement that grants the specified clue.
     *
     * @param clueID  the clue to grant
     * @param text    the text fo the dialogue statement
     * @param options the dialogue progression options
     */
    @DeserializationMethod(serialFieldNames = { "clueID", "text", "options" })
    public DialogueClue(String clueID, String text, List<DialogueOption> options) {
        super(text, options);
        this.clueID = clueID;
    }

    @Override
    public String say() {
        ClueHolder clues = LevelManager.getLevelData().clues();
        clues.findClue(this.clueID);
        return super.say();
    }

}
