package ecs.engr302.team14.gothim.app;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.ComponentInputMap;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

public class KeybindManager {
    private static final ActionMap actionMap = new ActionMap();
    private static final InputMap inputMap = new InputMap();

    KeybindManager() {
        this.setActions();
        this.setBindings();
    }

    private void setActions() {
        actionMap.put("moveUp", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                ActionHandler.handleMove(/*Direction.Up*/);
            }
        });
        actionMap.put("moveDown", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                ActionHandler.handleMove(/*Direction.Down*/);
            }
        });
        actionMap.put("moveLeft", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                ActionHandler.handleMove(/*Direction.Left*/);
            }
        });
        actionMap.put("moveRight", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                ActionHandler.handleMove(/*Direction.Right*/);
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
        inputMap.put(KeyStroke.getKeyStroke("control X"), "exit");
        inputMap.put(KeyStroke.getKeyStroke("control S"), "save");
        inputMap.put(KeyStroke.getKeyStroke("control R"), "load");
    }

    public static void applyBindings(JComponent component) {
        ComponentInputMap componentInputMap = new ComponentInputMap(component);
        KeyStroke[] var2 = inputMap.keys();
        int var3 = var2.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            KeyStroke keyStroke = var2[var4];
            componentInputMap.put(keyStroke, inputMap.get(keyStroke));
        }

        component.setInputMap(2, componentInputMap);
        component.setActionMap(actionMap);
    }

    public static void disableActions(List<String> actionNames) {
        Iterator var1 = actionNames.iterator();

        while(var1.hasNext()) {
            String actionName = (String)var1.next();
            actionMap.get(actionName).setEnabled(false);
        }

    }

    public static void enableActions(List<String> actionNames) {
        Iterator var1 = actionNames.iterator();

        while(var1.hasNext()) {
            String actionName = (String)var1.next();
            actionMap.get(actionName).setEnabled(true);
        }

    }
}
