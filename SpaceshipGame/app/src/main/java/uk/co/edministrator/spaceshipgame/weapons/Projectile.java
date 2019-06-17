package uk.co.edministrator.spaceshipgame.weapons;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import java.io.InputStream;
import java.io.Serializable;
import java.util.Random;

import uk.co.edministrator.spaceshipgame.Ships.ShipType;
import uk.co.edministrator.spaceshipgame.Ships.parts.Part;
import uk.co.edministrator.spaceshipgame.tools.FileIO;

public class Projectile implements Serializable {
    protected transient int x, y, speed, width, height, dmg;
    protected transient boolean Alive;
    protected transient Part target;
    protected Bitmap sprite;
    protected WeaponClass type;
    protected int hitChance, critChance;
    public boolean hasStarted = false;

    public Projectile(int x, int y, int height, int width, int speed, int dmg, int crit, int hit, Part target, Bitmap sprite, WeaponClass type, int sfx, Context context){
        this.sprite = sprite;
        if (this.sprite == null){
            FileIO assets = new FileIO(context);
            InputStream is = assets.readAsset("Sprites/projectiles/blueBlast.png");
            this.sprite = BitmapFactory.decodeStream(is);
            this.sprite = Bitmap.createScaledBitmap(sprite, 32,32,true);
        }
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.speed = speed;
        this.target = target;
        this.type  = type;
        this.dmg = dmg;
        Alive = true;
        this.critChance = crit;
        this.hitChance = hit;
        boolean hasFired = false;
        this.target.setSprite(true);
    }
    //Copy proj (for gattling gun)
    public Projectile(Projectile projectile){
        this.x = projectile.x;
        this.y = projectile.y;
        this.width = projectile.width;
        this.height = projectile.height;
        this.speed = projectile.speed;
        this.target = projectile.target;
        this.type = projectile.type;
        this.dmg = projectile.dmg;
        Alive = true;
        this.sprite = projectile.sprite;

    }
    public void setupSound(Context context){

    }
    public void Move(){
        //calc angle (direction)
        float deltaX = (target.getX() + target.getWidth()/2) - x;
        float deltaY = (target.getY() + target.getHeight()/2) - y;
        double angle = Math.atan2(deltaY,deltaX);
        x += speed * Math.cos(angle);
        y += speed * Math.sin(angle);
        collision();
    }
    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public int getSpeed() {
        return speed;
    }

    public boolean isAlive() {
        return Alive;
    }

    public void setAlive(boolean alive) {
        Alive = alive;
    }
    public Rect getBounds(){
        return new Rect(x,y,x + width, y + height);
    }

    public void collision(){
        if (Alive) {
            int text = 0;   //0: normal, 1: crit, 2: power bonus, 3: evasion bonus, 4: shield bonus.
            if (getBounds().intersect(target.getBounds())) {
                this.setAlive(false);
                //calc if hit or miss
                Random r = new Random();
                int hit = r.nextInt(100);
                ShipType shipType = target.getShip().getShipType();
                if (hit < hitChance) {
                    Log.d("Combat", "Hit!");
                    int crit = r.nextInt(100);
                    if (crit < critChance)
                    {
                        dmg = dmg + (dmg/2);
                        text = 1;
                    }
                    switch (shipType) {
                        case ARMOUR:
                            if (type == WeaponClass.POWER) {
                                float n = (float) dmg;
                                n *= 2;
                                dmg = (int) n;
                                text = 2;
                            }
                            if (type == WeaponClass.ACCURATE){
                                int n = dmg / 5;
                                dmg -= n;   //20% less
                            text = 5;
                        }
                            break;
                        case EVASION:
                            if (type == WeaponClass.ACCURATE) {
                                dmg = dmg * 2;
                                text = 3;
                            }
                            if (type == WeaponClass.ENERGY){
                                int n = dmg / 5;
                                dmg -= n;   //20% less
                                text = 5;
                            }
                            break;
                        case ENERGY:
                            if (type == WeaponClass.ENERGY) {
                                if (target.getShip().getShield() > 0) dmg = dmg * 2;
                                text = 4;
                            }
                            if (type == WeaponClass.POWER){
                                int n = dmg / 5;
                                dmg -= n;   //20% less
                                text = 5;
                            }
                            break;
                    }
                    target.hit(dmg, text);
                }
                else
                {
                    Log.d("Combat", "Miss!");
                    target.hit(0, 0);
                }
            }
        }
    }

    public void setNewPos(float x){
        this.x = (int) x;
    }
    public void draw(Canvas canvas, Paint p, int rot) {
        if (Alive) {
            Matrix rotator = new Matrix();
            rotator.postRotate(rot, 0, 0);
            if (rot > 0) {
                rotator.postTranslate(x + width, y + height);
            } else {
                rotator.postTranslate(x, y);
            }
            canvas.drawBitmap(sprite, rotator, p);
        }
    }


}
