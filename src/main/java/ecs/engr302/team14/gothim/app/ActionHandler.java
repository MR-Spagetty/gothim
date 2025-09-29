package ecs.engr302.team14.gothim.app;

import ecs.engr302.team14.gothim.renderer.Renderer;

/**
 * Class for handling actions.
 */
public class ActionHandler {
    public ActionHandler() {}

    /**
     * handles moving the player in the given direction.
     */
    public static void handleMove(/*Direction direction*/) {
        //GameManager.movePlayer(direction);
        System.out.println("Moving...");
        if (Main.rendererPanel != null) {
            Main.rendererPanel.revalidate();
            Main.rendererPanel.repaint();
        }
    }

    /**
     * handle the the starting of a new game.
     */
    public static void handleNewGame() {
        System.out.println("Starting a new game. . .");
        Main main = Main.getMainInstance();
        main.setGameState(GameState.Playing);
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
