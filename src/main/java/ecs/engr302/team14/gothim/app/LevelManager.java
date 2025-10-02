package ecs.engr302.team14.gothim.app;

import ecs.engr302.team14.gothim.logic.LevelHolder;
import ecs.engr302.team14.gothim.map.Board;
import ecs.engr302.team14.gothim.persistancy.Serialization;

public class LevelManager {

    private static Board curBoard;

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
            LevelHolder currentLevelData = Serialization.loadLevel(level.filename());
            curBoard = currentLevelData.map();  // record accessor
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to load the level: " + level, e);
        }
    }

    /**
     * Get the currently loaded board.
     *
     * @return the current board
     */
    public static Board getCurBoard() {
        if (curBoard == null) {
            throw new IllegalStateException("No level has been loaded yet.");
        }
        return curBoard;
    }
}
