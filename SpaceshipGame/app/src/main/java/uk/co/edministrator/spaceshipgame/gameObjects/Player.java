package uk.co.edministrator.spaceshipgame.gameObjects;


import android.app.Activity;
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
import java.util.ArrayList;

import uk.co.edministrator.spaceshipgame.CONSTANTS;
import uk.co.edministrator.spaceshipgame.Ships.parts.Part;
import uk.co.edministrator.spaceshipgame.Ships.ShipClass;
import uk.co.edministrator.spaceshipgame.Ships.Inventory;
import uk.co.edministrator.spaceshipgame.Ships.playerships.Escort;
import uk.co.edministrator.spaceshipgame.Ships.playerships.Fighter;
import uk.co.edministrator.spaceshipgame.Ships.playerships.Frigate;
import uk.co.edministrator.spaceshipgame.map.Planet;
import uk.co.edministrator.spaceshipgame.map.SolarSystem;
import uk.co.edministrator.spaceshipgame.map.SolarSystemPoint;
import uk.co.edministrator.spaceshipgame.tools.FileIO;
import uk.co.edministrator.spaceshipgame.weapons.GenNewGun;
import uk.co.edministrator.spaceshipgame.weapons.Weapon;


public class Player implements Serializable {
    private transient  Paint paint;
    private transient SolarSystemPoint location;
    private transient Planet planetLocation;
    private boolean isMoving;
    private Inventory inventory;
    private transient Ship ship;
    private float x,y;
    private int width, height;
    private transient Bitmap sprite;

    public Player(Ship ship, Bitmap sprite, Activity activity){
        this.ship = ship;
        this.sprite = sprite;
        this.inventory = new Inventory(activity);
        width = CONSTANTS.getScreenWidth() / 10;
        height = CONSTANTS.getScreenHeight() / 10;
        isMoving = false;
        inventory.addToWeapons(GenNewGun.GenNewGun(1,ship,activity));
        inventory.addToWeapons(GenNewGun.GenNewGun(1,ship,activity));
        inventory.addToWeapons(GenNewGun.GenNewGun(1,ship,activity));

        for (int i = 0; i < inventory.getWeapons().size(); i++){
            inventory.getWeapons().get(i).setEquiped(true);
//            ship.addWeapons(inventory.getWeapons().get(i));
        }
    }

    public Player (Ship ship, Inventory inv, Context context){
        this.ship = ship;
        FileIO fio = new FileIO(context);
        InputStream is;
        String addr = "";
        if (ship instanceof Fighter){
            addr = "Sprites/player/destroyer.png";
        }
        else if (ship instanceof Escort){
            addr = "Sprites/player/escort.png";
        }
        else {
            addr = "Sprites/player/frigate.png";
        }
        is = fio.readAsset(addr);
        this.sprite = Bitmap.createScaledBitmap(BitmapFactory.decodeStream(is), 128,128,true);
        this.inventory = inv;
        isMoving = false;
    }

    public void setX(float newX){
        x = newX;
    }
    public void setY(float newY){
        y = newY;
    }
    public float getX(){
        return x;
    }
    public float getY(){
        return y;
    }

    public Ship getShip() {
        return ship;
    }

    public ShipClass getShipClass() {
        return ship.shipClass;
    }

    public ArrayList<Weapon> getWeapons() {
        return inventory.getWeapons();
    }

    public Inventory getInventory() {
        return inventory;
    }

    public ArrayList<Part> getShipParts() {
        return ship.shipParts;
    }

    public void setMoving(boolean moving) {
        isMoving = moving;
    }

    public boolean isMoving() {
        return isMoving;
    }

    public boolean calcDist(float xPos, float yPos) {
       // Log.d("moving", "Y: "+y+", X: "+x+", yPos: "+yPos+", xPos: "+xPos);
        double xDist = (xPos - this.x);
        xDist= xDist * xDist; //square.
       // Log.d("moving", "xDist squared: "+xDist);
        double yDist = (yPos - this.y);
        yDist = yDist * yDist;
        //Log.d("moving", "yDist squared: "+yDist);
        double dist = xDist + yDist;
        dist = Math.sqrt(dist);
        Log.d("moving", "xDist squared: "+xDist);
       // double dist = Math.sqrt((yPos - this.y) * (yPos - this.y) + (xPos - this.x) * (xPos - this.x));

        Log.d("moving", "square root: " +dist);
        if (dist <= 5)
            return false;   //has reached destination
        return true;
    }
    public void Move(float xPos, float yPos){
        float t = 0.1f;
        this.x = lerp(this.x, xPos, t);
        this.y = lerp(this.y, yPos, t);
    }
    //LERP function
    public float lerp(float point1, float point2, float t){
        return point1 + t * (point2 - point1);
    }

    public void setPlanetLocation(Planet planetLocation) {
        this.planetLocation = planetLocation;
        //setX(planetLocation.getX());
        //setY(planetLocation.getY());
    }

    public Planet getPlanetLocation() {
        return planetLocation;
    }

    public void setLocation(SolarSystemPoint s){
        location = s;
    }

    public SolarSystemPoint getLocation() {
        return location;
    }
    public Rect getBounds(){
        Rect myLocation = new Rect((int) x,(int)  y,(int)  x +(int)width,(int)   y +(int)height);
        return myLocation;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public int getEquipedSlots(){
        int e = 0;
        for (Weapon w : inventory.getWeapons()){
            if (w.isEquiped()) e++;
        }
        return e;
    }

    public void draw(Canvas canvas, float rot){

        sprite.getScaledHeight(canvas);
        sprite.getScaledWidth(canvas);
        Matrix rotator = new Matrix();

        if (rot == 90) {
            rotator.postRotate(rot, 0, 0);
            rotator.postTranslate(x + sprite.getScaledWidth(canvas) , y );
        }
        else if (rot == 180){
            rotator.postRotate(rot, 0, 0);
            rotator.postTranslate(x + sprite.getScaledWidth(canvas), y + sprite.getScaledHeight(canvas));
        }
        else if (rot == 270){
            rotator.postRotate(rot, 0, 0);
            rotator.postTranslate(x , y + sprite.getScaledHeight(canvas));
        }
        else if (rot !=0){
            rotator.postRotate(rot,sprite.getWidth()/2,sprite.getHeight()/2);
            rotator.postTranslate(x,y);
            //rotator.postTranslate(x,y);
        }
        else {
            rotator.postTranslate(x, y);
        }
        canvas.drawBitmap(sprite, rotator,paint);
    }
}
