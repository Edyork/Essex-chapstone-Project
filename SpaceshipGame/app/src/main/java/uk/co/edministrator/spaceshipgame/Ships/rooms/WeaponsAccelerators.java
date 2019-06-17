package uk.co.edministrator.spaceshipgame.Ships.rooms;

import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

import uk.co.edministrator.spaceshipgame.gameObjects.Ship;
import uk.co.edministrator.spaceshipgame.weapons.Weapon;

public class WeaponsAccelerators extends Room{
    private long cooldown= 0L;
    private Timer T;

    public void start() {
        if(T != null) {
            return;
        }
        T = new Timer();
        TimerTask timerTask = new TimerTask() {

            @Override
            public void run() {
                for (Weapon w: ship.getWeapons()){
                    w.resetDuration();
                    Log.d("accel", "reset weapons acc");
                    cooldownState = 2;
                }
                stop();
            }
        }; //End of Timer
        T.schedule(timerTask, 12000);
    }

    public void stop() {
        T.cancel();
        T = null;
    }

    public WeaponsAccelerators(int id, Ship ship) {
        this.id = id;
        type = RoomType.WEAPONSACC;
        this.ship = ship;
        this.name = "Weapons Accelerator";
    }
    @Override
    public void effect(){
        Log.d("AbilityBut","WA effect");
        if (ship != null) {
            if (System.currentTimeMillis() - cooldown >= 15000) {
                for (Weapon w : ship.getWeapons()){
                    w.accelerate();
                    start();
                    cooldownState = 1;
                }
                Log.d("accel", "weapons acc started");
                cooldown = System.currentTimeMillis();
            }
        }
    }
    @Override
    public int getCooldownState() {
        if (System.currentTimeMillis() - cooldown >= 15000) cooldownState = 0;
        return super.getCooldownState();
    }

    @Override
    public void setShip(Ship ship) {
        super.setShip(ship);
    }
}
