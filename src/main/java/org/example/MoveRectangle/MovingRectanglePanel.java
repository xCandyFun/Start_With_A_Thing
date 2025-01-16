package org.example.MoveRectangle;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class MovingRectanglePanel extends JPanel implements KeyListener {

    private int rectX = 250;
    private int rectY = 150;
    private final int RECT_WIDTH = 30;
    private final int RECT_HEIGHT = 30;
    private final int MOVE_DISTANCE = 10;
    private int followRectX = 30;
    private int followRectY = 30;
    private final double followSpeed = 1;

    private final Set<Integer> activeKey = new HashSet<>();

    public MovingRectanglePanel(){
        // Add the KeyListener to the panel
        this.addKeyListener(this);
        this.setFocusable(true); // Ensure the panel can receive focus

        this.requestFocusInWindow();

        // Timer to handle continuous movement
        Timer moveTimer = new Timer(40, e -> updatePosition());
        moveTimer.start();

        // Start the following mechanism
        Timer timer = new Timer(15, e-> moveFollowing());
        timer.start();
    }

    //Update the position of the main rectangle based on active keys
    private void updatePosition(){
        if (activeKey.contains(KeyEvent.VK_W) && rectY > 0 ){
            rectY -= MOVE_DISTANCE;
        }
        if (activeKey.contains(KeyEvent.VK_S) && rectY + RECT_HEIGHT < getHeight()) {
            rectY += MOVE_DISTANCE;
        }
        if (activeKey.contains(KeyEvent.VK_A) && rectX >0){
            rectX -= MOVE_DISTANCE;
        }
        if (activeKey.contains(KeyEvent.VK_D) && rectX + RECT_WIDTH < getWidth()) {
            rectX += MOVE_DISTANCE;
        }

        repaint();
    }

    // Move the hunter rectangle towards the main rectangle
    private void moveFollowing() {
        // Calculate the difference
        int dx = rectX - followRectX;
        int dy = rectY - followRectY;

        // Normalize the direction and move the follow rectangle
        double distance = Math.sqrt(dx * dx + dy * dy);
        if (distance > 0) {
            followRectX += Math.round(followSpeed * (dx / distance ));
            followRectY += Math.round(followSpeed * (dy / distance ));
        }

        // Repaint the panel to update positions
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g); // Call the superclass for proper rendering

        // Set the background color
        this.setBackground(Color.LIGHT_GRAY);

        // Draw the rectangle
        g.setColor(Color.BLUE);
        g.fillRect(rectX, rectY, RECT_WIDTH, RECT_HEIGHT);

        // Draw the following rectangle
        g.setColor(Color.RED);
        g.fillRect(followRectX, followRectY, RECT_WIDTH, RECT_HEIGHT);

    }


    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

        activeKey.add(e.getKeyCode());

        // Save the current position of the main rectangle
//        int previousRectX = rectX;
//        int previousRectY = rectY;

        // Move the rectangle based on key press
//        switch (e.getKeyCode()) {
//            case KeyEvent.VK_W -> rectY -= MOVE_DISTANCE;
//            case KeyEvent.VK_S -> rectY += MOVE_DISTANCE;
//            case KeyEvent.VK_A -> rectX -= MOVE_DISTANCE;
//            case KeyEvent.VK_D -> rectX += MOVE_DISTANCE;
//        }

        // Update the following rectangle's position to the main rectangle's previous position
//        followRectX = previousRectX;
//        followRectY = previousRectY;

        // Repaint the panel to update the rectangle position
        repaint();

    }

    @Override
    public void keyReleased(KeyEvent e) {
        activeKey.remove(e.getKeyCode());
    }
}
