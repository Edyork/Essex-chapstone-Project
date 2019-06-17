package uk.co.edministrator.spaceshipgame.weapons;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import uk.co.edministrator.spaceshipgame.CombatActivity;
import uk.co.edministrator.spaceshipgame.Ships.parts.Part;
import uk.co.edministrator.spaceshipgame.gameObjects.Ship;
import uk.co.edministrator.spaceshipgame.tools.FileIO;

public class Weapon implements Serializable {
    int id = 0;
    int speed = 1;
    int dmg;
    int timerDuration;
    int originalTimerDuration;
    int hitChance;
    int critChance;
    public Part target;
    WeaponClass greg;
    WeaponType weaponType;
    private boolean equiped = false;
    Bitmap inventorySprite;
    Bitmap inventorySpriteEq;
    Bitmap inventorySpriteUnEq;
    String name = "";
    public ArrayList<Projectile> bullets = new ArrayList<>();
    transient Ship parent;
    transient Timer timer;
    Bitmap sprite;
    String bitmapAddr;
    String bitmapAddrEq;
    String bitmapAddrUnEq;
    int state;  //0: available, 1: selected, 2: on cooldown
    float x,y;
    Context context;
    int sound;




    public void start() {
        if(timer != null) {
            return;
        }
        timer = new Timer();
        TimerTask timerTask = new TimerTask() {

            @Override
            public void run() {
                if (target!= null) {
                    bullets.add(new Projectile((int) x, (int) y, 25, 50, 20*speed, dmg, 10, 80, target, sprite , greg,2, context));
                    Log.d("accel","generic " + timerDuration);
                    target = null;
                }
            }
        }; //End of Timer
        timer.scheduleAtFixedRate(timerTask, 0, timerDuration);
    }

    public void stop() {
        timer.cancel();
        timer = null;
    }

    public Weapon(Ship parent, Context context){
        bullets = new ArrayList<Projectile>();
        this.x = parent.getX();
        this.y = parent.getY();
        this.dmg = 5;
        this.timerDuration = 30;
        this.originalTimerDuration = timerDuration;
        this.hitChance = 100;
        bullets = new ArrayList<>();
        this.parent = parent;
        state = 0;
        this.name = "Basic Laser canon mk.1";
        FileIO assets = new FileIO(context);
        String projSprite = "Sprites/projectiles/blueBlast.png";
        InputStream is = assets.readAsset( projSprite);
        sprite = BitmapFactory.decodeStream(is);
        sprite = Bitmap.createScaledBitmap(sprite, 32,32,true);
    }

    public void getBitmaps(Context context){

        FileIO assets = new FileIO(context);
        String projSprite = "Sprites/projectiles/blueBlast.png";
        InputStream is = assets.readAsset( projSprite);
        sprite = BitmapFactory.decodeStream(is);
        sprite = Bitmap.createScaledBitmap(sprite, 32,32,true);
        bitmapAddr = "UI/inventory/blaster.png";
        is = assets.readAsset(bitmapAddr);
        inventorySprite = BitmapFactory.decodeStream(is);
        bitmapAddrEq = "UI/inventory/blaster_eq.png";
        is = assets.readAsset(bitmapAddr);
        inventorySpriteEq = BitmapFactory.decodeStream(is);
        bitmapAddrUnEq = "UI/inventory/blaster_uneq.png";
        is = assets.readAsset(bitmapAddr);
        inventorySpriteUnEq = BitmapFactory.decodeStream(is);
        try {
            is.close();
            assets = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setTarget(Part target){
        this.target = target;
        start();
    }
    public void shoot() {
        if (bullets !=null) {
            ArrayList<Projectile> isdead = new ArrayList<>();
            for (Projectile bullet : bullets) {
                if (!bullet.hasStarted){
                    bullet.hasStarted = true;
                   CombatActivity.PlaySound(sound);
                    Log.d("Pew", "pew");
                }
                bullet.Move();
                if (!bullet.isAlive()){
                    isdead.add(bullet);
                }
            }
            bullets.removeAll(isdead);
//            if (!target.isAlive()) {
//                target = null;
//            }
        }
    }

    public void setLocation(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public WeaponClass getGreg() {
        return greg;
    }

    public WeaponType getWeaponType() {
        return weaponType;
    }

    public ArrayList<Projectile> getBullets() {
        return bullets;
    }

    public void setBullets(ArrayList<Projectile> bullets) {
        this.bullets = bullets;
    }

    public boolean isEquiped() {
        return equiped;
    }

    public String getName() {
        return name;
    }

    public void setEquiped(boolean equiped) {
        this.equiped = equiped;
    }

    public Bitmap getInventorySprite() {
        return inventorySprite;
    }

    public String getBitmapAddr() {
        return bitmapAddr;
    }

    public String getBitmapAddrEq() {
        return bitmapAddrEq;
    }

    public String getBitmapAddrUnEq() {
        return bitmapAddrUnEq;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public Timer getTimer() {
        return timer;
    }

    public void setTimer(Timer timer) {
        this.timer = timer;
    }

    public int getDmg() {
        return dmg;
    }

    public int getTimerDuration() {

        return timerDuration;
    }

    public void setTimerDuration(int timerDuration) {
        this.timerDuration = timerDuration;
    }

    public int getOriginalTimerDuration() {
        return originalTimerDuration;
    }

    //for Weapons acc
    public void accelerate(){
        timerDuration = originalTimerDuration/2; //50% increase
        speed = 2;
    }
    //for resetting weapons acc
    public void resetDuration(){
        timerDuration = originalTimerDuration;
        speed = 1;
    }

    public void emptyBullets() {
        bullets.clear();
    }

    public void setParent(Ship parent) {
        this.parent = parent;
    }

    public void draw(Canvas canvas, int rot){
        Paint p = new Paint();
        if (bullets != null) {
            for (Projectile bullet : bullets) {
                bullet.draw(canvas, p, rot);
            }
        }
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
