package ecs.engr302.team14.gothim.logic.dialogue.prerequisites;

import ecs.engr302.team14.gothim.entities.Player;
import ecs.engr302.team14.gothim.logic.ClueHolder;
import ecs.engr302.team14.gothim.persistancy.annotations.DeserializationMethod;
import ecs.engr302.team14.gothim.persistancy.annotations.SerializedField;
import java.util.concurrent.ExecutionException;

/**
 * Dialogue prerequisite that a certain clue must be found.
 *
 * @author MR-Spagetty
 */
public class ClueFound implements DialoguePrerequisite {


    /**
     * Gets a prerequisite that checks that the given clue is found.
     *
     * @param clueID the id of the clue that must be found for this prerequisite to be met
     * @return the resulting prerequisite
     */
    @DeserializationMethod(serialFieldNames = { "clueID" })
    public static DialoguePrerequisite of(String clueID) {
        DialoguePrerequisite key = new ClueFound(clueID);
        try {
            return cache.get(key, () -> key);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    @SerializedField
    String clueID;

    private ClueFound(String clueID) {
        this.clueID = clueID;
    }

    public String clueID() {
        return this.clueID;
    }

    @Override
    public boolean met(Player interacting) {
        ClueHolder clues = null; // TODO get clue holder for current level
        return clues.isFound(clueID);
    }

}
