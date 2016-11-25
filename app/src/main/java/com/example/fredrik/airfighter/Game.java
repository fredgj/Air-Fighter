package com.example.fredrik.airfighter;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class Game extends AppCompatActivity {

    GamePanel panel;

    private boolean mIsBound = false;
    private MusicService mServ;
    private ServiceConnection Scon = new ServiceConnection(){

        public void onServiceConnected(ComponentName name, IBinder binder) {
            System.out.println("---> MSERV CREATED");
            mServ = ((MusicService.ServiceBinder) binder).getService();
        }

        public void onServiceDisconnected(ComponentName name) {
            mServ = null;
        }
    };

    void doBindService(){
        mServ.bindService(new Intent(this,MusicService.class),
                Scon, Context.BIND_AUTO_CREATE);
        mIsBound = true;
    }

    void doUnbindService()
    {
        if(mIsBound)
        {
            mServ.unbindService(Scon);
            mIsBound = false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        System.out.println("Actionbar is now! " + getActionBar());
        panel = new GamePanel(this);
        setContentView(panel);

        /*doBindService();
        Intent music = new Intent();
        music.setClass(this, MusicService.class);
        startService(music);
        */
    }

    @Override
    protected void onPause() {
        super.onPause();
        panel.getThread().onPause();
        //mServ.pauseMusic();
    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("--> Resuming");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.out.println("---> Destroying");
    }
}
