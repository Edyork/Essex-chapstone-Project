package uk.co.edministrator.spaceshipgame.views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.SurfaceHolder;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

import uk.co.edministrator.spaceshipgame.CONSTANTS;
import uk.co.edministrator.spaceshipgame.Combat.AIController;
import uk.co.edministrator.spaceshipgame.Combat.PlayerController;
import uk.co.edministrator.spaceshipgame.Game;
import uk.co.edministrator.spaceshipgame.ShipSelectActivity;
import uk.co.edministrator.spaceshipgame.Ships.enemyships.Cargo;
import uk.co.edministrator.spaceshipgame.Ships.enemyships.Carrier;
import uk.co.edministrator.spaceshipgame.Ships.enemyships.Cruiser;
import uk.co.edministrator.spaceshipgame.Ships.enemyships.Destroyer;
import uk.co.edministrator.spaceshipgame.Ships.enemyships.Shuttle;
import uk.co.edministrator.spaceshipgame.Ships.parts.Part;
import uk.co.edministrator.spaceshipgame.Ships.rooms.EMP;
import uk.co.edministrator.spaceshipgame.Ships.rooms.EngineeringCentre;
import uk.co.edministrator.spaceshipgame.Ships.rooms.GenNewRoom;
import uk.co.edministrator.spaceshipgame.Ships.rooms.Room;
import uk.co.edministrator.spaceshipgame.Ships.rooms.RoomType;
import uk.co.edministrator.spaceshipgame.Ships.rooms.ShieldGenerator;
import uk.co.edministrator.spaceshipgame.Ships.rooms.WeaponsAccelerators;
import uk.co.edministrator.spaceshipgame.SolarSystemActivity;
import uk.co.edministrator.spaceshipgame.UI.AbilityButton;
import uk.co.edministrator.spaceshipgame.UI.EndOfCombatScreen;
import uk.co.edministrator.spaceshipgame.UI.PanView;
import uk.co.edministrator.spaceshipgame.UI.WeaponButton;
import uk.co.edministrator.spaceshipgame.gameObjects.Player;
import uk.co.edministrator.spaceshipgame.gameObjects.PlayerData;
import uk.co.edministrator.spaceshipgame.gameObjects.Ship;
import uk.co.edministrator.spaceshipgame.gameObjects.Sun;
import uk.co.edministrator.spaceshipgame.map.Planet;
import uk.co.edministrator.spaceshipgame.tools.FileIO;
import uk.co.edministrator.spaceshipgame.tools.TouchControl;
import uk.co.edministrator.spaceshipgame.weapons.GattlingGun;
import uk.co.edministrator.spaceshipgame.weapons.GenNewGun;
import uk.co.edministrator.spaceshipgame.weapons.LaserCannon;
import uk.co.edministrator.spaceshipgame.weapons.RocketLauncher;
import uk.co.edministrator.spaceshipgame.weapons.ThermalCanon;
import uk.co.edministrator.spaceshipgame.weapons.Weapon;
import uk.co.edministrator.spaceshipgame.weapons.WeaponClass;
import uk.co.edministrator.spaceshipgame.weapons.WeaponType;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;
import static android.content.Intent.FLAG_ACTIVITY_NO_ANIMATION;

public class NewCombatView extends customView implements SurfaceHolder.Callback{
    private Context context;
    private Game game;
    private TouchControl touch;
    private Ship playerShip;
    private Ship enemy;
    private PlayerController playerController;
    private AIController enemyController;
    private PanView left;
    private PanView right;
    private Ship deadPlayer;
    private int difficulty;
    private FileIO assets;
    private Bitmap background;
    public EndOfCombatScreen combatEnd;
    private boolean end = false;
    private Player player;
    private Sun sun;
    private ArrayList<Planet> myMap;
    private boolean lost;
    private boolean genLoot;
    private boolean istest;
//    private SolarSystem map;

