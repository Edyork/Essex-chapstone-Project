package uk.co.edministrator.spaceshipgame.map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Random;

import uk.co.edministrator.spaceshipgame.CONSTANTS;
import uk.co.edministrator.spaceshipgame.tools.FileIO;


public class Planet implements Serializable {
    private transient float x,y;
    private transient Planet parent, child;
    private int id;
    private int picId;
    private int parentId;
    private int childId;
    private int ring;
    private transient Bitmap sprite;
    private transient Paint paint;
    private int diff;
    public transient float radius;
    private boolean unexplored = true;
    private transient Bitmap tick;

    public Planet(int id, int diff, FileIO fio, Planet parent){
        this.id = id;
        Random random = new Random();
        this.diff = diff + (random.nextInt(4)-2);
        Random r = new Random();
        ring = r.nextInt(3);
        int extrarot = 0;
        double centerX = (CONSTANTS.getScreenWidth() / 2);
        if (ring == 0) {
            this.x = (int) (CONSTANTS.getScreenWidth() / 4.1);
            radius =(float) (centerX - x);
        }
        else if (ring == 1){
            this.x = (int) (CONSTANTS.getScreenWidth() / 3.2);
            radius =(float) (centerX - x);
            extrarot = 5;
        }
        else {
            this.x = (int) (CONSTANTS.getScreenWidth() / 2.6);
            radius =(float) (centerX - x);
            extrarot = 10;
        }
        this.y = CONSTANTS.getScreenHeight() / 2;
        double centerY = (CONSTANTS.getScreenHeight() / 2);
        double newX = centerX + (this.x- centerX)*Math.cos(id*20+extrarot) - (this.y- centerY)*Math.sin(id*20+extrarot);
        double newY = centerY + (this.x- centerX)*Math.sin(id*20+extrarot) + (this.y- centerY)*Math.cos(id*20+extrarot);
        this.x = (int) newX;
        this.y = (int) newY;
        this.parent = parent;
        picId = new Random().nextInt(16);
        if (parent != null) {
            this.parentId = parent.getId();
            if (parent !=null) parent.setChild(this);
        }
        paint = new Paint();
    }

    public Planet(int x, int y, FileIO fio){
        this.x = x;
        this.y = y;
        this.sprite = genBitmap(fio,new Random().nextInt(16));
        this.parent = this;
        this.parentId = parent.getId();
    }


