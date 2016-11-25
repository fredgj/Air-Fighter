package com.example.fredrik.airfighter;

import android.graphics.Bitmap;

/**
 * Created by fredrik on 11.09.16.
 */
public class Animation {

    private Bitmap frames[];
    private int currentFrame;
    private long startTime;
    private long delay;
    private boolean animatedOnce; // Animation we only want to be played once


    public void update() {
        long time = (System.nanoTime() - startTime) / 1000000;
        if (time > delay) {
            currentFrame++;
            startTime = System.nanoTime();
        }

        if (currentFrame == frames.length) {
            currentFrame = 0;
            animatedOnce = true;
        }
    }

    public void setFrames(Bitmap frames[]) {
        this.frames = frames;
        currentFrame = 0;
        startTime = System.nanoTime();
    }

    public void setFrame(int i) {
        currentFrame = i;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public Bitmap getImage() {
        return frames[currentFrame];
    }

    public int getFrame() {
        return currentFrame;
    }

    public boolean animatedOnce() {
        return animatedOnce;
    }
}
