package com.example.slingkong02;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

public class GameMoule {

    private ArrayList<Hook> Hooks= new ArrayList<Hook>();
    private Boolean flagForThread;



    public GameMoule(ArrayList<Hook> Hooks)
    {
        this.Hooks=Hooks;
    }

    // TODO: 05/01/2026 make it that hooks are relativly closer to each other(not a must cuz we might add other stuff later to the screen) 
    public void initDefaultHooks(Paint p,float width,float height) {
        Random R=new Random();
        float tx1=R.nextInt(75,(int)width-75);
        float ty1=R.nextInt(75,(int)height-75);
        Hooks.add(new Hook(tx1, ty1, 75, p));
        //try {}
        for(int i=1; i<3;i++)
        {
            float x1=R.nextInt(75,(int)width-75);
            float y1=R.nextInt(75,(int)height-75);
            if(x1!=tx1||y1!=ty1)
            {
                Hooks.add(new Hook(x1, y1, 75, p));
               tx1=x1;
               ty1=y1;
            }

        }


       // Hooks.add(new Hook(300, 500, 75,p));
       // Hooks.add(new Hook(600, 800, 75, p));
      //  Hooks.add(new Hook(900, 1100, 75, p));
    }
   /* public void RandomizeHooksLocation(float width,float height)
    {
        Random R=new Random();
        float tx1=R.nextInt((int)width);
        float ty1=R.nextInt((int)height);
        //Hooks.get(0).setNewLocation(tx1,ty1)

        for(int i=0;i<Hooks.size();i++)
        {
            float x=R.nextInt((int)width);
            float y=R.nextInt((int)height);


        }

    }*/

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
