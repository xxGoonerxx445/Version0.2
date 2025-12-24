package com.example.slingkong02;

import android.graphics.Canvas;
import android.graphics.Paint;

public class Hook extends Base{
    private float radius;
    private Paint paint;
    // TODO: 24/12/2025 add isActive to disable hook after you moved the ball to launch 



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
        if(isActive && x>=this.x-radius&&x<=this.x+radius&&y>=this.y-radius&&y<=this.y+radius)
        {
            isActive = false;
            return true;
        }
            
        return false;

    }
}
