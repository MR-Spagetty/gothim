package ecs.engr302.team14.gothim.app;

public class ActionHandler {
    public ActionHandler() {}

    public static void handleMove(/*Direction direction*/) {
        //GameManager.movePlayer(direction);
        System.out.println("Moving...");
        if (Main.rendererPanel != null) {
            Main.rendererPanel.revalidate();
            Main.rendererPanel.repaint();
        }
    }

    public static void handleNewGame(){
        System.out.println("Starting a new game. . .");
        Main main = Main.getMainInstance();
        main.setGameState(GameState.Playing);
    }

    public static void handleLoadGame(){
        System.out.println("Loading a previous game. . .");
    }

    public static void handleOpenInstructions(){
        System.out.println("Opening instructions. . .");
    }

    public static void handleOpenTaskbook(){
        System.out.println("Opening taskbook. . .");
    }

    public static void handleSaveGame(){
        System.out.println("Saving current game. . .");
    }

    public static void handleQuit(){
        System.exit(0);
    }

}
