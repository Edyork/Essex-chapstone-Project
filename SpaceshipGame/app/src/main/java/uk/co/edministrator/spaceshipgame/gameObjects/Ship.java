package uk.co.edministrator.spaceshipgame.gameObjects;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;

import uk.co.edministrator.spaceshipgame.Ships.parts.Bridge;
import uk.co.edministrator.spaceshipgame.Ships.parts.Engine;
import uk.co.edministrator.spaceshipgame.Ships.parts.Hull;
import uk.co.edministrator.spaceshipgame.Ships.parts.Part;
import uk.co.edministrator.spaceshipgame.Ships.ShipClass;
import uk.co.edministrator.spaceshipgame.Ships.ShipType;
import uk.co.edministrator.spaceshipgame.Ships.parts.WeaponLocations;
import uk.co.edministrator.spaceshipgame.Ships.rooms.Room;
import uk.co.edministrator.spaceshipgame.weapons.Weapon;

public class Ship implements Serializable {

    protected int id;
    public boolean isAlive;
    protected Part target;

    /** Ships Stats */
    protected int totalHp,
            currentHp,
            armour,
            evasion,
            shield,
            NumOfParts,
            weaponSlots,
            crewSlots,
            speed,
            points,
            modifier,
            level = 1,
            xp = 0,
            xpThreshold = 300;
    protected ShipType shipType;
    protected ShipClass shipClass;

    /**Ship Equipment*/
    public  ArrayList<Part> shipParts = new ArrayList<>();
    protected transient ArrayList<Room> rooms = new ArrayList<>();
    protected transient ArrayList<Weapon> weapons =new ArrayList<>();
    protected int[] bridgeLocation;
    protected int[] hullLocation;
    protected int[][] enginesLocation;
    protected int[][] weaponLocations;

    /** Parts */
    protected Hull hull;
    protected Bridge bridge;
    protected ArrayList<Engine> engines = new ArrayList<>();
    protected ArrayList<WeaponLocations> weaponsPoints = new ArrayList<>();

    /** Sprite info **/
    protected Bitmap sprite;
    protected float x;
    protected float y;
    protected float width;
    protected float height;
    protected int rot;
    private transient Paint p = new Paint();
    protected int anim = 0;
    boolean up;

    public boolean isAlive() {
        return isAlive;
    }


    /** Getters */

    public ShipClass getShipClass() {
        return shipClass;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getWidth() {
        return sprite.getWidth();
    }

    public float getHeight() {
        return sprite.getHeight();
    }

    public int getCurrentHp(){
        int n = 0;
        for (Part r : shipParts) {
        n += r.getHp();
    }
        currentHp = n;
        return currentHp;
    }

    public int getTotalHp() {
        return totalHp;
    }

    public ShipType getShipType() {
        return shipType;
    }

    public int getArmour() {
        return armour;
    }

    public int getEvasion() {
        return evasion;
    }

    public int getShield() {
        return shield;
    }

    public ArrayList<Weapon> getWeapons() {
        return weapons;
    }

    public ArrayList<Room> getRooms() {
        return rooms;
    }

    public Rect getBounds(){
        return new Rect((int)x,(int)y, (int)(x + width), (int)(y + height));
    }

    public int getId() {
        return id;
    }


    public int getWeaponSlots() {
        return weaponSlots;
    }

    public int[][] getWeaponLocations() {
        return weaponLocations;
    }

    public ArrayList<Part> getShipParts() {
        return shipParts;
    }
    /** Setters */

    public void setAlive(boolean alive) {
        isAlive = alive;
    }
    public void addEvasion(int evasion) {
        this.evasion += evasion;
    }
    public void removeEvasion(int evasion){
        this.evasion -= evasion;
    }

    public void setWeapons(ArrayList<Weapon> weapons) {
        this.weapons = weapons;
    }

    public void setRooms(ArrayList<Room> rooms) {
        this.rooms = rooms;
    }

    public void setShield(int dmg) {
        this.shield -= dmg;
        Log.d("Combat", "Shield hp: "+shield);
    }

    public void setTarget(Part target){
        Log.d("Combat", "Set new ShipPart");
        this.target = target;
        for (Weapon w : weapons){
            w.setTarget(target);
        }
    }
    public void addWeapons(Weapon w){
        weapons.add(w);
    }

    public void addRooms(Room r){rooms.add(r);}

    public void Shoot(){
        for (Weapon w : weapons){
            w.shoot();
        }
    }
    public void update(){
        if (currentHp <= 0){
            isAlive = false;
        }
        if (anim >= 100) up = false;
        if (anim <= 0) up = true;

        if (up) anim++;
        else anim--;
    }

    public Bitmap getSprite() {
        return sprite;
    }

    public void reinit(){
        for (Room r : rooms){
            r.setShip(this);
        }
        for (Part p : shipParts){
            p.setShip(this);
        }
    }
    public int calcPoints(){
        float totalDPS = 0;
        for (Weapon w : weapons){
            totalDPS += ((float)w.getDmg() / (float)(w.getTimerDuration()/60)) * 1000;
        }
        points = totalHp + shield + (int)totalDPS;
        return points;
    }

    public void setXp(int xp) {
        this.xp = xp;
        if (xp >= xpThreshold)
            levelUp();
    }

    public int getXp() {
        return xp;
    }

    public void addXp(int xp) {
        this.xp += xp;
        if (xp >= xpThreshold)
            levelUp();
    }

    public int getXpThreshold() {
        return xpThreshold;
    }

    public int getLevel() {
        return level;
    }

    public void levelUp(){
        level++;
        double temp = ((double)totalHp * 1.05);
        Log.d("Level","HP increase: "+temp);
        totalHp = (int)temp;
        for (Part p : shipParts){
            p.setHp(totalHp/NumOfParts);
        }
        xpThreshold = (int) ((float) xpThreshold * 1.5);
    }
    public void draw(Canvas canvas){
        //EMP explosion;


        Matrix rotator = new Matrix();
        rotator.postTranslate(x, y + anim);
        if (isAlive) {
            p.setColor(Color.BLUE);
            //canvas.drawRect(x,y,t, p);
//            p.setTextSize(50);

            canvas.drawBitmap(sprite, rotator, p);
//            canvas.drawText("Point value: " + points, x+width, y + height,p);
            for (Part p : shipParts) p.draw(canvas, anim);
            for (Weapon w : weapons) {
                w.draw(canvas, rot);
            }
        }
        if (shield > 0){
            p.setStyle(Paint.Style.STROKE);
            p.setStrokeWidth(5);
            p.setARGB(150,84,193,255);
            canvas.drawOval((int)(x * 1),(int)(y*1)+ anim,x+ (int)(sprite.getWidth()*1.1),y+(int)(sprite.getHeight()*1.05)+anim ,p);
            p.setStyle(Paint.Style.FILL_AND_STROKE);
            p.setARGB(50,84,193,255);
            canvas.drawOval((int)(x * 1),(int)(y*1)+ anim,x+ (int)(sprite.getWidth()*1.1),y+(int)(sprite.getHeight()*1.05)+ anim ,p);
            p.setTextSize(55);
            p.setARGB(255, 142,221,255);
            if (shipClass == ShipClass.FIGHTER)
            canvas.drawText("Shield: "+shield, x+ sprite.getWidth(), y + 45 + anim,p);
            else canvas.drawText("Shield: "+shield, x - 55, y + 45 + anim,p);
        }
    }
    public void ChangeColor(){
        p.setColor(Color.RED);
    }
}
