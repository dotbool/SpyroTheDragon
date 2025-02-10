package dam.pmdm.spyrothedragon.services;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import dam.pmdm.spyrothedragon.R;

/**
 * Este servicio se creó para explorar los servicios de android. En realidad, según la
 * documentación oficial, con un subproceso sería suficiente, esto es, crear un hilo que
 * reproduzca el sonido. La documentación oficial expone: "Si debes realizar tareas fuera
 * de tu subproceso principal, pero solo mientras el usuario interactúa con tu aplicación,
 * debes crear un subproceso nuevo en el contexto de otro componente de la aplicación.
 * Por ejemplo, si quieres reproducir música, pero solo mientras se ejecuta tu actividad,
 * puedes crear un subproceso en onCreate(), comenzar a ejecutarlo en onStart() y detenerlo
 * en onStop()".
 *
 * En cualquier caso, esta clase es la encargaaa de reproducir el sonido de fondo que acompaña
 * la guía
 */
public class MediaPlayerService extends Service implements DefaultLifecycleObserver {

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
        }
        mp.release();
        mp=null;
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

//    public class LocalBinder extends Binder {
//        public MediaPlayerService getService() {
//            // Return this instance of LocalService so clients can call public methods.
//            return MediaPlayerService.this;
//        }
//    }
//
//    public void play(){
//      mp.start();
//    }
//
//    public void stop(){
//        mp.stop();
//    }
//
//
//    private final IBinder binder = new LocalBinder();
    MediaPlayer mp;
}