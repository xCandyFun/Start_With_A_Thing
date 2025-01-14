package org.example.MoveRectangle;

import javax.swing.*;

public class MoveARectangle {

    public void rectangleOnMove(){
        // Create the Main frame
        JFrame frame = new JFrame("Move Rectangle");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400,400);

        // Create the custom panel for graphics
        MovingRectanglePanel panel = new MovingRectanglePanel();

        // Add the Panel to the frame
        frame.add(panel);
        frame.setVisible(true);

        // Request focus to capture keyboard input
        panel.requestFocusInWindow();
    }
}
