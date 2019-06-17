package uk.co.edministrator.spaceshipgame.map;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import com.google.gson.Gson;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import uk.co.edministrator.spaceshipgame.CONSTANTS;
import uk.co.edministrator.spaceshipgame.tools.FileIO;

public class Galaxy implements Serializable {
    private int x, y;
    private SolarSystemPoint Rootpoint;
    private ArrayList<SolarSystemPoint> generatedPoints;
    private Paint paint;
    private int idCount;

    public Galaxy(FileIO fio){
        paint = new Paint();

        //get X and Y based on 1/16th of screen size
        x = CONSTANTS.getScreenWidth() / 4;
        y = CONSTANTS.getScreenHeight() / 4;

        Rootpoint = new SolarSystemPoint(idCount++,x, y,1, fio);
        generatedPoints = new ArrayList<>();
        generatedPoints.add(Rootpoint);
    }

    public void genPoints(int x, int y, SolarSystemPoint p, FileIO fio) {
        Random rand = new Random();
        int difficulty = generatedPoints.size() * 5;

        ArrayList type = new ArrayList();
        type.add("left");
        type.add("up");
        type.add("right");
        type.add("down");

        Log.d("Children", "###New Node###");
        SolarSystemPoint temp; //inherits parents co-ordinates

        //get new co-ordinates
        int left = x - CONSTANTS.getScreenWidth() / 4;
        int up = y - CONSTANTS.getScreenHeight() / 4;
        int right = x + CONSTANTS.getScreenWidth() / 4;
        int down = y + CONSTANTS.getScreenHeight() / 4;

        //check grand-parent node (if exists)
        if (p.getParent() != null) {
            Log.d("Children", "Checking grandparent....");
            //left
            temp = new SolarSystemPoint(left, y);
            if (temp.getBounds().intersect(p.getParent().getBounds())) {
                Log.d("Children", "Intersect Left!");
                type.remove("left");
            }
            //up
            temp = new SolarSystemPoint(x, up);
            if (temp.getBounds().intersect(p.getParent().getBounds())) {
                Log.d("Children", "Intersect Up!");
                type.remove("up");
            }
            //right
            temp = new SolarSystemPoint (right, y);
            if (temp.getBounds().intersect(p.getParent().getBounds())) {
                Log.d("Children", "Intersect Right!");
                type.remove("right");
            }
            //down
            temp = new SolarSystemPoint(x,down);
            if (temp.getBounds().intersect(p.getParent().getBounds())) {
                Log.d("Children", "Intersect Down!");
                type.remove("down");
            }
        }


        Iterator it = generatedPoints.iterator();
        Log.d("Children", "Checking list");
        while (it.hasNext()){
            SolarSystemPoint current = (SolarSystemPoint) it.next();
            //left
            temp = new SolarSystemPoint(left, y);
            if (type.contains("left") && temp.getBounds().intersect(current.getBounds())){
                type.remove("left");
                Log.d("Children", "Intersect left");

            }
            //up
            temp = new SolarSystemPoint(x, up);
            if (type.contains("up") && temp.getBounds().intersect(current.getBounds())){
                type.remove("up");
                Log.d("Children", "Intersect up");
            }
            //right
            temp = new SolarSystemPoint(right, y);
            if (type.contains("right") && temp.getBounds().intersect(current.getBounds())){
                type.remove("right");
                Log.d("Children","Intersect Right");
            }
            //down
            temp = new SolarSystemPoint(x, down);
            if (type.contains("down") && temp.getBounds().intersect(current.getBounds())){
                type.remove("down");
                Log.d("Children", "Intersect down");
            }
        }

        //check if empty, strictly speaking impossible, if it is just return.
        if (type.isEmpty()){
            Log.d("Children", "Nowhere to go!");
            return;
        }
        //How many children?
        int numOfChildren = rand.nextInt(type.size());  //evenly distributed at
        numOfChildren++;
        Log.d("Children", "Num:" + numOfChildren);
        //Which children?
        for (int i = 0; i < numOfChildren; i++){
            int pickIndex = rand.nextInt(type.size());
            String selectedChild = (String) type.get(pickIndex);
            Log.d("Children","Generating child " + type.get(pickIndex));
            switch (selectedChild){
                case ("left"):
                    type.remove("left");
                    p.setlChild(new SolarSystemPoint(idCount++,left + (rand.nextInt(40) - 20),y + (rand.nextInt(40) - 20),difficulty,p,fio));
                    generatedPoints.add(p.getlChild());
                    break;
                case ("up"):
                    type.remove("up");
                    p.setupChild(new SolarSystemPoint(idCount++,x + (rand.nextInt(40) - 20),up + (rand.nextInt(40) - 20),difficulty,p,fio));
                    generatedPoints.add(p.getupChild());
                    break;
                case ("right"):
                    type.remove("right");
                    p.setrChild(new SolarSystemPoint(idCount++,right+ (rand.nextInt(40) - 20),y + (rand.nextInt(40) - 20),difficulty,p,fio));
                    generatedPoints.add(p.getrChild());
                    break;
                case ("down"):
                    type.remove("down");
                    p.setdownChild(new SolarSystemPoint(idCount++,x + (rand.nextInt(40) - 20),down + (rand.nextInt(40) - 20),difficulty,p,fio));
                    generatedPoints.add(p.getDownChild());
                    break;
            }
        }
    }
    public void SaveGalaxy(Context context){
        Gson gson = new Gson();
        try {
            File myFile = new File("/sdcard/Android/data/uk.co.edministrator.spaceshipgame/files/galaxymap.json");
            myFile.createNewFile();
            FileOutputStream fOut = new FileOutputStream(myFile);
            OutputStreamWriter osw = new OutputStreamWriter(fOut);
            gson.toJson(generatedPoints, osw);
            osw.flush();
            osw.close();
            Log.d("GsonTest", "success");
        }
        catch (IOException e){
            Log.d("GsonTest",e.toString());
        }
    }

    public ArrayList getGeneratedPoints() {
        return generatedPoints;
    }

    public SolarSystemPoint getRootpoint() {
        return Rootpoint;
    }
    public void movePoints(int x, int y){
        Rootpoint.movePoint(x,y);
    }
    public void draw(Canvas canvas){
        Rootpoint.draw(canvas);
    }
}
