package main;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, FontFormatException {
        BufferedImage cursorImg = ImageIO.read(new File(System.getProperty("user.dir")+"/mouse.png"));
// Create a new blank cursor.
        Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(
                cursorImg, new Point(0, 0), "mouse");

// Set the blank cursor to the JFrame.

        JFrame window = new JFrame();
        window.setUndecorated(true);
        window.getContentPane().setCursor(blankCursor);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("Noah against the cocks");
        GamePanel gamePanel = new GamePanel();
        window.add(gamePanel);
        window.pack();
        window.setLocationRelativeTo(null);
        window.setExtendedState(JFrame.MAXIMIZED_BOTH);
        window.setVisible(true);

        gamePanel.startGameThread();
    }
}