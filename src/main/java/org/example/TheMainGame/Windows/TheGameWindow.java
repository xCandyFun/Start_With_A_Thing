package org.example.TheMainGame.Windows;

import javax.swing.*;

public class TheGameWindow {

    public void rectangleOnMove(){
        // Create the Main frame
        JFrame frame = new JFrame("Move Rectangle");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000,800);

        frame.setLocationRelativeTo(null);
        //frame.setSize(400,400);

        // Show the start screen
        StartScreen startScreen = new StartScreen(frame);

        // Add the startScreen to the frame
        frame.add(startScreen);
        frame.setVisible(true);
    }
}
