package com.example.slingkong02;

import android.graphics.Canvas;
import android.graphics.Paint;

public class Saw extends Base{
    private float radius;
    private Paint paint;



    public Saw(float x, float y,float radius,Paint paint) {
        super(x, y);
        this.radius=radius;
        this.paint=paint;

    }


    @Override
    public void draw(Canvas canvas) { // TODO: 3/25/2026 add image instead of circle
        // Add your drawing logic here
        canvas.drawCircle(x,y,radius,paint);

    }
    public void setY(float y) {
        this.y = y;
    }
    public boolean Collision(float x,float y)
    {

        return x >= this.x - radius && x <= this.x + radius && y >= this.y - radius && y <= this.y + radius;

    }

    public void SetPosition(float X,float Y)
    {
        this.y=Y;
        this.x=X;
    }








}
