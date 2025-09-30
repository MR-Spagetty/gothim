package ecs.engr302.team14.gothim.app;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;

/**
 * Class for managing the buttons within the UI.
 */
public class ButtonManager {
    private static final List<JButton> menuButtons = new ArrayList<>();
    private static final List<JButton> playingButtons = new ArrayList<>();

    ButtonManager() {
        this.createButtons();
    }

    private void createButtons() {
        menuButtons.clear();
        playingButtons.clear();

        JButton newGameButton = this.createButton("Start New Game", _ -> {
            ActionHandler.handleNewGame();
        });
        JButton loadGameButton = this.createButton("Load Previous Game", _ -> {
            ActionHandler.handleLoadGame();
        });
        JButton openInstructionsButton = this.createButton("Instructions", _ -> {
            ActionHandler.handleOpenInstructions();
        });
        menuButtons.add(newGameButton);
        menuButtons.add(loadGameButton);
        menuButtons.add(openInstructionsButton);

        JButton openTaskbookButton = this.createButton("Taskbook", _ -> {
            ActionHandler.handleOpenTaskbook();
        });
        JButton saveGameButton = this.createButton("Save Current Game", _ -> {
            ActionHandler.handleSaveGame();
        });
        JButton quitGameButton = this.createButton("Quit", _ -> {
            ActionHandler.handleQuit();
        });
        playingButtons.add(openTaskbookButton);
        playingButtons.add(saveGameButton);
        playingButtons.add(quitGameButton);

    }

    static List<JButton> getMenuButtons() {
        return menuButtons;
    }

    static List<JButton> getPlayingButtons() {
        return playingButtons;
    }

    private JButton createButton(String text, ActionListener action) {
        JButton button = new JButton(text);
        button.addActionListener(action);
        return button;
    }
}
