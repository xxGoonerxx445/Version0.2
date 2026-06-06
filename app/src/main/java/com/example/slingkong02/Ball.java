package com.example.slingkong02;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

public class Ball extends Base {
    private float dx, dy;
    private float radius;
    private Bitmap bitmap;

    private boolean Death=false;
    private boolean hooked = false;
    private final RectF rect = new RectF();


    public Ball(float x, float y, float dx, float dy, float radius, Bitmap bitmap) {
        super(x, y);
        this.dx = dx;
        this.dy = dy;
        this.radius = radius;
        this.bitmap=bitmap;
    }

    public boolean isHooked() {
        return hooked;
    }
    public float GetDy()
    {
        return this.dy;
    }

    public void setHooked(boolean hooked) {
        this.hooked = hooked;
    }

    @Override
    public void draw(Canvas canvas) {
        if (bitmap != null) {

            rect.set(x - radius, y - radius, x + radius, y + radius);
            canvas.drawBitmap(bitmap, null, rect, null);
        }
    }


    public void move() {
        x = x + dx; //adds x to the dx every frame
        y = y + dy; //adds y to the dy every frame
    }

    public void applyGravity() {
        if (!hooked) {
            dy += 0.06f; // Gravity constant
        }
    }

    public void setX(float x) { this.x = x; }
    public void setY(float y) { this.y = y; }

    public void setDx(float dx) { this.dx = dx; }
    public void setDy(float dy) { this.dy = dy; }

    public void setNewLocation(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public boolean didusertouch(float tx, float ty) {

        return (tx >= x - radius && tx <= x + radius && ty >= y - radius && ty <= y + radius);
    }

    public void TouchedEdge(float screenWidth, float screenHeight) {
        if (x - radius <= 0 || x + radius >= screenWidth) {
            dx = -dx;
        }
    }
    public boolean isDeath()
    {
        return Death;
    }
    public void SetDeath()
    {
        Death=true;
    }
}
