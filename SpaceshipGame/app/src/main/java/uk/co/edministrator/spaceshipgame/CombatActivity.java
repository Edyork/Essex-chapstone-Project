package uk.co.edministrator.spaceshipgame;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import uk.co.edministrator.spaceshipgame.Ships.Inventory;
import uk.co.edministrator.spaceshipgame.gameObjects.Player;
import uk.co.edministrator.spaceshipgame.gameObjects.PlayerData;
import uk.co.edministrator.spaceshipgame.gameObjects.Ship;
import uk.co.edministrator.spaceshipgame.gameObjects.Sun;
import uk.co.edministrator.spaceshipgame.map.Planet;
import uk.co.edministrator.spaceshipgame.map.SolarSystem;
import uk.co.edministrator.spaceshipgame.map.SolarSystemPoint;
import uk.co.edministrator.spaceshipgame.tools.Music;
import uk.co.edministrator.spaceshipgame.views.NewCombatView;

public class CombatActivity  extends Activity {
    private static SoundPool soundPool;
    private static int laser1,laser2,laser3,explosion1,explosion2, woof;
    private static boolean loaded = false;
    public static int soundSelector;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set full screen view
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //Hide title
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Fix to landscape
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        //set constants
        CONSTANTS.setConstants(this);
        //Instantiate game
        Intent intent = getIntent();
        //get player
        Inventory inv= PlayerData.loadInventory(this);
        Ship s = PlayerData.loadShip(this);
        Player player = new Player(s, inv, this);
        SolarSystem solarSystem = PlayerData.loadSolarSystem(this);
        ArrayList<Planet> myMap = solarSystem.getMap();
        Sun sun = solarSystem.getSun();
        int diff = intent.getIntExtra("diff",0);
        Log.d("Diff","Diff in Activity"+diff);
        boolean istesting = intent.getBooleanExtra("testing", false);
        NewCombatView cv = new NewCombatView(this, myMap, player, sun, diff, istesting);

        //build Soundpool using 5.1 constructor
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build();

        soundPool = new SoundPool.Builder()
                .setMaxStreams(6)
                .setAudioAttributes(audioAttributes)
                .build();

        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int mySoundId, int status) {
                loaded = true;
            }
        });
        laser1 = soundPool.load(this, R.raw.laser1, 1);
        laser2 = soundPool.load(this, R.raw.laser2, 1);
        laser3 = soundPool.load(this, R.raw.laser3, 1);
        explosion1 = soundPool.load(this, R.raw.explosion1, 1);
        explosion2 = soundPool.load(this, R.raw.explosion2, 1);
        woof = soundPool.load(this, R.raw.woof, 1);


        //music
        if (ShipSelectActivity.music != null){
            ShipSelectActivity.music.stop();
            ShipSelectActivity.music.setTrack(this, 2);
            ShipSelectActivity.music.play();
        }
        this.setContentView(cv);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        soundPool = null;
        if (ShipSelectActivity.music != null) {
            ShipSelectActivity.music.stop();
            ShipSelectActivity.music.setTrack(this, 1);
            ShipSelectActivity.music.play();
        }
    }

    public static void PlaySound(int i){
        if (soundPool != null && loaded) {
            switch (i){
                case 1:
                    soundPool.play(laser1, 0.5f,0.5f, 0, 0, 1);
                    break;
                case 2:
                    soundPool.play(laser2, 0.5f,0.5f, 0, 0, 1);
                    break;
                case 3:
                    soundPool.play(laser3, 0.5f,0.5f, 0, 0, 1);
                    break;
                case 4:
                    soundPool.play(explosion1, 0.5f,0.5f, 0, 0, 1);
                    break;
                case 5:
                    soundPool.play(explosion2, 0.5f,0.5f, 0, 0, 1);
                    break;
                case 6:
                    soundPool.play(woof,0.5f,0.5f,1,0,1);
            }

        }
    }
}


