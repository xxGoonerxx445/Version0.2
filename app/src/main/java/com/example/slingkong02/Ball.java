package com.example.slingkong02;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Ball extends Base {
    private float dx, dy;
    private float radius;
    private boolean hooked = false;
    private Paint paint;
    private Bitmap Monkey;
    private float G=0.03f;

/* Ball(float x, float y, float dx, float dy, float radius, Paint paint,Bitmap monkey) {
        super(x, y);
        this.dx = dx;
        this.dy = dy;
        this.radius = radius;
        this.paint = paint;
        //Monkey=BitmapFactory.decodeResource(getResource(),R.drawable.monkey);


    }*/

    public Ball(float x, float y, float dx, float dy, float radius,Bitmap monkey) {
        super(x, y);
        this.dx = dx;
        this.dy = dy;
        this.radius = radius;
        this.Monkey=monkey;



    }

    public boolean isHooked()  {
        G=0.03F;
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
        //canvas.drawCircle(x, y, radius, paint);
        canvas.drawBitmap(Monkey,x,y,paint);
    }

    public float GetX() { return x; }
    public float GetY() { return y; }
    public float GetDx() { return dx; }
    //public float GetDy() { return dy; }


    public void move() {
        x = x + dx;
        y = y + dy;
    }

    public void applyGravity() {
        if (!hooked) {
            dy += G; // Gravity constant
            G+=0.002F;

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
            y = screenHeight - radius;
        }
    }
}
