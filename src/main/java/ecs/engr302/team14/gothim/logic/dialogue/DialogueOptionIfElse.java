package ecs.engr302.team14.gothim.logic.dialogue;

import ecs.engr302.team14.gothim.entities.Player;
import ecs.engr302.team14.gothim.logic.dialogue.prerequisites.DialoguePrerequisite;
import ecs.engr302.team14.gothim.persistancy.annotations.DeserializationMethod;
import ecs.engr302.team14.gothim.persistancy.annotations.SerializedField;
import java.util.Objects;
import java.util.Optional;

/**
 * Branching Dialogue option that gives a different result depending on whether
 * or not the condition is met.
 *
 * @author MR-Spagetty
 */
public class DialogueOptionIfElse implements DialogueOption {
    @SerializedField
    private final String text;
    @SerializedField
    private final DialoguePrerequisite condition;
    @SerializedField(deserialParamName = "if")
    private final Dialogue routeIf;
    @SerializedField(deserialParamName = "else")
    private final Dialogue routeElse;

    /**
     * Creates a new IfElse Dialogue option with te specified text, condition,
     * and routes.
     *
     * @param text the text of the option (what the player says)
     * @param cond the condition for the if else
     * @param routeIf the route to take if the condition is met
     * @param routeElse the rout to take if the condition is not met
     */
    @DeserializationMethod(serialFieldNames = { "condition", "if", "else" })
    public DialogueOptionIfElse(String text, DialoguePrerequisite cond, Dialogue routeIf,
            Dialogue routeElse) {
        this.text = Objects.requireNonNull(text);
        this.condition = Objects.requireNonNull(cond);
        this.routeIf = routeIf;
        this.routeElse = routeElse;
    }

    @Override
    public String text() {
        return this.text;
    }

    @Override
    public Optional<Dialogue> result(Player interacting) {
        if (this.condition.met(interacting)) {
            return Optional.ofNullable(this.routeIf);
        }
        return Optional.ofNullable(this.routeElse);
    }

}
