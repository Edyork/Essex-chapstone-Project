package uk.co.edministrator.spaceshipgame.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import uk.co.edministrator.spaceshipgame.Game;
import uk.co.edministrator.spaceshipgame.gameObjects.Player;
import uk.co.edministrator.spaceshipgame.gameObjects.PlayerData;
import uk.co.edministrator.spaceshipgame.map.SolarSystem;
import uk.co.edministrator.spaceshipgame.tools.TouchControl;
import uk.co.edministrator.spaceshipgame.weapons.Weapon;

public class customView extends SurfaceView implements SurfaceHolder.Callback {
    private Context context;
    private Game game;
    private TouchControl touch;
    private Player player;


    public customView(Context context) {
        super(context);
        this.setFocusable(true);
        this.getHolder().addCallback(this);
        this.context = context;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
    public void update(){

    }
    public void draw(){

    }
}
