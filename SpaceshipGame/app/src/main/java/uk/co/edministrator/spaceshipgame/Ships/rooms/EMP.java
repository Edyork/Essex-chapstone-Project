package uk.co.edministrator.spaceshipgame.Ships.rooms;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

import uk.co.edministrator.spaceshipgame.CONSTANTS;
import uk.co.edministrator.spaceshipgame.Ships.parts.Part;
import uk.co.edministrator.spaceshipgame.gameObjects.Ship;

public class EMP extends Room {
    private Paint p = new Paint();
    private long cooldown= 0L;
    private int x,y,r;

    private Timer T;
    private Ship target;

    public void start() {
        if(T != null) {
            return;
        }
        T = new Timer();
        TimerTask timerTask = new TimerTask() {

            @Override
            public void run() {
                r += 1;
                Log.d("abi","r: " +r);
                for (Part p : target.getShipParts()) {
                    if (x + r == p.getX()){
                        p.heal(-5);
                    }
                }
                if (r >= CONSTANTS.getScreenWidth()*2.5){
                    stop();
                    r = 0;
                    cooldownState = 2;
                }
            }
        }; //End of Timer
        T.scheduleAtFixedRate(timerTask, 0,1);
    }

    public void stop() {
        T.cancel();
        T = null;
    }

    public EMP(int id, Ship ship) {
        this.id = id;
        type = RoomType.EMP;
        this.ship = ship;
        this.x = (int)(ship.getX()+ ship.getWidth()/2);
        this.y = (int) (ship.getY() + ship.getHeight()/2);
        this.r = 1;
        this.name = "EMP bomb";
    }

    public void effect(Ship target){
        Log.d("AbilityBut","EMP effect");
        if (ship != null) {
            if (System.currentTimeMillis() - cooldown >= 20000) {
                cooldown = System.currentTimeMillis();
                this.target = target;
//                for (Part p : target.getShipParts()){
//                    p.heal(-5);
//                }
                start();
                cooldownState = 1;

            }
        }
    }

    @Override
    public int getCooldownState() {
        if (System.currentTimeMillis() - cooldown >= 20000) cooldownState = 0;
        return super.getCooldownState();
    }

    @Override
    public void setShip(Ship ship) {
        super.setShip(ship);
    }

    public void draw(Canvas canvas){
        if (cooldownState == 1) {
            p.setStyle(Paint.Style.STROKE);
            p.setStrokeWidth(5);
            p.setARGB(255,0,221,255);
            canvas.drawCircle(x, y, r, p);
            p.setStrokeWidth(10);
            p.setARGB(150,150,211,255);
            canvas.drawCircle(x, y, r-10, p);
        }
    }
}
