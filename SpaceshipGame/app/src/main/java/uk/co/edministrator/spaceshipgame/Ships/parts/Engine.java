package uk.co.edministrator.spaceshipgame.Ships.parts;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;

import uk.co.edministrator.spaceshipgame.gameObjects.Ship;
import uk.co.edministrator.spaceshipgame.tools.FileIO;

public class Engine extends Part{
    private transient  Paint p = new Paint();
    private boolean added = false;
    private boolean removed = false;

    public Engine(int id, int x, int y,int hp, Ship ship, float scale, Context context) {
        this.ship = ship;
        this.id = id;
        maxHp = hp;
        this.hp = hp;
        this.x = x;
        this.y = y;
        FileIO assets = new FileIO(context);
        InputStream is = assets.readAsset("Sprites/rooms/FriendlyRoom.png");
        Bitmap original = BitmapFactory.decodeStream(is);
        sprite = Bitmap.createScaledBitmap(original, (int)(original.getWidth() * scale),
                (int)(original.getHeight() * scale), false);
        is = assets.readAsset("Sprites/rooms/targetedRoom.png");
        original = BitmapFactory.decodeStream(is);
        targetedSprite = Bitmap.createScaledBitmap(original, (int)(original.getWidth() * scale),
                (int)(original.getHeight() * scale), false);
        try {
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //get evasion amount
        isAlive = true;
    }
    public void effect(){
        if (!isAlive) {
            if (ship != null) {
                if (!added) {
                    ship.addEvasion(5); // add additional value;
                    added = true;
                }

            }
        }else {
            if (!removed){
                ship.removeEvasion(5);
                removed = true;
            }
        }
    }
    @Override
    public void heal(int amount){
        this.hp+= amount;
        //if (!isAlive && hp > 50) this.isAlive=true;
    }
}
