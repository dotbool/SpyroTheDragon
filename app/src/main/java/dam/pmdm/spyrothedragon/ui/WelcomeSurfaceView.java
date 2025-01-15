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

import java.util.Random;

import dam.pmdm.spyrothedragon.models.Bolita;


public class WelcomeSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

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


    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        Log.d("SURFACED CREATED", "lA SURFACE HA SIDO CREADA");
        limitX = getWidth();
        limitY = getHeight();
        bubbleThread = new WelcomeBubbleThread();
        letRun = true;
        Log.d("ESTADIO", bubbleThread.getState().name());
        bubbleThread.start();

    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
        Log.d("SURFACED CAMBIADA", "lA SURFACE HA SIDO CAMBIADA");

//            bitmap = Bitmap.createBitmap(limitX, limitY, Bitmap.Config.ARGB_8888);


    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
        Log.d("SURFACED DESTRUIDA", "lA SURFACE HA SIDO DESTRUIDA");
        boolean retry = true;
        letRun = false;

        while (retry) {
            try {
                Log.d("ALIVE", String.valueOf(bubbleThread.isAlive()));
                bubbleThread.join();
                retry = false;
                Log.d("ALIVE", String.valueOf(bubbleThread.isAlive()));

            } catch (InterruptedException e) {
                Log.d("INterrupte", "destroye");
            }
        }


    }

    private class WelcomeBubbleThread extends Thread {

        public WelcomeBubbleThread() {

            ran = new Random();
            p = new Paint();

        }

        @Override
        public void run() {

            int initialPositionXB1 = (int) (Math.random() * limitX);
            int initialPositionXB2 = (int) (Math.random() * limitX);
            int initialPositionXB3 = (int) (Math.random() * limitX);
            int initialPositionXB4 = (int) (Math.random() * limitX);
            int initialPositionXB5 = (int) (Math.random() * limitX);
            Bolita b1 = new Bolita(initialPositionXB1, limitY, limitX, limitY);
            Bolita b2 = new Bolita(initialPositionXB2, limitY, limitX, limitY);
            Bolita b3 = new Bolita(initialPositionXB3, limitY, limitX, limitY);
            Bolita b4 = new Bolita(initialPositionXB4, limitY, limitX, limitY);
            Bolita b5 = new Bolita(initialPositionXB5, limitY, limitX, limitY);


            while (letRun){

                if(surfaceHolder.getSurface().isValid()){
                    try {

                        canvas = surfaceHolder.lockCanvas(); //bloqueamos el objeto canvas
//                        if (bitmap == null) {
//                            bitmap = Bitmap.createBitmap(limitX, limitY, Bitmap.Config.ARGB_8888);
//                            canvas.setBitmap(bitmap);
//                        }


                        p.setColor(Color.WHITE);
                        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR); //borramos todo

                        canvas.drawCircle(b1.getCx(), b1.getCy(), 4, p);
                        int direction = ran.nextBoolean() ? 1: -1;
                        b1.setCx(b1.getCx() + direction);
                        b1.setCy(b1.getCy() - 20);

                        canvas.drawCircle(b2.getCx(), b2.getCy(), 8, p);
                        direction = ran.nextBoolean() ? 1: -1;
                        b2.setCx(b2.getCx() + direction);
                        b2.setCy(b2.getCy() - 16);

                        canvas.drawCircle(b3.getCx(), b3.getCy(), 12, p);
                        direction = ran.nextBoolean() ? 1: -1;
                        b3.setCx(b3.getCx() + direction);
                        b3.setCy(b3.getCy() - 12);

                        canvas.drawCircle(b4.getCx(), b4.getCy(), 16, p);
                        direction = ran.nextBoolean() ? 1: -1;
                        b4.setCx(b4.getCx() + direction);
                        b4.setCy(b4.getCy() - 8);

                        canvas.drawCircle(b5.getCx(), b5.getCy(), 20, p);
                        direction = ran.nextBoolean() ? 1: -1;
                        b5.setCx(b5.getCx() + direction);
                        b5.setCy(b5.getCy() - 4);

                    }
                    catch (Exception e){

                        Log.d("RUN SURFACE VIEW", e.getMessage());
                    }
                    finally {
                        surfaceHolder.unlockCanvasAndPost(canvas);

                    }

                }
            }
        }

        private void doDraw(){

        }
        Random ran;

    }

    public void setLetRun(boolean letRun) {
        this.letRun = letRun;
    }

    public boolean letRun;
    protected Thread bubbleThread;
    private SurfaceHolder surfaceHolder;
    private Bitmap bitmap;
    Paint p;
    private int limitX;
    private int limitY;
    private Canvas canvas;

}

//                    Drawable d = ResourcesCompat.getDrawable(getResources(), R.drawable.welcome_background, null);
//                    d.setBounds(0,0,canvas.getWidth(),canvas.getHeight());
//                    d.draw(canvas);