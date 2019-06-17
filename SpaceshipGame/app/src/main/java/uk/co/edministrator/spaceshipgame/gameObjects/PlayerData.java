package uk.co.edministrator.spaceshipgame.gameObjects;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;

import uk.co.edministrator.spaceshipgame.Ships.Inventory;
import uk.co.edministrator.spaceshipgame.Ships.playerships.Escort;
import uk.co.edministrator.spaceshipgame.Ships.playerships.Fighter;
import uk.co.edministrator.spaceshipgame.Ships.playerships.Frigate;
import uk.co.edministrator.spaceshipgame.map.Galaxy;
import uk.co.edministrator.spaceshipgame.map.SolarSystem;
import uk.co.edministrator.spaceshipgame.map.SolarSystemPoint;

public class PlayerData {
    private static SharedPreferences shipPrefs;

    public static Boolean savePlayer(Context context, Player player){
        Gson gson = new Gson();
        String invjson = gson.toJson(player.getInventory());
        shipPrefs = context.getSharedPreferences("Player",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = shipPrefs.edit();
        String shipData = player.getShip().getShipClass()+ " "+player.getShip().getTotalHp()+" "+player.getShip().getXp()+ " "+player.getShip().getXpThreshold();
        editor.putString("Ship", shipData);
        editor.putString("Inv", invjson);
        return editor.commit();
    }

    public static boolean saveSolarSystem(Context context, SolarSystem solarSystem){
        Gson gson = new Gson();
        String map = gson.toJson(solarSystem);
        shipPrefs = context.getSharedPreferences("SolarSystem",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = shipPrefs.edit();
        //editor.putString("Map",map);
        editor.putString("Map", map);
        return editor.commit();

    }

    public static Inventory loadInventory(Context context){
        Gson gson = new Gson();
        shipPrefs = context.getSharedPreferences("Player", Context.MODE_PRIVATE);
        String json = shipPrefs.getString("Inv", "");
        if (json.equals("") || json == null){
            return null;
        }
        else{
            Inventory inventory = gson.fromJson(json, Inventory.class);
            return inventory;
        }
    }

    public static Ship loadShip(Context context){
        Gson gson = new Gson();
        shipPrefs = context.getSharedPreferences("Player", Context.MODE_PRIVATE);
        String data = shipPrefs.getString("Ship","");
        if (data.equals("") || data == null){
            return null;
        }
        else{
            String[] s = data.split(" ");
            String sClass = s[0];
            String sHP = s[1];
            String sXP = s[2];
            String sXPThresh = s[3];
            if (sClass.equals("FIGHTER")){
                return new Fighter(Integer.parseInt(sHP), Integer.parseInt(sXP),Integer.parseInt(sXPThresh),context);
            }
            else if (sClass.equals("ESCORT")){
                return new Escort(Integer.parseInt(sHP), Integer.parseInt(sXP),Integer.parseInt(sXPThresh),context);
            }
            else return new Frigate(Integer.parseInt(sHP), Integer.parseInt(sXP),Integer.parseInt(sXPThresh),context);
        }
    }


    public static SolarSystem loadSolarSystem(Context context){
        Gson gson = new Gson();
        shipPrefs = context.getSharedPreferences("SolarSystem", Context.MODE_PRIVATE);
        String json = shipPrefs.getString("Map", "");
        if (json.equals("") || json == null){
            return null;
        }
        else{
            SolarSystem system = gson.fromJson(json, SolarSystem.class);
            system.reinit(context);
            return system;
        }
    }
}
