package dam.pmdm.spyrothedragon.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

import dam.pmdm.spyrothedragon.R;

/**
 * Clase que sirve para dibujar la llama que sale de la "boca" de Spyro
 */
public class FireBreathView extends View {

    public FireBreathView(Context ctx, View view) {
       this(ctx, null, view);

    }

    public FireBreathView(Context context, @Nullable AttributeSet attrs, View view) {
        super(context, attrs);
        characterView = view;

        int[] location = new int[2];
        characterView.getLocationInWindow(location); //obtenemos la localizazión de spyro
        startX = location[0] + characterView.getWidth()/2 ;
        startY = location[1];
        endX = startX;
        endY = startX + 600;


        paint = new Paint();
        path = new Path();

        paint.setStrokeWidth(20);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeCap(Paint.Cap.ROUND);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            paint.setShader(new RadialGradient(startX, startY, 400, Color.YELLOW, Color.RED, Shader.TileMode.DECAL));
        }

        startAnimation();

    }


    /**
     * Draw dibuja dos líneas cuadráticas (Van a su destino pasando por el punto de control y describiendo
     * una curva en lugar de una recta). La vista se invalida cada vez que se actualiza para que dibuje
     * con los nuevos valores
     * @param canvas the canvas on which the background will be drawn
     */
    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);

        path.moveTo(startX,startY);
        path.quadTo(controlX, controlY, endX, endY);

        path.moveTo(startX ,startY);
        path.quadTo((float) (controlX * - 0.25), controlY  , endX, endY);

        canvas.drawPath(path,paint);
        path.reset();

    }

    /**
     *
     */
    public void startAnimation() {

        ValueAnimator animator = ValueAnimator.ofFloat(startY, 750, startY);
        animator.setDuration(1500);

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(@NonNull ValueAnimator valueAnimator) {

                controlY = ((Float) valueAnimator.getAnimatedValue()).intValue();
                controlX = ((Float) valueAnimator.getAnimatedValue()).intValue();
                endY = ((Float) valueAnimator.getAnimatedValue()).intValue();
                invalidate();

            }
        });
        animator.start();
        /**
         * No queremos que se pueda interactuar con la app mientras la animación es tá funcionado
         */
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(@NonNull Animator animation) {
                setClickable(false);
                setVisibility(GONE);
            }
        });
    }

    int startX;
    int startY;
    int endX;
    int endY;

    int controlX;
    int controlY;

    Paint paint;
    Path path;
    View characterView;
}
