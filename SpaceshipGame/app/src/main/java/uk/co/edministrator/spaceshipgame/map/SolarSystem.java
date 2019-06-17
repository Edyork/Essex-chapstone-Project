package uk.co.edministrator.spaceshipgame.map;


import android.content.Context;
import android.graphics.Canvas;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;
import uk.co.edministrator.spaceshipgame.gameObjects.Sun;
import uk.co.edministrator.spaceshipgame.tools.FileIO;

public class SolarSystem implements Serializable {
    private transient Planet rootNode;
    private ArrayList<Planet> hasPlanet;
    private transient int idCount;
    private int difficulty;
    public Sun sun;

    public SolarSystem(FileIO fio, int difficulty){
        //Divide into 1/100 chunk to make 10x10 grid.
        int gridSize = 5;

        //set difficulty modifier
        this.difficulty = difficulty;
        //true or false random dist: does this section have a planet

        //Create a list of sections with solar systems in it so we don't have to go through the whole list each time;
        hasPlanet = new ArrayList<>();


        rootNode = new Planet(idCount++,difficulty, fio, null);
        Planet previous = rootNode;
        hasPlanet.add(rootNode);
        Random r = new Random();
        int n = r.nextInt(5)+3;
        for (int i = 0; i < n; i++){
            Planet temp = new Planet(idCount++,difficulty, fio, previous);
            hasPlanet.add(temp);
            previous = temp;
        }
        sun = new Sun(fio);
    }
    public SolarSystem(ArrayList<Planet> importedList){
        hasPlanet = importedList;
        rootNode = hasPlanet.get(0);
        for (int i = 0; i < importedList.size(); i++) {
            Planet current = importedList.get(i);
            for (int j = 0; j < importedList.size(); j++) {
                if (i == j) continue;
                Planet selected = importedList.get(j);
                if (current.getParentId() == selected.getId()) {
                    current.setParent(selected);
                    continue;
                }
                if (current.getChildId() == selected.getId()) {
                    current.setChild(selected);
                }
            }
        }
    }

    public Sun getSun() {
        return sun;
    }


    public Planet getRootNode() {
        return rootNode;
    }

    public ArrayList<Planet> getMap(){
        return hasPlanet;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void reinit(Context context){
        this.rootNode = hasPlanet.get(0);
        for (int i = 0; i < hasPlanet.size(); i++){
            hasPlanet.get(i).reinit(context);
        }
        for (int i = 0; i < hasPlanet.size(); i++){
            for (int j = 0; j < hasPlanet.size(); j++){
                if (hasPlanet.get(i).getChildId() == hasPlanet.get(j).getId())
                    hasPlanet.get(i).setChild(hasPlanet.get(j));
                else if (hasPlanet.get(i).getParentId() == hasPlanet.get(j).getId())
                    hasPlanet.get(i).setParent(hasPlanet.get(j));
            }
        }
    }


    public void draw(Canvas c){
        for (Planet s : hasPlanet){
               s.draw(c);
           }
    }
}
