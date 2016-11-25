package com.example.fredrik.airfighter;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Created by fredrik on 11.09.16.
 */
public class Player extends GameObject implements SensorEventListener {

    private Bitmap spritesheet;
    private Animation animation;
    private double dxa;
    private long startTime;
    private GamePanel panel;
    private boolean flippedLeft, flippedRight, middle;
    Bitmap image[];

    int width, height;

    SensorManager sm;
    Sensor accelerometer;


    public Player(Bitmap spritesheet, int width, int height, int numFrames, GamePanel panel) {
        super((GamePanel.WIDTH/2)-(width/2), GamePanel.HEIGHT-height-50, 0, 0, width, height);

        animation = new Animation();
        image = new Bitmap[numFrames]; // frames
        this.spritesheet = spritesheet;
        this.panel = panel;
        this.height = height;
        this.width = width;
        flippedLeft = false;
        flippedRight = false;
        middle = true;

        for (int i = 0; i < image.length; i++) {
            image[i] = Bitmap.createBitmap(spritesheet, 0, i*height, width, height);
        }

        animation.setFrames(image);
        animation.setDelay(150);
        startTime = System.nanoTime();

        sm = panel.sm;
        accelerometer = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sm.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);


    }

    public void update() {
        animation.update();
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(animation.getImage(), x, y, null);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float x = event.values[0];

        if (x <= 0.8f && x >= -0.8f && !middle) {
            spritesheet = BitmapFactory.decodeResource(panel.getResources(), R.drawable.plane);
            //System.out.println("Middle");
            middle = true;
            flippedLeft = false;
            flippedRight = false;

            for (int i = 0; i < image.length; i++) {
                image[i] = Bitmap.createBitmap(spritesheet, 0, i*height, width, height);
            }

            animation.setFrames(image);
        } else if (x > 1.8f && !flippedLeft) {
            //System.out.println("Left");
            flippedLeft = true;
            middle = false;
            spritesheet = BitmapFactory.decodeResource(panel.getResources(), R.drawable.plane_left);
            for (int i = 0; i < image.length; i++) {
                image[i] = Bitmap.createBitmap(spritesheet, 0, i*height, width, height);
            }
            animation.setFrames(image);
        } else if (x < -1.8f && !flippedRight) {
            //System.out.println("Right");
            flippedRight = true;
            middle = false;
            spritesheet = BitmapFactory.decodeResource(panel.getResources(), R.drawable.plane_right);
            for (int i = 0; i < image.length; i++) {
                image[i] = Bitmap.createBitmap(spritesheet, 0, i*height, width, height);
            }

            animation.setFrames(image);

        }

        this.x += 4 * -x;
        if (this.x > (panel.getWidth()-(this.width*3))) {
            this.x = panel.getWidth()-(this.width*3);
        } else if (this.x < 0) {
            this.x = 0;
        }
        //System.out.println("Moved " + x + " - " + y + " - " + z);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
