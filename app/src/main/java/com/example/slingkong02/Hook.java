package com.example.slingkong02;

import android.graphics.Canvas;
import android.graphics.Paint;

public class Hook extends Base{
    private float radius;
    private Paint paint;
    private Boolean Active;


    public Hook(float x, float y,float radius,Paint paint) {
        super(x, y);
        this.radius=radius;
        this.paint=paint;
        this.Active=true;
    }


    @Override
    public void draw(Canvas canvas) {
        // Add your drawing logic here
        canvas.drawCircle(x,y,radius,paint);

    }
    public void setY(float y) {
        this.y = y;
    }
    public boolean Collision(float x,float y)
    {
        if(!Active) return false;
        if(Active && x>=this.x-radius&&x<=this.x+radius&&y>=this.y-radius&&y<=this.y+radius)
        {
            this.Active=false;
            return true;
        }
            
        return false;

    }
    public boolean GetMode()
    {
        return Active;
    }
    public void Activate()
    {this.Active=true;}
    public boolean isHooking(Ball b)
    {
        if(this.x==b.getX()&&this.y==b.getY())
            return true;
        return false;
    }
    public void SetPosition(float X,float Y)
    {
        this.y=Y;
        this.x=X;
    }








}
