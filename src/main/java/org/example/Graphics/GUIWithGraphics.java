package org.example.Graphics;

import javax.swing.*;

public class GUIWithGraphics {

    CustomGraphicsPanel customGraphicsPanel;
    public void graphicsWindow(){

        JFrame frame = new JFrame("Swing with Graphics");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400,400);

        // Create a custom panel for graphics
        JPanel graphicsPanel = new CustomGraphicsPanel();

        // Add the custom panel to the frame
        frame.add(graphicsPanel);

        // Make the frame visible
        frame.setVisible(true);
    }
}
