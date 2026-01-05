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
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowMetrics;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class BoardGame extends View {

    private Ball b;
    private Paint p;
    private float viewWidth, viewHeight;

    private Handler animationHandler;
    private Handler loweringspeed;
    private Paint p2;
    private float dx;
    private float dy;
    private boolean F;
    private float startX, startY;
    private Thread thread;
    private Hook h;
    private Hook h2;
    private GameMoule GM;

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
        BackGround=BitmapFactory.decodeResource(getResources(),R.drawable.bgimage);
        DisplayMetrics ds = getResources().getDisplayMetrics();
        width=ds.widthPixels;
        height=ds.heightPixels;
        p = new Paint();
        p.setColor(Color.BLUE);
        b = new Ball(width/2, height-200, 0, 0, 50, p); // TODO: 04/01/2026 fix dx dy 
        p2 = new Paint();
        
        GM=new GameMoule(new ArrayList<Hook>());
        GM.initDefaultHooks(p2,width,height);



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
        p2.setColor(Color.RED);
        p2.setStyle(Paint.Style.STROKE);
        p2.setStrokeWidth(5);
        GM.DrawHooks(canvas);




    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if(b.didusertouch(event.getX(), event.getY()))
                {
                    b.setHooked(false);   //  release from hook
                    F=true;
                    startX = b.GetX();
                    startY = b.GetY();

                }

                break;
            case MotionEvent.ACTION_MOVE:
                float touchX = event.getX();
                float touchY = event.getY();
                dx = (touchX - startX)/10;
                dy = (touchY - startY)/10; // TODO: 04/01/2026 fix velocity 
                b.setDx(-dx);
                b.setDy(-dy);
                
                
                if((dx*dx)+(dy*dy)<150*150)
                {
                    b.setNewLocation(touchX, touchY);
                }
              

                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                // You could use dx and dy here to launch the ball
                //פה זה אמור להבין שהפסקתי את הפעולה ולהתחיל את move בעזרת הthread וhandler


                F=false;
                ThreadGame threadGame = new ThreadGame(); // TODO: 05/01/2026 fix velocity by disabling making new threads all the time 
                //ThreadGame t2=new ThreadGame();
                //t2.start();
                threadGame.start(); //starts the thread
                animationHandler=new Handler(new Handler.Callback() {
                    @Override
                    public boolean handleMessage(@NonNull android.os.Message msg) {
                        b.move();
                        invalidate();

                        if(GM.isCollide(b))
                        {
                            F=true;  // Stop the thread from calling move()
                        }


                        b.TouchedEdge(width,height);

                        return true;
                    }
                });


                loweringspeed = new Handler(new Handler.Callback() {
                    @Override
                    public boolean handleMessage(@NonNull Message msg) {

                        b.LowerSpeed();
                        invalidate();


                        //להוסיף את מה שמוריד מהירות

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
