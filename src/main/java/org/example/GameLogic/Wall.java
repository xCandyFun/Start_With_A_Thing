package org.example.GameLogic;

import java.awt.*;

public class Wall {
    private final int x;
    private final int y;
    private final int width;
    private final int height;

    public Wall(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    //Check if a rectangle collides with walls
    public boolean collidesWith(int rectX, int rectY, int rectWidth, int rectHeight) {
        return rectX < x + width && rectX + rectWidth > x && rectY < y + height && rectY + rectHeight > y;
    }

    // Walls be slippery
    public boolean collidesHorizontally(int nextX, int currentY, int rectWidth, int rectHeight) {
        return nextX < x + width && nextX + rectWidth > x && currentY < y + height && currentY + rectHeight > y;
    }

    public boolean collidesVertically(int currentX, int nextY, int rectWidth, int rectHeight) {
        return currentX < x + width && currentX + rectWidth > x && nextY < y + height && nextY + rectHeight > y;
    }

    // Draw the Walls
    public void draw(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(x, y, width, height);
    }
}
