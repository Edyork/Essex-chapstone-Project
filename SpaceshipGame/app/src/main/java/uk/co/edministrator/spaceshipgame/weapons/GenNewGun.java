package uk.co.edministrator.spaceshipgame.weapons;

import android.content.Context;

import java.util.Random;

import uk.co.edministrator.spaceshipgame.gameObjects.Ship;

public class GenNewGun {
    public static Weapon GenNewGun(int mod, Ship player, Context context){
        int modifier = 1 + (mod/100);
        Random rand = new Random();
        int pick = rand.nextInt(4);
        int damage;
        switch (pick){
            case 0:
                 damage = rand.nextInt((12*modifier) - (5*modifier)) + (5*modifier);
                return new GattlingGun(player,context, damage);
                case 1:
                damage = rand.nextInt((10*modifier) - (5*modifier)) + (5*modifier);
                return new LaserCannon(player,context, damage);

            case 2:
                damage = rand.nextInt((25*modifier) - (10*modifier)) + (10*modifier);
                return new RocketLauncher(player,context, damage);
            case 3:
                damage = rand.nextInt((35*modifier) - (15*modifier)) + (15*modifier);
                return new ThermalCanon(player,context,damage);
        }
        return null;
    }
}
