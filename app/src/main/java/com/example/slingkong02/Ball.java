package com.example.slingkong02;

import android.graphics.Canvas;
import android.graphics.Paint;

public class Ball extends Base{
    //private float x, y;
    private float dx, dy;
    private float radius;
    private Paint paint;

    public Ball(float x, float y, float dx, float dy, float radius, Paint paint) {
        super(x, y);
        this.dx = dx;
        this.dy = dy;
        this.radius = radius;
        this.paint = paint;

    }
    public void draw(Canvas canvas)
    {

        canvas.drawCircle(x,y,radius,paint);
    }
    public float GetX()
    {
        return x;
    }
    public float GetY()
    {
        return y;
    }
    public void move()
    {

        x = x + dx;
        y = y + dy;
    }

    public void setDx(float dx) {
        this.dx = dx;
    }
    public void setDy(float dy) {
        this.dy = dy;
    }

    public void setNewLocation(float x, float y) {
        this.x = x;
        this.y = y;
    }
    public boolean didusertouch(float x, float y)
    {
        if(x>=this.x-radius&&x<=this.x+radius&&y>=this.y-radius&&y<=this.y+radius);
        return true;

    }
}
