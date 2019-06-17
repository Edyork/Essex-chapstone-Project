package uk.co.edministrator.spaceshipgame.Ships.rooms;

import android.content.Context;

import java.util.Random;

import uk.co.edministrator.spaceshipgame.CONSTANTS;
import uk.co.edministrator.spaceshipgame.gameObjects.Ship;

public class GenNewRoom {
    public static Room GenNewRoom(Ship player, Context context){
        Random rand = new Random();
        int pick = rand.nextInt(4);
        CONSTANTS.setRoomId(CONSTANTS.getRoomId()+1);
        switch (pick){
            case 0:
                return new EngineeringCentre(CONSTANTS.getRoomId(),player);
            case 1:
                return new ShieldGenerator(CONSTANTS.getRoomId(),player);
            case 2:
                return new WeaponsAccelerators(CONSTANTS.getRoomId(),player);
            case 3:
                return new EMP(CONSTANTS.getRoomId(),player);
        }
        return null;
    }
}