    public void createBitmap(Context context){
        FileIO fio = new FileIO(context);
        sprite = genBitmap(fio, picId);
        sprite = Bitmap.createScaledBitmap(sprite, 128,128,true);
        InputStream is = fio.readAsset("UI/tick.png");
        tick = BitmapFactory.decodeStream(is);
        try {
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Bitmap genBitmap(FileIO fio, int id){
        InputStream is;
        switch (id){
            case (0):
                is = fio.readAsset("backgrounds/planets/planet_0.png");
                sprite = BitmapFactory.decodeStream(is);
                return sprite;
            case (1):
                is = fio.readAsset("backgrounds/planets/planet_1.png");
                sprite = BitmapFactory.decodeStream(is);
                return sprite;
            case (2):
                is = fio.readAsset("backgrounds/planets/planet_2.png");
                sprite = BitmapFactory.decodeStream(is);
                return sprite;
            case (3):
                is = fio.readAsset("backgrounds/planets/planet_3.png");
                sprite = BitmapFactory.decodeStream(is);

                return sprite;
            case (4):
                is = fio.readAsset("backgrounds/planets/planet_4.png");
                sprite = BitmapFactory.decodeStream(is);
                return sprite;
            case (5):
                is = fio.readAsset("backgrounds/planets/planet_5.png");
                sprite = BitmapFactory.decodeStream(is);
                return sprite;
            case (6):
                is = fio.readAsset("backgrounds/planets/planet_6.png");
                sprite = BitmapFactory.decodeStream(is);
                return sprite;
            case (7):
                is = fio.readAsset("backgrounds/planets/planet_7.png");
                sprite = BitmapFactory.decodeStream(is);
                return sprite;
            case (8):
                is = fio.readAsset("backgrounds/planets/planet_8.png");
                sprite = BitmapFactory.decodeStream(is);
                return sprite;
            case (9):
                is = fio.readAsset("backgrounds/planets/planet_9.png");
                sprite = BitmapFactory.decodeStream(is);
                return sprite;
            case (10):
                is = fio.readAsset("backgrounds/planets/planet_10.png");
                sprite = BitmapFactory.decodeStream(is);
                return sprite;
            case (11):
                is = fio.readAsset("backgrounds/planets/planet_11.png");
                sprite = BitmapFactory.decodeStream(is);
                return sprite;
            case (12):
                is = fio.readAsset("backgrounds/planets/planet_12.png");
                sprite = BitmapFactory.decodeStream(is);
                return sprite;
            case (13):
                is = fio.readAsset("backgrounds/planets/planet_13.png");
                sprite = BitmapFactory.decodeStream(is);
                return sprite;
            case (14):
                is = fio.readAsset("backgrounds/planets/planet_14.png");
                sprite = BitmapFactory.decodeStream(is);
                return sprite;
            case (15):
                is = fio.readAsset("backgrounds/planets/planet_15.png");
                sprite = BitmapFactory.decodeStream(is);
                return sprite;

        }
        return null;
    }

    public void reinit(Context context){

        sprite = Bitmap.createScaledBitmap(genBitmap(new FileIO(context), picId),128,128,true);
        paint = new Paint();
        int extrarot = 0;
        double centerX = (CONSTANTS.getScreenWidth() / 2);
        if (ring == 0) {
            this.x = (int) (CONSTANTS.getScreenWidth() / 4.1);
            radius =(float) (centerX - x);
        }
        else if (ring == 1){
            this.x = (int) (CONSTANTS.getScreenWidth() / 3.2);
            radius =(float) (centerX - x);
            extrarot = 5;
        }
        else {
            this.x = (int) (CONSTANTS.getScreenWidth() / 2.6);
            radius =(float) (centerX - x);
            extrarot = 10;
        }
        this.y = CONSTANTS.getScreenHeight() / 2;
        double centerY = (CONSTANTS.getScreenHeight() / 2);
        double newX = centerX + (this.x- centerX)*Math.cos(id*20+extrarot) - (this.y- centerY)*Math.sin(id*20+extrarot);
        double newY = centerY + (this.x- centerX)*Math.sin(id*20+extrarot) + (this.y- centerY)*Math.cos(id*20+extrarot);
        this.x = (int) newX - ( sprite.getWidth()/2);
        this.y = (int) newY - (sprite.getHeight()/2);

    }


    public int getId() {
        return id;
    }

    public int getParentId() {
        return parentId;
    }

    public int getChildId() {
        return childId;
    }

    public void setParent(Planet parent) {
        this.parent = parent;
        parentId = parent.getId();
    }

    public int getPicId() {
        return picId;
    }

    public void setChild(Planet s){
        child = s;
        childId = child.getId();
    }
    public boolean getUnexplored(){
        return unexplored;
    }
    public void setUnexplored(boolean unexplored) {
        this.unexplored = unexplored;
    }

    public int getDiff() {
        return diff;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public Planet getChild() {
        return child;
    }

    public Planet getParent() {
        return parent;
    }
    public Rect getBounds(){
        Rect myLocation = new Rect((int) x, (int) y, (int) x +sprite.getWidth(),  (int) y +sprite.getHeight());
        return myLocation;
    }
    public void draw(Canvas c){
        paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.STROKE);
        c.drawBitmap(sprite,x,y, paint);
        if (unexplored) {
            paint.setStyle(Paint.Style.FILL);
            paint.setTextSize(25);
            c.drawText(diff + "%", x - 10, y - 10, paint);
        }
        else {
            c.drawBitmap(tick,x - 10, y - 10, paint);
        }
    }
    public String toString(){
        return "X: " + x + ", Y: " + y;
    }

    public Bitmap getSprite() {
        return sprite;
    }
}
