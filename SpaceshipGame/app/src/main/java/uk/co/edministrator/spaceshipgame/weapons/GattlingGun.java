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

public class GattlingGun extends Weapon implements Serializable {
    private Timer timer;
//    private Bitmap inventorySprite;

    //rate of Fire
    private transient TimerTask timerTask = new TimerTask() {


        @Override
        public void run() {
            if (target != null) {
                bullets.add(new Projectile((int)x, (int) y, 25, 50, 25*speed,dmg,critChance,hitChance, target,sprite, greg,2,context));
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

    public GattlingGun(Ship parent, Context context, int damage){
        super(parent, context);
        this.dmg = damage;
        this.timerDuration = 4000;
        Random r = new Random();
        int upperbound = (int)(timerDuration * 1.1);
        int lowerbound = (int)(timerDuration*0.9f);
        this.timerDuration = r.nextInt(upperbound - lowerbound)+ lowerbound;
        this.hitChance = 95;
        this.critChance = 5;
        bullets = new ArrayList<>();
        this.greg = WeaponClass.ACCURATE;
        weaponType = WeaponType.GATTLING;
        this.parent = parent;
        x = parent.getX() + parent.getWidth()/2;
        y = parent.getY() + parent.getHeight()/2;

        state = 0;
        this.name = "Gattling Gun";

        FileIO assets = new FileIO(context);
        String projSprite = "Sprites/projectiles/gattlingBullet.png";
        InputStream is = assets.readAsset( projSprite);
        sprite = BitmapFactory.decodeStream(is);
        sprite = Bitmap.createScaledBitmap(sprite, 125,32,true);
    }
    @Override
    public void getBitmaps(Context context){
        FileIO assets = new FileIO(context);
        String projSprite = "Sprites/projectiles/gattlingBullet.png";
        InputStream is = assets.readAsset( projSprite);
        sprite = BitmapFactory.decodeStream(is);
        sprite = Bitmap.createScaledBitmap(sprite, 125,32,true);
        bitmapAddr = "UI/inventory/gattling.png";
        is = assets.readAsset(bitmapAddr);
        inventorySprite = BitmapFactory.decodeStream(is);
        bitmapAddrEq = "UI/inventory/gattling_eq.png";
        is = assets.readAsset(bitmapAddr);
        inventorySpriteEq = BitmapFactory.decodeStream(is);
        bitmapAddrUnEq = "UI/inventory/gattling_uneq.png";
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
