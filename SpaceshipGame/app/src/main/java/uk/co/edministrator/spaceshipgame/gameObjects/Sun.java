package uk.co.edministrator.spaceshipgame.gameObjects;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import com.google.gson.Gson;

import java.io.InputStream;
import java.io.Serializable;
import java.util.Random;

import uk.co.edministrator.spaceshipgame.CONSTANTS;
import uk.co.edministrator.spaceshipgame.tools.FileIO;

public class Sun implements Serializable {
    private float x;
    private float y;
    public Bitmap sprite;
    public Bitmap glow;
    private transient Paint p = new Paint();
    float scale;

    public Sun(FileIO fileIO){
        this.x = CONSTANTS.getScreenWidth()/2;
        this.y = CONSTANTS.getScreenHeight()/2;

        Random r = new Random();
        int type = r.nextInt(5);
        InputStream is;
        if (type == 0){
            is = fileIO.readAsset("backgrounds/stars/star_blue01.png");
            sprite = BitmapFactory.decodeStream(is);
            is = fileIO.readAsset("backgrounds/stars/star_blue01_glow.png");
            glow = BitmapFactory.decodeStream(is);

        }
        else if (type == 1){
            is = fileIO.readAsset("backgrounds/stars/star_orange01.png");
            sprite = BitmapFactory.decodeStream(is);
            is = fileIO.readAsset("backgrounds/stars/star_orange01_glow.png");
            glow = BitmapFactory.decodeStream(is);
        }
        else if (type == 2){
            is = fileIO.readAsset("backgrounds/stars/star_red01.png");
            sprite = BitmapFactory.decodeStream(is);
            is = fileIO.readAsset("backgrounds/stars/star_red01_glow.png");
            glow = BitmapFactory.decodeStream(is);
        }
        else if (type == 3){
            is = fileIO.readAsset("backgrounds/stars/star_white01.png");
            sprite = BitmapFactory.decodeStream(is);
            is = fileIO.readAsset("backgrounds/stars/star_white01_glow.png");
            glow = BitmapFactory.decodeStream(is);
        }
        else if (type == 4){
            is = fileIO.readAsset("backgrounds/stars/star_yellow01.png");
            sprite = BitmapFactory.decodeStream(is);
            is = fileIO.readAsset("backgrounds/stars/star_yellow01_glow.png");
            glow = BitmapFactory.decodeStream(is);
        }
        sprite = Bitmap.createScaledBitmap(sprite, 256,256,false);
        glow = Bitmap.createScaledBitmap(glow,256,256,true);
        scale = 256;
    }

    public Bitmap getSprite() {
        return sprite;
    }


    public void draw(Canvas canvas){
        canvas.drawBitmap( sprite, x - sprite.getWidth()/2, y - sprite.getHeight()/2, p);
    }
}
