package org.example.TheMainGame;

import org.example.GameLogic.GameRenderer;
import org.example.GameLogic.HighScoreEntry;
import org.example.GameLogic.HighScoreTable;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.*;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class TheMainGameLogic extends JPanel implements KeyListener {

    private final GameRenderer renderer = new GameRenderer();
    private final HighScoreTable highScoreTable = new HighScoreTable("highscores.txt");


    // Main rectangle start position
    private int rectX = 280;
    private int rectY = 330;

    private int startPositionX = rectX;
    private int startPositionY = rectY;

    // Main rectangle size
    private final int RECT_WIDTH = 30;
    private final int RECT_HEIGHT = 30;

    // Main rectangle movement speed
    private final int MOVE_DISTANCE = 10;

    // The following rectangle start position
    private int followRectX = 30;
    private int followRectY = 50;

    private int startFollowPositionX = followRectX;
    private int startFollowPositionY = followRectY;

    // The following rectangle start speed
    private double followSpeed = 1;

    private double startFollowSpeed = followSpeed;

    private final int CIRCLE_DIAMETER = 20;
    private int circleX, circleY;
    private int score = 0;

    // Wall properties
    private final int Wall_X = 150;
    private final int Wall_Y = 150;
    private final int Wall_WIDTH = 100;
    private final int Wall_HEIGHT = 20;

    // Second wall properties
    private final int Wall2_X = 270;
    private final int Wall2_Y = 220;
    private final int Wall2_WIDTH = 20;
    private final int Wall2_HEIGHT = 100;

    private final Set<Integer> activeKey = new HashSet<>();
    private Timer moveTimer;
    private Timer followTimer;
    private Timer clockTimer;

    private int elapsedTime = 0;
    private final Random random = new Random();

    public TheMainGameLogic(){
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
                followSpeed += 1;
            }

            repaint(); // Repaint to update the displayed
        });
        clockTimer.start();
    }

    //Update the position of the main rectangle based on active keys
    private void updatePosition(){
        int newX = rectX;
        int newY = rectY;

        if (activeKey.contains(KeyEvent.VK_W) || activeKey.contains(KeyEvent.VK_UP)){
            if (rectY > 0) {
                newY -= MOVE_DISTANCE;
            }
        }
        if (activeKey.contains(KeyEvent.VK_S) || activeKey.contains(KeyEvent.VK_DOWN)) {
            if (rectY + RECT_HEIGHT < getHeight()) {
                newY += MOVE_DISTANCE;
            }
        }
        if (activeKey.contains(KeyEvent.VK_A) || activeKey.contains(KeyEvent.VK_LEFT)){
            if (rectX > 0) {
                newX -= MOVE_DISTANCE;
            }
        }
        if (activeKey.contains(KeyEvent.VK_D) || activeKey.contains(KeyEvent.VK_RIGHT)) {
            if (rectX + RECT_WIDTH < getWidth()) {
                newX += MOVE_DISTANCE;
            }
        }


        // Check collision with the wall
        if (!checkWallCollision(newX, newY, RECT_WIDTH, RECT_HEIGHT)) {
            rectX = newX;
            rectY = newY;
        }
        if (checkWallCollision(newX, rectY, RECT_WIDTH, RECT_HEIGHT)) {
            newX = rectX;
        }
        if (checkWallCollision(rectX, newY, RECT_WIDTH, RECT_HEIGHT)) {
            newY = rectY;
        }


        rectX = newX;
        rectY = newY;


        // Check for circle collision
        if (checkCircleCollision()) {
            score++;
            spawnNewCircle();
        }


        repaint();
    }

    private boolean checkWallCollision(int nextX, int nextY, int rectWidth, int rectHeight){
        return (isWallCollision(Wall_X, Wall_Y, Wall_WIDTH, Wall_HEIGHT, nextX, nextY, rectWidth, rectHeight) ||
                isWallCollision(Wall2_X, Wall2_Y, Wall2_WIDTH, Wall2_HEIGHT, nextX, nextY, rectWidth, rectHeight));
    }

    private boolean isWallCollision(int wallX, int wallY, int wallWidth, int wallHeight, int rectX, int rectY, int rectWidth, int rectHeight) {
        return rectX < wallX + wallWidth &&
                rectX + rectWidth > wallX &&
                rectY < wallY + wallHeight &&
                rectY + rectHeight > wallY;
    }

    private void spawnNewCircle() {
        // Ensure the panel size is valid for circle placement
        if (getWidth() > CIRCLE_DIAMETER && getHeight() > CIRCLE_DIAMETER){
            boolean validPosition = false;

            do {
                circleX = random.nextInt(getWidth() - CIRCLE_DIAMETER);
                circleY = random.nextInt(getHeight() - CIRCLE_DIAMETER);

                validPosition = !(isCircleCollidingWithWall(circleX, circleY, CIRCLE_DIAMETER, Wall_X, Wall_Y, Wall_WIDTH, Wall_HEIGHT) ||
                        isCircleCollidingWithWall(circleX, circleY, CIRCLE_DIAMETER, Wall2_X, Wall2_Y, Wall2_WIDTH, Wall2_HEIGHT));
            } while (!validPosition);
        }
    }

    private boolean isCircleCollidingWithWall(int circleX, int circleY, int circleDiameter, int wallX, int wallY, int wallWidth, int wallHeight) {
        int circleRight = circleX + circleDiameter;
        int circleBottom = circleY + circleDiameter;
        return circleX < wallX + wallWidth &&
                circleRight > wallX &&
                circleY < wallY + wallHeight &&
                circleBottom > wallY;
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
        int newFollowX = followRectX;
        int newFollowY = followRectY;

        // Calculate the difference
        int dx = rectX - followRectX;
        int dy = rectY - followRectY;

        // Normalize the direction and move the follow rectangle
        double distance = Math.sqrt(dx * dx + dy * dy);

        if (distance > 0) {
            newFollowX += Math.round(followSpeed * (dx / distance));
            newFollowY += Math.round(followSpeed * (dy / distance));
        }

        //Check collision with the wall
        if (!checkWallCollision(newFollowX, newFollowY, RECT_WIDTH, RECT_HEIGHT)) {
            followRectX = newFollowX;
            followRectY = newFollowY;
        }
        if (checkWallCollision(newFollowX, followRectY, RECT_WIDTH, RECT_HEIGHT)) {
            newFollowX = followRectX;
        }
        if (checkWallCollision(followRectX, newFollowY, RECT_WIDTH, RECT_HEIGHT)) {
            newFollowY = followRectY;
        }


        followRectX = newFollowX;
        followRectY = newFollowY;

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
        String playerName = JOptionPane.showInputDialog(this, "Game over! Enter your name");
        if (playerName != null && !playerName.trim().isEmpty()){
            highScoreTable.addHighScore(playerName, score, elapsedTime);
        }

        showHighScores();

        int option = JOptionPane.showOptionDialog(
                this,
                "Would you like to try again?",
                "Game Over",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                new Object[]{"Yes", "No"},
                "Yes"
        );

        if (option == JOptionPane.YES_OPTION) {
            restartGame();
        }else {
            System.exit(0);
        }
    }

    private void showHighScores() {
        StringBuilder sb = new StringBuilder("High Scores:\n");
        for (HighScoreEntry entry : highScoreTable.getHighScores()) {
            sb.append(entry.getName())
                    .append(": Score ")
                    .append(entry.getScore())
                    .append(", Time ")
                    .append(entry.getElapsedTime())
                    .append("s\n");
        }
        JOptionPane.showMessageDialog(this, sb.toString());
    }

    private void restartGame() {
        // Clear the active keys to stop any ongoing movement
        activeKey.clear();

        // Reset game variables
        score = 0;
        elapsedTime = 0;
        rectX = startPositionX;
        rectY = startPositionY;
        followRectX = startFollowPositionX;
        followRectY = startFollowPositionY;
        followSpeed = startFollowSpeed;

        // Spawn a new circle
        spawnNewCircle();

        // Restart timers
        moveTimer.start();
        followTimer.start();
        clockTimer.start();

        // Repaint the screen
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g); // Call the superclass for proper rendering

        // Set the background color
        this.setBackground(Color.LIGHT_GRAY);

        renderer.drawMainRectangle(g, rectX, rectY, RECT_WIDTH, RECT_HEIGHT);
        renderer.drawFollowingRectangle(g, followRectX, followRectY, RECT_WIDTH, RECT_HEIGHT);
        renderer.drawCircle(g, circleX, circleY, CIRCLE_DIAMETER);
        renderer.drawWall(g, Wall_X, Wall_Y, Wall_WIDTH, Wall_HEIGHT);
        renderer.drawWall(g, Wall2_X, Wall2_Y, Wall2_WIDTH, Wall2_HEIGHT);
        renderer.drawText(g, elapsedTime, followSpeed, score);

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
