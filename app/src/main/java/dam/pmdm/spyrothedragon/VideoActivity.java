package dam.pmdm.spyrothedragon;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.WindowInsets;
import android.view.WindowInsetsController;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import dam.pmdm.spyrothedragon.databinding.ActivityVideoBinding;

public class VideoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityVideoBinding binding = ActivityVideoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        WindowInsetsController windowInsetsController = getWindow().getInsetsController();
        if(windowInsetsController!=null){
            windowInsetsController.hide(WindowInsets.Type.statusBars());
            windowInsetsController.hide(WindowInsets.Type.systemBars());
        }

        String videoPath = getIntent().getExtras().get("videoPath").toString();
        binding.videoView.setVideoPath(videoPath);
            binding.videoView.start();
            binding.videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    finish();
                }
            });
    }
}
