package uk.co.edministrator.spaceshipgame.views;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.util.Log;
import android.view.SurfaceHolder;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

import uk.co.edministrator.spaceshipgame.CONSTANTS;
import uk.co.edministrator.spaceshipgame.CombatActivity;
import uk.co.edministrator.spaceshipgame.EquipmentActivity;
import uk.co.edministrator.spaceshipgame.Game;
import uk.co.edministrator.spaceshipgame.Ships.Inventory;
import uk.co.edministrator.spaceshipgame.gameObjects.Player;
import uk.co.edministrator.spaceshipgame.gameObjects.PlayerData;
import uk.co.edministrator.spaceshipgame.gameObjects.Ship;
import uk.co.edministrator.spaceshipgame.gameObjects.Sun;
import uk.co.edministrator.spaceshipgame.map.Planet;
import uk.co.edministrator.spaceshipgame.map.SolarSystem;
import uk.co.edministrator.spaceshipgame.tools.FileIO;
import uk.co.edministrator.spaceshipgame.tools.TouchControl;

public class SolarSystemView extends customView implements SurfaceHolder.Callback {
    public Player player;
    private Context context;
    private Game game;
    private TouchControl touch;
    private Bitmap background;
    private SolarSystem map;

    private float[] rings = {0,0,0};
    private Sun sun;
    private int rot = 0;
    private boolean hasRotated;
    private boolean ismoving;
    private Planet destination;
    private boolean testing;
    private Paint paint = new Paint();
    public boolean openedInv;

