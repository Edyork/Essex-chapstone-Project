package uk.co.edministrator.spaceshipgame.Ships.enemyships;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

import uk.co.edministrator.spaceshipgame.CONSTANTS;
import uk.co.edministrator.spaceshipgame.Ships.ShipClass;
import uk.co.edministrator.spaceshipgame.Ships.ShipType;
import uk.co.edministrator.spaceshipgame.Ships.parts.Bridge;
import uk.co.edministrator.spaceshipgame.Ships.parts.Engine;
import uk.co.edministrator.spaceshipgame.Ships.parts.Hull;
import uk.co.edministrator.spaceshipgame.Ships.parts.Part;
import uk.co.edministrator.spaceshipgame.Ships.parts.WeaponLocations;
import uk.co.edministrator.spaceshipgame.Ships.rooms.Room;
import uk.co.edministrator.spaceshipgame.gameObjects.Ship;
import uk.co.edministrator.spaceshipgame.tools.FileIO;
import uk.co.edministrator.spaceshipgame.weapons.GattlingGun;
import uk.co.edministrator.spaceshipgame.weapons.Weapon;

public class Destroyer extends Ship {
    private Paint p = new Paint();
    public float scale;

    public Destroyer(int id, int x, int y, int rot, int modifier, ArrayList<Weapon> importedWeapons, Context context) {
        /**Variables*/
        this.id = id;
        shipClass = ShipClass.FRIGATE;
        shipType = ShipType.ENERGY;

        float mod = 450 * (1 + ((float)(modifier)/100));
        totalHp = (int)(mod);

        currentHp = totalHp;
        int rhp = totalHp / 7;
        armour = 0;
        evasion = 0;
        shield = 150 * (int)(1 + ((float)(modifier)/400));
        weaponSlots = 6;
        crewSlots = 2;
        isAlive = true;
        speed = 10;
        this.rot = rot;
        FileIO assets = new FileIO(context);
        InputStream is = assets.readAsset("Sprites/npc/destroyer.png");
        Bitmap original = BitmapFactory.decodeStream(is);

        /**sprite*/
        scale = CONSTANTS.getScreenHeight() / original.getHeight();
        this.sprite = Bitmap.createScaledBitmap(original, (int) (original.getWidth() * scale), (int) (original.getHeight() * scale), false);
        this.width = sprite.getWidth();
        this.height = sprite.getHeight();
        this.x = x + CONSTANTS.getScreenWidth() / 4;
        this.y = y;
        this.rot = rot;
        isAlive = true;

        /**Shootable Parts*/
        shipParts = new ArrayList<>();
        this.bridgeLocation = new int[]{(int) (186 * scale + this.x), (int) (84 * scale)};
        this.hullLocation = new int[]{(int) (46 * scale + this.x), (int) (84 * scale)};
        this.enginesLocation = new int[][]{
                {(int) (236 * scale + this.x), (int) (15 * scale)},
                {(int) (236 * scale + this.x), (int) (155 * scale)}
        };
        weaponLocations = new int[][]{
                {(int) (115 * scale + this.x), (int) (84 * scale)},
                {(int) (156 * scale + this.x), (int) (155 * scale)},
                {(int) (156 * scale + this.x), (int) (155 * scale)},
                {(int) (156 * scale + this.x), (int) (15 * scale)}
        };
        bridge = new Bridge(1, bridgeLocation[0], bridgeLocation[1],rhp, this, scale, context);
        hull = new Hull(2, hullLocation[0], hullLocation[1],rhp, this, scale, context);
        engines.add(new Engine(3, enginesLocation[0][0], enginesLocation[0][1],rhp, this, scale, context));
        engines.add(new Engine(4, enginesLocation[1][0], enginesLocation[1][1],rhp, this, scale, context));
        weaponsPoints.add(new WeaponLocations(5, weaponLocations[0][0], weaponLocations[0][1],rhp, this, scale, context));
        weaponsPoints.add(new WeaponLocations(6, weaponLocations[1][0], weaponLocations[1][1],rhp, this, scale, context));
        weaponsPoints.add(new WeaponLocations(7, weaponLocations[2][0], weaponLocations[2][1],rhp, this, scale, context));
        shipParts.add(hull);
        shipParts.add(bridge);
        shipParts.addAll(engines);
        shipParts.addAll(weaponsPoints);

        /**Rooms */
        for (Room r : rooms) {
            r.effect();
        }
        /**Weapons*/
        weapons = new ArrayList<Weapon>();
        if (importedWeapons == null) {
            GattlingGun gattlingGun = new GattlingGun(this, context, 5);
            GattlingGun gattlingGun2 = new GattlingGun(this, context, 5);
            weapons.add(gattlingGun);
//            weapons.add(gattlingGun2);
        } else {
            for (Weapon w : importedWeapons) {
                w.setParent((Ship) this);
                if (w.isEquiped()) weapons.add(w);
//                GattlingGun basicGun = new GattlingGun(this, context);
//                weapons.add(basicGun);
            }
        }
        p.setStrokeWidth(4);
        p.setStyle(Paint.Style.STROKE);
        Random r = new Random();
        anim = r.nextInt(100);
    }

    @Override
    public Rect getBounds() {
        return new Rect((int) x, (int) y, (int) x + 600, (int) y + 600);
    }

    @Override
    public float getY() {
        return y;
    }

    @Override
    public float getX() {
        return x;
    }

    @Override
    public float getHeight() {
        return super.getHeight();
    }

    @Override
    public float getWidth() {
        return super.getWidth();
    }

    @Override
    public ArrayList<Part> getShipParts() {
        return shipParts;
    }

    @Override
    public int getCurrentHp() {
        int n = 0;
        for (Part r : shipParts) {
            n += r.getHp();
        }
        currentHp = n;
        return currentHp;
    }

    public void setSprite(Bitmap sprite) {
        this.sprite = sprite;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }


    public ArrayList<Weapon> getWeapons() {
        return weapons;
    }

    @Override
    public void Shoot() {
        super.Shoot();
    }

    @Override
    public void setTarget(Part target) {
        Log.d("Combat", "Set new ShipPart");
        this.target = target;
        for (Weapon w : weapons) {
            w.setTarget(target);
        }
    }

    @Override
    public void ChangeColor() {
        p.setColor(Color.RED);
    }

    public int getId() {
        return id;
    }


    public String toString() {
        return "ShipPart: " + id + " at co-ordinates: " + x + ", " + y + ", W: " + width + ", H: " + height;
    }

}