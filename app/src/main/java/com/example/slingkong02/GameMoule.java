package com.example.slingkong02;

import android.graphics.Canvas;
import android.graphics.Paint;
import java.util.ArrayList;
import java.util.Random;

public class GameMoule {

    private ArrayList<Hook> Hooks = new ArrayList<Hook>();
    private Random random = new Random();
    //private int tmp;
    private float Distance;

    public GameMoule(ArrayList<Hook> Hooks) {
        this.Hooks = Hooks;
    }

    // TODO: 04/02/2026 make the initdefualt no spawn hooks too close to the edges of the screen 
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
    //shiftHooks has a bug that bump the first hook to the top
    /*public void ShiftHooks(Ball b) //make it so only the other hooks go down... after fixing that make them reappear again
    {
        int HookedHook=-1;
        for(int i=0; i< Hooks.size(); i++) //line 72 fix- check which hook hooked him
        {
            if (Hooks.get(i).isHooking(b))
                HookedHook = i;
            break;
        }
            //--------------------------------------//
        for(int i=0; i< Hooks.size(); i++)
        {
            Hooks.get(i).setY(Hooks.get(i).getY()+Distance*0.8f);
            //b.setNewLocation(Hooks.get(tmp).getX(),Hooks.get(tmp).getY());

        }



    }*/
    public void ShiftHooks(Ball b)
    {
        int hookedHookIndex = -1;
        // 1. Find the index of the hook the ball is currently hooked to (FIRST)
        for (int i = 0; i < Hooks.size(); i++) {
            if (Hooks.get(i).isHooking(b)) {
                hookedHookIndex = i;
                break; // Found the hooked hook, exit this loop
            }
        }

        // 2. Shift all hooks (SECOND)
        for (int i = 0; i < Hooks.size(); i++) {
            Hooks.get(i).setY(Hooks.get(i).getY() + Distance * 0.9f);
        }

        // 3. If the ball is hooked, update its position to match the new position of the hooked hook (LAST)
        if (hookedHookIndex != -1) {
            Hook hookedHook = Hooks.get(hookedHookIndex);
            b.setNewLocation(hookedHook.getX(), hookedHook.getY());
        }
    }



    // TODO: 26/01/2026 need to add moving animation in order to make this work 
    // TODO: 26/01/2026 recycling hooks, for now, may make it create new hooks in the future
    // TODO: 25/01/2026 spawnNewHooks: spawn new after a great distance has been made, also if the hooks moved under the screen delete them. 
    public void SpawnNewHooks(float screenHeight,Ball b,float screenWidth) //--bugs: if you hook on a hook that is close to the end of the screen, the hook bumps to the top of the screen with the ball
    {
        for(int i=0; i<Hooks.size(); i++)
        {
            if(Hooks.get(i).getY()>screenHeight)
            {
                //give number between 0 and the location of ball y(so above ball)
                float y_forrecycle = random.nextInt((int) b.getY()-200);
                float x_forrecycle = random.nextInt((int) screenWidth); //between 0 and width
                for(int j=0; j<Hooks.size(); j++)
                {
                    if(i!=j&&Hooks.get(j).getX()>x_forrecycle-75 && Hooks.get(j).getX()<x_forrecycle+75)
                        x_forrecycle = random.nextInt((int) screenWidth);

                    if(i!=j&&Hooks.get(j).getY()>y_forrecycle-75 && Hooks.get(j).getY()<y_forrecycle+75)
                        y_forrecycle = random.nextInt((int) b.getY()-200);
                }
                Hooks.get(i).SetPosition(x_forrecycle,y_forrecycle);
            }

        }
    }
    public int CalcScore()
    {
        return 10;
    }
    public void ShowScore(Canvas canvas,Paint p,int score)
    {
        canvas.drawText("Score= "+score,5,150,p);
    }

    public void FixedShiftHooks(Ball b,float distance)
    {


        for(int i=0; i< Hooks.size(); i++)
        {

        }
    }
}
