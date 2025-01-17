package org.example.MoveRectangle;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.*;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class MovingRectanglePanel extends JPanel implements KeyListener {

    // Main rectangle start position
    private int rectX = 280;
    private int rectY = 330;

    // Main rectangle size
    private final int RECT_WIDTH = 30;
    private final int RECT_HEIGHT = 30;

    // Main rectangle movement speed
    private final int MOVE_DISTANCE = 10;

    // The following rectangle start position
    private int followRectX = 30;
    private int followRectY = 50;

    // The following rectangle start speed
    private double followSpeed = 1;

    private final int CIRCLE_DIAMETER = 20;
    private int circleX, circleY;
    private int score = 0;

    private final Set<Integer> activeKey = new HashSet<>();
    private Timer moveTimer;
    private Timer followTimer;
    private Timer clockTimer;

    private int elapsedTime = 0;
    private final Random random = new Random();

    public MovingRectanglePanel(){
        // Add the KeyListener to the panel
        this.addKeyListener(this);
        this.setFocusable(true); // Ensure the panel can receive focus
        this.requestFocusInWindow();

        // Add a ComponentListener to ensure the panel is ready before spawning the circle
        this.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                spawnNewCircle();
            }
        });

        // Timer to handle continuous movement
        moveTimer = new Timer(40, e -> updatePosition());
        moveTimer.start();

        // Start the following mechanism
        followTimer = new Timer(15, e-> moveFollowing());
        followTimer.start();

        // Timer for game clock
        clockTimer = new Timer(1000, e -> {
            elapsedTime++; // increment the elapsed time every second

            // Increase the hunter speed every 10 seconds
            if (elapsedTime % 10 == 0){
                followSpeed += 0.7;
            }

            repaint(); // Repaint to update the displayed
        });
        clockTimer.start();
    }

    //Update the position of the main rectangle based on active keys
    private void updatePosition(){
        if (activeKey.contains(KeyEvent.VK_W) || activeKey.contains(KeyEvent.VK_UP)){
            if (rectY > 0) {
                rectY -= MOVE_DISTANCE;
            }
        }
        if (activeKey.contains(KeyEvent.VK_S) || activeKey.contains(KeyEvent.VK_DOWN)) {
            if (rectY + RECT_HEIGHT < getHeight()) {
                rectY += MOVE_DISTANCE;
            }
        }
        if (activeKey.contains(KeyEvent.VK_A) || activeKey.contains(KeyEvent.VK_LEFT)){
            if (rectX > 0) {
                rectX -= MOVE_DISTANCE;
            }
        }
        if (activeKey.contains(KeyEvent.VK_D) || activeKey.contains(KeyEvent.VK_RIGHT)) {
            if (rectX + RECT_WIDTH < getWidth()) {
                rectX += MOVE_DISTANCE;
            }
        }

        // Ensure the rectangle stays within bounds
        //if (rectX < 0) rectX = 0;
        //if (rectY < 0) rectY = 0;
        //if (rectX + RECT_WIDTH > getWidth()) rectX = getWidth() - RECT_WIDTH;
        //if (rectY + RECT_HEIGHT > getHeight()) rectY = getHeight() - RECT_HEIGHT;

        // Check for circle collision
        if (checkCircleCollision()) {
            score++;
            spawnNewCircle();
        }

        repaint();
    }

    private void spawnNewCircle() {
        // Generate random position for the circle within the panel
        if (getWidth() > CIRCLE_DIAMETER && getHeight() > CIRCLE_DIAMETER){
            circleX = random.nextInt(getWidth() - CIRCLE_DIAMETER);
            circleY = random.nextInt(getHeight() - CIRCLE_DIAMETER);
        }
    }

    private boolean checkCircleCollision(){
        int circleCenterX = circleX + CIRCLE_DIAMETER / 2;
        int circleCenterY = circleY + CIRCLE_DIAMETER / 2;

        int rectCenterX = rectX + CIRCLE_DIAMETER / 2;
        int rectCenterY = rectY + CIRCLE_DIAMETER / 2;

        double distance = Math.sqrt(Math.pow(circleCenterX- rectCenterX, 2) + Math.pow(circleCenterY - rectCenterY, 2));
        boolean isColliding = distance < (CIRCLE_DIAMETER / 2 + Math.min(RECT_WIDTH, RECT_HEIGHT) / 2);

        if (isColliding) {
            //score++;
            spawnNewCircle();

            // Decrease the hunter's speed by 0.1, but ensure it doesn't go below a minimum threshold (e.g., 0.5)
            followSpeed = Math.max(0.5, followSpeed - 0.1);
        }
        return isColliding;
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

        // Check for collision
        if (checkRectangleCollision()) {
            endGame();
        }

        // Repaint the panel to update positions
        repaint();
    }

    private boolean checkRectangleCollision(){
        return rectX < followRectX + RECT_WIDTH &&
                rectX + RECT_WIDTH > followRectX &&
                rectY < followRectY + RECT_HEIGHT &&
                rectY + RECT_HEIGHT > followRectY;
    }

    private void endGame() {
        moveTimer.stop();
        followTimer.stop();
        clockTimer.stop();
        JOptionPane.showMessageDialog(this, "Game Over! The hunter caught you.");
        System.exit(0);
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

        // Draw the circle
        g.setColor(Color.GREEN);
        g.fillOval(circleX, circleY, CIRCLE_DIAMETER, CIRCLE_DIAMETER);

        // Display the elapsed time
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.drawString("Time: " + elapsedTime + "s", 10,20);
        g.drawString("Hunter speed: " + String.format("%.1f", followSpeed), 10, 40);
        g.drawString("Score: " + score, 10, 60);

    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Not in use
    }

    @Override
    public void keyPressed(KeyEvent e) {
        activeKey.add(e.getKeyCode());
        repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        activeKey.remove(e.getKeyCode());
    }
}
