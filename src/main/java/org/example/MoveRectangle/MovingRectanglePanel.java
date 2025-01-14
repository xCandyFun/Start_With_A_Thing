package org.example.MoveRectangle;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.*;

public class MovingRectanglePanel extends JPanel implements KeyListener {

    private int rectX, rectY = 150;
    private final int RECT_WIDTH = 50;
    private final int RECT_HEIGHT = 30;
    private final int MOVE_DISTANCE = 10;

    public MovingRectanglePanel(){
        // Add the KeyListener to the panel
        this.addKeyListener(this);
        this.setFocusable(true); // Ensure the panel can receive focus
    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g); // Call the superclass for proper rendering

        // Set the background color
        this.setBackground(Color.LIGHT_GRAY);

        // Draw the rectangle
        g.setColor(Color.BLUE);
        g.fillRect(rectX, rectY, RECT_WIDTH, RECT_HEIGHT);

    }


    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        // Move the rectangle based on key press
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W -> rectY -= MOVE_DISTANCE;
            case KeyEvent.VK_S -> rectY += MOVE_DISTANCE;
            case KeyEvent.VK_A -> rectX -= MOVE_DISTANCE;
            case KeyEvent.VK_D -> rectX += MOVE_DISTANCE;
        }

        // Repaint the panel to update the rectangle position
        repaint();

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
