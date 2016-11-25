package com.example.fredrik.airfighter;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by fredrik on 11.09.16.
 */
public class GamePanel extends SurfaceView implements SurfaceHolder.Callback {

    public static final int WIDTH = 1000;
    public static final int HEIGHT = 1500;
    public static final int Y_SPEED = 8; // 5
    private MainThread thread;
    private Background bg;
    private Player player;
    private ArrayList<Enemy> enemies;
    private ArrayList<Projectile> projectiles;
    private ArrayList<Explosion> explosions;
    private ArrayList<SoundEffectThread> soundeffectcs;
    private ArrayList<EnemyProjectile> enemyProjectiles;
    private long enemyStartTime, enemyElapsed;
    private Random random;
    private int score;

    SensorManager sm;

    public GamePanel(Context context) {
        super(context);
        this.getHolder().addCallback(this);
        thread = new MainThread(getHolder(), this);

        sm = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        bg = new Background(BitmapFactory.decodeResource(getResources(), R.drawable.background));
        player = new Player(BitmapFactory.decodeResource(getResources(), R.drawable.plane), 230, 152, 3, this);
        enemies = new ArrayList<Enemy>();
        projectiles = new ArrayList<Projectile>();
        explosions = new ArrayList<Explosion>();
        soundeffectcs = new ArrayList<SoundEffectThread>();
        enemyProjectiles = new ArrayList<EnemyProjectile>();
        enemyStartTime = System.nanoTime();
        random = new Random();
        score = 0;

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        System.out.println("-----SURFACE VIEW CREATED-----");
        //bg = new Background(BitmapFactory.decodeResource(getResources(), R.drawable.background));


        // start gameloop
        //thread.setRunning(true);

        if (!thread.isRunning()) {
            System.out.println("THREAD IS NOT RUNNING");
            thread.start();
            thread.setRunning(true);
        } else {
            System.out.println("THREAD IS RUNNING");
            thread.onResume();
        }

        // make gamepanel focusable so it can handle events
        setFocusable(true);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        System.out.println("---- CHANGED ----");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        System.out.println("---- DESTROYED ----");
        boolean retry = true;
        /*while (retry) {
            try {

                thread.join();
            } catch (InterruptedException e) {
            }
            retry = false;

        }*/
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        System.out.println("Shooting");

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            SoundEffectThread se = new SoundEffectThread(getContext(), R.raw.shoot);
            se.start();
            soundeffectcs.add(se);
            Projectile p = new Projectile(BitmapFactory.decodeResource(getResources(),R.drawable.projectile),
                                          player.getX()+(player.getWidth()/2),
                                          player.getY(),
                                          20, 16, 16, 3);
            projectiles.add(p);

            if (score > 0) {
                score--;
            }
            return true;
        }
        return super.onTouchEvent(event);
    }

    public void update() {

        enemyElapsed = (System.nanoTime() - enemyStartTime) / 1000000;
        //System.out.println(enemyElapsed%random.nextInt(10));
        if (enemyElapsed-2000 > 45 && enemies.size() < 7) {
            int high = 17; // 13
            int low = 12; // 8
            int speed = random.nextInt(high-low)+low;
            Enemy enemy = new Enemy(BitmapFactory.decodeResource(getResources(), R.drawable.enemy),
                                    random.nextInt(900), -100, 106, 113, speed, this);


            enemies.add(enemy);

            enemyStartTime = System.nanoTime();
        }

        for (int i = 0; i < enemies.size(); i++) {
            Enemy e = enemies.get(i);
            e.update();
            if (e.getY() > HEIGHT) {
                enemies.remove(i);
                System.out.println("Removed enemy");
            }
        }

        for (int i = 0; i < projectiles.size(); i++) {
            Projectile p = projectiles.get(i);
            p.update();

            if (p.getY() < -20) {
                projectiles.remove(i);
            } else {
                for (int j = 0; j < enemies.size(); j++) {
                    Enemy e = enemies.get(j);
                    if (collision(p, e)) {
                        e.hit();
                        score += 2;
                        Explosion exp;
                        SoundEffectThread se;
                        if (e.isAlive()) {
                            se = new SoundEffectThread(getContext(), R.raw.enemy_hit);
                            exp = new Explosion(BitmapFactory.decodeResource(getResources(), R.drawable.explosion_small),
                                    p.getX()-e.getWidth()/2, e.getY(), 110, 110, 16, e.getSpeed()/1.5, 4);

                        } else {
                            se = new SoundEffectThread(getContext(), R.raw.enemy_explosion);
                            exp = new Explosion(BitmapFactory.decodeResource(getResources(), R.drawable.explosion),
                                                        e.getX()-(e.getWidth()/2), e.getY(), 192, 192, 16, e.getSpeed()/2, 4);
                            enemies.remove(j);
                            score += 3;
                        }
                        se.start();
                        soundeffectcs.add(se);
                        explosions.add(exp);
                        projectiles.remove(i);
                    }
                }
            }
        }

        for (int i = 0; i < explosions.size(); i++) {
            Explosion e = explosions.get(i);
            if (e.isDone()) {
                explosions.remove(i);
            } else {
                e.update();
            }

        }

        for (int i = 0; i < soundeffectcs.size(); i++) {
            SoundEffectThread s = soundeffectcs.get(i);
            if (!s.isRunning()) {
                try {
                    s.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                soundeffectcs.remove(i);
            }
        }

        for (int i = 0; i < enemyProjectiles.size(); i++) {
            EnemyProjectile ep = enemyProjectiles.get(i);
            ep.update();
            if (ep.getY() > getHeight()) {
                enemyProjectiles.remove(ep);
                //System.out.println("Removed Enenmy projectile");
            }
        }



        bg.update();
        player.update();
    }

    private boolean collision(GameObject a, GameObject b) {
        return Rect.intersects(a.getRectangle(), b.getRectangle());
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        final float scaleFactorX = (float) getWidth()/bg.witdth;
        final float scaleFactorY = (float) getHeight()/bg.height;

        if (canvas != null) {
            final int savedState = canvas.save();
            canvas.scale(scaleFactorX, scaleFactorY);
            bg.draw(canvas);
            player.draw(canvas);

            for (Enemy e : enemies) {
                e.draw(canvas);
            }

            for (Projectile p : projectiles) {
                p.draw(canvas);
            }

            for (Explosion e : explosions) {
                e.draw(canvas);
            }

            for (EnemyProjectile ep : enemyProjectiles) {
                ep.draw(canvas);
            }

            drawScore(canvas);

            canvas.restoreToCount(savedState);
        }
    }

    public void drawScore(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(60);
        paint.setTypeface(Typeface.create(Typeface.MONOSPACE, Typeface.BOLD));
        canvas.drawText("Score: " + score, 100, 100, paint);
    }

    public MainThread getThread() {
        return thread;
    }

    public Player getPlayer() {
        return player;
    }

    public ArrayList<EnemyProjectile> getEnemyProjectiles() {
        return enemyProjectiles;
    }
}