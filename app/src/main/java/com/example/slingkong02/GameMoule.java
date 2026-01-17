package com.example.slingkong02;

import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.ArrayList;

public class GameMoule {

    private ArrayList<Hook> Hooks= new ArrayList<Hook>();
    private Boolean flagForThread;



    public GameMoule(ArrayList<Hook> Hooks)
    {
        this.Hooks=Hooks;
    }
    public void initDefaultHooks(Paint p) {
        Hooks.add(new Hook(300, 500, 75, p));
        Hooks.add(new Hook(600, 800, 75, p));
        Hooks.add(new Hook(900, 1100, 75, p));
    }

    public void AddHook(Hook h)
    {
        Hooks.add(h);
    }

    // TODO: 29/12/2025 for each hook in arraylist, if isnt connected to ball, activate
    public void DeAct()
    {

    }
    public void DrawHooks(Canvas canvas)
    {
        for(int i=0;i<Hooks.size();i++)
        {
            Hooks.get(i).draw(canvas);
        }
    }
    //check for collision
    public boolean isCollide(Ball b)
    {
        if (b.isHooked()) return true;

        for(int i=0;i<Hooks.size();i++)
        {
            if(Hooks.get(i).Collision(b.GetX(),b.GetY()))
            {
                b.setNewLocation(Hooks.get(i).GetPostionX(),Hooks.get(i).GetPostionY());
                b.setDx(0); // Stop movement
                b.setDy(0);
                b.setHooked(true);
                ReActivate(b);

                return true;

            }


        }
        return false;
   //
    }
    public void ReActivate(Ball ball)
    {
        for(int i=0;i<Hooks.size();i++)
        {
            if(!(Hooks.get(i).isHooking(ball)))
                Hooks.get(i).Activate();

        }
    }

}
