package com.example.fredrik.airfighter;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Point;

import java.util.Vector;

/**
 * Created by fredrik on 13.09.16.
 */
public class EnemyProjectile extends GameObject {

    private Bitmap image;
    private int toX;
    double dir;
    double _dy, _dx;

    public EnemyProjectile(Bitmap image, int x, int y, int width, int height, Player player) {
        super(x, y, 0, 0, width, height);
        this.image = image;
        toX = player.getX()+(player.getWidth()/2);
        Matrix m = new Matrix();

        float angle = getAngle(new Point(player.getX()+(player.getWidth()/2), player.getY()));
        m.postRotate(angle);
        this.image = Bitmap.createBitmap(image, 0, 0, width, height, m, true);

        _dx = ((player.getX()+(player.getWidth()/2))-x);
        _dy = (player.getY()-y);
        double length = Math.sqrt(_dx*_dx + _dy*_dy);
        _dx /= length;
        _dy /= length;
        _dx *= 20;
        _dy *= 20;

    }


    public void update() {
        y += (int) _dy;
        x += (int) _dx;
    }

    private float getAngle(Point other) {
        double x_distance = other.y - this.y;
        double y_distance = other.x - this.x;

        //double theta = Math.atan2(y_distance, x_distance);
        //theta *= 180.0 / Math.PI;
        //return (float) theta * -1;

        double m = y_distance/x_distance;
        return (float) Math.toDegrees(Math.atan(m))*-1;
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(image, x , y, null);
    }
}
