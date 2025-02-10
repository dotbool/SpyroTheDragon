package dam.pmdm.spyrothedragon;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.res.ResourcesCompat;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;
import dam.pmdm.spyrothedragon.databinding.ActivityGuideBinding;
import dam.pmdm.spyrothedragon.services.MediaPlayerService;
import dam.pmdm.spyrothedragon.ui.CharactersIconDrawable;
import dam.pmdm.spyrothedragon.ui.CollectiblesIconDrawable;
import dam.pmdm.spyrothedragon.ui.WorldIconDrawable;

/**
 * Clase que aloja la guía. La clase se sustenta en  una view que cubre toda la pantalla.
 * Sobre la vista transparente se dibujan las diferentes animaciones.
 * Cuando se navega, se hace a los fragmentos de la main activity pues ésta actividad aloja
 * un fragmentContainer que apunta al gráfico de navegación de la main Activity
 */
public class GuideActivity extends AppCompatActivity  {


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

        //creamos un timer para demorar el release de el soundpool
        //el handler es para demorar algunas cosas también.
        //el uso de ambas funciones es para aprender
        timer = new Timer();
        handler = new Handler(Looper.getMainLooper());

        /*Animación bocadillo que informa sobre el icono de personajes
        * Se crea un drawable (esta fue la primera vez y a partir de las
        siguientes, se usa un xml para drawables sencillos (shapes))
        * Se trae al frente la vista que será dibujada con el drawable
        * Se establece el background de la vista con el drawable creado
        */
        binding.charactersBubbleDrawable.bringToFront();
        ObjectAnimator goAnimator = ObjectAnimator.ofFloat(binding.btnGo, "scaleX",  1f,.8f, 1f);
        goAnimator.setDuration(500);
        goAnimator.setRepeatCount(8);
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

        //cargamos los sonidos antes de su ejecución para que estén a punto
        soundPool = new SoundPool.Builder().setMaxStreams(1).build();
        soundId = soundPool.load(this, R.raw.muelle, 1);
        soundLevelUp = soundPool.load(this, R.raw.level_up, 1);
        binding.btnSkipGuide.setOnClickListener(v->{
            showBtnSkipGuideDialog();
        });



        //Cuando se pincha el botón avanzar, en función de en que fragmento estemos, se
        // realiza una acción de navegación. Cuando se navega ocurren las animaciones
        binding.btnGo.setOnClickListener(v -> {

                    if (navController.getCurrentDestination() != null) {

                        String label = (String) navController.getCurrentDestination().getLabel();
                        int destination = 0;

                        switch (Objects.requireNonNull(label)) {

                            case "Personajes":
                                destination = R.id.action_navigation_characters_to_navigation_worlds;
                                //Al navegar al fragmento worlds:
                                // empieza animación de salida del bubble de personajes
                                binding.guideLayout.invalidate(); //invalidamos la vista para que se pite de nuevo

                                bubbleAnimatorOutSet.setTarget(binding.charactersBubbleDrawable);
                                bubbleAnimatorOutSet.addListener(bubbleOutListenerAdapter);
                                bubbleAnimatorOutSet.setStartDelay(400);
                                bubbleAnimatorOutSet.start();
                                //empieza animación de salida del icono de personajes
                                iconAnimatorOutSet.setTarget(binding.charactersIconDrawable);
                                iconAnimatorOutSet.start();

                                //Al navegar a worlds empieza animación de entrada de bubble de worlds
                                bubbleAnimatorInSet.setTarget(binding.worldBubble);
                                bubbleAnimatorInSet.addListener(bubbleInListenerAdapter);
                                bubbleAnimatorInSet.setStartDelay(800);
                                bubbleAnimatorInSet.start();

                                //Empieza animación icono world
                                iconAnimatorInSet.setTarget(binding.worldIconWithDrawable);
                                iconAnimatorInSet.start();
                                break;

                            case "Mundos":  //si estamos en el fragmento Mundos navegaremos al Colleccionables
                                destination = R.id.action_navigation_worlds_to_navigation_collectibles;
                                //Animaciones que salen
                                binding.guideLayout.invalidate();

                                iconAnimatorOutSet.setTarget(binding.worldIconWithDrawable);
                                iconAnimatorOutSet.start();
                                bubbleAnimatorOutSet.setTarget(binding.worldBubble);
                                bubbleAnimatorOutSet.addListener(bubbleOutListenerAdapter);
                                bubbleAnimatorOutSet.start();

                                //Animaciones que entran
                                iconAnimatorInSet.setTarget(binding.collectiblesIconDrawable);
                                iconAnimatorInSet.start();
                                bubbleAnimatorInSet.setTarget(binding.collectibleBubble);
                                bubbleAnimatorInSet.addListener(bubbleInListenerAdapter);
                                bubbleAnimatorInSet.start();
                                break;

                                // en el caso de coleccinables, ya no hay más fragmentos, pero
                            // si que hay más animaciones porque hay que explicar el botón del
                            //menú y finalmente el resumen
                            case "Coleccionables":
                                //Vistas que salen
                                if(!isScreen5) {
                                    isScreen5 = true;
                                    binding.guideLayout.invalidate();

                                    bubbleAnimatorOutSet.setTarget(binding.collectibleBubble);
                                    bubbleAnimatorOutSet.addListener(bubbleOutListenerAdapter);
                                    bubbleAnimatorOutSet.start();
                                    iconAnimatorOutSet.setTarget(binding.collectiblesIconDrawable);
                                    iconAnimatorOutSet.start();
                                    //vistas que entran
                                    binding.worldBubble.setText("Icono informativo: información sobre el creador de la app");
                                    bubbleAnimatorInSet.setTarget(binding.worldBubble);
                                    bubbleAnimatorInSet.addListener(bubbleInListenerAdapter);
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


                                }
                                //este es el caso del summary de lo que hemos visto en la guía
                                else {
                                    binding.guideLayout.setDrawable(AppCompatResources.getDrawable(this, R.drawable.welcome_background));
                                    menuItemView.setBackground(defaultMenuItemBackground);
                                    binding.btnSkipGuide.setVisibility(View.GONE);
                                    binding.btnGo.setVisibility(View.GONE);
                                    binding.finalSummary.bringToFront();
                                    bubbleAnimatorInSet.setTarget(binding.finalSummary);
                                    bubbleAnimatorInSet.addListener(bubbleInListenerAdapter);

                                    bubbleAnimatorInSet.start();
                                    iconAnimatorInSet.setTarget(binding.btnToMain);
                                    iconAnimatorInSet.start();
                                    binding.btnToMain.setEnabled(true);
                                    binding.guideLayout.invalidate();
                                }
                                break;
                        }
                        if (destination != 0) {

                            navController.navigate(destination);
                        }

                    }
                }
        );


