package com.example.slingkong02;

import android.graphics.Canvas;
import android.graphics.Paint;
import java.util.ArrayList;
import java.util.Random;

public class GameMoule {

    private ArrayList<Hook> Hooks = new ArrayList<Hook>();
    private ArrayList<Saw> Saws = new ArrayList<Saw>();
    private Random random = new Random();
    private float totalDistanceMoved = 0; // Tracks total progress for score
    private int score;

    public GameMoule(ArrayList<Hook> Hooks,ArrayList<Saw> Saws) {
        this.Hooks = Hooks;
        this.Saws=Saws;
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

    public void initDefaultSaws(Paint p, float width, float height) {
        // TODO: 3/25/2026 need to check that it doesnt spawn next to a hook or close to it
        Saws.clear();
        float currentY = height - 500;
        for (int i = 0; i < 2; i++) {
            float x = 100 + random.nextInt((int) width - 200);
            Saws.add(new Saw(x, currentY, 75, p));
            currentY -= 300 + random.nextInt(200);
        }
    }

    public void DrawHooksAndSaws(Canvas canvas) {
        for (Hook hook : Hooks) {
            hook.draw(canvas);

        }
        for (Saw saw : Saws) {
            saw.draw(canvas);
        }
    }


    /**
     * The heart of the scrolling logic. 
     * If the ball goes above 40% of the screen, we move the world down instead.
     */
    public void updateScrolling(Ball b, float screenHeight) { // TODO: 3/20/2026 לתקן את הסלידה, כנראה בצורה שדומה לזאת של הgemini אבל שבאצת יעבוד 
        // TODO: 3/25/2026 make it so that saws don't act like hooks in movement(don't "climb up")
        float scrollThreshold = screenHeight * 0.4f; // Top 40% of screen

        if (b.getY() < scrollThreshold) {
            float shiftDistance = scrollThreshold - b.getY();
            
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
            if (hook.Collision(b.getX(), b.getY())) {
                b.setNewLocation(hook.getX(), hook.getY());
                b.setDx(0);
                b.setDy(0);
                b.setHooked(true);
                ReActivate(b);
                return true;
            }
        }
        return false;
    }

    public boolean isCollideSaws(Ball b) {

        for (Saw saw : Saws) {
            if (saw.Collision(b.getX(), b.getY())) {
                b.setDx(0);
                b.setDy(0);
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

    public void SpawnNewSaws(float screenHeight, float screenWidth) {
        // TODO: 3/25/2026 need to check that it doesnt spawn next to a hook or close to it
        for (Saw saw : Saws) {
            if (saw.getY() > screenHeight) {
                // Find the highest hook to place the recycled one even higher
                float highestY = screenHeight;
                for (Saw s : Saws) {
                    if (s.getY() < highestY) highestY = s.getY();
                }

                float newX = 100 + random.nextInt((int) screenWidth - 200);
                float newY = highestY - (250 + random.nextInt(250));

                saw.SetPosition(newX, newY);

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
