package dam.pmdm.spyrothedragon;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.res.ResourcesCompat;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import java.util.Objects;

import dam.pmdm.spyrothedragon.databinding.ActivityGuideBinding;
import dam.pmdm.spyrothedragon.ui.CharactersIconDrawable;
import dam.pmdm.spyrothedragon.ui.CollectiblesIconDrawable;
import dam.pmdm.spyrothedragon.ui.WorldIconDrawable;

public class GuideActivity extends AppCompatActivity implements Animator.AnimatorListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGuideBinding.inflate(getLayoutInflater());
        binding.guideLayout.bringToFront();
        setContentView(binding.getRoot());



        NavHostFragment navHostFragment =
                (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_guide);
        if(navHostFragment!=null) {
            navController = navHostFragment.getNavController();
        }


        /*Animación bocadillo que informa sobre el icono de personajes
        * Se crea un drawable (esta fue la primera vez y a partir de las
        siguientes, se usa un xml para drawables sencillos (shapes))
        * Se trae al frente la vista que será dibujada con el drawable
        * Se establece el background de la vista con el drawable creado
        */
        binding.charactersBubbleDrawable.bringToFront();
        ObjectAnimator goAnimator = ObjectAnimator.ofFloat(binding.btnGo, "scaleX",  1f,.8f, 1f);
        goAnimator.setDuration(2000);
        goAnimator.setRepeatCount(3);
        goAnimator.setRepeatMode(ValueAnimator.RESTART);
        goAnimator.start();

        bubbleAnimatorInSet = (AnimatorSet) AnimatorInflater.loadAnimator(
                this, R.animator.bubble_in_animator);
        bubbleAnimatorOutSet =(AnimatorSet) AnimatorInflater.loadAnimator(
                this, R.animator.bubble_out_animator);
        iconAnimatorOutSet = (AnimatorSet) AnimatorInflater.loadAnimator(
                this, R.animator.icon_animator_out);
        iconAnimatorInSet = (AnimatorSet) AnimatorInflater.loadAnimator(
                this, R.animator.icon_animator_in);

        CharactersIconDrawable cid = new CharactersIconDrawable(this); //creamos un drawable
        binding.charactersIconDrawable.bringToFront();
        binding.charactersIconDrawable.setBackground(cid); //establecemos el backgroud de la vista con
        //el drawable

        //instanciamos el drawable del icono world
        WorldIconDrawable wid= new WorldIconDrawable(this);
        binding.worldIconWithDrawable.bringToFront();
        binding.worldIconWithDrawable.setBackground(wid);

        CollectiblesIconDrawable collectiblesIconDrawable = new CollectiblesIconDrawable(this);
        binding.collectiblesIconDrawable.bringToFront();
        binding.collectiblesIconDrawable.setBackground(collectiblesIconDrawable);

        SoundPool soundPool = new SoundPool.Builder().setMaxStreams(1).build();
        int soundId = soundPool.load(this, R.raw.muelle, 1);



        binding.btnGo.setOnClickListener(v -> {

                    if (navController.getCurrentDestination() != null) {

                        String label = (String) navController.getCurrentDestination().getLabel();
                        int destination = 0;

                        switch (Objects.requireNonNull(label)) {

                            case "Personajes":
                                destination = R.id.action_navigation_characters_to_navigation_worlds;
                                //Al navegar al fragmento worlds:
                                // empieza animación de salida del bubble de personajes
                                binding.guideLayout.invalidate();

                                bubbleAnimatorOutSet.setTarget(binding.charactersBubbleDrawable);
                                bubbleAnimatorOutSet.addListener(this);
                                bubbleAnimatorOutSet.setStartDelay(400);
                                bubbleAnimatorOutSet.start();
                                //empieza animación de salida del icono de personajes
                                iconAnimatorOutSet.setTarget(binding.charactersIconDrawable);
                                iconAnimatorOutSet.start();

                                //Al navegar a worlds empieza animación de entrada de bubble de worlds
                                bubbleAnimatorInSet.setTarget(binding.worldBubble);
                                bubbleAnimatorInSet.addListener(this);
                                bubbleAnimatorInSet.setStartDelay(800);
                                bubbleAnimatorInSet.start();

                                //Empieza animación icono world
                                iconAnimatorInSet.setTarget(binding.worldIconWithDrawable);
                                iconAnimatorInSet.start();

                                handler = new Handler(Looper.getMainLooper());
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        soundPool.play(soundId, 1f, 1f, 1, 0, 1f);

                                    }
                                },800);



//                                soundPool.release();
//                                soundPool = null;
                                break;
                            case "Mundos":  //si estamos en el fragmento Mundos navegaremos al Colleccionables
                                destination = R.id.action_navigation_worlds_to_navigation_collectibles;
                                //Animaciones que salen
                                binding.guideLayout.invalidate();

                                iconAnimatorOutSet.setTarget(binding.worldIconWithDrawable);
                                iconAnimatorOutSet.start();
                                bubbleAnimatorOutSet.setTarget(binding.worldBubble);
                                bubbleAnimatorOutSet.start();

                                //Animaciones que entran
                                iconAnimatorInSet.setTarget(binding.collectiblesIconDrawable);
                                iconAnimatorInSet.start();
                                bubbleAnimatorInSet.setTarget(binding.collectibleBubble);
                                bubbleAnimatorInSet.start();

                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        soundPool.play(soundId, 1f, 1f, 1, 0, 1f);

                                    }
                                },800);

                                break;

                            case "Coleccionables":
                                //Vistas que salen
                                if(!isScreen5) {
                                    isScreen5 = true;
                                    binding.guideLayout.invalidate();

                                    bubbleAnimatorOutSet.setTarget(binding.collectibleBubble);
                                    bubbleAnimatorOutSet.start();
                                    iconAnimatorOutSet.setTarget(binding.collectiblesIconDrawable);
                                    iconAnimatorOutSet.start();
                                    //vistas que entran
                                    binding.worldBubble.setText("Icono informativo: información sobre el creador de la app");
                                    bubbleAnimatorInSet.setTarget(binding.worldBubble);
                                    bubbleAnimatorInSet.start();


                                    //aquí se me empezó a ocurrir otra idea para dibujar. En lugar de
                                    //crear una vista para cada línea, decidí dibujar en la vista que
                                    //cubre toda la pantalla. Se le pasan unas coordenadas y dibuja
                                    //una línea. El trabajo que había realizado previamente se dejó tal
                                    //cual
                                    binding.guideLayout.setPointFrom(
                                            new Point(binding.guideLayout.getWidth() * 11 / 12, 0));
                                    binding.guideLayout.setPointTo(
                                            new Point(binding.guideLayout.getWidth() / 2,
                                                    binding.guideLayout.getHeight() / 2));

                                    menuItemView = findViewById(R.id.action_info);
                                    defaultMenuItemBackground = menuItemView.getBackground();
                                    menuItemView.setBackground(ResourcesCompat.getDrawable(getResources(),
                                            R.drawable.circle_pressed, null));

                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            soundPool.play(soundId, 1f, 1f, 1, 0, 1f);

                                        }
                                    },800);

                                }

                                else {
                                    binding.guideLayout.setDrawable(AppCompatResources.getDrawable(this, R.drawable.welcome_background));
                                    menuItemView.setBackground(defaultMenuItemBackground);
                                    binding.btnSkipGuide.setVisibility(View.GONE);
                                    binding.btnGo.setVisibility(View.GONE);
                                    binding.finalSummary.bringToFront();
                                    bubbleAnimatorInSet.setTarget(binding.finalSummary);
                                    bubbleAnimatorInSet.start();
                                    iconAnimatorInSet.setTarget(binding.btnToMain);
                                    iconAnimatorInSet.start();
                                    binding.btnToMain.setEnabled(true);
                                    binding.guideLayout.invalidate();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            soundPool.play(soundId, 1f, 1f, 1, 0, 1f);

                                        }
                                    },800);

                                }


                                break;
                        }
                        if (destination != 0) {

                            navController.navigate(destination);
                        }

                    }
                }
        );

        binding.btnToMain.setOnClickListener(v->{
            soundPool.release();
            goToMain();
            Log.d("YENFO A MAIN", "");

        });

    }

    private void goToMain(){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();

    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.d("ONSTART", "ON START");

    }

    /**
     * Esté método se ha implementado en esta actividad para que el icono del menú
     * sea mostrado
     * @param menu The options menu in which you place your items.
     *
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.about_menu, menu);


        return true;
    }


    @Override
    public void onAnimationStart(@NonNull Animator animation) {

        binding.btnGo.setEnabled(false);
        binding.btnSkipGuide.setEnabled(false);

    }

    @Override
    public void onAnimationEnd(@NonNull Animator animation) {
        binding.btnGo.setEnabled(true);
        binding.btnSkipGuide.setEnabled(true);
        bubbleAnimatorOutSet.removeAllListeners();
    }

    @Override
    public void onAnimationCancel(@NonNull Animator animation) {
        binding.btnGo.setEnabled(true);
        binding.btnSkipGuide.setEnabled(true);
        bubbleAnimatorOutSet.removeAllListeners();
    }

    @Override
    public void onAnimationRepeat(@NonNull Animator animation) {

    }

    NavController navController;
    ActivityGuideBinding binding;
    AnimatorSet bubbleAnimatorInSet;
    AnimatorSet bubbleAnimatorOutSet;
    AnimatorSet iconAnimatorOutSet;
    AnimatorSet iconAnimatorInSet;
    boolean isScreen5;
    View menuItemView;
    Drawable defaultMenuItemBackground;
    Handler handler;


}
