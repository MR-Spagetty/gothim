package ecs.engr302.team14.gothim.app;

import ecs.engr302.team14.gothim.logic.LevelHolder;
import ecs.engr302.team14.gothim.persistancy.Serialization;

/**
 * Static data class for managing the current level.
 */
public class LevelManager {

    static LevelHolder currentLevelData;

    /**
     * Load and set the current level.
     *
     * @param level the level to load
     */
    public static void setLevel(LevelEnum level) {
        if (level == null) {
            throw new IllegalArgumentException("Level cannot be null");
        }

        try {
            System.out.println("Loading level: " + level.name());
            currentLevelData = Serialization.loadLevel(level.filename());
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to load the level: " + level, e);
        }
    }

    /**
     * Get the currently loaded level.
     *
     * @return the current level
     */
    public static LevelHolder getLevelData() {
        if (currentLevelData == null) {
            throw new IllegalStateException("No level has been loaded yet.");
        }
        return currentLevelData;
    }
}
