package com.example.slingkong02;

import java.util.ArrayList;

public class GameMoule {

    private ArrayList<Hook> Hooks= new ArrayList<Hook>();
    private Boolean flagForThread;

    public GameMoule(ArrayList<Hook> Hooks)
    {
        this.Hooks=Hooks;

    }
    public void AddHook(Hook h)
    {
        Hooks.add(h);
    }

    // TODO: 29/12/2025 for each hook in arraylist, if isnt connected to ball, activate
    public void DeAct()
    {

    }
    //check for collision
    public void isCollide(Ball b)
    {
        for(int i=0;i<Hooks.size();i++)
        {
            if(Hooks.get(i).Collision(b.GetX(),b.GetY()))
            {
                b.setNewLocation(Hooks.get(i).GetPostionX(),Hooks.get(i).GetPostionY());
                b.setDx(0); // Stop movement
                b.setDy(0);

                //F = true; // Stop the thread from calling move()
            }
        }

    }

}
