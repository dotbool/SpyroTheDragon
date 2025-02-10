package dam.pmdm.spyrothedragon.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import dam.pmdm.spyrothedragon.R;

/**
 * Clase que pinta sobre el icono de los mundos
 */
public class WorldIconDrawable extends Drawable {

    public WorldIconDrawable(Context ctx) {

        paint = new Paint();
        paint.setColor(ContextCompat.getColor(ctx, R.color.purple_transparent_70));
        paint.setStrokeWidth(15);
        paint.setStyle(Paint.Style.STROKE);
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        width = getBounds().width();
        height = getBounds().height();
        canvas.drawCircle(width/2, (height - height/6), width/6, paint);
        paint.setStrokeWidth(8);
        canvas.drawLine(width/2, 0, width/2, height *2/3, paint);

    }

    @Override
    public void setAlpha(int alpha) {

    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return PixelFormat.OPAQUE;
    }

    Paint paint;
    private int width;
    private int height;
}
