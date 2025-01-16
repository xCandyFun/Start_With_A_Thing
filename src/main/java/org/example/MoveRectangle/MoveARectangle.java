package org.example.MoveRectangle;

import org.example.StartScreen;

import javax.swing.*;

public class MoveARectangle {

    public void rectangleOnMove(){
        // Create the Main frame
        JFrame frame = new JFrame("Move Rectangle");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400,400);

        // Show the start screen
        StartScreen startScreen = new StartScreen(frame);

        // Add the startScreen to the frame
        frame.add(startScreen);
        frame.setVisible(true);
    }
}
