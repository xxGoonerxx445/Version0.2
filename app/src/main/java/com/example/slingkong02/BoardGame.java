package com.example.slingkong02;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowMetrics;
import android.widget.Toast;

import androidx.annotation.NonNull;

public class BoardGame extends View {

    private Ball b;
    private Paint p;
    private float viewWidth, viewHeight;

    private Handler animationHandler;
    private Paint p2;
    private float dx;
    private float dy;
    private boolean F;
    private float startX, startY;
    private Thread thread;
    private Hook h;
    private Bitmap BackGround;
    private Rect destRect;
    private int width,height;



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
        h=new Hook(b.getX(),b.getY()-300,50,p2);
        BackGround=BitmapFactory.decodeResource(getResources(),R.drawable.bgimage);
        DisplayMetrics ds = getResources().getDisplayMetrics();
        width=ds.widthPixels;
        height=ds.heightPixels;


        Toast.makeText(getContext(), "width="+width+"height="+height, Toast.LENGTH_SHORT).show();








        //animationHandler = new Handler()

        //animationHandler.postDelayed(animationRunnable, frameRate);
    }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        viewWidth = w;
        viewHeight = h;
        destRect = new Rect(0, 0, w, h);
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        if (destRect != null) {
            canvas.drawBitmap(BackGround, null, destRect, null);
        } else {
            canvas.drawBitmap(BackGround, 0, 0, null);
        }
        b.draw(canvas);
        h.draw(canvas);
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

                        if(h.Collision(b.GetX(),b.GetY()))
                        {
                            b.setNewLocation(h.GetPostionX(),h.GetPostionY());
                            F=true;
                        }
                        b.TouchedEdge(width,height);
//                        if(b.TouchedEdge(width,height))
//                        {
//                            Toast.makeText(getContext(), "touched edge", Toast.LENGTH_SHORT).show();
//
//                        }

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
                    sleep(16);
                    if(F==false)
                        animationHandler.sendEmptyMessage(0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
