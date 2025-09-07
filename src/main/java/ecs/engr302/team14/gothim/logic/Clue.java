package ecs.engr302.team14.gothim.logic;

import ecs.engr302.team14.gothim.persistancy.annotations.DeserializationMethod;
import ecs.engr302.team14.gothim.persistancy.annotations.SerializedField;

/**
 * Basic clue record.
 *
 * @author MR-Spagetty
 */
public record Clue(@SerializedField AccessModifier modifier,
        @SerializedField String id,
        @SerializedField String description) {
    @DeserializationMethod(serialFieldNames = { "modifier", "id", "description" })
    public Clue {
    }
}
