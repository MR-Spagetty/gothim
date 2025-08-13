package ecs.engr302.team14.gothim.app;

import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;

public class ButtonManager {
    private static final List<JButton> menuButtons = new ArrayList();
    private static final List<JButton> playingButtons = new ArrayList();

    ButtonManager() {
        this.createButtons();
    }

    private void createButtons(){
        menuButtons.clear();
        playingButtons.clear();

        JButton newGameButton = this.createButton("Start New Game", (e) -> {
            ActionHandler.handleNewGame();
        });

        JButton loadGameButton = this.createButton("Load Previous Game", (e) -> {
            ActionHandler.handleLoadGame();
        });

        JButton openInstructionsButton = this.createButton("Instructions", (e) -> {
            ActionHandler.handleOpenInstructions();
        });

        menuButtons.add(newGameButton);
        menuButtons.add(loadGameButton);
        menuButtons.add(openInstructionsButton);

    }

    static List<JButton> getMenuButtons() { return menuButtons; }

    static List<JButton> getPlayingButtons() {
        return playingButtons;
    }

    private JButton createButton(String text, ActionListener action) {
        JButton button = new JButton(text);
        button.addActionListener(action);
        return button;
    }
}
