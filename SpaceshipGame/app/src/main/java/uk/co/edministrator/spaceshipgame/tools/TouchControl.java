package uk.co.edministrator.spaceshipgame.tools;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import uk.co.edministrator.spaceshipgame.CONSTANTS;


public class TouchControl implements View.OnTouchListener {
    private float DownX = 0, DownY = 0;
    private float MoveX = 0, MoveY = 0;
    private float xDiff = 0, yDiff = 0;

    private MotionEvent event;
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        this.event = event;
        switch (event.getAction()) {
            case (MotionEvent.ACTION_DOWN):
                DownX = event.getX();
                DownY = event.getY();
                Log.d("Touch", "X: "+DownX + " Y "+DownY);
                return true;
            case (MotionEvent.ACTION_MOVE):
                MoveX = event.getRawX();
                MoveY = event.getRawY();

                xDiff = DownX - MoveX;
                yDiff = DownY - MoveY;

                DownX = MoveX;
                DownY = MoveY;
                MoveX    = 0;
                MoveY = 0;
                return true;

            default:
                return false;
        }
    }
    public double getDegreesFromTouchEvent(){
        double delta_x = DownX - (CONSTANTS.getScreenWidth()) /2;
        double delta_y = (CONSTANTS.getScreenHeight()) /2 - DownY;
        double radians = Math.atan2(delta_y, delta_x);

        return Math.toDegrees(radians);
    }


    public float getDownX() {
        return DownX;
    }

    public float getDownY() {
        return DownY;
    }

    public MotionEvent  getEvent(){
        return event;
    }

    public float getxDiff() {
        return xDiff;
    }

    public float getyDiff() {
        return yDiff;
    }
    public void resetDown(){
        DownY = -1;
        DownX = -1;
    }
    public void setTestInput(int x, int y){
        DownX = x;
        DownY = y;
    }
}
