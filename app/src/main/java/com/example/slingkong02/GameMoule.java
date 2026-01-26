package com.example.slingkong02;

import android.graphics.Canvas;
import android.graphics.Paint;
import java.util.ArrayList;
import java.util.Random;

public class GameMoule {

    private ArrayList<Hook> Hooks = new ArrayList<Hook>();
    private Random random = new Random();
    private float Distance;

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

    public void DrawHooks(Canvas canvas) {
        for (int i = 0; i < Hooks.size(); i++) {
            Hooks.get(i).draw(canvas);
        }
    }

    public boolean isCollide(Ball b,float StartY,float Ball_location) {
        if (b.isHooked()) return true;


        for (int i = 0; i < Hooks.size(); i++) {
            if (Hooks.get(i).Collision(b.GetX(), b.GetY())) {
                b.setNewLocation(Hooks.get(i).GetPostionX(), Hooks.get(i).GetPostionY());
                b.setDx(0);
                b.setDy(0);
                b.setHooked(true);
                ReActivate(b);
                Distance=Ball_location-StartY;
                if(Distance<0)
                    Distance=-Distance;

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
    public void ShiftHooks(Ball b) //make it so only the other hooks go down... after fixing that make them reappear again
    {
        //i think that i solved it
        for(int i=0; i< Hooks.size(); i++)
        {
            //Hooks.get(i).setY(Hooks.get(i).getY()+Distance);
            //b.setNewLocation(Hooks.get(i).getX(),Hooks.get(i).getY());
            if(!(Hooks.get(i).isHooking(b)))
            {Hooks.get(i).setY(Hooks.get(i).getY()+Distance);}
        }
    }
    //

    // TODO: 26/01/2026 need to add moving animation in order to make this work 
    // TODO: 26/01/2026 recycling hooks, for now, may make it create new hooks in the future
    // TODO: 25/01/2026 spawnNewHooks: spawn new after a great distance has been made, also if the hooks moved under the screen delete them. 
    public void SpawnNewHooks(float screenHeight,Ball b,float screenWidth)
    {
        for(int i=0; i<Hooks.size(); i++)
        {
            if(Hooks.get(i).getY()>screenHeight)
            {
                float y_forrecycle =random.nextFloat(0,b.getY());
                float x_forrecycle= random.nextFloat(0,screenWidth);
                Hooks.get(i).SetPosition(x_forrecycle,y_forrecycle);
            }

        }
    }
}
