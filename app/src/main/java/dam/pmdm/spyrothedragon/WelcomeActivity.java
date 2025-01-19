package dam.pmdm.spyrothedragon;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import dam.pmdm.spyrothedragon.databinding.ActivityWelcomeBinding;
import dam.pmdm.spyrothedragon.services.MediaPlayerService;

public class WelcomeActivity extends AppCompatActivity {

    AnimationDrawable fireAnimationLeft;
    AnimationDrawable fireAnimationRight;
    AnimationDrawable ovalAnimation;

    @Override
    protected void onDestroy() {
        binding.welcomeSurfaceView.setLetRun(false);
        Log.d("DESTRUIDA WELCOME A", "");
        super.onDestroy();


    }



    @Override
    protected void onPause() {
        Log.d("PAUSA WELCOME A", "");

        binding.welcomeSurfaceView.setLetRun(false);
        super.onPause();


    }

    @Override
    protected void onStop() {
        binding.welcomeSurfaceView.setLetRun(false);
        Log.d("STOP WELCOME A", "");

        super.onStop();


    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        view = new WelcomeSurfaceView(this);
        binding = ActivityWelcomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        ImageView fireImageLeft = binding.fireImageLeft;
        ImageView fireImageRight = binding.fireImageRight;
        fireImageLeft.setBackgroundResource(R.drawable.fire);
        fireImageRight.setBackgroundResource(R.drawable.fire);
        fireAnimationLeft = (AnimationDrawable) fireImageLeft.getBackground();
        fireAnimationRight = (AnimationDrawable) fireImageRight.getBackground();

        TextView textView = binding.welcomeText;
        textView.setText(R.string.welcome_text);

        ImageView imageOval = binding.oval;
        imageOval.setBackgroundResource(R.drawable.oval_glow);
        ovalAnimation = (AnimationDrawable) imageOval.getBackground();
        imageOval.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.spy_welcome, null));

        mps = new Intent(this, MediaPlayerService.class);
        startService(mps);
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//                mp = MediaPlayer.create(WelcomeActivity.this, R.raw.welcome_activity_music);
//                mp.setOnErrorListener(new MediaPlayer.OnErrorListener() {
//                    @Override
//                    public boolean onError(MediaPlayer mp, int what, int extra) {
//
//                        Log.d("ERROR EN MEDIAPLAYER", extra+"");
//                        return true;
//                    }
//                });
//
//                mp.start();
//            }
//        }).start();






        binding.btnWelcome.setOnClickListener(v-> {
            binding.welcomeSurfaceView.setLetRun(false);
            goToGuide();
        });





    }


    @Override
    protected void onStart() {
        super.onStart();
        fireAnimationLeft.start();
        fireAnimationRight.start();
        ovalAnimation.start();

    }


    private void goToGuide(){
        Intent i = new Intent(this, GuideActivity.class);
//        startActivity(i);
        startActivity(i, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        finish();

    }

//    public static int getScreenWidth(@NonNull Activity activity) {
//            WindowMetrics windowMetrics = activity.getWindowManager().getCurrentWindowMetrics();
//            Insets insets = windowMetrics.getWindowInsets()
//                    .getInsetsIgnoringVisibility(WindowInsets.Type.systemBars());
//            return windowMetrics.getBounds().width() - insets.left - insets.right;
//    }


    private void stopMediaPlayer(){

        Handler handler = new Handler(Looper.getMainLooper());
        AudioManager am = (AudioManager) getSystemService(AUDIO_SERVICE);
        int volume_level= am.getStreamVolume(AudioManager.STREAM_MUSIC);
        Log.d("VOLUMEN",volume_level+"");


    }

    ActivityWelcomeBinding binding;
//    private WelcomeSurfaceView view;
    MediaPlayer mp;
    Intent mps;

}
