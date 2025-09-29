package ecs.engr302.team14.gothim.logic.dialogue;

import ecs.engr302.team14.gothim.entities.Player;

/**
 * Interface for lumping different kinds of dialogue prerequisites together.
 *
 * @author MR-Spagetty
 */
public interface DialoguePrerequisite {

    /**
     * Determines whether or not this prerequisite has been met.
     *
     * @param interacting the player interacting with the source of this dialogue stream.
     * @return whether this has been met
     */
    boolean met(Player interacting);

}
