package uk.co.edministrator.spaceshipgame.UI;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

import uk.co.edministrator.spaceshipgame.weapons.Weapon;

public class combatButton {
    protected int width, height, x, y, state;
    protected Bitmap ready, active, cooldown;

    public Rect getBounds(){
        return new Rect(x,y,x+width,y+height);
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

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
