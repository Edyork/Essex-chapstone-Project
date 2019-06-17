package uk.co.edministrator.spaceshipgame.UI;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import java.io.InputStream;

import uk.co.edministrator.spaceshipgame.CONSTANTS;
import uk.co.edministrator.spaceshipgame.tools.FileIO;

public class PanView {
    private Bitmap bitmap;
    private int x, y, height, width;
    private Boolean active;
    private boolean isleft;

    public PanView(String side, boolean active, Context context){
        height = (int) (CONSTANTS.getScreenHeight()/2);  //same for both
        if (side.equals("left")){
            FileIO assets = new FileIO(context);
            InputStream is = assets.readAsset("UI/combat/PanLeft.png");
            Bitmap temp = BitmapFactory.decodeStream(is);
            x = -10;
            y = CONSTANTS.getScreenHeight()/4;
            width = CONSTANTS.getScreenHeight()/5;
            bitmap = Bitmap.createScaledBitmap(temp,width,height,true);
            this.active = active;
            isleft = true;

        }
        else if (side.equals("right")){
            FileIO assets = new FileIO(context);
            InputStream is = assets.readAsset("UI/combat/PanRight.png");
            Bitmap temp = BitmapFactory.decodeStream(is);
            y = CONSTANTS.getScreenHeight()/4;
            width = CONSTANTS.getScreenHeight()/5;
            x = CONSTANTS.getScreenWidth() - width - 10;
            bitmap = Bitmap.createScaledBitmap(temp,width,height,true);
            this.active = active;
        }
    }
    //LERP function
    public float lerp(float point1, float point2, float t){
        return point1 + t * (point2 - point1);
    }
    public static void moveCamera(Canvas canvas, float dir){
        canvas.translate(CONSTANTS.getScreenWidth() * dir, CONSTANTS.getScreenHeight() * dir);
    }
    public Rect getBounds(){
        return new Rect(x,y,x + width, y + height);
    }

    public void setActive(boolean active){
        this.active = active;
    }

    public Boolean getActive() {
        return active;
    }

    public void draw(Canvas canvas){
        Paint p = new Paint();
        if (isleft){
            canvas.drawBitmap(bitmap,x + (CONSTANTS.getScreenWidth() * 1.5f),y,p);
        }
        else canvas.drawBitmap(bitmap,x,y,p);
    }
}
