package uk.co.edministrator.spaceshipgame.views;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import uk.co.edministrator.spaceshipgame.CONSTANTS;
import uk.co.edministrator.spaceshipgame.EquipmentActivity;
import uk.co.edministrator.spaceshipgame.Game;
import uk.co.edministrator.spaceshipgame.Ships.Inventory;
import uk.co.edministrator.spaceshipgame.SolarSystemActivity;
import uk.co.edministrator.spaceshipgame.gameObjects.Player;
import uk.co.edministrator.spaceshipgame.gameObjects.PlayerData;
import uk.co.edministrator.spaceshipgame.gameObjects.Ship;
import uk.co.edministrator.spaceshipgame.map.Galaxy;
import uk.co.edministrator.spaceshipgame.map.SolarSystemPoint;
import uk.co.edministrator.spaceshipgame.tools.FileIO;

import uk.co.edministrator.spaceshipgame.tools.TouchControl;


public class GameView extends customView implements SurfaceHolder.Callback{
    public Player player;
    private Game game;
    public float x;
    public float y;
    private TouchControl touch;
    private FileIO assets;
    private Bitmap background;
    private Paint paint = new Paint();
    private Galaxy map;
    private Context context;
    private static int playerSpriteRot = 0;
    private int destX, destY;
    private SolarSystemPoint destination;
    private boolean istesting;
    public boolean openedInv;

