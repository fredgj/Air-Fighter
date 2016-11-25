package com.example.fredrik.airfighter;

import android.graphics.Canvas;
import android.provider.Settings;
import android.view.SurfaceHolder;

/**
 * Created by fredrik on 11.09.16.
 */
public class MainThread extends Thread {
    private int fps = 30;
    private double averageFps;
    private SurfaceHolder surfaceHolder;
    private GamePanel gamePanel;
    private boolean running;
    public static Canvas canvas;
    private Object pauseLock = new Object();
    boolean isPaused;

    public MainThread(SurfaceHolder surfaceHolder, GamePanel gamePanel) {
        super();
        this.surfaceHolder = surfaceHolder;
        this.gamePanel = gamePanel;
    }

    @Override
    public void run() {
        long startTime;
        long timeMilis;
        long waitTime;
        long totalTime = 0;
        int frameCount = 0;
        long targetTime = 1000/fps;

        while (true) {
            startTime = System.nanoTime();
            canvas =  null;
            try {
                canvas = this.surfaceHolder.lockCanvas();
                synchronized (surfaceHolder) {
                    gamePanel.update();
                    gamePanel.draw(canvas);
                }
            } catch (Exception e) {
            } finally {
                if (canvas != null) {
                    try {
                        surfaceHolder.unlockCanvasAndPost(canvas);
                    } catch (Exception e) {
                    }
                }
            }

            timeMilis = (System.nanoTime() - startTime) / 1000000;
            waitTime = Math.abs(targetTime-timeMilis);

            try {
                this.sleep(waitTime);
            } catch (InterruptedException e) {
            }

            totalTime += System.nanoTime()-startTime;
            frameCount++;

            if (frameCount == fps) {
                averageFps = 1000/((totalTime/frameCount)/1000000);
                frameCount = 0;
                totalTime = 0;
                //System.out.println("average fps " + averageFps);
            }

            synchronized (pauseLock) {
                while (isPaused) {
                    System.out.println("PAUSED");
                    try {
                        pauseLock.wait();
                    } catch (InterruptedException e) {
                    }
                }
            }

        }
    }

    public void onPause() {
        synchronized (pauseLock) {
            System.out.println("GOING TO PAUSE");
            isPaused = true;
        }
    }

    public void onResume() {
        synchronized (pauseLock) {
            System.out.println("WAKING UP");
            isPaused = false;
            pauseLock.notifyAll();
        }
    }

    public void setRunning(boolean b) {
        running = b;
    }

    public boolean isRunning() {
        return running;
    }
}