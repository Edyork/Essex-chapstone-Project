package uk.co.edministrator.spaceshipgame.UI;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;


import uk.co.edministrator.spaceshipgame.CONSTANTS;
import uk.co.edministrator.spaceshipgame.Ships.rooms.Room;
import uk.co.edministrator.spaceshipgame.weapons.Weapon;

public class EndOfCombatScreen {
    public Paint paint;
    public int width, height, x, y, transl;
    private Rect rect;
    private boolean isActive;
    private Bitmap bitmap;
    private boolean win;
    private int rewardType;
    private int xp;
    private Weapon weaponReward;
    private Room abilityReward;
    private int bigText,midText,smallText;

    public EndOfCombatScreen(float pos, boolean win, Bitmap bitmap){
        this.transl = (int) pos;
        this.x =100;
        this.y = 100;
        height = CONSTANTS.getScreenHeight() - 200;
        width =  CONSTANTS.getScreenWidth() - 200;
        isActive = false;
        paint = new Paint();
        this.win = win;
        this.bitmap = Bitmap.createScaledBitmap(bitmap,width,height,true);
        bigText = CONSTANTS.getScreenWidth() /10;
        midText = CONSTANTS.getScreenWidth() / 15;
        smallText = CONSTANTS.getScreenWidth() / 20;
    }
    public Rect getBounds(){
       // Log.d("Touch", "RECT x: "+x+ " y: "+ y+ " w: "+width+" h: "+height);
        return new Rect(x,y,width,height);
    }

    public void setRewardType(int rewardType) {
        this.rewardType = rewardType;
        Log.d("Reward","Reward type set to "+rewardType);
    }

    public void setXp(int xp) {
        this.xp = xp;
        Log.d("Reward","XP "+xp);
    }

    public void setWeaponReward(Weapon weaponReward) {
        this.weaponReward = weaponReward;
        Log.d("Reward","weapon: " + weaponReward.getName());
    }

    public void setAbilityReward(Room abilityReward) {
        this.abilityReward = abilityReward;
        Log.d("Reward","Ability "+abilityReward.getName());
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void draw(Canvas canvas){
        canvas.drawBitmap(bitmap,x,y,paint);
        if (win){
            paint.setTextSize(bigText);
            paint.setColor(Color.WHITE);
            paint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText("Victory!",x+width/2, y+height/4,paint);
            paint.setTextSize(smallText);
            if (rewardType == 1){   //xp
                canvas.drawText("You've been awarded:", x + width / 2, (y+height/4) + bigText, paint);
                paint.setTextSize(bigText);
                canvas.drawText(xp + " Experience", x + width / 2,(y+height/4) + bigText * 2, paint);
                Log.d("Reward","drawing xp");
            }
            else if (rewardType == 2){  //gun
                canvas.drawText("You've been awarded:", x + width / 2, (y+height/4) + smallText, paint);
                paint.setTextSize(smallText);
                canvas.drawText("A "+weaponReward.getName(), x + width / 2, (y+height/4) + bigText, paint);
                canvas.drawBitmap(weaponReward.getInventorySprite(),x + width / 2 - weaponReward.getInventorySprite().getWidth()/2, (y+height/4) + bigText * 2, paint);
                Log.d("Reward","drawing weapon");
            }
            else if (rewardType == 3){  //ability
                canvas.drawText("You've been awarded:", x + width / 2, (y+height/4) + bigText, paint);
                paint.setTextSize(midText);
                canvas.drawText("A "+abilityReward.getName(), x + width / 2, (y+height/4) + bigText*2, paint);
                //Log.d("Reward","drawing ability");
            }
            paint.setTextSize(smallText);
            canvas.drawText("Tap to go back", x + width / 2, (int)(y + (float)(height * 0.8)), paint);
        }
        else {
            paint.setTextSize(bigText);
            paint.setTextAlign(Paint.Align.CENTER);
            paint.setColor(Color.WHITE);
            canvas.drawText("Defeat!", x + width / 2 ,y + height / 2, paint);
            paint.setTextSize(midText);
            canvas.drawText("Tap to restart game", x + width / 2, (int)(y + (float)(height * 0.9)), paint);
        }
    }
}
