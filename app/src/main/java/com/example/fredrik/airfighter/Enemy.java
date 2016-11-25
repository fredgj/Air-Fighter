package com.example.fredrik.airfighter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by fredrik on 11.09.16.
 */
public class Enemy extends GameObject {

    private Bitmap image;
    private Animation animation;
    private Random random;
    private int speed;
    private int health;
    private long startTime;
    private GamePanel panel;

    public Enemy(Bitmap image, int x, int y, int width, int height, int speed, GamePanel panel) {
        super(x, y, 0,0, width, height);
        this.image = image;
        this.speed = speed;  //+ random.nextInt(20);
        this.panel = panel;
        health = 3;
        startTime = System.nanoTime();
        addEnemyProjectile();

    }

    public void update() {
        long time = System.nanoTime();
        time = (time-startTime)/1000000;
        if (time > 2000 && (y+height) > panel.getPlayer().getY()*1.5) {
            addEnemyProjectile();
            startTime = System.nanoTime();
        }

        y += speed;
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(image, x, y, null);
    }

    private void addEnemyProjectile() {
        EnemyProjectile ep = new EnemyProjectile(BitmapFactory.decodeResource(panel.getResources(), R.drawable.enemy_projectile),
                                x+(width/2), y+(height-10), 16, 42, panel.getPlayer());
        panel.getEnemyProjectiles().add(ep);
    }

    public int getSpeed() {
        return speed;
    }

    public void hit() {
        health--;
    }

    public boolean isAlive() {
        return health > 0;
    }

}
