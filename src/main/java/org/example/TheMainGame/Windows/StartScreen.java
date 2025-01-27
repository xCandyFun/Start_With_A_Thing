package org.example.TheMainGame.Windows;

import org.example.TheMainGame.TheMainGameLogic;

import javax.swing.*;
import java.awt.*;

public class StartScreen extends JPanel {
    public StartScreen(JFrame frame){
        this.setLayout(new BorderLayout());
        JLabel welcomeLabel = new JLabel("<html>Welcome! Click 'Start' to Begin" +
                "<br>To play use W, A, S, D keys or The Arrow Keys</html>", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        this.add(welcomeLabel, BorderLayout.CENTER);

        JButton startButton = new JButton("Start");
        startButton.setFont(new Font("Arial", Font.BOLD, 14));
        this.add(startButton, BorderLayout.SOUTH);

        // Action Listener for the start button
        startButton.addActionListener(e-> {
            frame.getContentPane().removeAll();
            TheMainGameLogic panel = new TheMainGameLogic();

            frame.add(panel);
            frame.revalidate();
            frame.repaint();

            //Request focus for the panel to capture keyboard input
            panel.requestFocusInWindow();
        });
    }
}
