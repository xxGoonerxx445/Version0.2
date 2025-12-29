package com.example.slingkong02;

import android.graphics.Canvas;
import android.graphics.Paint;

public class Hook extends Base{
    private float radius;
    private Paint paint;
    private Boolean Active;
    // TODO: 24/12/2025 add isActive to disable hook after you moved the ball to launch 



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



//   public void Activate(Ball ball)
//   {
//       // Check if the hook is currently inactive
//       if (!this.Active) {
//           float ballX = ball.GetX();
//           float ballY = ball.GetY();
//
//           // Reactivate if the ball is outside the bounding box of the hook
//           if (ballX < this.x - radius || ballX > this.x + radius ||
//                   ballY < this.y - radius || ballY > this.y + radius) {
//               this.Active = true;
//           }
//       }
//   }

   /* public void Activate(Ball ball) {
        if (!this.Active) {
            float ballX = ball.GetX();
            float ballY = ball.GetY();
            float distSq = (ballX - x) * (ballX - x) + (ballY - y) * (ballY - y);

            // If ball is further than the radius, reactivate the hook
            if (distSq > radius * radius) {
                this.Active = true;
            }
        }
    }*/
}