        /**
         *  Listener en el botón que salta la guía y va a MainActivity
         *  El botón para la el servicio musical
         *  A continuación reproduce un sonido que simboliza la entrada en MainActivity
         *  Liberamos los recursos del sounPool después de 5 segundos para que le de
         *  tiempo al sonido a ser reproducido
         *  Guardamos en el shared preference la consecución de la guía
         */
        binding.btnToMain.setOnClickListener(v->{
            soundPool.play(soundLevelUp,1,1,1,0,1);
            stopService(new Intent(this, MediaPlayerService.class));
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    soundPool.release();
                    handler = null;
                }
            },5000);
            SharedPreferences sp = getApplicationContext()
                    .getSharedPreferences(getString(R.string.app_pref),Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putBoolean(getString(R.string.guide_completed), true);
            editor.apply();
            goToMain();
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
        timer.purge();
        bubbleAnimatorOutSet.removeAllListeners();
        bubbleAnimatorInSet.removeAllListeners();
        iconAnimatorInSet.removeAllListeners();
        iconAnimatorOutSet.removeAllListeners();
    }

    private void goToMain(){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        finish();

    }


    @Override
    protected void onStart() {
        startService(new Intent(this,MediaPlayerService.class));
        super.onStart();

    }

    @Override
    protected void onStop() {
        stopService(new Intent(this, MediaPlayerService.class));
        super.onStop();


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

    /**
     * Salta la guía
     */
    private void showBtnSkipGuideDialog() {

        // Crear un diálogo de información
        new AlertDialog.Builder(this)
                .setTitle(R.string.btn_skip_guide_tittle)
                .setMessage(R.string.btn_skip_guide_message)
                .setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        binding.btnToMain.performClick();
                    }
                })
                .setNegativeButton(R.string.btn_skip_guide_leave,null)
                .show();
    }


    /**
     * Listener en las animaciones para deshabilitar los botones de avance y skip
     * y también para iniciar los efectos de sonido.
     */
    private AnimatorListenerAdapter bubbleInListenerAdapter = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animation) {
            binding.btnGo.setEnabled(true);
            binding.btnSkipGuide.setEnabled(true);
        }

        @Override
        public void onAnimationStart(Animator animation) {
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    soundPool.play(soundId, 1f, 1f, 1, 0, 1f);
                }
            },600);
        }
    };
    private final AnimatorListenerAdapter bubbleOutListenerAdapter = new AnimatorListenerAdapter() {

        @Override
        public void onAnimationStart(Animator animation) {
            binding.btnGo.setEnabled(false);
            binding.btnSkipGuide.setEnabled(false);
        }
    };



    NavController navController;
    ActivityGuideBinding binding;
    AnimatorSet bubbleAnimatorInSet;
    AnimatorSet bubbleAnimatorOutSet;
    AnimatorSet iconAnimatorOutSet;
    AnimatorSet iconAnimatorInSet;
    boolean isScreen5;
    View menuItemView;
    private Drawable defaultMenuItemBackground;
    private Handler handler;
    private Timer timer;
    private SoundPool soundPool;
    private int soundId;
    private int soundLevelUp;


}
