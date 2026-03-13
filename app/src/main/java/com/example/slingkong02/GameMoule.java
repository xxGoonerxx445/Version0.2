package com.example.slingkong02;

import android.graphics.Canvas;
import android.graphics.Paint;
import java.util.ArrayList;
import java.util.Random;

public class GameMoule {

    private ArrayList<Hook> Hooks = new ArrayList<Hook>();
    private Random random = new Random();
    private float Distance;
    private int score;

    public GameMoule(ArrayList<Hook> Hooks) {
        this.Hooks = Hooks;
    }

    /**
     * Initializes hooks and ensures they are not too close to the screen edges.
     */
    public void initDefaultHooks(Paint p, float width, float height) {
        Hooks.clear();
        // Start spawning hooks above the ball's starting position
        float currentY = height - 500; 
        
        for (int i = 0; i < 6; i++) {
            // Margin of 100 pixels from each side to prevent clipping
            float x = 100 + random.nextInt((int) width - 200);
            Hooks.add(new Hook(x, currentY, 75, p));
            currentY -= 300 + random.nextInt(200);
        }
    }

    public void DrawHooks(Canvas canvas) {
        for (int i = 0; i < Hooks.size(); i++) {
            Hooks.get(i).draw(canvas);
        }
    }

    public boolean isCollide(Ball b, float StartY, float Ball_location) {
        if (b.isHooked()) return true;

        for (int i = 0; i < Hooks.size(); i++) {
            if (Hooks.get(i).Collision(b.GetX(), b.GetY())) {
                b.setNewLocation(Hooks.get(i).GetPostionX(), Hooks.get(i).GetPostionY());
                b.setDx(0);
                b.setDy(0);
                b.setHooked(true);
                ReActivate(b);

                // Calculate vertical movement: Negative means moving UP in Android
                float verticalMove = Ball_location - StartY;
                
                // Only store movement for score and shifting if we moved UP (verticalMove < 0)
                if (verticalMove < 0) {
                    Distance = Math.abs(verticalMove);
                    score += CalcScoreFromDistance(Distance);
                } else {
                    Distance = 0; // Don't shift or add score if moving down
                }

                ShiftHooks(b);
                return true;
            }
        }
        return false;
    }

    public void ReActivate(Ball ball) {
        for (int i = 0; i < Hooks.size(); i++) {
            if (!(Hooks.get(i).isHooking(ball)))
                Hooks.get(i).Activate();
        }
    }

    public void ShiftHooks(Ball b) {
        if (Distance <= 0) return; // Only shift if there was upward movement

        int hookedHookIndex = -1;
        for (int i = 0; i < Hooks.size(); i++) {
            if (Hooks.get(i).isHooking(b)) {
                hookedHookIndex = i;
                break;
            }
        }

        // Shift all hooks down to create "scrolling" effect
        for (int i = 0; i < Hooks.size(); i++) {
            Hooks.get(i).setY(Hooks.get(i).getY() + Distance * 0.9f);
        }

        if (hookedHookIndex != -1) {
            Hook hookedHook = Hooks.get(hookedHookIndex);
            b.setNewLocation(hookedHook.getX(), hookedHook.getY());
        }
    }

    public void SpawnNewHooks(float screenHeight, Ball b, float screenWidth) {
        for (int i = 0; i < Hooks.size(); i++) {
            if (Hooks.get(i).getY() > screenHeight) {
                // Recycle hooks to appear at the top
                float y_forrecycle = random.nextInt((int) Math.max(100, b.getY() - 400));
                float x_forrecycle = 100 + random.nextInt((int) screenWidth - 200);
                Hooks.get(i).SetPosition(x_forrecycle, y_forrecycle);
            }
        }
    }

    private int CalcScoreFromDistance(float dist) {
        // Custom score logic: e.g., 1 point for every 100 pixels moved up
        return (int) (dist / 100);
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
        Distance = 0;
    }
}
