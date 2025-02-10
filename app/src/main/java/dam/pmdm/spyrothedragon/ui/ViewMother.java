package dam.pmdm.spyrothedragon.ui;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import dam.pmdm.spyrothedragon.R;


/**
 * Esta clase es una capa transparente sobre la guía. Se sibujan sobre ella las líneas que apuntan
 * a cada icono. Para meterla en el xml hay que anular el constructor que incluye el AttributeSet
 */
public class ViewMother extends View {

    public ViewMother(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        color = 0;
        paint = new Paint();
        paint.setColor(ContextCompat.getColor(context, R.color.purple_transparent_70));
        paint.setStrokeWidth(8);
        animator = ObjectAnimator.ofFloat(this, "alpha", 0.0f, 1.0f);
        animator.setDuration(2000);


    }

    public ViewMother(Context context) {
        this(context,null);
        paint = new Paint();
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        super.draw(canvas);
        animator.start();

        if(pointFrom!=null && pointTo!=null) {
            canvas.drawLine(pointFrom.x,pointFrom.y,pointTo.x,pointTo.y, paint);
            pointFrom = null;
            pointTo = null;
        }
        else if (drawable!=null) {
            setBackground(drawable);
        }

    }

    public void setPointFrom(Point pointFrom) {
        this.pointFrom = pointFrom;
    }
    public void setPointTo(Point pointTo) {
        this.pointTo = pointTo;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
    }

    Paint paint;
    Point pointFrom;
    Point pointTo;
    ObjectAnimator animator;
    int color;
    Drawable drawable;
}
