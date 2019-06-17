package uk.co.edministrator.spaceshipgame.Ships;

import android.content.Context;
import android.graphics.Canvas;

import android.graphics.Paint;
import android.graphics.Rect;

import java.io.Serializable;
import java.util.ArrayList;

import uk.co.edministrator.spaceshipgame.CONSTANTS;
import uk.co.edministrator.spaceshipgame.Ships.rooms.Room;
import uk.co.edministrator.spaceshipgame.weapons.Weapon;

public class Inventory implements Serializable {
    private ArrayList<Weapon> weapons;
    private ArrayList<Room> rooms;
    private int width;
    private int height;


    //default constructor
    public Inventory(Context context){
        weapons = new ArrayList<>();
        rooms = new ArrayList<>();
        width = CONSTANTS.getScreenWidth();
        height = CONSTANTS.getScreenHeight();
    }

    public void update(){
        
    }

    /**Weapons**/
    public void addToWeapons(Weapon weapon){
        weapons.add(weapon);
    }

    public ArrayList<Weapon> getWeapons() {
        return weapons;
    }

    public void setWeapons(ArrayList<Weapon> weapons) {
        this.weapons = weapons;
    }

    /**Rooms**/

    public void addToRooms(Room room){
        rooms.add(room);
    }

    public ArrayList<Room> getRooms() {
        return rooms;
    }

    public void setRooms(ArrayList<Room> rooms) {
        this.rooms = rooms;
    }

    public Rect getBounds(){
        return new Rect(0,0,width,height);
    }

    public void draw(Canvas canvas){
        Paint p = new Paint();
        int x = 20;
        int y = 0;
        for (Weapon w : weapons){
            canvas.drawBitmap(w.getInventorySprite(), x, y, p);
            y += 105;
        }
    }
}
