package com.example.slingkong02;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import java.util.ArrayList;
import java.util.Random;

public class GameModule {

    private ArrayList<Hook> Hooks = new ArrayList<Hook>();
    private ArrayList<Saw> Saws = new ArrayList<Saw>();
    private Random random = new Random();
    private float totalDistanceMoved = 0; // Tracks total progress for score
    private int score;

    public GameModule(ArrayList<Hook> Hooks,ArrayList<Saw> Saws) {
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
        // Start saws further up so they aren't right next to the ball at spawn
        float currentY = height - 800;
        for (int i = 0; i < 2; i++) {
            float x;
            int attempts = 0;
            // Check against hooks AND already-placed saws
            do {
                x = 100 + random.nextInt((int) width - 200);
                attempts++;
            } while ((isTooCloseToAnyHook(x, currentY) || isTooCloseToAnySaw(x, currentY, null)) && attempts < 30);

            Saws.add(new Saw(x, currentY, 75, sawBitmap));
            // Space saws at least 500px apart vertically to avoid spawning on top of each other
            currentY -= 500 + random.nextInt(200);
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
                float highestY = screenHeight; //check
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
        // Find the highest saw currently on screen (smallest Y = highest on screen)
        // so we can stagger respawned saws above it, not on top of each other
        float highestSawY = ballY - 500; // default: at least 500px above ball
        for (Saw saw : Saws) {
            if (saw.getY() <= screenHeight && saw.getY() < highestSawY) {
                highestSawY = saw.getY();
            }
        }

        for (Saw saw : Saws) {
            if (saw.getY() > screenHeight) {
                float newX;
                // Place the respawned saw above the highest existing saw by 400-700px,
                // and also guarantee at least 500px above the ball
                float baseY = Math.min(highestSawY, ballY - 500);
                float newY = baseY - (400 + random.nextInt(300));
                int attempts = 0;
                // Check against hooks AND other saws so they don't overlap
                do {
                    newX = 100 + random.nextInt((int) screenWidth - 200);
                    attempts++;
                } while ((isTooCloseToAnyHook(newX, newY) || isTooCloseToAnySaw(newX, newY, saw)) && attempts < 30);

                saw.SetPosition(newX, newY);
                // Update highestSawY so the next saw in this loop spawns even higher
                highestSawY = newY;
            }
        }
    }

    /**
     * Returns true if the given (x, y) position is too close to any existing hook.
     */
   private boolean isTooCloseToAnyHook(float x, float y) {
        final float MIN_DISTANCE = 350f;
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

    /**
     * Returns true if the given (x, y) position is too close to any existing saw.
     * Pass the saw being repositioned as excludeSaw so it doesn't compare against itself.
     * Minimum safe distance = saw diameter (150) + 200px buffer = 350px.
     */
    private boolean isTooCloseToAnySaw(float x, float y, Saw excludeSaw) {
        final float MIN_DISTANCE = 350f;
        for (Saw saw : Saws) {
            if (saw == excludeSaw) continue;
            float dx = x - saw.getX();
            float dy = y - saw.getY();
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
