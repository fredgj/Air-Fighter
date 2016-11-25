package com.example.fredrik.airfighter;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by fredrik on 12.09.16.
 */
public class Projectile extends GameObject {

    Animation animation;

    public Projectile(Bitmap spritesheet, int x, int y, int dy, int width, int height, int numFrames) {
        super(x, y, 0, dy, width, height);
        animation = new Animation();
        Bitmap image[] = new Bitmap[numFrames];

        for (int i = 0; i < image.length; i++) {
            image[i] = Bitmap.createBitmap(spritesheet, 0, i*height, width, height);
        }

        animation.setFrames(image);
        animation.setDelay(150);


    }

    public void update()  {
        this.y -= dy;
        animation.update();
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(animation.getImage(), x, y, null);
    }
}
