package org.example.Entities;

import java.awt.*;

public class RectangleEntity {

    private int x;
    private int y;
    private final int width;
    private final int height;
    private Color color;

    public RectangleEntity(int width, int height, int y, int x, Color color) {
        this.width = width;
        this.height = height;
        this.y = y;
        this.x = x;
        this.color = color;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public boolean collidesWith(RectangleEntity other){
        return this.x < other.x + other.width &&
                this.x + this.width > other.x &&
                this.y < other.y + other.height &&
                this.y + this.height > other.y;
    }

    //Draw the rectangle
    public void draw(Graphics g) {
        g.setColor(color);
        g.fillRect(x,y, width, height);
    }
}
