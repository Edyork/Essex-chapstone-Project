package uk.co.edministrator.spaceshipgame.weapons;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import uk.co.edministrator.spaceshipgame.gameObjects.Ship;
import uk.co.edministrator.spaceshipgame.tools.FileIO;


public class RocketLauncher extends Weapon implements Serializable {
    private Timer timer;

    //rate of Fire
    private transient TimerTask timerTask = new TimerTask() {


        @Override
        public void run() {
            if (target!= null) {
                bullets.add(new Projectile((int) x, (int)y, 40, 100, 20*speed,dmg,critChance,hitChance, target, sprite, greg,2,context));
                target = null;
            }
        }
    }; //End of Timer
    @Override
    public void start() {
        if(timer != null) {
            return;
        }
        timer = new Timer();
        timer.scheduleAtFixedRate(timerTask, 0, timerDuration);
    }

    public void stop() {
        timer.cancel();
        timer = null;
    }

    public RocketLauncher(Ship parent, Context context, int damage){
        super(parent, context);
        this.dmg = damage;
        this.timerDuration = 8000;
        Random r = new Random();
        int upperbound = (int)(timerDuration * 1.1);
        int lowerbound = (int)(timerDuration*0.9f);
        this.timerDuration = r.nextInt(upperbound - lowerbound)+ lowerbound;
        this.originalTimerDuration = timerDuration;
        this.hitChance = 75;
        this.critChance = 15;
        bullets = new ArrayList<>();
        this.parent = parent;
        x = parent.getX() + parent.getWidth()/2;
        y = parent.getY() + parent.getHeight()/2;
        this.greg = WeaponClass.POWER;
        weaponType = WeaponType.ROCKET;


        name = "Rocket Launcher";
        this.sound = 4;
        FileIO assets = new FileIO(context);
        String projSprite = "Sprites/projectiles/rocket.png";
        InputStream is = assets.readAsset( projSprite);
        sprite = BitmapFactory.decodeStream(is);
        sprite = Bitmap.createScaledBitmap(sprite, 64,32,true);
    }
    @Override
    public void getBitmaps(Context context){
        FileIO assets = new FileIO(context);
        String projSprite = "Sprites/projectiles/rocket.png";
        InputStream is = assets.readAsset( projSprite);
        sprite = BitmapFactory.decodeStream(is);
        sprite = Bitmap.createScaledBitmap(sprite, 64,32,true);
        bitmapAddr = "UI/inventory/rocket.png";
        is = assets.readAsset(bitmapAddr);
        inventorySprite = BitmapFactory.decodeStream(is);
        bitmapAddrEq = "UI/inventory/rocket_eq.png";
        is = assets.readAsset(bitmapAddr);
        inventorySpriteEq = BitmapFactory.decodeStream(is);
        bitmapAddrUnEq = "UI/inventory/rocket_uneq.png";
        is = assets.readAsset(bitmapAddr);
        inventorySpriteUnEq = BitmapFactory.decodeStream(is);
        try {
            is.close();
            assets = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //for Weapons acc
    @Override
    public void accelerate(){
        timerDuration = originalTimerDuration/2; //50% increase
        speed = 2;
    }
    //for resetting weapons acc
    @Override
    public void resetDuration(){

        timerDuration = originalTimerDuration;
    }
}
