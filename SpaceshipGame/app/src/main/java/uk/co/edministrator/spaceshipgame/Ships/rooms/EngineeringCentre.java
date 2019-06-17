package uk.co.edministrator.spaceshipgame.Ships.rooms;

import android.graphics.Paint;
import android.util.Log;

import uk.co.edministrator.spaceshipgame.Ships.parts.Part;
import uk.co.edministrator.spaceshipgame.gameObjects.Ship;


public class EngineeringCentre extends Room {
    Paint p = new Paint();
    private long cooldown= 0L;


    public EngineeringCentre(int id, Ship ship) {
        this.id = id;
        type = RoomType.ENGINEERING;
        this.ship = ship;
        this.name = "Ship Stabilizer";
    }
    @Override
    public void effect(){
        Log.d("AbilityBut","EC effect");
        if (ship != null) {
            if (System.currentTimeMillis() - cooldown >= 20000) {
                Log.d("Abi","Engi center used");
                cooldown = System.currentTimeMillis();
                for (Part p : ship.getShipParts()) {
                    p.heal(5);
                }
            }
        }
    }
    @Override
    public int getCooldownState() {
        if (System.currentTimeMillis() - cooldown >= 20000)cooldownState = 0;
        else cooldownState = 2;
        return cooldownState;
    }
    @Override
    public void setShip(Ship ship) {
        super.setShip(ship);
    }
}

