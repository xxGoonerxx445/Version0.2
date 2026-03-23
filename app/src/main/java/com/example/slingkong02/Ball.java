package com.example.slingkong02;

import android.graphics.Canvas;
import android.graphics.Paint;

public class Ball extends Base {
    private float dx, dy;
    private float radius;
    private boolean Death=false;
    private boolean hooked = false;
    private Paint paint;

    public Ball(float x, float y, float dx, float dy, float radius, Paint paint) {
        super(x, y);
        this.dx = dx;
        this.dy = dy;
        this.radius = radius;
        this.paint = paint;
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
        canvas.drawCircle(x, y, radius, paint);
    }

    public float GetDx() { return dx; }
    //public float GetDy() { return dy; }


    public void move() {
        x = x + dx;
        y = y + dy;
    }

    public void applyGravity() {
        if (!hooked) {
            dy += 0.02f; // Gravity constant
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
        if (y + radius >= screenHeight) {
            dy = -dy * 0.02f; // Bounce with some energy loss
            //dy = -dy;
            y = screenHeight - radius;
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
