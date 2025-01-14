package org.example;

import javax.swing.*;
import java.awt.*;

public class GUI {

    private final int width = 400;
    private final int height = 400;


    public void theMainWindow() {
        // Create the frame (the main window)
        JFrame frame = new JFrame("simple Swing Gui");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(width, height);

        // Create a label
        JLabel label = new JLabel("Welcome to Swing!", SwingConstants.CENTER);

        // Create a button
        JButton button = new JButton("Click me");
        button.addActionListener(e -> {
            JOptionPane.showMessageDialog(frame, "Button Clicked!");
        });

        // Add components to the frame
        frame.setLayout(new BorderLayout());
        frame.add(label, BorderLayout.CENTER);
        frame.add(button, BorderLayout.SOUTH);

        // Make the frame visible
        frame.setVisible(true);
    }

}
