package com.example.fredrik.airfighter;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by fredrik on 13.09.16.
 */
public class Explosion extends GameObject {

    private Animation animation;
    private boolean done;
    private double speed;

    public Explosion(Bitmap spritesheet, int x, int y, int width, int height, int numFrames, double speed, int rowSize) {
        super(x, y, 0, 0, width, height);
        this.speed = speed;
        animation = new Animation();
        Bitmap image[] = new Bitmap[numFrames];
        done = false;

        int row = 0;
        for (int i = 0; i < numFrames; i++) {
            if (i%rowSize==0 && i>0) {
                row++;
            }
            image[i] = Bitmap.createBitmap(spritesheet, (i-(rowSize*row))*width, row*height, width, height);
        }

        animation.setFrames(image);
        animation.setDelay(70);

    }

    public void update() {
        if (!animation.animatedOnce()) {
            animation.update();
            y += speed;
        } else {
            done = true;
        }
    }

    public void draw(Canvas canvas) {
        if (!animation.animatedOnce()) {
            canvas.drawBitmap(animation.getImage(), x, y, null);
        }
    }

    public boolean isDone() {
        return done;
    }

}
