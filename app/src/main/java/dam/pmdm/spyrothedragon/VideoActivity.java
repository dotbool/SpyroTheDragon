package dam.pmdm.spyrothedragon;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.WindowInsets;
import android.view.WindowInsetsController;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.WindowInsetsControllerCompat;

import dam.pmdm.spyrothedragon.databinding.ActivityVideoBinding;

/**
 * Clase creada para la reproducción del vídeo del easteregg de la gema
 */
public class VideoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityVideoBinding binding = ActivityVideoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        WindowInsetsController windowInsetsController = getWindow().getInsetsController();

        //hacemos que si el usuario toca la pantalla deslizando el dedo por dónde debiera andar
        //la barra del sistema, se muestren
        windowInsetsController.setSystemBarsBehavior(
                WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        );

        //escondems las barras del sistema para ver el vídeo en pantalla completa
        if(windowInsetsController!=null){
            windowInsetsController.hide(WindowInsets.Type.statusBars());
            windowInsetsController.hide(WindowInsets.Type.systemBars());
        }

        Bundle extras = getIntent().getExtras();
        String videoPath="";
        if(extras!=null){
            videoPath = extras.get("videoPath").toString();
        }
        binding.videoView.setVideoPath(videoPath);
            binding.videoView.start();
            binding.videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.release();
                    finish();
                }
            });
    }
}
