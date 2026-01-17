package com.example.slingkong02;

import android.graphics.Canvas;
import android.graphics.Paint;
import java.util.ArrayList;
import java.util.Random;

public class GameMoule {

    private ArrayList<Hook> Hooks = new ArrayList<Hook>();
    private Random random = new Random();

    public GameMoule(ArrayList<Hook> Hooks) {
        this.Hooks = Hooks;
    }

    public void initDefaultHooks(Paint p, float width, float height) {
        Hooks.clear();
        // Start spawning hooks above the ball's starting position
        float currentY = height - 500; 
        
        for (int i = 0; i < 6; i++) {
            float x = 75 + random.nextInt((int) width - 150);
            Hooks.add(new Hook(x, currentY, 75, p));
            currentY -= 300 + random.nextInt(200);
        }
    }

    public void shiftHooks(float offset, float width, float height, Paint p) {
        for (Hook h : Hooks) {
            h.setY(h.getY() + offset);
        }

        for (int i = Hooks.size() - 1; i >= 0; i--) {
            if (Hooks.get(i).getY() > height + 500) {
                Hooks.remove(i);
            }
        }

        float minY = height;
        for (Hook h : Hooks) {
            if (h.getY() < minY) {
                minY = h.getY();
            }
        } ////

        while (minY > -height) {
            float x = 75 + random.nextInt((int) width - 150);
            minY -= 300 + random.nextInt(200);
            Hooks.add(new Hook(x, minY, 75, p));
        }
    }

    public void DrawHooks(Canvas canvas) {
        for (int i = 0; i < Hooks.size(); i++) {
            Hooks.get(i).draw(canvas);
        }
    }

    public boolean isCollide(Ball b) {
        if (b.isHooked()) return true;

        for (int i = 0; i < Hooks.size(); i++) {
            if (Hooks.get(i).Collision(b.GetX(), b.GetY())) {
                b.setNewLocation(Hooks.get(i).GetPostionX(), Hooks.get(i).GetPostionY());
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
        for (int i = 0; i < Hooks.size(); i++) {
            if (!(Hooks.get(i).isHooking(ball)))
                Hooks.get(i).Activate();
        }
    }
}
