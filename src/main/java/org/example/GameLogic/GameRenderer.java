package org.example.GameLogic;

import java.awt.*;

public class GameRenderer {

    public void drawMainRectangle(Graphics g, int x, int y, int width, int height) {
        g.setColor(Color.BLUE);
        g.fillRect(x, y, width, height);
    }

    public void drawFollowingRectangle(Graphics g, int x, int y, int width, int height) {
        g.setColor(Color.RED);
        g.fillRect(x, y, width, height);
    }

    public void drawCircle(Graphics g, int x, int y, int diameter) {
        g.setColor(Color.GREEN);
        g.fillOval(x, y, diameter, diameter);
    }

    public void drawWall(Graphics g, int x, int y, int width, int height) {
        g.setColor(Color.BLACK);
        g.fillRect(x, y, width, height);
    }

    public void drawText(Graphics g, int elapsedTime, double followSpeed, int score) {
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.drawString("Time: " + elapsedTime + "s", 10, 20);
        g.drawString("Hunter speed: " + String.format("%.1f", followSpeed), 10, 40);
        g.drawString("Score: " + score, 10, 60);
    }
}
