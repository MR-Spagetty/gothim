package ecs.engr302.team14.gothim.app;

import ecs.engr302.team14.gothim.entities.Player;
import ecs.engr302.team14.gothim.logic.dialogue.Dialogue;
import ecs.engr302.team14.gothim.logic.dialogue.DialogueOption;
import ecs.engr302.team14.gothim.renderer.Renderer;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;



/**
 * Class for initialising the game and running it.
 */
public class Main {
    private static Main instance;
    public static JFrame frame;
    public static JPanel rendererPanel;
    public static JPanel buttonPanel;
    public static GameState currentState;
    public static int playerID = 0;
    private Set<Integer> pressedKeys = new HashSet<>();

    private Main() {
        currentState = GameState.Menu;
        LevelManager.setLevel(LevelEnum.ONE);
        frame = new JFrame();
        rendererPanel = Renderer.getInstance();
        buttonPanel = new JPanel();
        new ButtonManager();

        //Set frame size
        frame.setSize(900, 900);
        buttonPanel.setPreferredSize(new Dimension(174, 800));

        buttonPanel.setBackground(new Color(73, 157, 208));
        frame.add(buttonPanel, BorderLayout.WEST);
        frame.add(rendererPanel, BorderLayout.CENTER);

        KeybindManager.applyBindings(rendererPanel);
        // temp so <Developer 1> can change to her keybinds later
        setupKeyListeners();


        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        updateGUI();
        frame.setVisible(true);

        rendererPanel.repaint();
        //Another change (just for reference)
        startGameLoop();
    }

    //Added game loop for things happening
    private void startGameLoop() {
        // Simple game loop using Swing Timer
        Timer gameLoop = new Timer(200, _ -> {
            if (currentState == GameState.Playing) {
                rendererPanel.repaint();
            }
        });
        gameLoop.start();
    }

    /**
     * Initialises the game if it hasn't been already and returns the instance.
     *
     * @return the instance
     */
    public static Main getMainInstance() {
        if (instance == null) {
            if (frame != null) {
                frame.dispose();
            }
            instance = new Main();
        }
        return instance;
    }

    private void updateGUI() {
        buttonPanel.removeAll();

        List<JButton> buttons = getStateButtons();
        for (JButton button : buttons) {
            button.setAlignmentX(Component.CENTER_ALIGNMENT);
            button.setMargin(new Insets(5, 15, 5, 15));
            button.setMinimumSize(new Dimension(250, 30));
            button.setMaximumSize(new Dimension(250, 30));
            button.setBackground(new Color(124, 196, 241));
            button.setForeground(Color.white);
            buttonPanel.add(Box.createVerticalStrut(10));
            buttonPanel.add(button);
        }
    }

    /**
     * set the current state of the game.
     *
     * @param newState the new gamestate
     */
    public void setGameState(GameState newState) {
        Main.currentState = newState;
        updateGUI();

        // Request focus when switching to playing state
        if (newState == GameState.Playing) {
            rendererPanel.requestFocusInWindow();
        }
    }

    private List<JButton> getStateButtons() {
        return switch (currentState) {
            case Menu -> ButtonManager.getMenuButtons();
            case Playing -> ButtonManager.getPlayingButtons();
        };
    }

    private void setupKeyListeners() {
        rendererPanel.setFocusable(true);
        rendererPanel.requestFocusInWindow();

        rendererPanel.addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                pressedKeys.add(e.getKeyCode());
            }

            @Override
            public void keyReleased(KeyEvent e) {
                pressedKeys.remove(e.getKeyCode());
            }

            @Override
            public void keyTyped(KeyEvent e) {
                // Not needed
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
    }

    /**
     * Brings up a dialogue popup for the user.
     *
     * @param sourceName the name of the origin of the dialogue
     * @param dialogue the dialogue source to use
     */
    public static void dialogue(String sourceName, Dialogue dialogue, Player p) {
        if (!SwingUtilities.isEventDispatchThread()) {
            SwingUtilities.invokeLater(() -> dialogue(sourceName, dialogue, p));
            return;
        }
        Optional<Dialogue> curr = Optional.of(dialogue);
        do {
            int chosen = JOptionPane.showOptionDialog(frame, curr.get().say(), sourceName,
                    JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null,
                    curr.get().getOptions(p).stream().map(DialogueOption::text).toArray(),
                    DialogueOption.GoodBye.text()
            );
            if (chosen == JOptionPane.CLOSED_OPTION) {
                return;
            }
            curr = curr.get().progress(p, chosen);
        } while (curr.isPresent());
    }

}
