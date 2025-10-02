package ecs.engr302.team14.gothim.logic.dialogue;

import ecs.engr302.team14.gothim.entities.Player;
import ecs.engr302.team14.gothim.persistancy.annotations.SerializedConstant;
import java.util.Optional;

/**
 * Interface to lump all types of dialogue responses together.
 *
 * @author MR-Spagetty
 */
public interface DialogueOption {
    /**
     * the universal GoodBye option.
     */
    @SerializedConstant
    public static final DialogueOption GoodBye = new DialogueOption() {
        @Override
        public String text() {
            return "GoodBye";
        }

        @Override
        public Optional<Dialogue> result(Player p) {
            return Optional.empty();
        }
    };

    /**
     * the text of the option (what the player says).
     */
    public String text();

    /**
     * whether or not this dialogue option should be available to the given player.
     *
     * @param interacting the player interacting with the source of this dialogue stream
     * @return whether this option should be available
     */
    public default boolean isAvailableTo(Player interacting) {
        return true;
    }

    /**
     * the result of progressing down this option.
     *
     * @param interacting the player interacting with the source of the stream of dialogue
     * @return Optional of next dialogue statement (Empty if the dialogue ends)
     */
    public Optional<Dialogue> result(Player interacting);

}