    public NewCombatView(Context context, ArrayList<Planet> myMap, Player player, Sun sun,int difficulty, boolean istest) {
        super(context);
        this.setFocusable(true);
        this.getHolder().addCallback(this);
        this.player = player;
        this.context = context;
        this.myMap = myMap;
        this.sun = sun;
        this.difficulty = difficulty;
        Log.d("Diff","Diff in view"+this.difficulty);
        if (difficulty <= 0) difficulty = 1;
        touch = new TouchControl();
        setOnTouchListener(touch);
        assets = new FileIO(context);
        playerShip = player.getShip();
        playerShip.reinit();
        ArrayList<Weapon> temp = new ArrayList();
        for (Weapon w: player.getInventory().getWeapons()){
            if (w.isEquiped()){
                if (w.getWeaponType() == WeaponType.THERMAL){
                    // Ship parent, Context context, int damage
                    ThermalCanon tc = new ThermalCanon(playerShip, context, w.getDmg());
                    tc.setTimerDuration(w.getTimerDuration());
                    temp.add(tc);
                }
                else if (w.getWeaponType() == WeaponType.ROCKET){
                    RocketLauncher rl = new RocketLauncher(playerShip, context, w.getDmg());
                    rl.setTimerDuration(w.getTimerDuration());
                    temp.add(rl);
                }
                else if (w.getWeaponType() == WeaponType.LASER){
                    LaserCannon lc = new LaserCannon(playerShip, context, w.getDmg());
                    lc.setTimerDuration(w.getTimerDuration());
                    temp.add(lc);
                }
                else if (w.getWeaponType() == WeaponType.GATTLING){
                    GattlingGun gg = new GattlingGun(playerShip, context, w.getDmg());
                    gg.setTimerDuration(w.getTimerDuration());
                    temp.add(gg);
                }
                else{
                    Weapon newwep = new Weapon(playerShip,context);
                    temp.add(newwep);
                    //should never be called.
                }
            }
        }
        playerShip.setWeapons(temp);
        Log.d("DEBUG",""+playerShip.getWeapons().size());
        ArrayList<Room> rTemp = new ArrayList<>();
        for (Room r : player.getInventory().getRooms()){
            rTemp.add(r);
        }
        if (rTemp != null && rTemp.size() > 0) playerShip.setRooms(rTemp);
        ArrayList<Weapon> enemyWeapons = new ArrayList<>();
        Random r = new Random();
        int n = r.nextInt(5);
        int minWeps = 1, maxWeps = 1;
        switch (n){
            case 0:
                enemy = new Cargo(2, (int) (CONSTANTS.getScreenWidth() * 1.5), 0,180, difficulty, enemyWeapons, context);
                minWeps = 2;
                maxWeps = enemy.getWeaponSlots();
                break;
            case 1:
                enemy = new Carrier(2, (int) (CONSTANTS.getScreenWidth() * 1.5), 0,180, difficulty, enemyWeapons, context);
                minWeps = 2;
                maxWeps = enemy.getWeaponSlots();
                break;
            case 2:
                enemy = new Cruiser(2, (int) (CONSTANTS.getScreenWidth() * 1.5), 0,180, difficulty,  enemyWeapons, context);
                minWeps = 3;
                maxWeps = enemy.getWeaponSlots();
                break;
            case 3:
                enemy = new Destroyer(2, (int) (CONSTANTS.getScreenWidth() * 1.5), 0,180, difficulty, enemyWeapons, context);
                minWeps = 3;
                maxWeps = enemy.getWeaponSlots();
                break;
            case 4:
                enemy = new Shuttle(2, (int) (CONSTANTS.getScreenWidth() * 1.5), 0,180, difficulty, enemyWeapons, context);
                minWeps = 1;
                maxWeps = enemy.getWeaponSlots();
                break;
        }
        int slotCounter = 0;
        if (difficulty <= 5){
            maxWeps -= maxWeps/2;

        }
        else if (difficulty <= 10){
            maxWeps -= maxWeps/4;
        }
        else if (difficulty <= 20){
            maxWeps = maxWeps - 1;
        }
        int bound = 0;
        if (maxWeps - minWeps < 1) bound = 1;
        else bound = (r.nextInt(maxWeps - minWeps) + minWeps);
        for (int i = 0; i < bound; i++){
            Weapon w = GenNewGun.GenNewGun(difficulty,enemy,context);

            w.setLocation(enemy.getWeaponLocations()[slotCounter][0],enemy.getWeaponLocations()[slotCounter][1]);
            slotCounter++;
            if (slotCounter == enemy.getWeaponLocations().length) slotCounter = 0;
            enemy.addWeapons(w);
        }
        playerController = new PlayerController(touch, playerShip, context, istest);
        enemyController = new AIController(enemy, context);
        left = new PanView("left", false, context);
        right = new PanView("right", true, context);
        InputStream is = assets.readAsset("backgrounds/space/space.png");
        background = BitmapFactory.decodeStream(is);
        background = Bitmap.createScaledBitmap(background,CONSTANTS.getScreenWidth(),CONSTANTS.getScreenHeight(),false);
       // this.deadPlayer = enemy;
        //end = true;
        this.istest = istest;
        if (istest){
            right.setActive(false);
            left.setActive(true);
        }
        try {
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
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

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    public void update() {
        if (combatEnd == null){
            if (enemy.getCurrentHp() <= 0) {
            enemy.isAlive = false;
            touch.resetDown();
            end = true;
            deadPlayer = enemy;
        } else if (playerShip.getCurrentHp() <= 0) {
            playerShip.setAlive(false);
            touch.resetDown();
            end = true;
            deadPlayer = playerShip;
        }
        playerController.update(enemy);
        if (right.getBounds().contains((int) touch.getDownX(), (int) touch.getDownY())) {
            left.setActive(true);
            right.setActive(false);
        }

        if (left.getBounds().contains((int) touch.getDownX(), (int) touch.getDownY())) {
            left.setActive(false);
            right.setActive(true);
        }
        Random rand = new Random();
        ArrayList<Part> isAvailable = new ArrayList<>();
        for (Weapon weapon : enemy.getWeapons()) {
            for (Part r : playerShip.getShipParts()) {
                r.update();
                if (r.isAlive() && r.getHp() > 0){
                    isAvailable.add(r);
                }
            }
            weapon.setTarget(isAvailable.get(rand.nextInt(isAvailable.size())));
        }
        enemyController.update();
    }
    //if combatEnd is active
    if(end) {
        Bitmap bitmap;
        int rewardType; //1 = xp, 2 = weapon, 3 = ability;
        if (deadPlayer == enemy) {
            FileIO fileIO = new FileIO(context);
            InputStream is = fileIO.readAsset("UI/win.png");
            bitmap  = BitmapFactory.decodeStream(is);
            lost = false;
            //Kill weapon timers
            for (Weapon w : enemy.getWeapons()) {
                w.setTarget(null);
                w.bullets.clear();
                if (w.getTimer() != null) {
                    w.getTimer().cancel();
                    w.getTimer().purge();
                }
            }
            for (Weapon w : playerShip.getWeapons()) {
                w.setTarget(null);
                w.emptyBullets();
                if (w.getTimer() != null) {
                    w.getTimer().cancel();
                    w.getTimer().purge();
                }
            }
        }
        else {
            FileIO fileIO = new FileIO(context);
            InputStream is = fileIO.readAsset("UI/lose.png");
            bitmap  = BitmapFactory.decodeStream(is);
            lost = true;
        }
        if (left.getActive()) {
            right.setActive(true);
            left.setActive(false);
            if (deadPlayer == playerShip)
            combatEnd = new EndOfCombatScreen(CONSTANTS.getScreenWidth() * 1.5f, false, bitmap);
            else combatEnd = new EndOfCombatScreen(CONSTANTS.getScreenWidth() * 1.5f, true, bitmap);
        } else {
            if (deadPlayer == playerShip)
                combatEnd = new EndOfCombatScreen(0, false, bitmap);
            else combatEnd = new EndOfCombatScreen(0, true, bitmap);
        }
            Random random = new Random();
            int pick = random.nextInt(10);
            if (pick <= 3) {
                Weapon w = GenNewGun.GenNewGun(difficulty, player.getShip(), context);
                w.setEquiped(false);
                player.getInventory().addToWeapons(w);
                combatEnd.setWeaponReward(w);
                combatEnd.setRewardType(2);
            }
            if (pick > 8) {
                playerShip.addXp(200);
                combatEnd.setXp(300);
                combatEnd.setRewardType(1);
            }
            if (pick > 3 && pick < 8) {
                Room r = GenNewRoom.GenNewRoom(player.getShip(), context);
                boolean canAdd = true;
                for (Room r2 : player.getShip().getRooms()) {
                    if (r instanceof EngineeringCentre && r2.getType() == RoomType.ENGINEERING)
                        canAdd = false;
                    if (r instanceof ShieldGenerator && r2.getType() == RoomType.SHIELDGEN)
                        canAdd = false;
                    if (r instanceof WeaponsAccelerators && r2.getType() == RoomType.WEAPONSACC)
                        canAdd = false;
                    if (r instanceof EMP && r2.getType() == RoomType.EMP)
                        canAdd = false;
                }
                if (canAdd) {
                    player.getInventory().addToRooms(r);
                    combatEnd.setAbilityReward(r);
                    combatEnd.setRewardType(3);
                } else {
                    playerShip.addXp(150);
                    combatEnd.setXp(250);
                    combatEnd.setRewardType(1);
                } //give XP.
            }
        combatEnd.setActive(true);
        touch.resetDown();
        end = false;
        }
        if (combatEnd != null ){
            if (!lost){
            }
            if (combatEnd.getBounds().contains((int) touch.getDownX(),(int) touch.getDownY())) {
                if (lost) {
                    game.setRunning(false);
                    Intent intent = new Intent(context, ShipSelectActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | FLAG_ACTIVITY_CLEAR_TASK | FLAG_ACTIVITY_NO_ANIMATION);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    game.setRunning(false);
                    if (istest) intent.setAction("com.google.intent.action.TEST_LOOP");
                    context.startActivity(intent);
                } else {
                    playerShip.addXp(100); //give flat xp anyway.
                    Intent intent = new Intent(context, SolarSystemActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    playerShip.setWeapons(new ArrayList<Weapon>());
                    PlayerData.savePlayer(context, player);
                    game.setRunning(false);
                    context.startActivity(intent);
                    ((Activity) context).finish();   //kills current layer
                }
            }
    }
}


    public void draw(Canvas canvas) {
        super.draw(canvas);
        Paint p = new Paint();
        if (combatEnd == null) {
            if (left.getActive()) {
                canvas.translate(-CONSTANTS.getScreenWidth() * 1.5f, 0);
                canvas.drawBitmap(background, CONSTANTS.getScreenWidth() * 1.5f, 0, p);
                p.setColor(Color.RED);

                for (WeaponButton button : playerController.buttons)
                    button.draw(canvas, CONSTANTS.getScreenWidth() * 1.5f);
                for (AbilityButton button : playerController.Abuttons)
                    button.draw(canvas, CONSTANTS.getScreenWidth() * 1.5f);
            }
            if (right.getActive()) {
                canvas.translate(0, 0);
                canvas.drawBitmap(background, 0, 0, p);
                for (WeaponButton button : playerController.buttons) button.draw(canvas, 0);
                for (AbilityButton button : playerController.Abuttons) button.draw(canvas, 0);
            }

            if (enemy.isAlive) enemyController.draw(canvas);
            if (playerShip.isAlive()) playerController.draw(canvas);
            if (left.getActive()) {
                canvas.translate(0, 0);
                left.draw(canvas);
            }
            if (right.getActive()) {
                canvas.translate(0, 0);
                right.draw(canvas);
            }
        }
       else{
            canvas.drawBitmap(background, 0, 0, p);
           combatEnd.draw(canvas);
        }

    }
}
