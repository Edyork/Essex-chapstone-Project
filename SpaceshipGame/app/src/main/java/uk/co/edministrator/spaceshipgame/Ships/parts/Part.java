package uk.co.edministrator.spaceshipgame.Ships.parts;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import uk.co.edministrator.spaceshipgame.Ships.ShipType;
import uk.co.edministrator.spaceshipgame.gameObjects.Ship;


public class Part implements Serializable {
    int id;
    int x, y;
    int hp;
    int maxHp;
    Bitmap sprite;
    Bitmap targetedSprite;
    boolean isAlive;
    transient Ship ship;
    private  transient Paint p = new Paint();
    private ArrayList<DamageDisplay> dmgDis = new ArrayList<>();
    private boolean istarget;

    public void setShip(Ship ship){
        this.ship = ship;
    }
    public void update(){
        ArrayList<DamageDisplay> removal = new ArrayList<>();
        for (DamageDisplay d : dmgDis){
            if (d.getLifetime() > 30){
                removal.add(d);
            }
        }
        dmgDis.removeAll(removal);
    }

    public void hit(int dmg, int type) {
        if (isAlive) {  //prevents hitting dead targets
            setSprite(false);
            if (dmg == 0) {
                dmgDis.add(new DamageDisplay("Missed!",0, this.x + 15, this.y + this.getHeight() / 2));
            } else {
                //SHIELD HP
                if (ship.getShield() > 0) {
                    ship.setShield(dmg);
                    dmgDis.add(new DamageDisplay((int) dmg + "",type, this.x + 15, this.y + this.getHeight() / 2));
                } else {
                    Random r = new Random();
                    int evade = r.nextInt(100);
                    Log.d("Combat", "Evade roll: " + evade + ", Armour: " + ship.getArmour());

                    //EVASION DODGE
                    if (evade > ship.getEvasion()) {
                        //ARMOUR DR
                        float drValue = ship.getArmour();
                        drValue = drValue / 100;
                        drValue = 1 - drValue;
                        drValue = dmg * drValue;
                        hp -= (int) drValue;   //Armour, yes I have to do it this way because of int rounding
                        if (hp < 0) hp = 0; //prevents overkill
                        dmgDis.add(new DamageDisplay((int) drValue + "",type, this.x + 15, this.y + this.getHeight() / 2));
                        Log.d("Combat", "Hit taken dmg: " + dmg + ", DR'd: " + drValue);
                        if (hp <= 0) isAlive = false;
                    } else {
                        Log.d("Combat", "Hit evaded");
                        dmgDis.add(new DamageDisplay("Evaded!", type, this.x + 15, this.y + this.getHeight() / 2));
                    }
                }
            }
        }
    }

    public void setAlive(boolean life){
        isAlive = life;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public int getHp() {
        return hp;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public int getId() {
        return id;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
    public int getWidth(){
        return sprite.getWidth();
    }
    public int getHeight(){
        return sprite.getHeight();
    }

    public Ship getShip() {
        return ship;
    }

    public Rect getBounds(){
        return new Rect(x,y,x + sprite.getWidth(), y + sprite.getHeight());
    }
    public void setLocation(int[] coords){
        x = coords[0];
        y = coords[1];
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public void setSprite(Boolean istarget){
        this.istarget = istarget;
    }

    public void heal(int amount){
        this.hp += amount;
        Log.d("Abilities", "healed to: " + this.hp);
        //if (!isAlive && hp > 50) this.isAlive=true;
    }

    public void draw(Canvas canvas, int anim){
        if (isAlive){
            if (istarget) canvas.drawBitmap(targetedSprite,x,y + anim,p);
            else canvas.drawBitmap(sprite,x,y + anim,p);
            p.setTextSize(50);
            p.setColor(Color.GREEN);
            canvas.drawText(""+hp, x + getWidth()-125,y + getHeight()-25 + anim,p);
        }
        for (DamageDisplay d: dmgDis){
            d.Draw(canvas);
        }
    }
}
