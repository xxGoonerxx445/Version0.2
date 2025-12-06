package com.example.slingkong02;

import android.graphics.Canvas;

public class Hook extends Base{


    public Hook(float x, float y) {
        super(x, y);
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
    }
}
