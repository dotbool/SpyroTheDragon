package dam.pmdm.spyrothedragon;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import dam.pmdm.spyrothedragon.databinding.ActivityWelcomeBinding;
import dam.pmdm.spyrothedragon.services.MediaPlayerService;
import dam.pmdm.spyrothedragon.ui.WelcomeSurfaceView;

/**
 * Esta clase aloja la pantalla de bienvenida. Lee las preferencias para saber si la
 * guía se completó, en cuyo caso va a MainActivity
 */
public class WelcomeActivity extends AppCompatActivity {


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWelcomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


/*        --------------------------SHARED PREFERENCE---------------------------------

        leemos el shared preferences para saber si la guía ya se realizó.
        El sharedPreferences por defecto guarda en el contexto de la actividad
        por lo que he usado el contexto de la app, ya que se escribe en GuideActivity
        y se lee en WelcomeActivity
 */
        SharedPreferences sp = getApplicationContext()
                .getSharedPreferences(getString(R.string.app_pref),Context.MODE_PRIVATE);
        boolean completed = sp.getBoolean(getString(R.string.guide_completed), false);

//        ----------------- COMENTAR EL SIGUIENTE IF PARA PODER VER LA GUÍA MÁS DE UNA VEZ --------
        if(completed){
            goToMain();
        }


        /*---------------------- WELCOMESURFACEVIEW---------------------------------
        He hecho que el surfaceView escuche el ciclo de vida de la actividad. De esta forma
        se puede gestionar su funcionamiento en el código de la propia clase en lugar
        de en la actividad
         */
        welcomeSurfaceView = binding.welcomeSurfaceView;
        getLifecycle().addObserver(welcomeSurfaceView);



        /* -------------------- ANIMACIONES ---------------------------------

            Animación de las llamas Drawable que flanquean el título
         */
        ImageView fireImageLeft = binding.fireImageLeft;
        ImageView fireImageRight = binding.fireImageRight;
        fireImageLeft.setBackgroundResource(R.drawable.fire);
        fireImageRight.setBackgroundResource(R.drawable.fire);

        fireAnimationLeft = (AnimationDrawable) fireImageLeft.getBackground();
        AnimatorSet setLeft = new AnimatorSet();
        ObjectAnimator animatorY = ObjectAnimator.ofFloat(fireImageLeft, "scaleY", 0, 1);
        animatorY.setDuration(2000);
        ObjectAnimator animatorX = ObjectAnimator.ofFloat(fireImageLeft, "scaleX", 0, 1);
        animatorX.setDuration(2000);
        setLeft.playTogether(animatorY, animatorX);
        setLeft.start();

        fireAnimationRight = (AnimationDrawable) fireImageRight.getBackground();
        AnimatorSet setRight = new AnimatorSet();
        ObjectAnimator animatorYR = ObjectAnimator.ofFloat(fireImageRight, "scaleY", 0, 1);
        animatorYR.setDuration(2000);
        ObjectAnimator animatorXR = ObjectAnimator.ofFloat(fireImageRight, "scaleX", 0, 1);
        animatorXR.setDuration(2000);
        setRight.playTogether(animatorYR, animatorXR);
        setRight.start();



        TextView textView = binding.welcomeText;
        textView.setText(R.string.welcome_text);

        /*
        Animación del fondo amarillo que rodea a Spyro
         */
        ImageView imageOval = binding.oval;
        imageOval.setBackgroundResource(R.drawable.oval_glow);
        ovalAnimation = (AnimationDrawable) imageOval.getBackground();
        imageOval.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.spy_welcome, null));

//        --------------------------------------------------------------------------------

        // Es el intent utilizado para iniciar el servicio musical. Esto es mejor
        //hacerlo con un hilo
        mps = new Intent(this, MediaPlayerService.class);

        binding.btnWelcome.setOnClickListener(v-> {
            goToGuide();
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        super.onStart();
        fireAnimationLeft.start();
        fireAnimationRight.start();
        ovalAnimation.start();
        startService(mps);

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onStop() {
        super.onStop();
        fireAnimationLeft.stop();
        fireAnimationRight.stop();
        ovalAnimation.stop();

    }


    private void goToGuide(){
        welcomeCompleted = true;
        Intent i = new Intent(this, GuideActivity.class);
        startActivity(i, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        finish();

    }

    private void goToMain(){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        finish();

    }


    boolean welcomeCompleted;
    WelcomeSurfaceView welcomeSurfaceView;
    AnimationDrawable fireAnimationLeft;
    AnimationDrawable fireAnimationRight;
    AnimationDrawable ovalAnimation;
    ActivityWelcomeBinding binding;
    Intent mps;

}
