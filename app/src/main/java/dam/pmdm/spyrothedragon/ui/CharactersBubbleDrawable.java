package dam.pmdm.spyrothedragon.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import dam.pmdm.spyrothedragon.R;

public class CharactersBubbleDrawable extends Drawable {

    public CharactersBubbleDrawable(Context ctx) {
        paint = new Paint();
        paint.setColor(ContextCompat.getColor(ctx, R.color.purple_transparent_70));


    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        width = getBounds().width();
        height = getBounds().height();
        rec = new RectF(0,0, width, (float)height);
        canvas.drawOval(rec,paint);
        paint.setStrokeWidth(5);
        canvas.drawLine(width/2, height, width/3, height-100, paint);

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

    private int width;
    private int height;
    Paint paint;
    RectF rec;
}
