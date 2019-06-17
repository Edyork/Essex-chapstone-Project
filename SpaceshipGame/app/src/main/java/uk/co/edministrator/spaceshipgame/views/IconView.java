package uk.co.edministrator.spaceshipgame.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;

import uk.co.edministrator.spaceshipgame.gameObjects.Player;
import uk.co.edministrator.spaceshipgame.gameObjects.Ship;
import uk.co.edministrator.spaceshipgame.weapons.Weapon;


public class IconView extends android.support.v7.widget.AppCompatImageView {
    private Weapon weapon;
    public Bitmap eq;
    public Bitmap uneq;
    private Player player;
    private int state;

    public IconView(Context context, Player player) {
        super(context);
        this.player = player;
    }

    public Weapon getWeapon() {
        return weapon;
    }

    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;

    }
    public boolean hasSlot(){
        if (player.getShip().getWeaponSlots() >player.getEquipedSlots() ) return true;
        else return false;
    }
    public void setEq(Bitmap eq) {
        this.eq = eq;
    }

    public void setUneq(Bitmap uneq) {
        this.uneq = uneq;
    }
    public void setState(int s){
        if (s == 1){        //is equiped
            this.setImageBitmap(eq);
            this.state = 1;
        }else if (s == 0){      //unequip
            this.setImageBitmap(uneq);
            this.state = 0;
        }
    }

    public int getState(){
        return state;
    }
    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (weapon != null)
            weapon.draw(canvas,0);
    }
}
