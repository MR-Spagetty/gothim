package ecs.engr302.team14.gothim.logic;

import ecs.engr302.team14.gothim.entities.Entity;
import ecs.engr302.team14.gothim.entities.Player;
import ecs.engr302.team14.gothim.map.Board;
import ecs.engr302.team14.gothim.map.MapBuilder;
import ecs.engr302.team14.gothim.persistancy.annotations.DeserializationMethod;
import ecs.engr302.team14.gothim.persistancy.annotations.SerializedField;
import ecs.engr302.team14.gothim.util.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Holds all the information for a level.
 *
 * @param levelID The ID of the level
 * @param map The map of the level
 * @param clues The clues for the level
 */
public record LevelHolder(@SerializedField String levelID,
        @SerializedField(deserialParamName = "map") MapBuilder mapBuilder,
        Board map,
        @SerializedField ClueHolder clues,
        @SerializedField ArrayList<Entity> entities,
        @SerializedField ArrayList<Player> players,
        @SerializedField Point spawnPoint) {

    /**
     * Creates a new LevelHolder. DO NOT USE DIRECTLY,
     * instead please use {@link #LevelHolder(String, MapBuilder, ClueHolder, List, List, Point)}.
     *
     * @param levelID the ID of the level
     * @param mapBuilder the map builder used to create the map
     * @param map the map of the level
     * @param clues the clues for the level
     * @param entities the entities within the level
     * @param players the players within the level
     * @param spawnPoint the spawn point for the level
     */
    public LevelHolder {
        Objects.requireNonNull(levelID);
        Objects.requireNonNull(mapBuilder);
        Objects.requireNonNull(map);
        Objects.requireNonNull(clues);
        Objects.requireNonNull(entities);
        Objects.requireNonNull(players);
        Objects.requireNonNull(spawnPoint);
        Stream.concat(entities.stream(), players.stream())
                .parallel()
                .forEach(e -> map.getTile(e.getPosition()).setOcupant(e));
        String mismatchedEntities = Stream.concat(entities.stream(), players.stream())
                .parallel()
                .filter(e -> map.getTile(e.getPosition()).getOcupant().orElse(null) == e)
                .map(Object::toString)
                .collect(Collectors.joining("\n"));
        if (!mismatchedEntities.isBlank()) {
            throw new IllegalStateException(
                "The following entities were not placed on the map correctly:\n"
                + mismatchedEntities
                );
        }
    }

    /**
     * Creates a new LevelHolder, constructing the map using hte provided builder.
     *
     * @param levelID the ID of the level
     * @param mapBuilder the map builder used to create the map
     * @param clues the clues for the level
     * @param entities the entities within the level
     * @param players the players within the level
     * @param spawnPoint the spawn point for the level
     */
    @DeserializationMethod(serialFieldNames = {
        "levelID", "map", "clues", "entities", "players", "spawnPoint"
    })
    public LevelHolder(String levelID, MapBuilder mapBuilder, ClueHolder clues,
            List<Entity> entities, List<Player> players, Point spawnPoint) {
        this(levelID, mapBuilder, mapBuilder.build(), clues, new ArrayList<>(entities),
                new ArrayList<>(players), spawnPoint);
    }
}
