package dam.pmdm.spyrothedragon.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;

import java.util.Random;

import dam.pmdm.spyrothedragon.models.Bolita;

/**
 * Clase que se encarga de pintar las bolitas de la pantalla de welcome. Se trata de una superficie
 * transparente colocada encima de la vista welcome de forma que sólo es preciso dibujar las bolitas
 * La clase está vinculada al ciclo de vida de la welcome activity de forma que en función del estado
 * de aquella el run de esta clase se activa o desactiva
 */
public class WelcomeSurfaceView extends SurfaceView implements SurfaceHolder.Callback, DefaultLifecycleObserver {

    public WelcomeSurfaceView(Context context) {
        this(context, null);
    }

    public WelcomeSurfaceView(Context context, AttributeSet attrs) {
            super(context, attrs);

            setZOrderOnTop(true);
            surfaceHolder = getHolder();
            surfaceHolder.setFormat(PixelFormat.TRANSPARENT);
            surfaceHolder.addCallback(this);

    }


    /**
     * Cuando la actividad se inicia comienza el ascenso de las bolitas
     * @param owner
     */
    @Override
    public void onResume(@NonNull LifecycleOwner owner) {
        letRun = true;

    }

    /**
     * Cuando la actividdd se para se detiene el run del hilo de la surface
     * @param owner
     */
    @Override
    public void onPause(@NonNull LifecycleOwner owner) {
        letRun = false;
    }

    /**
     * Cuando la actividad termina se quita el observer
     * @param owner
     */
    @Override
    public void onDestroy(@NonNull LifecycleOwner owner) {
        owner.getLifecycle().removeObserver(this);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        limitX = getWidth();
        limitY = getHeight();
        bubbleThread = new WelcomeBubbleThread();
        letRun = true;
        bubbleThread.start();

    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    /**
     * Cuando la surface es destruida permitimos que el hilo que corre en ella termine
     * @param holder The SurfaceHolder whose surface is being destroyed.
     */
    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
        bubbleThread.interrupt();
        letRun = false;

        try {
            bubbleThread.join();
        }
        catch (InterruptedException e) {
        }
    }


    /**
     * Esta clase es el hilo que pinta las bolitas que ascienden en la pantalla de welcome
     */
    private class WelcomeBubbleThread extends Thread {

        public WelcomeBubbleThread() {

            ran = new Random();
            p = new Paint();
            p.setColor(Color.WHITE);

            int initialPositionXB1 = (int) (Math.random() * limitX);
            int initialPositionXB2 = (int) (Math.random() * limitX);
            int initialPositionXB3 = (int) (Math.random() * limitX);
            int initialPositionXB4 = (int) (Math.random() * limitX);
            int initialPositionXB5 = (int) (Math.random() * limitX);
            b1 = new Bolita(initialPositionXB1, limitY, limitX, limitY);
            b2 = new Bolita(initialPositionXB2, limitY, limitX, limitY);
            b3 = new Bolita(initialPositionXB3, limitY, limitX, limitY);
            b4 = new Bolita(initialPositionXB4, limitY, limitX, limitY);
            b5 = new Bolita(initialPositionXB5, limitY, limitX, limitY);

        }

        @Override
        public void run() {
            int direction;

            while (letRun){
                if(surfaceHolder.getSurface().isValid()){
                    try {
                        canvas = surfaceHolder.lockCanvas();

                        if(canvas!=null) {
                        if (bitmap == null) {
                            bitmap = Bitmap.createBitmap(limitX, limitY, Bitmap.Config.ARGB_8888);
                            canvas.setBitmap(bitmap);
                        }

                            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR); //borramos todo

                            canvas.drawCircle(b1.getCx(), b1.getCy(), 4, p);
                            direction = ran.nextBoolean() ? 1 : -1;
                            b1.setCx(b1.getCx() + direction);
                            b1.setCy(b1.getCy() - 20);


                            canvas.drawCircle(b2.getCx(), b2.getCy(), 8, p);
                            direction = ran.nextBoolean() ? 1 : -1;
                            b2.setCx(b2.getCx() + direction);
                            b2.setCy(b2.getCy() - 16);

                            canvas.drawCircle(b3.getCx(), b3.getCy(), 12, p);
                            direction = ran.nextBoolean() ? 1 : -1;
                            b3.setCx(b3.getCx() + direction);
                            b3.setCy(b3.getCy() - 12);


                            canvas.drawCircle(b4.getCx(), b4.getCy(), 16, p);
                            direction = ran.nextBoolean() ? 1 : -1;
                            b4.setCx(b4.getCx() + direction);
                            b4.setCy(b4.getCy() - 8);

                            canvas.drawCircle(b5.getCx(), b5.getCy(), 20, p);
                            direction = ran.nextBoolean() ? 1 : -1;
                            b5.setCx(b5.getCx() + direction);
                            b5.setCy(b5.getCy() - 4);
                        }

                    }

                    catch (Exception e){
                        letRun = false;
                    }

                    finally {
                        if(surfaceHolder.getSurface().isValid() && canvas!=null ) {
                            surfaceHolder.unlockCanvasAndPost(canvas);
                        }
                    }
                }
            }
        }

        Random ran;
        Bolita b1,b2,b3,b4,b5;

    }

    public void setLetRun(boolean letRun) {
        this.letRun = letRun;
    }

    public boolean letRun;
    protected Thread bubbleThread;
    private final SurfaceHolder surfaceHolder;
    private Bitmap bitmap;
    Paint p;
    private int limitX;
    private int limitY;
    private Canvas canvas;

}
