package dam.pmdm.spyrothedragon;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import dam.pmdm.spyrothedragon.databinding.ActivityGuideBinding;

public class GuideActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityGuideBinding binding = ActivityGuideBinding.inflate(getLayoutInflater());
        binding.guideLayout.bringToFront();
        setContentView(binding.getRoot());


        NavHostFragment navHostFragment =
                (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_guide);
        if(navHostFragment!=null) {
            navController = navHostFragment.getNavController();
        }

        binding.btnGo.setOnClickListener(v -> {

            String label = (String) navController.getCurrentDestination().getLabel();
            Log.d("laBEL", label);

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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.about_menu, menu);
        return true;
    }

    NavController navController;


}
