package ecs.engr302.team14.gothim.logic;

import ecs.engr302.team14.gothim.map.Board;
import ecs.engr302.team14.gothim.map.MapBuilder;
import ecs.engr302.team14.gothim.persistancy.annotations.DeserializationMethod;
import ecs.engr302.team14.gothim.persistancy.annotations.SerializedField;

/**
 * Holds all the information for a level.
 *
 * @param levelID The ID of the level
 * @param map The map of the level
 * @param clues The clues for the level
 */
public record LevelHolder(
        @SerializedField String levelID,
        @SerializedField(deserialParamName = "map") MapBuilder mapBuilder,
        Board map,
        @SerializedField ClueHolder clues) {

    @DeserializationMethod(serialFieldNames = {"levelID", "map", "clues"})
    public LevelHolder(String levelID, MapBuilder mapBuilder, ClueHolder clues) {
        this(levelID, mapBuilder, mapBuilder.build(), clues);
    }
}
