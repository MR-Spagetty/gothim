package ecs.engr302.team14.gothim.renderer.behaviours;

import ecs.engr302.team14.gothim.persistancy.annotations.DeserializationMethod;
import ecs.engr302.team14.gothim.persistancy.annotations.SerializedField;

/**
 * Asset behaviour holder.
 *
 * @author MR-Spagetty
 */
public record Behaviour(
        @SerializedField
        String assetName,
        @SerializedField
        BehaviourCondition cond
) {
    @DeserializationMethod(serialFieldNames = { "assetName", "cond" })
    public Behaviour {}
}
