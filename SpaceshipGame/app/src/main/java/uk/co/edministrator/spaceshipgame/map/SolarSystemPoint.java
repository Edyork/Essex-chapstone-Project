package uk.co.edministrator.spaceshipgame.map;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Rect;
import uk.co.edministrator.spaceshipgame.tools.FileIO;


public class SolarSystemPoint{
    private int x,y, id;
    private transient SolarSystemPoint parent;
    private transient SolarSystemPoint lChild, upChild, rChild, downChild;
    private int parentId = 0, lChildId = 0,upChildId = 0, rChildId = 0, downChildId = 0;
    public SolarSystem solarSystem;
    private Bitmap sprite;
    private int difficulty;


    //temp node(s)
    public SolarSystemPoint(int x, int y){
        this.x = x;
        this.y = y;
        id = -1;
    }
    //Root node
    public SolarSystemPoint(int id, int x, int y,int diff, FileIO fio){
        this.id = id;
        this.x = x;
        this.y = y;
        this.difficulty = diff;
        int width = 128;
        int height = 128;
        solarSystem = new SolarSystem(fio, difficulty);
        this.sprite = Bitmap.createScaledBitmap(solarSystem.sun.getSprite(), width,height,true);
    }

    //All other nodes
    public SolarSystemPoint(int id, int x, int y,int diff, SolarSystemPoint parent, FileIO fio){
        this.id = id;
        this.x = x;
        this.y = y;
        this.parent = parent;
        parentId = parent.getId();
        int width = 128;
        int height = 128;
        this.difficulty = diff;
        solarSystem = new SolarSystem(fio, difficulty);
        this.sprite = Bitmap.createScaledBitmap(solarSystem.sun.getSprite(), width,height,true);
    }

    public void setlChild(SolarSystemPoint lChild) {
        this.lChild = lChild;
        this.lChildId = lChild.getId();
    }

    public int getId() {
        return id;
    }

    public void setupChild(SolarSystemPoint upChild) {
        this.upChild = upChild;
        upChildId = upChild.getId();
    }

    public void setrChild(SolarSystemPoint rChild) {
        this.rChild = rChild;
        rChildId = rChild.getId();
    }

    public void setdownChild(SolarSystemPoint downChild) {
        this.downChild = downChild;
        downChildId = downChild.getId();
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }
    public void setXY(int x, int y){
        this.x = x;
        this.y = y;
    }
    public Rect getBounds(){
        Rect myLocation = new Rect(x, y, x +128,  y +128);
        return myLocation;
    }

    public int getID(){
        return id;
    }
    public SolarSystemPoint getParent() {
        return parent;
    }

    public int getParentId() {
        return parentId;
    }

    public SolarSystemPoint getlChild() {
        return lChild;
    }

    public int getlChildId() {
        return lChildId;
    }

    public SolarSystemPoint getupChild() {
        return upChild;
    }

    public int getUpChildId() {
        return upChildId;
    }

    public SolarSystemPoint getrChild() {
        return rChild;
    }

    public int getrChildId() {
        return rChildId;
    }

    public SolarSystemPoint getDownChild() {
        return downChild;
    }

    public int getDownChildId() {
        return downChildId;
    }

    public void setSolarSystem(SolarSystem solarSystem) {
        this.solarSystem = solarSystem;
    }

    public void setParent(SolarSystemPoint parent) {
        this.parent = parent;
    }
    public SolarSystem getSolarSystem(){
        return solarSystem;
    }
    public void movePoint(int x, int y){
        this.x += x;
        this.y += y;
        if (lChild !=null) lChild.movePoint(x,y);
        if (upChild !=null) upChild.movePoint(x,y);
        if (rChild !=null) rChild.movePoint(x,y);
        if (downChild !=null)downChild.movePoint(x,y);
    }
    public void draw(Canvas canvas){
        Paint p = new Paint();
        p.setColor(Color.WHITE);
        int w = sprite.getWidth() / 2;
        int h = sprite.getHeight() / 2;
        p.setPathEffect(new DashPathEffect(new float[]{8,16},50));
        if (lChild != null){
            canvas.drawLine(x +w,y + h,lChild.getX() + w,lChild.getY() + h,p);
            lChild.draw(canvas);
        }
        if (upChild != null){
            canvas.drawLine(x + h,y + h,upChild.getX() + w,upChild.getY() + h,p);
            upChild.draw(canvas);
        }
        if (rChild != null){
            canvas.drawLine(x + w,y +h,rChild.getX() + w,rChild.getY() + h,p);
            rChild.draw(canvas);
        }
        if (downChild !=null){
            canvas.drawLine(x + w, y + h, downChild.getX() + w, downChild.getY() + h, p);
            downChild.draw(canvas);
        }
        canvas.drawBitmap(sprite,x,y,p);
    }
}
