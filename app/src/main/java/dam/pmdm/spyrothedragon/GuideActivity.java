package dam.pmdm.spyrothedragon;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import dam.pmdm.spyrothedragon.databinding.ActivityGuideBinding;
import dam.pmdm.spyrothedragon.ui.CharactersBubbleDrawable;
import dam.pmdm.spyrothedragon.ui.CharactersIconDrawable;

public class GuideActivity extends AppCompatActivity {

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

        CharactersBubbleDrawable cd = new CharactersBubbleDrawable(this);
        binding.charactersBubbleDrawable.bringToFront();
        binding.charactersBubbleDrawable.setBackground(cd);
        CharactersIconDrawable cid = new CharactersIconDrawable(this);
        binding.charactersIconDrawable.bringToFront();
        binding.charactersIconDrawable.setBackground(cid);

//        binding.charactersIcon.setForeground(
//                ResourcesCompat.getDrawable(getResources(),R.drawable.circle, null));



        binding.btnGo.setOnClickListener(v -> {

            String label = (String) navController.getCurrentDestination().getLabel();

            int destination = 0;

            switch (label) {

                case "Personajes":
                    destination = R.id.navigation_worlds;
                    break;
                case "Mundos":
                    destination = R.id.navigation_collectibles;
                    break;
                case "Coleccionables":
                    destination = R.id.navigation_collectibles;
                    break;
            }
            if (destination != 0) {
                navController.navigate(destination);
            }

        });


    }


    @Override
    protected void onStart() {
        super.onStart();

        AnimatorSet set = (AnimatorSet) AnimatorInflater.loadAnimator(
                this, R.animator.character_icon_animator);
        set.setTarget(binding.charactersIconDrawable);
        set.start();

        AnimatorSet set2 = (AnimatorSet) AnimatorInflater.loadAnimator(
                this, R.animator.character_bubble_animator
        );
        set2.setTarget(binding.charactersBubbleDrawable);
        set2.start();

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

    NavController navController;
    ActivityGuideBinding binding;


}
