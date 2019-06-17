package uk.co.edministrator.spaceshipgame.Ships.parts;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import java.io.InputStream;
import java.io.Serializable;

import uk.co.edministrator.spaceshipgame.tools.FileIO;
import uk.co.edministrator.spaceshipgame.Ships.parts.PartType;

public class ShipPart implements Serializable {
    private int id;
    private int dr;     // short for Damage reduction
    private int crew;
    private int hp;
    private float x, y, width, height;
    private float localX, localY;
    private PartType type;
    private Bitmap sprite;
    boolean alive;
    private  transient Paint p ;


    public ShipPart(float x, float localX, float y, float localY, int id, int dr, int crew, int hp, PartType type, Context context){
        this.id = id;
        this.localX = localX;
        this.localY = localY;
        this.x = x + localX;
        this.y = y + localY;
        this.dr = dr;
        this.crew = crew;
        this.hp = hp;
        this.type = type;
        width = 100;
        height =100;
        FileIO assets = new FileIO(context);
        InputStream is = assets.readAsset("UI/combat/FriendlyRoom.png");
        Bitmap RoomSprite = BitmapFactory.decodeStream(is);
        this.sprite = Bitmap.createScaledBitmap(RoomSprite,(int)width,(int)height,true);
        alive = true;
        p = new Paint();
        p.setColor(Color.GREEN);
//        Log.d("Combat","ShipPart: "+id+" at co-ordinates: " + x + ", "+y +", W: "+ width + ", H: "+height);
    }
    public void Move(float tx, float ty, int speed){
        float deltaX = tx - x;
        float deltaY = ty - y;
        double angle = Math.atan2(deltaY,deltaX);
        x += speed * Math.cos(angle);
        y += speed * Math.sin(angle);
    }

    public void hit(int dmg){
        float n = dmg * (1-(float)dr/100);
        hp -= n;
        if (hp <= 0){
            alive = false;
        }

    }

    public void heal(int heal){};

    public void setNewPos(float x){
        this.x = (int) x + localX;
    }
    public Rect getBounds(){
        return new Rect((int) x,
                (int) y,
                (int)x+  (int) width ,
                (int) y+ (int) height);
    }

    public int getHp() {
        return hp;
    }

    public void draw(Canvas canvas){
        if (alive)
        canvas.drawBitmap(sprite, x, y, p);
        //canvas.drawRect(getBounds(), p);
    }

    public float getId() {
        return id;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public boolean isAlive() {
        return alive;
    }

    public String toString(){
        return  "ShipPart: "+id+" at co-ordinates: " + x + ", "+y +", W: "+ width + ", H: "+height;
    }
}
