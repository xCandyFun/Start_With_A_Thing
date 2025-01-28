package org.example.Entities;

import java.awt.*;

public class Wall {
    private int x, y, width, height;
    private int dx, dy;
    private int speed;

    public Wall(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.dx = 0;
        this.dy = 0;
        this.speed = 0;
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

    public void setMovement(int dx, int dy, int speed) {
        this.dx = dx;
        this.dy = dy;
        this.speed = speed;
    }

    public void move() {
        x += dx * speed;
        y += dy * speed;

        // Optional: keep walls within game bounds (bounce off edges)
        if (x < 0 || x + width < 1000) {
            dx *= -1;
        }
        if (y < 0 || y + height > 800) {
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

    // Draw the Walls
    public void draw(Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(x, y, width, height);
    }
}
