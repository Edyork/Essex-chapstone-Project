package uk.co.edministrator.spaceshipgame.Combat;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

import uk.co.edministrator.spaceshipgame.CONSTANTS;
import uk.co.edministrator.spaceshipgame.Ships.parts.Part;
import uk.co.edministrator.spaceshipgame.Ships.rooms.EMP;
import uk.co.edministrator.spaceshipgame.Ships.rooms.EngineeringCentre;
import uk.co.edministrator.spaceshipgame.Ships.rooms.Room;
import uk.co.edministrator.spaceshipgame.Ships.rooms.RoomType;
import uk.co.edministrator.spaceshipgame.Ships.rooms.ShieldGenerator;
import uk.co.edministrator.spaceshipgame.Ships.rooms.WeaponsAccelerators;
import uk.co.edministrator.spaceshipgame.UI.AbilityButton;
import uk.co.edministrator.spaceshipgame.UI.WeaponButton;
import uk.co.edministrator.spaceshipgame.UI.combatButton;
import uk.co.edministrator.spaceshipgame.gameObjects.Ship;
import uk.co.edministrator.spaceshipgame.tools.TouchControl;
import uk.co.edministrator.spaceshipgame.weapons.Weapon;

public class PlayerController extends Controller {
    private TouchControl touch;
    private Ship player;
    private Ship enemy;
    private Context context;
    public ArrayList<WeaponButton> buttons;
    public ArrayList<AbilityButton> Abuttons;
    private Weapon SelectedWeapon;
    private Room SelectedRoom;
    private combatButton selectedButton;
    private boolean hasButtonClick;
    private EngineeringCentre ec;
    private ShieldGenerator sg;
    private WeaponsAccelerators wa;
    private EMP emp;
    private boolean test;
    private Random r = new Random();
    private boolean hasTesButtonSelected;
    private long cooldown;
    private boolean testHasWepSelected;
    public PlayerController(TouchControl touch, Ship player, Context context, boolean testing){
        this.touch = touch;
        this.player = player;
        this.context = context;
        this.test = testing;
        ArrayList<Weapon> weaponsList = player.getWeapons();
        for (Weapon w : weaponsList){
            w.setTarget(null);
        }
        buttons = new ArrayList<>();
        int x = 40;
        int y = CONSTANTS.getScreenHeight() - 138;   //64 x 64 bitmap, so at bottom minus height minus 10px;
        for (Weapon weapon : weaponsList) {
            buttons.add(new WeaponButton(weapon.getGreg(), context, x, y, weapon));
            x+= 138;
        }

        Abuttons = new ArrayList<>();
        int ax = 40;
        int ay = CONSTANTS.getScreenHeight() - (138 * 2 + 10);   // places it row above weapons
        for (Room r : player.getRooms()){
            boolean alreadyInList = false;
            switch (r.getType()){
                case ENGINEERING:
                    ec = new EngineeringCentre(r.getId(), player);
                    Abuttons.add(new AbilityButton(context,ax,ay,ec));
                    ax+= 138;
                    break;
                case SHIELDGEN:
                    sg = new ShieldGenerator(r.getId(), player);
                    Abuttons.add(new AbilityButton(context,ax,ay,sg));
                    ax+= 138;
                    break;
                case WEAPONSACC:
                    wa = new WeaponsAccelerators(r.getId(),player);
                    Abuttons.add(new AbilityButton(context,ax,ay,wa));
                    ax+= 138;
                    break;
                case EMP:
                    emp = new EMP(r.getId(), player);
                    Abuttons.add(new AbilityButton(context,ax,ay,emp));
                    ax+= 138;
                    break;
            }
        }
    }

    public void update(Ship enemy){

        for (WeaponButton button : buttons){
            //Test code
            if (test) {
                if (System.currentTimeMillis() - cooldown >= 5000){
                    cooldown = System.currentTimeMillis();
                    testHasWepSelected = true;
                    touch.setTestInput(button.getX(), button.getY());
                    hasTesButtonSelected = true;
                }
            }
             //Normal code
            if (button.getBounds().contains((int) touch.getDownX(), (int) touch.getDownY())) {
                button.setState(1);
                hasButtonClick = true;
                SelectedWeapon = button.getWeapon();
                selectedButton = button;
                selectedButton.setState(1);
            }

            if (button != selectedButton && button.getState() == 1) button.setState(0);
        }



        for (AbilityButton ab : Abuttons){
            if (ab != selectedButton) {
                if (ab.getRoom().getCooldownState() == 0) ab.setState(0);
                else if (ab.getRoom().getCooldownState() == 1) ab.setState(1);
                else if (ab.getRoom().getCooldownState() == 2) ab.setState(2);
            }

            //Test code
            if (test){
                if (testHasWepSelected){
                    touch.setTestInput(ab.getX(),ab.getY());
                    testHasWepSelected = false;
                }
            }

            if (ab.getBounds().contains((int) touch.getDownX(), (int) touch.getDownY())){
                touch.resetDown();
                switch (ab.getRoom().getType()){
                    case ENGINEERING:
                        Log.d("AbilityBut","Touch ENG");
                        ec.effect();
                        break;
                    case SHIELDGEN:
                        Log.d("AbilityBut","Touch SG");
                        ab.getRoom().setCooldownState(1);
                        selectedButton = ab;
                        hasButtonClick = true;
                        if (test) hasTesButtonSelected = true;
                        break;
                    case WEAPONSACC:
                        Log.d("AbilityBut","Touch WA");
                        wa.effect();
                        break;
                    case EMP:
                        Log.d("AbilityBut","Touch ENG");
                        emp.effect(enemy);
                }
            }
        }
        //Testing selected weapons/abilities
        if (hasTesButtonSelected){
            int n = r.nextInt(enemy.getShipParts().size());
            int x = 0;
            while (x == 0) {
                if (enemy.getShipParts().get(n).isAlive()) x = 1;
                else x = 0;
            }
            touch.setTestInput(enemy.getShipParts().get(n).getX() + 5 - +(int) (CONSTANTS.getScreenWidth() * 1.5f), enemy.getShipParts().get(n).getY() + 5);
            hasTesButtonSelected = false;
        }
        if (hasButtonClick) {
            if (selectedButton instanceof WeaponButton) {
                for (Part r : enemy.getShipParts()) {
                    if (r.getBounds().contains((int) (touch.getDownX() + (int) (CONSTANTS.getScreenWidth() * 1.5f)), (int) touch.getDownY())) {
                        SelectedWeapon.setTarget(r);
                        r.setSprite(true);
                        selectedButton.setState(0);
                        touch.resetDown();
                    }
                }
            } else if (selectedButton instanceof AbilityButton) {
                selectedButton.setState(1);
                    for (Part p : player.getShipParts()) {
                        if (p.getBounds().contains((int) touch.getDownX(), (int) touch.getDownY())) {
                            sg.effect(p);
                            selectedButton.setState(2);
                            hasButtonClick = false;
                    }
                }
            }
        }
        player.update();
        player.Shoot();
    }

    public void draw(Canvas canvas){
        if (emp !=null) {
            emp.draw(canvas);
        }
        player.draw(canvas);
    }
}
