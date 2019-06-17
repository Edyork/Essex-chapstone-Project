package uk.co.edministrator.spaceshipgame;

import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

import uk.co.edministrator.spaceshipgame.UI.PanView;
import uk.co.edministrator.spaceshipgame.views.customView;

public class Game extends Thread {
    public boolean running;
    public  boolean pan = false;
    public boolean left;
    private customView view;
    private SurfaceHolder surfaceHolder;

    public Game (customView gameView, SurfaceHolder surfaceHolder){
        this.view = gameView;
        this.surfaceHolder = surfaceHolder;
    }

    public void setRunning(boolean running){
        this.running = running;
    }

    public void run(){
        while (running){
            Canvas canvas = null;
            try {
                canvas = this.surfaceHolder.lockCanvas(); //Lock canvas to change/draw

                //Synchronized so no logic/drawing concurrent modification errors
                if (canvas != null) {
                    synchronized (canvas) {
                        view.update();
                        view.draw(canvas);
                        if (pan){
                            if (left){
                                canvas.translate(CONSTANTS.getScreenWidth(), CONSTANTS.getScreenHeight());
                            }
                            else canvas.translate(CONSTANTS.getScreenWidth() * - 1, CONSTANTS.getScreenHeight() * -1);
                        }
                    }
                }
            }
            catch (Exception e){
                e.printStackTrace(); //Print error
            }
            finally {
                if (canvas!=null){
                    //unlock canvas
                    this.surfaceHolder.unlockCanvasAndPost(canvas);
                    Log.d("","");
                }
            }
        }
    }
    public void end(){

    }
}
