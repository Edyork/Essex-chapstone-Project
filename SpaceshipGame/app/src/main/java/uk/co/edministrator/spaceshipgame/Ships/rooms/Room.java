package uk.co.edministrator.spaceshipgame.Ships.rooms;

import android.util.Log;

import java.io.Serializable;
import java.util.Timer;

import uk.co.edministrator.spaceshipgame.Ships.parts.Part;
import uk.co.edministrator.spaceshipgame.gameObjects.Ship;

public class Room implements Serializable {
    int id;
    int cooldownState;  //0: available, 1: in use, 2: on cd
    RoomType type;
    int timerDuration;
    transient Ship ship;
    String name;

    private transient Timer timer;


    public void effect(){

    }
    public void effect(Part p){
        Log.d("abi", "calling wrong method");
    }

    public RoomType getType() {
        return type;
    }

    public Ship getShip() {
        return ship;
    }

    public int getId() {
        return id;
    }

    public int getCooldownState() {
        return cooldownState;
    }

    public void setCooldownState(int i){
        cooldownState = i;
    }
    public void setShip(Ship ship){
        this.ship = ship;
    }

    public String getName() {
        return name;
    }
}
