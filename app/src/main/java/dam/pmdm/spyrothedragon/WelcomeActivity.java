package dam.pmdm.spyrothedragon;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Insets;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.WindowInsets;
import android.view.WindowMetrics;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import dam.pmdm.spyrothedragon.databinding.ActivityWelcomeBinding;
import dam.pmdm.spyrothedragon.ui.WelcomeSurfaceView;

public class WelcomeActivity extends AppCompatActivity {

    AnimationDrawable fireAnimationLeft;
    AnimationDrawable fireAnimationRight;
    AnimationDrawable ovalAnimation;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = new WelcomeSurfaceView(this);
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

//        OvalDrawable d = new OvalDrawable();
        ImageView imageOval = binding.oval;
        imageOval.setBackgroundResource(R.drawable.oval_glow);
        ovalAnimation = (AnimationDrawable) imageOval.getBackground();
        imageOval.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.spy_welcome, null));
//        imageOval.setImageDrawable(d);

        binding.btnWelcome.setOnClickListener(v-> {
            binding.welcomeSurfaceView.setLetRun(false);
            goToMain();
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

    private void goToMain(){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
//        startActivity(i, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
        finish();

    }

    public static int getScreenWidth(@NonNull Activity activity) {
            WindowMetrics windowMetrics = activity.getWindowManager().getCurrentWindowMetrics();
            Insets insets = windowMetrics.getWindowInsets()
                    .getInsetsIgnoringVisibility(WindowInsets.Type.systemBars());
            return windowMetrics.getBounds().width() - insets.left - insets.right;
    }

    ActivityWelcomeBinding binding;
    private WelcomeSurfaceView view;


}
