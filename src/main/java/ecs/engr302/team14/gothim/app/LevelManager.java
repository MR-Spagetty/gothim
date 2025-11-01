package ecs.engr302.team14.gothim.app;

import ecs.engr302.team14.gothim.logic.LevelHolder;
import ecs.engr302.team14.gothim.persistancy.Serialization;

/**
 * Static data class for managing the current level.
 */
public class LevelManager {

    static LevelHolder currentLevelData;

    private static LevelEnum currLevel = null;

    /**
     * Load and set the current level.
     *
     * @param level the level to load
     */
    public static void setLevel(LevelEnum level) {
        if (level == null) {
            System.out.println("No level provided");
            return;
        }

        if (level.filename() == null) {
            System.out.println("no level resource specified for: " + level.name());
        }

        try {
            System.out.println("Loading level: " + level.name());
            currentLevelData = Serialization.loadLevel(level.filename());
            currentLevelData.getPlayer(0);
        } catch (Exception e) {
            System.err.println("Failed to load the level: " + level);
            e.printStackTrace();
            return;
        }
        currLevel = level;
    }

    public static LevelEnum currLevel() {
        return currLevel;
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
