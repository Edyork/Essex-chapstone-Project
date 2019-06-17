package uk.co.edministrator.spaceshipgame.Ships.rooms;

import android.graphics.Paint;
import android.util.Log;

import uk.co.edministrator.spaceshipgame.Ships.parts.Part;
import uk.co.edministrator.spaceshipgame.gameObjects.Ship;


public class ShieldGenerator extends Room {
    Paint p = new Paint();
    private long cooldown= 0L;

    public ShieldGenerator(int id, Ship ship) {
        this.id = id;
        type = RoomType.SHIELDGEN;
        this.ship = ship;
        this.name = "Focused Repair";
    }
    @Override
    public void effect(Part p){
        Log.d("AbilityBut","SG effect");
        if (ship != null) {
            if (System.currentTimeMillis() - cooldown >= 10000) {
                Log.d("Abi","Shield gen used");
                    p.heal(20);
                    cooldown = System.currentTimeMillis();
                    cooldownState = 2;
            }
        }
    }
    @Override
    public int getCooldownState() {
        if (System.currentTimeMillis() - cooldown >= 10000)cooldownState = 0;
        return cooldownState;
    }
    @Override
    public void setShip(Ship ship) {
        super.setShip(ship);
    }
}

