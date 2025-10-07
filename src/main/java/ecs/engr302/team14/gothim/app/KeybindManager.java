package ecs.engr302.team14.gothim.app;

import ecs.engr302.team14.gothim.util.Direction;
import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.ComponentInputMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;


/**
 * Class for managing the game keybinds.
 */
public class KeybindManager {
    private static final ActionMap actionMap = new ActionMap();
    private static final InputMap inputMap = new InputMap();

    KeybindManager() {
        this.setActions();
        this.setBindings();
    }

    // Ensure actions and bindings are created when the class is loaded so callers
    // don't need to instantiate KeybindManager manually.
    static {
        new KeybindManager();
    }

    private void setActions() {
        actionMap.put("moveUp", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                ActionHandler.handleMove(Direction.Up);
            }
        });
        actionMap.put("moveDown", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                ActionHandler.handleMove(Direction.Down);
            }
        });
        actionMap.put("moveLeft", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                ActionHandler.handleMove(Direction.Left);
            }
        });
        actionMap.put("moveRight", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                ActionHandler.handleMove(Direction.Right);
            }
        });
        actionMap.put("exit", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                ActionHandler.handleQuit();
            }
        });
        actionMap.put("save", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                ActionHandler.handleSaveGame();
            }
        });
        actionMap.put("load", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                ActionHandler.handleLoadGame();
            }
        });
    }

    private void setBindings() {
        inputMap.put(KeyStroke.getKeyStroke("UP"), "moveUp");
        inputMap.put(KeyStroke.getKeyStroke("DOWN"), "moveDown");
        inputMap.put(KeyStroke.getKeyStroke("LEFT"), "moveLeft");
        inputMap.put(KeyStroke.getKeyStroke("RIGHT"), "moveRight");
        inputMap.put(KeyStroke.getKeyStroke("W"), "moveUp");
        inputMap.put(KeyStroke.getKeyStroke("S"), "moveDown");
        inputMap.put(KeyStroke.getKeyStroke("A"), "moveLeft");
        inputMap.put(KeyStroke.getKeyStroke("D"), "moveRight");
        inputMap.put(KeyStroke.getKeyStroke("control X"), "exit");
        inputMap.put(KeyStroke.getKeyStroke("control S"), "save");
        inputMap.put(KeyStroke.getKeyStroke("control R"), "load");
    }

    /**
     * Applies the keybindings to the given component.
     *
     * @param component the component to apply the keybindings to
     */
    public static void applyBindings(JComponent component) {
        ComponentInputMap componentInputMap = new ComponentInputMap(component);
        KeyStroke[] var2 = inputMap.keys();
        if (var2 != null) {
            for (KeyStroke keyStroke : var2) {
                componentInputMap.put(keyStroke, inputMap.get(keyStroke));
            }
        }

        component.setInputMap(2, componentInputMap);
        component.setActionMap(actionMap);
    }

    /**
     * Disable the specified actions.
     *
     * @param actionNames the list of action names to disable
     */
    public static void disableActions(List<String> actionNames) {
        for (String actionName : actionNames) {
            actionMap.get(actionName).setEnabled(false);
        }
    }

    /**
     * Enable the specified actions.
     *
     * @param actionNames the list of action names to enable
     */
    public static void enableActions(List<String> actionNames) {
        for (String actionName : actionNames) {
            actionMap.get(actionName).setEnabled(true);
        }

    }
}
