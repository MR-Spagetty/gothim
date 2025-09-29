package ecs.engr302.team14.gothim.logic.dialogue.prerequisites;

import ecs.engr302.team14.gothim.persistancy.annotations.SerializedField;

/**
 * Abstract class to minimise repeated code for binary operation  dialogue prerequisites
 * such as "Or" and "And".
 *
 * @author MR-Spagetty
 */
public abstract class BinaryOperationPrerequisite implements DialoguePrerequisite {
    @SerializedField(deserialParamName = "a")
    final DialoguePrerequisite opA;
    @SerializedField(deserialParamName = "b")
    final DialoguePrerequisite opB;

    public BinaryOperationPrerequisite(DialoguePrerequisite a, DialoguePrerequisite b) {
        this.opA = a;
        this.opB = b;
    }
}
