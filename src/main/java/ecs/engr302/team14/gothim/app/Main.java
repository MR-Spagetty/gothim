package ecs.engr302.team14.gothim.app;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyListener;
import java.util.List;
import java.util.HashSet;
import java.util.Set;
import java.awt.event.KeyEvent;
import ecs.engr302.team14.gothim.renderer.Renderer;
import ecs.engr302.team14.gothim.entities.Player;
import ecs.engr302.team14.gothim.entities.NPC;
import ecs.engr302.team14.gothim.util.Point;


public class Main {
    private static Main instance;
    public static JFrame frame;
    public static JPanel rendererPanel, buttonPanel;
    public static GameState currentState;
    private KeybindManager keyBinds;
    private static Renderer renderer = Renderer.getInstance();
    private Player player;
    private Set<Integer> pressedKeys = new HashSet<>();
    private java.util.List<NPC> npcs;

    private Main(){
        currentState = GameState.Menu;
        frame = new JFrame();
        rendererPanel = Renderer.getInstance();
        buttonPanel = new JPanel();
        keyBinds = new KeybindManager();
        new ButtonManager();

        // Initialize player and NPCs
        player = new Player("Player", new Point(450, 450));
        npcs = java.util.List.of(
                new NPC("Guard", "Stay out of restricted areas!", new Point(300, 300)),
                new NPC("Scientist", "The research is progressing well.", new Point(600, 200))
        );

        //Pass to renderer (should ideally be passing the level object)
        renderer.setPlayer(player);
        renderer.setNPCs(npcs);

        //Set frame size
        frame.setSize(900, 900);
        buttonPanel.setPreferredSize(new Dimension(174, 800));

        buttonPanel.setBackground(new Color(73,157,208));
        frame.add(buttonPanel, BorderLayout.WEST);
        frame.add(rendererPanel, BorderLayout.CENTER);

        keyBinds.applyBindings(rendererPanel);
        // temp so <Developer 1> can change to her keybinds later
        setupKeyListeners();


        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        updateGUI();
        frame.setVisible(true);

        //Another change (just for reference)
        startGameLoop();
    }

    //Added game loop for things happening
    private void startGameLoop() {
        // Simple game loop using Swing Timer
        Timer gameLoop = new Timer(16, e -> {
            if (currentState == GameState.Playing) {
                // Update player position
                player.update(pressedKeys, rendererPanel.getWidth(), rendererPanel.getHeight());
                // Repaint the game
                rendererPanel.repaint();
            }
        });
        gameLoop.start();
    }

    public static Main getMainInstance(){
        if (instance == null) {
            if(frame != null) {
                frame.dispose();
            }
            instance = new Main();
        }
        return instance;
    }

    private void updateGUI(){
        buttonPanel.removeAll();

        List<JButton> buttons = getStateButtons();
        for(JButton button : buttons){
            button.setAlignmentX(Component.CENTER_ALIGNMENT);
            button.setMargin(new Insets(5, 15, 5, 15));
            button.setMinimumSize(new Dimension(250,30));
            button.setMaximumSize(new Dimension(250, 30));
            button.setBackground(new Color(124, 196, 241));
            button.setForeground(Color.white);
            buttonPanel.add(Box.createVerticalStrut(10));
            buttonPanel.add(button);
        }
    }

    public void setGameState(GameState newState) {
        this.currentState = newState;
        updateGUI();

        // Request focus when switching to playing state
        if (newState == GameState.Playing) {
            rendererPanel.requestFocusInWindow();
        }
    }

    private List<JButton> getStateButtons(){
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

}
