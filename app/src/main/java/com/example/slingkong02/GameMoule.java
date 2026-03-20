package com.example.slingkong02;

import android.graphics.Canvas;
import android.graphics.Paint;
import java.util.ArrayList;
import java.util.Random;

public class GameMoule {

    private ArrayList<Hook> Hooks = new ArrayList<Hook>();
    private Random random = new Random();
    private float totalDistanceMoved = 0; // Tracks total progress for score
    private int score;

    public GameMoule(ArrayList<Hook> Hooks) {
        this.Hooks = Hooks;
    }

    public void initDefaultHooks(Paint p, float width, float height) {
        Hooks.clear();
        float currentY = height - 500; 
        for (int i = 0; i < 6; i++) {
            float x = 100 + random.nextInt((int) width - 200);
            Hooks.add(new Hook(x, currentY, 75, p));
            currentY -= 300 + random.nextInt(200);
        }
    }

    public void DrawHooks(Canvas canvas) {
        for (Hook hook : Hooks) {
            hook.draw(canvas);
        }
    }

    /**
     * The heart of the scrolling logic. 
     * If the ball goes above 40% of the screen, we move the world down instead.
     */
    public void updateScrolling(Ball b, float screenHeight) {
        float scrollThreshold = screenHeight * 0.4f; // Top 40% of screen

        if (b.GetY() < scrollThreshold) {
            float shiftDistance = scrollThreshold - b.GetY();
            
            // Move ball back to the threshold so it stays visible
            b.setY(scrollThreshold);
            
            // Move all hooks down
            for (Hook hook : Hooks) {
                hook.setY(hook.getY() + shiftDistance);
            }
            
            // Update total distance and score based on actual movement
            totalDistanceMoved += shiftDistance;
            score = (int) (totalDistanceMoved / 50); // 1 point per 50 pixels climbed
        }
    }

    public boolean isCollide(Ball b) {
        if (b.isHooked()) return true;

        for (Hook hook : Hooks) {
            if (hook.Collision(b.GetX(), b.GetY())) {
                b.setNewLocation(hook.GetPostionX(), hook.GetPostionY());
                b.setDx(0);
                b.setDy(0);
                b.setHooked(true);
                ReActivate(b);
                return true;
            }
        }
        return false;
    }

    public void ReActivate(Ball ball) {
        for (Hook hook : Hooks) {
            if (!(hook.isHooking(ball)))
                hook.Activate();
        }
    }

    /**
     * Recycles hooks that fell off the bottom of the screen to the top.
     */
    public void SpawnNewHooks(float screenHeight, float screenWidth) {
        for (Hook hook : Hooks) {
            if (hook.getY() > screenHeight) {
                // Find the highest hook to place the recycled one even higher
                float highestY = screenHeight;
                for (Hook h : Hooks) {
                    if (h.getY() < highestY) highestY = h.getY();
                }

                float newX = 100 + random.nextInt((int) screenWidth - 200);
                float newY = highestY - (250 + random.nextInt(250));
                
                hook.SetPosition(newX, newY);
                hook.Activate();
            }
        }
    }

    public int CalcScore() {
        return score;
    }

    public void ShowScore(Canvas canvas, Paint p, int currentScore) {
        canvas.drawText("Score: " + currentScore, 50, 150, p);
    }

    public void Restart() {
        Hooks.clear();
        score = 0;
        totalDistanceMoved = 0;
    }
}
