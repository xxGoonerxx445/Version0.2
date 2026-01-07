package com.example.slingkong02;

import android.graphics.Canvas;
import android.graphics.Paint;

// TODO: 24/12/2025 fix velocity 
// TODO: 24/12/2025 add ActiveCollison to check if ball is hooked-disable when you try to launch ball
public class Ball extends Base{
    //private float x, y;
    private float dx, dy;
    private float radius;
    private boolean hooked = false;
    private float StopLocation;

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
    public void setHooked(boolean hooked) {
        this.hooked = hooked;
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
    public float GetDy() {return this.dy;}
    public float GetDx() {return this.dx;}

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
    public void TouchedEdge(float screenWidth, float screenHeight) //checks if edeges of the screen where touched, if true than changes to the opposite dx/dy
    {

        if(this.x+this.radius<=0 ||this.x+this.radius>=screenWidth)
       {
            this.dx=-this.dx;
       }

        if(this.y+this.radius>=screenHeight||this.y+this.radius<=0)
        {
            this.dy=-this.dy;
        }


    }
    public void LowerSpeed()
    {
//        if(this.dx>0)
//            this.dx=this.dx+10f;
//        else
//            this.dx=this.dx-10f;
        if(this.dy>0&&this.dy!=0)
            this.dy-=100f;
        if(this.dy<0&&this.dy!=0)
            this.dy+=100f;




    }
    public void IsHooked()
    {}

}

