package dam.pmdm.spyrothedragon.services;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import dam.pmdm.spyrothedragon.R;

public class MediaPlayerService extends Service {
    public MediaPlayerService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mp = MediaPlayer.create(this, R.raw.welcome_activity_music);
        mp.setLooping(true);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mp.start();
        return START_STICKY;

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mp.isPlaying()){
            mp.stop();
            mp.release();
            mp=null;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    MediaPlayer mp;
}