package org.example.Entities;

import java.awt.*;

public class Wall {
    private int x, y, width, height;
    private int dx, dy;
    private int minX, maxX, minY, maxY;
    private int speed;

    public Wall(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.dx = 0;
        this.dy = 0;
        this.speed = 0;

        this.minX = x;
        this.maxX = x;
        this.minY = y;
        this.maxY = y;
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
//    public boolean collidesWith(int rectX, int rectY, int rectWidth, int rectHeight) {
//        return rectX < x + width && rectX + rectWidth > x && rectY < y + height && rectY + rectHeight > y;
//    }

    public boolean collidesWith(int rectX, int rectY, int rectWidth, int rectHeight) {
        Rectangle playerBounds = new Rectangle(rectX, rectY, rectWidth, rectHeight);
        return this.getBounds().intersects(playerBounds);
    }

    // Walls be slippery
    public boolean collidesHorizontally(int nextX, int currentY, int rectWidth, int rectHeight) {
        return nextX < x + width && nextX + rectWidth > x && currentY < y + height && currentY + rectHeight > y;
    }

    public boolean collidesVertically(int currentX, int nextY, int rectWidth, int rectHeight) {
        return currentX < x + width && currentX + rectWidth > x && nextY < y + height && nextY + rectHeight > y;
    }

    public void setMovement(int dx, int dy, int speed, int minX, int maxX, int minY, int maxY) {
        this.dx = dx;
        this.dy = dy;
        this.speed = speed;

        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;
    }

    public void move() {
        x += dx * speed;
        y += dy * speed;

        // Optional: keep walls within game bounds (bounce off edges)
//        if (x < 0 || x + width > 1000) {
//            dx *= -1;
//        }
//        if (y < 0 || y + height > 800) {
//            dy *= -1;
//        }
        if (x <= minX || x + width >= maxX) {
            dx *= -1;
        }
        if (y <= minY || y + height >= maxY) {
            dy *= -1;
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public boolean collidesWithTest(int px,int py, int pWidth, int pHeight) {
        Rectangle wallBounds = getBounds();
        Rectangle playerBounds = new Rectangle(px, py, pWidth, pHeight);
        return wallBounds.intersects(playerBounds);
    }

    public boolean isMoving() {
        return dx != 0 || dy != 0; // If dx or dy is not zero, the wall moves
    }

    // Draw the Walls
    public void draw(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(x, y, width, height);
    }
}
