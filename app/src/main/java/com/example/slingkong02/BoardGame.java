package com.example.slingkong02;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class BoardGame extends View {

    private Ball b;
    private Paint p;
    private float viewWidth, viewHeight;

    private Paint p2;
    private float dx, dy;
    private boolean F = true; // Start with physics disabled (dragging mode)
    private float startX, startY;
    private ThreadGame threadGame;

    private GameMoule GM;

    private Bitmap BackGround;
    private Rect destRect;

    public BoardGame(Context context) {
        super(context);
        BackGround = BitmapFactory.decodeResource(getResources(), R.drawable.bgimage);
        DisplayMetrics ds = getResources().getDisplayMetrics();
        
        // Initial fallback sizes
        viewWidth = ds.widthPixels;
        viewHeight = ds.heightPixels;

        p = new Paint();
        p.setColor(Color.BLUE);
        // Start ball at bottom center
        b = new Ball(viewWidth / 2f, viewHeight - 200, 0, 0, 50, p);

        p2 = new Paint();
        p2.setColor(Color.RED);
        p2.setStyle(Paint.Style.STROKE);
        p2.setStrokeWidth(5);

        GM = new GameMoule(new ArrayList<Hook>());
        GM.initDefaultHooks(p2);

        // Start the physics thread ONCE
        threadGame = new ThreadGame();
        threadGame.start();

        Toast.makeText(getContext(), "Game Started", Toast.LENGTH_SHORT).show();
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
        }
        b.draw(canvas);
        GM.DrawHooks(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (b.didusertouch(touchX, touchY)) {
                    b.setHooked(false);
                    F = true; // Enable drag mode (disables physics thread)
                    startX = b.GetX();
                    startY = b.GetY();
                }
                break;

            case MotionEvent.ACTION_MOVE:
                if (F) {
                    float distX = touchX - startX;
                    float distY = touchY - startY;
                    float distance = (float) Math.sqrt(distX * distX + distY * distY);
                    float maxRadius = 300f; // Limits how far the ball follows the touch

                    if (distance > maxRadius) {
                        float ratio = maxRadius / distance;
                        float limitedX = startX + distX * ratio;
                        float limitedY = startY + distY * ratio;
                        b.setNewLocation(limitedX, limitedY);
                        b.setDx(-(limitedX - startX) / 10f);
                        b.setDy(-(limitedY - startY) / 10f);
                    } else {
                        b.setNewLocation(touchX, touchY);
                        b.setDx(-distX / 10f);
                        b.setDy(-distY / 10f);
                    }
                    invalidate(); 
                }
                break;

            case MotionEvent.ACTION_UP:
                if (F) {
                    F = false; // Release: Enable physics thread
                }
                break;
        }
        return true;
    }

    private class ThreadGame extends Thread {
        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(16); // ~60 FPS
                    if (!F) { // If physics is active (not being dragged)
                        b.move();
                        b.TouchedEdge(viewWidth, viewHeight);
                        
                        if (GM.isCollide(b)) {
                            F = true; // Stop moving if we hit a hook
                        }
                        
                        // postInvalidate() tells the UI thread to redraw from a background thread
                        postInvalidate();
                    }
                } catch (InterruptedException e) {
                    break;
                }
            }
        }
    }
}
