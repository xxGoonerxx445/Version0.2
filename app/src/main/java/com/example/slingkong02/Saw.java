package com.example.slingkong02;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;

public class Saw extends Base{
    private float radius;
    private Bitmap bitmap;
    // הגדרת האובייקט פעם אחת בלבד כדי למנוע הקצאות זיכרון מיותרות
    private final RectF rect = new RectF();

    public Saw(float x, float y, float radius, Bitmap bitmap) {
        super(x, y);
        this.radius = radius;
        this.bitmap = bitmap;
    }


    @Override
    public void draw(Canvas canvas) {
        if (bitmap != null) {
            // עדכון הערכים של האובייקט הקיים במקום יצירת חדש
            rect.set(x - radius, y - radius, x + radius, y + radius);
            canvas.drawBitmap(bitmap, null, rect, null);
        }
    }

    public void setY(float y) {
        this.y = y;
    }

    public boolean Collision(float x, float y)
    {
        return x >= this.x - radius && x <= this.x + radius && y >= this.y - radius && y <= this.y + radius;
    }

    public void SetPosition(float X, float Y)
    {
        this.y = Y;
        this.x = X;
    }
}
