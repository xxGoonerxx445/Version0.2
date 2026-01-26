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
    private ThreadGame threadGame = new ThreadGame();
    private Paint p2;
    private float dx;
    private float StartYforShift;
    private float dy;
    private boolean F;
    private float startX, startY;


    private GameMoule GM;

    private Bitmap BackGround;
    private Rect destRect;
    private int width,height;



    public BoardGame(Context context) {
        super(context);
        BackGround = BitmapFactory.decodeResource(getResources(), R.drawable.bgimage);
        DisplayMetrics ds = getResources().getDisplayMetrics();
        width = ds.widthPixels;
        height = ds.heightPixels;
        p = new Paint();
        p.setColor(Color.BLUE);
        b = new Ball(width / 2, height - 200, 0, 0, 50, p); // TODO: 04/01/2026 fix dx dy
        p2 = new Paint();

        GM = new GameMoule(new ArrayList<Hook>());
        GM.initDefaultHooks(p2, width, height);
        animationHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull android.os.Message msg) {
                if (!F) {
                    b.move();
                    //GM.SpawnNewHooks(height,b,width);



                    // --------------------------------
                    if (GM.isCollide(b,StartYforShift,b.GetY())) {
                        F = true; // Hooked

                    }

                    b.TouchedEdge(width, height);
                    invalidate();
                }
                return true;
            }
        });


        threadGame.start();
        //Toast.makeText(getContext(), "Infinite Mode Active", Toast.LENGTH_SHORT).show();
        Toast.makeText(getContext(), "width="+width+"height="+height, Toast.LENGTH_SHORT).show();



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
                    StartYforShift=startY;


                }

                break;
            case MotionEvent.ACTION_MOVE:

                if (F && !b.isHooked()) { // Only allow dragging if we touched the ball
                    float touchX = event.getX();
                    float touchY = event.getY();
                    dx = (touchX - startX);
                    dy = (touchY - startY);

                    // Limit drag distance
                    if ((dx * dx) + (dy * dy) < 300 * 300) {
                        b.setNewLocation(touchX, touchY);
                    }
                }
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                if (F && !b.isHooked()) {
                    // Launch ball in opposite direction of drag
                    b.setDx(-(event.getX() - startX) / 10f);
                    b.setDy(-(event.getY() - startY) / 10f);
                    F = false; // Start movement
                }



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
                    animationHandler.sendEmptyMessage(0);
                } catch (InterruptedException e) {
                   break;
                }
            }
        }

    }
}
