package ecs.engr302.team14.gothim.app;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class main {
    public static JFrame frame;

    private main(){
        frame = new JFrame();
        frame.setSize(200, 200);

        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(main::new);
    }

}
