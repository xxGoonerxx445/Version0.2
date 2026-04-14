package com.example.slingkong02;

import android.graphics.Bitmap;
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
            //float x = 100 + random.nextInt((int) width - 200);
            float x = 125 + random.nextInt((int) width - 225);
            Hooks.add(new Hook(x, currentY, 75, p));
            currentY -= 300 + random.nextInt(200);
        }
    }

    public void initDefaultSaws(Bitmap sawBitmap, float width, float height) {
        Saws.clear();
        float currentY = height - 500;
        for (int i = 0; i < 2; i++) {
            float x;
            int attempts = 0;
            // Keep re-rolling X until the saw is far enough from all hooks,
            // or give up after 20 tries to avoid an infinite loop
            do {
                x = 100 + random.nextInt((int) width - 200);
                attempts++;
            } while (isTooCloseToAnyHook(x, currentY) && attempts < 20);

            Saws.add(new Saw(x, currentY, 75, sawBitmap));
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
    public void updateScrolling(Ball b, float screenHeight) {
        float scrollThreshold = screenHeight * 0.4f; // Top 40% of screen

        if (b.getY() < scrollThreshold) {
            float shiftDistance = scrollThreshold - b.getY();

            // Move ball back to the threshold so it stays visible
            b.setY(scrollThreshold);

            // Move all hooks down
            for (Hook hook : Hooks) {
                hook.setY(hook.getY() + shiftDistance);
            }

            // Move all saws down with the world so they don't stay frozen in place
            // while the player climbs. Unlike hooks, saws are NOT recycled back to
            // the top here — once they fall off the bottom, SpawnNewSaws respawns them
            // above the player instead.
            for (Saw saw : Saws) {
                saw.setY(saw.getY() + shiftDistance);
            }

            // Update total distance and score based on actual movement
            totalDistanceMoved += shiftDistance;
            score = (int) (totalDistanceMoved / 100); // 1 point per 100 pixels climbed
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

                //float newX = 100 + random.nextInt((int) screenWidth - 200);
                float newX = 125 + random.nextInt((int) screenWidth - 225);
                float newY = highestY - (250 + random.nextInt(250));

                hook.SetPosition(newX, newY);
                hook.Activate();
            }
        }
    }

    /**
     * Respawns saws that have fallen off the bottom of the screen.
     * Unlike hooks (which recycle to above the highest hook), saws always
     * respawn above the player's current position so they remain a threat
     * as the player climbs higher.
     *
     * @param ballY the ball's current Y position, used as the spawn anchor
     */
    public void SpawnNewSaws(float screenHeight, float screenWidth, float ballY) {
        for (Saw saw : Saws) {
            if (saw.getY() > screenHeight) {
                // Place the saw 300–600px above the player
                float newX;
                float newY = ballY - (300 + random.nextInt(300));
                int attempts = 0;
                // Keep re-rolling X until the saw doesn't overlap a hook,
                // or give up after 20 tries to avoid an infinite loop
                do {
                    newX = 100 + random.nextInt((int) screenWidth - 200);
                    attempts++;
                } while (isTooCloseToAnyHook(newX, newY) && attempts < 20);

                saw.SetPosition(newX, newY);
            }
        }
    }

    /**
     * Returns true if the given (x, y) position is too close to any existing hook.
     * Used when spawning saws to make sure they never overlap or sit right next to a hook,
     * which would make it impossible for the player to safely grab that hook.
     * Minimum safe distance = hook radius (75) + saw radius (75) + 100px buffer = 250px.
     */
    private boolean isTooCloseToAnyHook(float x, float y) {
        //final float MIN_DISTANCE = 250f;
        final float MIN_DISTANCE = 350f; //fixed threshold, not something that should change mid-function
        for (Hook hook : Hooks) {
            float dx = x - hook.getX();
            float dy = y - hook.getY();
            float distance = (float) Math.sqrt(dx * dx + dy * dy);
            if (distance < MIN_DISTANCE) {
                return true;
            }
        }
        return false;
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
