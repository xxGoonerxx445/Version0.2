package com.example.slingkong02;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class BoardGame extends View {
    private Ball b;
    private boolean isDialogShown = false;
    private Handler animationHandler;
    private ThreadGame threadGame = new ThreadGame();
    private Paint p, p2, p3;
    private float dx, dy;
    private boolean F, WasFirstDrag = false; //דגל המציין האם השחקן נמצא כרגע בתהליך של מתיחת הדמות (Dragging).
    private float startX, startY;
    private GameModule GM;
    private int Score;

    private Bitmap BackGround;
    private Bitmap sawBitmap;
    private Bitmap monkeyBitmap;
    private Rect destRect;
    private int width, height;

    public BoardGame(Context context) {
        super(context);
        BackGround = BitmapFactory.decodeResource(getResources(), R.drawable.bgimage);
        sawBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icons8sawblade96);
        monkeyBitmap=BitmapFactory.decodeResource(getResources(), R.drawable.monkey);
        DisplayMetrics ds = getResources().getDisplayMetrics();
        width = ds.widthPixels;
        height = ds.heightPixels;

        p = new Paint();
        p.setColor(Color.BLUE);
        b = new Ball(width / 2, height -301, 0, 0, 75, monkeyBitmap); //here

        p2 = new Paint();
        p2.setColor(Color.parseColor("#65442E"));
        p2.setStyle(Paint.Style.STROKE);
        p2.setStrokeWidth(7);

        p3 = new Paint();
        p3.setColor(Color.BLACK);
        p3.setStrokeWidth(5);
        p3.setTextSize(75);

        GM = new GameModule(new ArrayList<Hook>(), new ArrayList<Saw>());
        GM.initDefaultHooks(p2, width, height);
        GM.initDefaultSaws(sawBitmap, width, height);

        animationHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull android.os.Message msg) {
                if (isDialogShown) return true;

                if (!F) {
                    if (WasFirstDrag) b.applyGravity();
                    b.move();

                    //  פונקציית הגלילה
                    GM.updateScrolling(b, height);

                    // עדכון קריאה ל-isCollide (ללא פרמטרים מיותרים)
                    if (GM.isCollide(b)) {
                        F = true;

                    }
                    if(GM.isCollideSaws(b))// TODO: 3/25/2026 check collison with saws
                    {
                        F=true;
                        b.SetDeath();
                        b.setDy(0);
                        b.setDx(0);
                        isDialogShown = true;
                        CustomDialog customDialog = new CustomDialog(getContext(), BoardGame.this);
                        customDialog.show();
                    }

                    b.TouchedEdge(width, height);
                }

                // עדכון הניקוד מה-GM בכל פריים
                Score = GM.CalcScore();

                if (WasFirstDrag && b.getY() + 75 >= height && !isDialogShown) {
                    b.SetDeath();
                    b.setDy(0);
                    b.setDx(0);
                    isDialogShown = true;
                    CustomDialog customDialog = new CustomDialog(getContext(), BoardGame.this);
                    customDialog.show();
                }

                // עדכון קריאה ל-SpawnNewHooksו SpawnNewSaws (ללא פרמטר b)
                GM.SpawnNewHooks(height, width);
                // Pass b.getY() so saws respawn above the player's current position
                GM.SpawnNewSaws(height, width, b.getY());

                invalidate();
                return true;
            }
        });

        threadGame.start();
    }

    public int getScore() {
        return Score;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
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
        GM.ShowScore(canvas, p3, Score);
        GM.DrawHooksAndSaws(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isDialogShown) return false;

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (b.didusertouch(event.getX(), event.getY()) && (b.isHooked() || !WasFirstDrag)) {
                    b.setHooked(false);
                    F = true;
                    //startX = b.GetX();
                    startX=b.getX();
                    startY = b.getY();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (F && !b.isHooked()) {
                    float touchX = event.getX();
                    float touchY = event.getY();
                    dx = (touchX - startX);
                    dy = (touchY - startY);
                    if ((dx * dx) + (dy * dy) < 130 * 130) {
                        b.setNewLocation(touchX, touchY);
                    }
                }
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                if (F && !b.isHooked()) {
                    // Use the ball's visual position (b.getX/Y) which is locked to 130px,
                    // instead of the finger's position (event.getX/Y).
                    float touchX_ENDOFMOVMENT = b.getX();
                    float touchY_ENDOFMOVMENT = b.getY();

                    // the velocity calculation  matches the visual drag distance.
                    b.setDx(-(touchX_ENDOFMOVMENT - startX) / 12f);
                    b.setDy(-(touchY_ENDOFMOVMENT - startY) / 12f);


                    F = false;
                    WasFirstDrag = true;
                }
                break;
        }
        return true;
    }

    private class ThreadGame extends Thread {
        @Override
        public void run() {
            while (!isInterrupted()) {
                try {
                    sleep(16);
                    animationHandler.sendEmptyMessage(0);
                } catch (InterruptedException e) {
                    break;
                }
            }
        }
    }

    public void restartGame() {
        GM.Restart();
        GM.initDefaultHooks(p2, width, height);
        GM.initDefaultSaws(sawBitmap, width, height);
        Score = 0;
        b = new Ball(width / 2, height - 200, 0, 0, 75, monkeyBitmap);
        isDialogShown = false;
        WasFirstDrag = false;
        F = false;
        invalidate();
    }
}
