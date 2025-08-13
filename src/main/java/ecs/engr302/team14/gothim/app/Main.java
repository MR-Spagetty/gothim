package ecs.engr302.team14.gothim.app;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class Main {
    private static Main instance;
    public static JFrame frame;
    public static JPanel rendererPanel, buttonPanel;
    public static GameState currentState;

    private Main(){
        currentState = GameState.Menu;
        frame = new JFrame();
        rendererPanel = new JPanel();
        buttonPanel = new JPanel();
        new ButtonManager();

        //Set frame size
        frame.setSize(900, 900);
        buttonPanel.setPreferredSize(new Dimension(174, 800));

        buttonPanel.setBackground(new Color(73,157,208));
        frame.add(buttonPanel, BorderLayout.WEST);
        frame.add(rendererPanel, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        updateGUI();
        frame.setVisible(true);
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
    }

    private List<JButton> getStateButtons(){
        return switch (currentState) {
            case Menu -> ButtonManager.getMenuButtons();
            case Playing -> ButtonManager.getPlayingButtons();
        };
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
    }

}
