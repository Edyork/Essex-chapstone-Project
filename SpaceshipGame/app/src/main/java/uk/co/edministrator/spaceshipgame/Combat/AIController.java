package uk.co.edministrator.spaceshipgame.Combat;

import android.content.Context;
import android.graphics.Canvas;

import java.util.ArrayList;

import uk.co.edministrator.spaceshipgame.Ships.playerships.Frigate;
import uk.co.edministrator.spaceshipgame.UI.WeaponButton;
import uk.co.edministrator.spaceshipgame.gameObjects.Ship;
import uk.co.edministrator.spaceshipgame.tools.TouchControl;
import uk.co.edministrator.spaceshipgame.weapons.Weapon;

public class AIController extends Controller {
    private TouchControl touch;
    private Ship agent;
    private Ship enemy;
    private Context context;
    private ArrayList<WeaponButton> buttons;
    private ArrayList<Weapon> weaponsList;


   public AIController(Ship agent, Context context){
        this.agent = agent;
        this.context = context;
        weaponsList = agent.getWeapons();
        agent.update();
    }

    public void update(){
        agent.Shoot();
        agent.update();
    }

    public void draw(Canvas canvas){
        agent.draw(canvas);
    }
}

