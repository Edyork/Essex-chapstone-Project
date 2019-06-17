package uk.co.edministrator.spaceshipgame.Ships.playerships;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
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
import uk.co.edministrator.spaceshipgame.Ships.parts.WeaponLocations;
import uk.co.edministrator.spaceshipgame.Ships.rooms.EngineeringCentre;
import uk.co.edministrator.spaceshipgame.Ships.rooms.Room;
import uk.co.edministrator.spaceshipgame.gameObjects.Ship;
import uk.co.edministrator.spaceshipgame.tools.FileIO;
import uk.co.edministrator.spaceshipgame.weapons.Weapon;

public class Fighter extends Ship {
    private Paint p = new Paint();
    public float scale;

    public Fighter(int id, int x, int y, int rot, Bitmap sprite, ArrayList<Weapon> importedWeapons, Context context) {
        /**Variables*/
        this.id = id;
        shipClass = ShipClass.FIGHTER;
        shipType = ShipType.ENERGY;
        NumOfParts = 7;
        this.totalHp = 300;
        this.currentHp = totalHp;
        int rhp = totalHp / NumOfParts;
        armour = 5;
        evasion = 0;
        shield = 100;
        weaponSlots = 7;
        crewSlots = 2;
        isAlive = true;
        speed = 10;
        this.rot = rot;
        FileIO assets = new FileIO(context);
        InputStream is = assets.readAsset("Sprites/player/destroyer.png");
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
        this.bridgeLocation = new int[]{(int) (205 * scale + this.x), (int) (119 * scale)};
        this.hullLocation = new int[]{(int) (65 * scale + this.x), (int) (119 * scale)};
        this.enginesLocation = new int[][]{
                {(int) (45 * scale + this.x), (int) (235 * scale)},
                {(int) (45 * scale + this.x), (int) (3 * scale)}};
        weaponLocations = new int[][]{
                {(int) (135 * scale + this.x), (int) (119 * scale)},
                {(int) (135 * scale + this.x), (int) (235 * scale)},
                {(int) (135 * scale + this.x), (int) (3 * scale)}
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

        /**Weapons*/
        weapons = new ArrayList<Weapon>();
        if (importedWeapons != null) {
            {
                for (Weapon w : importedWeapons) {
                    w.setParent(this);
                    if (w.isEquiped()) weapons.add(w);
                }
            }
            int slotCounter=0;
            for (int i = 0; i < weapons.size(); i++) {
                weapons.get(i).setLocation(weaponLocations[slotCounter][0], weaponLocations[slotCounter][1]);
                slotCounter++;
                if (slotCounter == weaponLocations.length) slotCounter = 0;
            }
        }
        Random r = new Random();
        anim = r.nextInt(100);
    }


    public Fighter(int thp, int xp, int xpt, Context context){
        /**Variables*/
        this.id = 1;
        this.xp = xp;
        this.xpThreshold = xpt;
        shipClass = ShipClass.FIGHTER;
        shipType = ShipType.ENERGY;
        NumOfParts = 7;
        this.totalHp = thp;
        this.currentHp = totalHp;
        int rhp = totalHp / NumOfParts;
        armour = 5;
        evasion = 0;
        shield = 100;
        weaponSlots = 7;
        crewSlots = 2;
        isAlive = true;
        speed = 10;
        this.rot = 0;
        FileIO assets = new FileIO(context);
        InputStream is = assets.readAsset("Sprites/player/destroyer.png");
        Bitmap original = BitmapFactory.decodeStream(is);

        /**sprite*/
        scale = CONSTANTS.getScreenHeight() / original.getHeight();
        this.sprite = Bitmap.createScaledBitmap(original, (int) (original.getWidth() * scale), (int) (original.getHeight() * scale), false);
        this.width = sprite.getWidth();
        this.height = sprite.getHeight();
        this.x = CONSTANTS.getScreenWidth() / 4;
        this.y = 0;
        this.rot = 0;
        isAlive = true;

        /**Shootable Parts*/
        shipParts = new ArrayList<>();
        this.bridgeLocation = new int[]{(int) (205 * scale + this.x), (int) (119 * scale)};
        this.hullLocation = new int[]{(int) (65 * scale + this.x), (int) (119 * scale)};
        this.enginesLocation = new int[][]{
                {(int) (45 * scale + this.x), (int) (235 * scale)},
                {(int) (45 * scale + this.x), (int) (3 * scale)}};
        weaponLocations = new int[][]{
                {(int) (135 * scale + this.x), (int) (119 * scale)},
                {(int) (135 * scale + this.x), (int) (235 * scale)},
                {(int) (135 * scale + this.x), (int) (3 * scale)}
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
        weapons = new ArrayList<Weapon>();
        rooms = new ArrayList<Room>();
        Random r = new Random();
        anim = r.nextInt(100);
    }
}