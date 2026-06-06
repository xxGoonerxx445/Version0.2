package com.example.slingkong02;

import android.graphics.Canvas;

public abstract class Base { //cant create a base thing, classes can only inherit methods
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
public abstract void draw(Canvas canvas); //cuz we must draw the objects, but every object has its own way to be drawn
}
