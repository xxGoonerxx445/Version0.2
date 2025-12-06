package com.example.slingkong02;

import android.graphics.Canvas;

public abstract class Base {
protected float x,y;
public Base(float x, float y)
{
    this.x=x;
    this.y=y;
}
public float getX()
{
    return x;
}
public float getY()
{
    return y;
}
public abstract void draw(Canvas canvas);}
