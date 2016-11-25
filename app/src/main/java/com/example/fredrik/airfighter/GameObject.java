package com.example.fredrik.airfighter;

import android.graphics.Rect;

/**
 * Created by fredrik on 11.09.16.
 */
public abstract class GameObject {

    protected int x, y, dx, dy, width, height;

    public GameObject(int x, int y, int dx, int dy, int width, int height) {
        this.x = x;
        this.y = y;
        this.dx = dx;
        this.dy = dy;
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

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Rect getRectangle() {
        return new Rect(x, y, x+width, y+height);
    }
}
