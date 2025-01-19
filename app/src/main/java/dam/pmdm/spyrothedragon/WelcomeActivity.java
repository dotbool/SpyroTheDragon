package dam.pmdm.spyrothedragon;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Insets;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.KeyEvent;
import android.view.WindowInsets;
import android.view.WindowMetrics;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import dam.pmdm.spyrothedragon.databinding.ActivityWelcomeBinding;
import dam.pmdm.spyrothedragon.ui.WelcomeSurfaceView;

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


        new Thread(new Runnable() {
            @Override
            public void run() {
//                mp = new MediaPlayer();
//                mp.setAudioAttributes(
//                        new AudioAttributes.Builder()
//                                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
//                                .setUsage(AudioAttributes.USAGE_MEDIA)
//                                .build()
//                );
//
//                FileDescriptor fd = getResources().openRawResourceFd(R.raw.welcome_activity).getFileDescriptor();
//                try {
//                    mp.setDataSource(fd);
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
                mp = MediaPlayer.create(WelcomeActivity.this, R.raw.welcome_activity);
                mp.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                    @Override
                    public boolean onError(MediaPlayer mp, int what, int extra) {

                        Log.d("ERROR EN MEDIAPLAYER", extra+"");
                        return true;
                    }
                });
//        mp.setLooping(true);
//                mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//                    @Override
//                    public void onPrepared(MediaPlayer mp) {
//                        Log.d("PREPARED", mp.toString());
//                        mp.start();
//                    }
//                });

                mp.start();

            }
        }).start();

//        mp = MediaPlayer.create(this,R.raw.welcome_activity);





        binding.btnWelcome.setOnClickListener(v-> {
            binding.welcomeSurfaceView.setLetRun(false);
            goToGuide();
        });






//        binding.getRoot().setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//
//                if (event.getAction() == MotionEvent.ACTION_DOWN) {
//
//                    ImageView iv = new androidx.appcompat.widget.AppCompatImageView(WelcomeActivity.this) {
//                        Paint p = new Paint();
//
//                        @Override
//                        protected void onDraw(@NonNull Canvas canvas) {
//
//                            Log.d("DRAW", "llamando a ion dra");
//                            p.setColor(Color.RED);
//                            canvas.drawCircle(event.getX(), event.getY()-200, 200, p);
//                        }
//
//
//                    };
//
//                    iv.setOnClickListener(vista->{
//                        iv.setVisibility(View.GONE);
//                    });
//
//                    addContentView(iv, new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT));
//
//                }
//                return true;
//            }
//        });
//
//
//
    }


    @Override
    protected void onStart() {
        super.onStart();
        fireAnimationLeft.start();
        fireAnimationRight.start();
        ovalAnimation.start();

    }

    @Override
    protected void onResume() {
        super.onResume();
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


}