    @SuppressLint("ClickableViewAccessibility")
    public GameView(Context context, Ship ship, boolean istesting, boolean isNewGame) {

        //initialize
        super(context);
        this.setFocusable(true);
        this.getHolder().addCallback(this);
        this.context = context;


        //create touch controller
        touch = new TouchControl();
        this.setOnTouchListener(touch);

        //get background
        assets = new FileIO(context);
        InputStream is =  assets.readAsset("background.png");
        background = Bitmap.createScaledBitmap(BitmapFactory.decodeStream(is),CONSTANTS.getScreenWidth(),CONSTANTS.getScreenHeight(),true);
        this.istesting = istesting;

        Bitmap playersprite = Bitmap.createScaledBitmap(ship.getSprite(), 128,128,true);
        try {
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //create map
        if (isNewGame) {
            //create player
            player = new Player(ship,playersprite, (Activity) context);
            map = new Galaxy(assets);
            player.setLocation(map.getRootpoint());
            player.setX(player.getLocation().getX());
            player.setY(player.getLocation().getY());
            map.genPoints(player.getLocation().getX(), player.getLocation().getY(), player.getLocation(), assets);
            PlayerData.savePlayer(context, player);
        }
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

    public void update() {
        //Check inventory changes
        if (openedInv) {
            Inventory inv = PlayerData.loadInventory(context);
            Ship s = PlayerData.loadShip(context);
            if (inv != null) {
                if (inv.getWeapons() != player.getInventory().getWeapons()) {
                    player.getInventory().setWeapons(inv.getWeapons());
                }
                if (inv.getRooms() != player.getInventory().getRooms()) {
                    player.getInventory().setRooms(inv.getRooms());
                }
                if (s.getXp() != player.getShip().getXp()) {
                    {
                        player.getShip().setXp(s.getXp());
                        Log.d("LootGen", "xp: " + player.getShip().getXp());
                    }
                }
            }
        }

        /**TESTING**/
        if (!player.isMoving()) {
            if (istesting) {
                inputTest();
            }

            if (player.getLocation().getlChild() != null) {
                if (player.getLocation().getlChild().getBounds().contains((int) touch.getDownX(), (int) touch.getDownY())) {
                    map.genPoints(player.getLocation().getlChild().getX(), player.getLocation().getlChild().getY(), player.getLocation().getlChild(), assets);
                    playerSpriteRot = 180;
                    destX = player.getLocation().getlChild().getX();
                   destY = player.getLocation().getlChild().getY();
                    destination = player.getLocation().getlChild();
                    player.setMoving(true);
                }
            }
            if (player.getLocation().getupChild() != null) {
                if (player.getLocation().getupChild().getBounds().contains((int) touch.getDownX(), (int) touch.getDownY())) {
                    map.genPoints(player.getLocation().getupChild().getX(), player.getLocation().getupChild().getY(), player.getLocation().getupChild(), assets);
                    playerSpriteRot = 270;
                    destX = player.getLocation().getupChild().getX();
                    destY = player.getLocation().getupChild().getY();
                    destination = player.getLocation().getupChild();
                    player.setMoving(true);
                }
            }
            if (player.getLocation().getrChild() != null) {
                if (player.getLocation().getrChild().getBounds().contains((int) touch.getDownX(), (int) touch.getDownY())) {
                    map.genPoints(player.getLocation().getrChild().getX(), player.getLocation().getrChild().getY(), player.getLocation().getrChild(), assets);
                    playerSpriteRot = 0;
                    destX = player.getLocation().getrChild().getX();
                    destY = player.getLocation().getrChild().getY();
                    destination = player.getLocation().getrChild();
                    player.setMoving(true);
                }
            }
            if (player.getLocation().getDownChild() != null) {
                if (player.getLocation().getDownChild().getBounds().contains((int) touch.getDownX(), (int) touch.getDownY())) {
                    map.genPoints(player.getLocation().getDownChild().getX(), player.getLocation().getDownChild().getY(), player.getLocation().getDownChild(), assets);
                    playerSpriteRot = 90;
                    destX = player.getLocation().getDownChild().getX();
                    destY = player.getLocation().getDownChild().getY();
                    destination = player.getLocation().getDownChild();
                    player.setMoving(true);
                }
            }
            if (player.getLocation().getParent() != null) {
                if (player.getLocation().getParent().getBounds().contains((int) touch.getDownX(), (int) touch.getDownY())) {
                    if (player.getLocation().getParent().getlChild() == player.getLocation())
                        playerSpriteRot = 0;
                    if (player.getLocation().getParent().getupChild() == player.getLocation())
                        playerSpriteRot = 90;
                    if (player.getLocation().getParent().getrChild() == player.getLocation())
                        playerSpriteRot = 180;
                    if (player.getLocation().getParent().getDownChild() == player.getLocation())
                        playerSpriteRot = 270;
                    destX = player.getLocation().getParent().getX();
                    destY = player.getLocation().getParent().getY();
                    destination = player.getLocation().getParent();
                    player.setMoving(true);
                }
            }

            //Camera Pan
            if (touch.getEvent() != null) {
                if (touch.getEvent().getAction() == MotionEvent.ACTION_MOVE) {
                    map.movePoints((int) touch.getxDiff() * -1, (int) touch.getyDiff() * -1);
                    player.setX(player.getX() + (touch.getxDiff()+1) * -1);
                    player.setY(player.getY() + (touch.getyDiff()+1) * -1);
                }
            }
        }
        else {
            player.Move(destX, destY);
            if (!player.calcDist(destX, destY)) {
                player.setLocation(destination);
                player.setMoving(false);
            }
        }
//        if (openedInv) openedInv = false;
    }

    public void Explore(){
        touch.resetDown();
        //PlayerData.savePlayer(this.context, this.player);
        PlayerData.saveSolarSystem(this.context, this.player.getLocation().getSolarSystem());
        Intent intent = new Intent(context, SolarSystemActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        //intent.putExtra("return",1);
        if (istesting) intent.putExtra("testing", true);
        context.startActivity(intent);
        game.setRunning(false);
    }

    public void Inventory(){
        game.setRunning(false);
        PlayerData.savePlayer(context,player);
        Intent intent = new Intent(context, EquipmentActivity.class);
        if (istesting) intent.putExtra("testing", true);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        context.startActivity(intent);
    }


    /**Firebase test script
     * Simply sets the touch inputs automatically
     **/

    private void inputTest(){
        Random randomEvent = new Random();
        int n = randomEvent.nextInt(5);
        if (n <= 3) randomMove();
        else if (n == 4){   // Open inventory
            //touch.setTestInput(button.x+ 5, button.y+ 5);
            //touch.setTestInput(invButton.x + 5, invButton.y + 5);
        }
        else {  // go to solar system activity
            //touch.setTestInput(button.x+ 5, button.y+ 5);
        }
    }

    private void randomMove(){
        int n = 0;
        while (n == 0) {
            Random r = new Random();
            switch (r.nextInt(4)) {
                case 0:
                    if (player.getLocation().getlChild() != null) {
                        touch.setTestInput(player.getLocation().getlChild().getX()+ 5, player.getLocation().getlChild().getY()+ 5);
                        n++;
                    }
                    break;
                case 1:
                    if (player.getLocation().getrChild() != null) {
                        touch.setTestInput(player.getLocation().getrChild().getX()+ 5, player.getLocation().getrChild().getY()+ 5);
                        n++;
                    }
                    break;
                case 2:
                    if (player.getLocation().getupChild() != null) {
                        touch.setTestInput(player.getLocation().getupChild().getX()+ 5, player.getLocation().getupChild().getY()+ 5);
                        n++;
                    }
                    break;
                case 3:
                    if (player.getLocation().getDownChild() != null) {
                        touch.setTestInput(player.getLocation().getDownChild().getX()+ 5, player.getLocation().getDownChild().getY()+ 5);
                        n++;
                    }
                    break;
            }
        }
    }
    public void draw(Canvas canvas){
        super.draw(canvas);
        canvas.drawARGB(255,0,11,30);
        canvas.drawBitmap(background,0,0,paint);
        map.draw(canvas);
        //button.draw(canvas);
        //invButton.draw(canvas);
        player.draw(canvas, playerSpriteRot);
    }
}


