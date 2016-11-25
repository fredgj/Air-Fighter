package com.example.fredrik.airfighter;

import android.content.Context;
import android.media.MediaPlayer;

/**
 * Created by fredrik on 13.09.16.
 */
public class SoundEffect {
    Thread t;

    public SoundEffect(Context context, int soundId) {
        final MediaPlayer mp = MediaPlayer.create(context, soundId);
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.reset();
                mp.release();
                mp = null;
                try {
                    t.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        });

        t = new Thread(new Runnable() {
            @Override
            public void run() {
                mp.start();
            }
        });

    }

    public void play() {
        t.start();
    }
}
