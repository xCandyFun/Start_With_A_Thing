package org.example;

import org.example.MoveRectangle.MovingRectanglePanel;

import javax.swing.*;
import java.awt.*;

public class StartScreen extends JPanel {
    public StartScreen(JFrame frame){
        this.setLayout(new BorderLayout());
        JLabel welcomeLabel = new JLabel("Welcome! Click 'Start' to Begin", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        this.add(welcomeLabel, BorderLayout.CENTER);

        JButton startButton = new JButton("Start");
        startButton.setFont(new Font("Arial", Font.BOLD, 14));
        this.add(startButton, BorderLayout.SOUTH);

        // Action Listener for the start button
        startButton.addActionListener(e-> {
            frame.getContentPane().removeAll();
            MovingRectanglePanel panel = new MovingRectanglePanel();

            frame.add(panel);
            frame.revalidate();
            frame.repaint();

            //Request focus for the panel to capture keyboard input
            panel.requestFocusInWindow();
        });
    }
}
