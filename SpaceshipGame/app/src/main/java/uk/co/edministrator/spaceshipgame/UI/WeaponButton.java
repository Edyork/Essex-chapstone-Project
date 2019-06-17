package uk.co.edministrator.spaceshipgame.UI;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import java.io.IOException;
import java.io.InputStream;

import uk.co.edministrator.spaceshipgame.tools.FileIO;
import uk.co.edministrator.spaceshipgame.weapons.Weapon;
import uk.co.edministrator.spaceshipgame.weapons.WeaponClass;


public class WeaponButton extends combatButton{
    public Paint paint;
    private Weapon weapon;

    public WeaponButton(WeaponClass type, Context context, int x, int y, Weapon weapon ){
        this.state = 0;
        FileIO assets = new FileIO(context);
        String name = "";
        switch (type){
            case POWER:
                name="rocket";
                break;
            case ACCURATE:
                name="bullet";
                break;
            case ENERGY:
                name="laser";
                break;
        }
        InputStream is = assets.readAsset("UI/combat/"+name+".png");
        ready = BitmapFactory.decodeStream(is);
        is = assets.readAsset("UI/combat/"+name+"Active.png");
        active = BitmapFactory.decodeStream(is);
        is = assets.readAsset("UI/combat/"+name+"Cooldown.png");
        cooldown = BitmapFactory.decodeStream(is);
        ready = Bitmap.createScaledBitmap(ready, 128, 128,true);
        active = Bitmap.createScaledBitmap(active, 128, 128,true);
        cooldown = Bitmap.createScaledBitmap(cooldown, 128, 128,true);
        try {
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.x = x;
        this.y = y;
        this. width = ready.getWidth();
        this.height = ready.getHeight();
        this.weapon = weapon;
        paint = new Paint();
    }
    public Rect getBounds(){
        return new Rect(x,y,x+width,y+height);
    }

    public Weapon getWeapon() {
        return weapon;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getState() {
        return state;
    }

    @Override
    public void draw(Canvas canvas, float translate){
        switch (state){
            case (0):
                canvas.drawBitmap(ready, x + translate,y,new Paint());
                break;
            case (1):
                canvas.drawBitmap(active, x + translate,y,new Paint());
                break;
            case (2):
                canvas.drawBitmap(cooldown, x + translate,y,new Paint());
                break;
        }
    }
}
