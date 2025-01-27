package org.example.TheMainGame;

import org.example.GameLogic.GameRenderer;
import org.example.GameLogic.HighScoreEntry;
import org.example.GameLogic.HighScoreTable;
import org.example.entities.Wall;
import org.example.entities.RectangleEntity;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class TheMainGameLogic extends JPanel implements KeyListener {

    private final GameRenderer renderer = new GameRenderer();
    private final HighScoreTable highScoreTable = new HighScoreTable("highscores.txt");
    private RectangleEntity playerRectangle;
    private RectangleEntity hunterRectangle;


    // Main rectangle start position
    private int rectX = 330;
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

    private final int CIRCLE_DIAMETER = 30;
    private int circleX, circleY;
    private int score = 0;

    private final Set<Integer> activeKey = new HashSet<>();
    private Timer moveTimer;
    private Timer followTimer;
    private Timer clockTimer;

    private int elapsedTime = 0;
    private final Random random = new Random();

    private final List<Wall> walls = new ArrayList<>();

    public TheMainGameLogic(){
        // Add the KeyListener to the panel
        this.addKeyListener(this);
        this.setFocusable(true); // Ensure the panel can receive focus
        this.requestFocusInWindow();

        playerRectangle = new RectangleEntity(RECT_WIDTH,RECT_HEIGHT, startPositionY, startPositionX, Color.BLUE);
        hunterRectangle = new RectangleEntity(RECT_WIDTH, RECT_HEIGHT, startFollowPositionY, startFollowPositionX, Color.RED);

        walls.add(new Wall(150,150,400,20));
        walls.add(new Wall(270,220,20,140));
        walls.add(new Wall(230,410,200,20));
        walls.add(new Wall(120,250,20,240));
        walls.add(new Wall(500,220,20,380));
        walls.add(new Wall(600,220,300,20));
        walls.add(new Wall(750,320,20,350));

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

        int newX = playerRectangle.getX();
        int newY = playerRectangle.getY();


        if (activeKey.contains(KeyEvent.VK_W) || activeKey.contains(KeyEvent.VK_UP)){
            if (newY > 0) {
                newY -= MOVE_DISTANCE;
            }
        }
        if (activeKey.contains(KeyEvent.VK_S) || activeKey.contains(KeyEvent.VK_DOWN)) {
            if (newY + playerRectangle.getHeight() < getHeight()) {
                newY += MOVE_DISTANCE;
            }
        }
        if (activeKey.contains(KeyEvent.VK_A) || activeKey.contains(KeyEvent.VK_LEFT)){
            if (newX > 0) {
                newX -= MOVE_DISTANCE;
            }
        }
        if (activeKey.contains(KeyEvent.VK_D) || activeKey.contains(KeyEvent.VK_RIGHT)) {
            if (newX + playerRectangle.getWidth() < getWidth()) {
                newX += MOVE_DISTANCE;
            }
        }

        boolean collidesHorizontally = false;
        boolean collidesVertically = false;
        //boolean canMoveHorizontally = true;
        //boolean canMoveVertically = true;

        // Check wall collisions
        for (Wall wall : walls) {
            if (wall.collidesHorizontally(newX, playerRectangle.getY(), playerRectangle.getWidth(), playerRectangle.getHeight())) {
                collidesHorizontally = true;
            }
            if (wall.collidesVertically(playerRectangle.getX(), newY, playerRectangle.getWidth(), playerRectangle.getHeight())) {
                collidesVertically = true;
            }
        }

        // Update position if no collision
        if (!collidesHorizontally) {
            playerRectangle.setX(newX);
        }
        if (!collidesVertically) {
            playerRectangle.setY(newY);
        }

        // Check for circle collision
        if (checkCircleCollision()) {
            score++;
            spawnNewCircle();
        }

        repaint();
    }

    private void spawnNewCircle() {

        boolean isInWall;

        do {
            // Generate random position for the circle within the panel
            if (getWidth() > CIRCLE_DIAMETER && getHeight() > CIRCLE_DIAMETER) {
                circleX = random.nextInt(getWidth() - CIRCLE_DIAMETER);
                circleY = random.nextInt(getHeight() - CIRCLE_DIAMETER);
            }

            // Check if the circle collides with any wall
            isInWall = false;
            for (Wall wall : walls) {
                if (wall.collidesWith(circleX, circleY, CIRCLE_DIAMETER, CIRCLE_DIAMETER)){
                    isInWall = true;
                    break;
                }
            }
        } while (isInWall);
    }

    private boolean checkCircleCollision(){
        int circleCenterX = circleX + CIRCLE_DIAMETER / 2;
        int circleCenterY = circleY + CIRCLE_DIAMETER / 2;

        int rectCenterX = playerRectangle.getX() + CIRCLE_DIAMETER / 2;
        int rectCenterY = playerRectangle.getY() + CIRCLE_DIAMETER / 2;

        double distance = Math.sqrt(Math.pow(circleCenterX- rectCenterX, 2) + Math.pow(circleCenterY - rectCenterY, 2));
        boolean isColliding = distance < (CIRCLE_DIAMETER / 2 + Math.min(playerRectangle.getWidth(), playerRectangle.getHeight()) / 2);

        if (isColliding) {
            spawnNewCircle();

            // Decrease the hunter's speed by 0.1, but ensure it doesn't go below a minimum threshold (e.g., 0.5)
            followSpeed = Math.max(0.5, followSpeed - 0.3);
        }
        return isColliding;
    }

    // Move the hunter rectangle towards the main rectangle
    private void moveFollowing() {

        // Calculate the difference
        int dx = playerRectangle.getX() - hunterRectangle.getX();
        int dy = playerRectangle.getY() - hunterRectangle.getY();

        // Normalize the direction and move the follow rectangle
        double distance = Math.sqrt(dx * dx + dy * dy);

        int newX = hunterRectangle.getX();
        int newY = hunterRectangle.getY();

        if (distance > 0) {
            newX += (int) Math.round(followSpeed * (dx / distance));
            newY += (int) Math.round(followSpeed * (dy / distance));

        }

        // Making wall slippery
        boolean collidesHorizontally = false;
        boolean collidesVertically = false;
        //boolean canMovehorizontally = true;
        //boolean canMovevertically = true;

        // Check wall collisions
        for (Wall wall : walls){
            if (wall.collidesHorizontally(newX, hunterRectangle.getY(), hunterRectangle.getWidth(), hunterRectangle.getHeight())) {
                collidesHorizontally = true;
            }
            if (wall.collidesVertically(hunterRectangle.getX(), newY, hunterRectangle.getWidth(), hunterRectangle.getHeight())) {
                collidesVertically = true;
            }
        }

        //update position if no collision
        if (!collidesHorizontally) {
            hunterRectangle.setX(newX);
        }
        if (!collidesVertically) {
            hunterRectangle.setY(newY);
        }

        // Check for collision for player
        if (hunterRectangle.collidesWith(playerRectangle)) {
            endGame();
        }

        // Repaint the panel to update positions
        repaint();
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

        playerRectangle.draw(g);
        hunterRectangle.draw(g);
        renderer.drawCircle(g, circleX, circleY, CIRCLE_DIAMETER);
        renderer.drawWall(g, walls);
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
