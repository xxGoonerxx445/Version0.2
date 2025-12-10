package com.example.slingkong02;

import android.graphics.Canvas;
import android.graphics.Paint;

public class Hook extends Base{
    private float radius;
    private Paint paint;



    public Hook(float x, float y,float radius,Paint paint) {
        super(x, y);
        this.radius=radius;
        this.paint=paint;
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
        if(x>=this.x-radius&&x<=this.x+radius&&y>=this.y-radius&&y<=this.y+radius)
            return true;
        return false;

    }
}
