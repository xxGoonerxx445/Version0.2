package com.example.slingkong02;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;

public class BoardGame extends View {

    private Ball b;
    private Paint p;
    private Handler animationHandler;
    private Paint p2;
    private float dx;
    private float dy;
    private boolean F;
    private float startX, startY;
    private Thread thread;

    private final long frameRate = 30; // Milliseconds per frame

   /* private Runnable animationRunnable = new Runnable() {
        @Override
        public void run() {
            b.move();
            invalidate(); // Redraw the view
            animationHandler.postDelayed(this, frameRate);
        }
    };*/

    public BoardGame(Context context) {
        super(context);
        p = new Paint();
        p.setColor(Color.BLUE);
        b = new Ball(500, 1000, 10, -10, 50, p);
        p2 = new Paint();
        //animationHandler.postDelayed(animationRunnable, frameRate);
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.WHITE);
        b.draw(canvas);
        p2.setColor(Color.RED);
        p2.setStyle(Paint.Style.STROKE);
        p2.setStrokeWidth(5);
        canvas.drawCircle(1000, 1000, 100, p2);




    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if(b.didusertouch(event.getX(), event.getY()))
                {
                    F=true;

                    startX = b.GetX();
                    startY = b.GetY();
                }
             //   startX = b.GetX();
                //startY = b.GetY();
                break;
            case MotionEvent.ACTION_MOVE:
                float touchX = event.getX();
                float touchY = event.getY();
                dx = touchX - startX;
                //if(dx)
                //dx=Math.abs(dx);
                dy = touchY - startY;
                b.setDx(-dx/15);
                b.setDy(-dy/15);
                

                if(dx<=150&&dy<=150) //&touchY>startY
                {
                    b.setNewLocation(touchX, touchY);
                }

                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                // You could use dx and dy here to launch the ball
                //פה זה אמור להבין שהפסקתי את הפעולה ולהתחיל את move בעזרת הthread וhandler
                F=false;
                ThreadGame threadGame = new ThreadGame();
                threadGame.start(); //starts the thread
                animationHandler=new Handler(new Handler.Callback() {
                    @Override
                    public boolean handleMessage(@NonNull android.os.Message msg) {
                        b.move();
                        invalidate();
                        return true;
                    }
                });





                break;
        }
        return true;
    }
    private class ThreadGame extends Thread{
        @Override
        public void run() {
            super.run();
            while (true)
            {
                try {
                    sleep(40);
                    if(F==false)
                        animationHandler.sendEmptyMessage(0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

