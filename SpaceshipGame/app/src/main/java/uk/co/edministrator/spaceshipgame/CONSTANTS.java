package uk.co.edministrator.spaceshipgame;

import android.app.Activity;
import android.util.DisplayMetrics;

public class CONSTANTS {
    private static int screenWidth;
    private static int screenHeight;
    private static int RoomId;

    public static void setConstants(Activity activity){
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        screenWidth = metrics.widthPixels;
        screenHeight = metrics.heightPixels;
        RoomId = 0;
    }

    public static int getScreenHeight() {
        return screenHeight;
    }

    public static int getScreenWidth() {
        return screenWidth;
    }

    public static int getRoomId() {
        return RoomId;
    }

    public static void setRoomId(int roomId) {
        RoomId = roomId;
    }
}
