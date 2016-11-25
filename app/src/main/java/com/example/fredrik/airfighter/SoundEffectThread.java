package com.example.fredrik.airfighter;

import android.content.Context;
import android.media.MediaPlayer;

/**
 * Created by fredrik on 13.09.16.
 */
public class SoundEffectThread extends Thread {

    private Context context;
    private int soundId;
    private boolean running;

    public SoundEffectThread(Context context, int soundId) {
        this.context = context;
        this.soundId = soundId;
        running = true;
    }

    public void run() {
        MediaPlayer mp = MediaPlayer.create(context, soundId);
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.reset();
                mp.release();
                mp=null;
                running = false;
            }

        });
        mp.start();
    }

    public boolean isRunning() {
        return running;
    }
}
