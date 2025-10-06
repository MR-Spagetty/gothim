package ecs.engr302.team14.gothim.logic.dialogue.prerequisites;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import ecs.engr302.team14.gothim.entities.Player;
import java.time.Duration;


/**
 * Interface for lumping different kinds of dialogue prerequisites together.
 *
 * @author MR-Spagetty
 */
public interface DialoguePrerequisite {
    static Cache<DialoguePrerequisite, DialoguePrerequisite> cache = CacheBuilder.newBuilder()
            .expireAfterAccess(Duration.ofMinutes(5)).build();

    static final DialoguePrerequisite True = _ -> true;
    static final DialoguePrerequisite False = _ -> false;

    /**
     * Determines whether or not this prerequisite has been met.
     *
     * @param interacting the player interacting with the source of this
     *      dialogue stream.
     * @return whether this has been met
     */
    boolean met(Player interacting);

}
