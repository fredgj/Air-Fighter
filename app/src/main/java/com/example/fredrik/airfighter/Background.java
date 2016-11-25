package com.example.fredrik.airfighter;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.provider.Settings;

/**
 * Created by fredrik on 11.09.16.
 */
public class Background {

    private Bitmap image;
    private int x, y, dy;
    int witdth, height;


    public Background(Bitmap image) {
        this.image = image;
        witdth = image.getWidth();
        height = image.getHeight();
        dy = GamePanel.Y_SPEED;
    }

    public void update() {
        //System.out.println("Y " + y);
        y += dy;
        if (y > GamePanel.HEIGHT) {
            y = 0;
        }
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(image, x, y, null);
        canvas.drawBitmap(image, x, y-GamePanel.HEIGHT, null);
    }

}