    public SolarSystemView(Context context, ArrayList<Planet> myMap, Sun sun, boolean testing) {
        super(context);
        this.setFocusable(true);
        this.getHolder().addCallback(this);
        this.context = context;
        FileIO assets = new FileIO(context);
        InputStream is = assets.readAsset("backgrounds/space/space.png");
        background = Bitmap.createScaledBitmap(BitmapFactory.decodeStream(is),CONSTANTS.getScreenWidth(),CONSTANTS.getScreenHeight(),false);
        try {
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //retreive player data from shared preferences.

        Inventory inv= PlayerData.loadInventory(context);
        Ship s = PlayerData.loadShip(context);
        player = new Player(s, inv, context);
        map = new SolarSystem(myMap);
        map.getRootNode().setParent(map.getMap().get(map.getMap().size()-1));
        for (Planet p : map.getMap()){
            for (int n = 0; n < 3; n++) {
                if (rings[0] != p.radius && rings[1] != p.radius && rings[2] != p.radius) {
                    if (rings[0] == 0)
                        rings[0] = p.radius;
                    else if (rings[1] == 0)
                        rings[1] = p.radius;
                    else if (rings[2] == 0)
                        rings[2] = p.radius;
                }
            }
        }

        touch = new TouchControl();
        this.setOnTouchListener(touch);
        player.setPlanetLocation(map.getRootNode());
        player.setX(map.getRootNode().getX());
        player.setY(map.getRootNode().getY());

        this.sun = sun;
        this.testing = testing;

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        this.game = new Game(this, holder);
        this.game.setRunning(true);
        this.game.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }



    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

   @Override
    public void update(){


        if (testing) testing();

       //Check inventory changes
       if (openedInv) {
           Inventory inv = PlayerData.loadInventory(context);
           Ship s = PlayerData.loadShip(context);
           if (inv != null) {
               if (inv.getWeapons() != player.getInventory().getWeapons()) {
                   player.getInventory().setWeapons(inv.getWeapons());
                    openedInv = false;
               }
               if (inv.getRooms() != player.getInventory().getRooms()) {
                   player.getInventory().setRooms(inv.getRooms());
                   openedInv = false;
               }
           }
           if (s != null) {
               if (s.getXp() != player.getShip().getXp()) {
                   player.getShip().setXp(s.getXp());
                   Log.d("LootGen", "xp: " + player.getShip().getXp());
                   openedInv = false;
               }
           }
       }

        if (!ismoving) {
            if (player.getPlanetLocation().getParent() != null) {
                if (player.getPlanetLocation().getParent().getBounds().contains((int) touch.getDownX(), (int) touch.getDownY())) {
                    destination = player.getPlanetLocation().getParent();
                    ismoving = true;
                }
            }
            if (player.getPlanetLocation().getChild() != null) {
                if (player.getPlanetLocation().getChild().getBounds().contains((int) touch.getDownX(), (int) touch.getDownY())) {
                    destination = player.getPlanetLocation().getChild();
                    ismoving = true;
                }
            }
        }
        else {
            if (!hasRotated) {
                double dx = destination.getX() - player.getX();
                double dy = destination.getY() - player.getY();
                double radians = Math.atan2(dy, dx);
                rot = (int) Math.toDegrees(radians);
                hasRotated = true;
            }
            int x = (int) destination.getX();
            int y = (int) destination.getY();
            player.Move(x, y);
            if (!player.calcDist(x, y)) {
                player.setPlanetLocation(destination);
                touch.resetDown();
                hasRotated = false;
                ismoving = false;
            }
        }
    }
    public void Explore(){
        if (player.getPlanetLocation().getUnexplored()) {
            player.getPlanetLocation().setUnexplored(false);
            touch.resetDown();
            Intent intent = new Intent(context, CombatActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            boolean savedata = PlayerData.savePlayer(context, player);
            if (savedata) Log.d("SaveData", "Save successful");
            else Log.d("SaveData", "Save unsuccessful");
            PlayerData.saveSolarSystem(context,map);
            intent.putExtra("diff", player.getPlanetLocation().getDiff());
            if (testing) intent.putExtra("testing", true);
            context.startActivity(intent);
        }
    }

    public void Inventory(){
        PlayerData.savePlayer(context, player);
        touch.resetDown();
        game.setRunning(false);
        Intent intent = new Intent(context, EquipmentActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.putExtra("PlanetLocation",this.player.getPlanetLocation().getId());
        context.startActivity(intent);
    }

    public void testing(){
        Random randomEvent = new Random();
        int n = randomEvent.nextInt(3);
        if (n == 0) randomMove();
        else if (n == 1){   // Open inventory
           // touch.setTestInput(invButton.x + 5, invButton.y + 5);
        }
        else {  // go to solar system activity
//            touch.setTestInput(button.x+ 5, button.y+ 5);
        }
    }

    private void randomMove(){
        Random r = new Random();
        int n = r.nextInt(2);
        if (n == 0){
            touch.setTestInput((int) player.getPlanetLocation().getParent().getX(),(int) player.getPlanetLocation().getParent().getY());
        }
        else
            touch.setTestInput((int) player.getPlanetLocation().getChild().getX(),(int) player.getPlanetLocation().getChild().getY());
    }

    @Override
    public void draw(Canvas canvas){
        super.draw(canvas);

        canvas.drawBitmap(background,0,0,paint);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.WHITE);
        for (int r = 0; r < 3; r++)
            canvas.drawCircle(CONSTANTS.getScreenWidth()/2, CONSTANTS.getScreenHeight()/2,rings[r], paint);
        paint.setPathEffect(new DashPathEffect(new float[]{8,16},50));
        sun.draw(canvas);
        paint.setColor(Color.GREEN);
        canvas.drawLine(player.getPlanetLocation().getX() + player.getPlanetLocation().getSprite().getWidth()/2 ,
                player.getPlanetLocation().getY() + player.getPlanetLocation().getSprite().getHeight()/2,
                player.getPlanetLocation().getChild().getX() + player.getPlanetLocation().getChild().getSprite().getWidth()/2,
                player.getPlanetLocation().getChild().getY() + player.getPlanetLocation().getChild().getSprite().getHeight()/2,
                paint);

        canvas.drawLine(player.getPlanetLocation().getX() + player.getPlanetLocation().getSprite().getWidth()/2,
                player.getPlanetLocation().getY() + player.getPlanetLocation().getSprite().getHeight()/2,
                player.getPlanetLocation().getParent().getX() + player.getPlanetLocation().getParent().getSprite().getWidth()/2,
                player.getPlanetLocation().getParent().getY() + player.getPlanetLocation().getChild().getSprite().getHeight()/2,
                paint);
//        button.draw(canvas);
        map.draw(canvas);

        player.draw(canvas, rot);
//        invButton.draw(canvas);
    }
}
