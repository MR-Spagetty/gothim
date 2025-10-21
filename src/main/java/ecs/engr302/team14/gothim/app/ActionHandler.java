package ecs.engr302.team14.gothim.app;

import ecs.engr302.team14.gothim.renderer.Renderer;
import ecs.engr302.team14.gothim.util.Direction;

/**
 * Class for handling actions.
 */
public class ActionHandler {
    public ActionHandler() {}

    /**
     * handles moving the player in the given direction.
     */
    public static void handleMove(Direction direction) {
        LevelManager.getLevelData().movePlayer(Main.playerID, direction);
        if (Main.rendererPanel != null) {
            Main.rendererPanel.revalidate();
        }
    }

    public static void handleInteract() {
        LevelManager.getLevelData().getPlayer(Main.playerID).interact();
    }

    /**
     * handle the starting of a new game.
     */
    public static void handleNewGame() {
        Main main = Main.getMainInstance();
        main.setGameState(GameState.Playing);
        LevelManager.setLevel(LevelEnum.ONE);
    }

    /**
     * handle the loading of a saved game.
     */
    public static void handleLoadGame() {
        System.out.println("Loading a previous game. . .");
    }

    /**
     * handle the opening of the instructions.
     */
    public static void handleOpenInstructions() {
        System.out.println("Opening instructions. . .");
    }

    /**
     * handle the opening of the taskbook.
     */
    public static void handleOpenTaskbook() {
        System.out.println("Opening taskbook. . .");
        Renderer.getInstance().toggleTaskbook();
    }

    /**
     * handle the saving of the current game state.
     */
    public static void handleSaveGame() {
        System.out.println("Saving current game. . .");
    }

    /**
     * handle quitting the game.
     */
    public static void handleQuit() {
        System.exit(0);
    }

}
