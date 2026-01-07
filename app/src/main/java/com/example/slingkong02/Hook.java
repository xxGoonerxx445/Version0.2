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
    public float GetPostionX() {
        return this.x;

    }
    public float GetPostionY() {
        return this.y; //
    }

    @Override
    public void draw(Canvas canvas) {
        // Add your drawing logic here
        canvas.drawCircle(x,y,radius,paint);

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
        if(this.x==b.GetX()&&this.y==b.getY())
            return true;
        return false;
    }
    public void SetPosition(float x,float y)
    {
        this.x=x;
        this.y=y;
    }






}
