package uk.co.edministrator.spaceshipgame.tools;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;

import java.io.IOException;

public class Music {
    MediaPlayer mediaPlayer;
    FileIO assets;
    private boolean isPlaying = false;

    public Music(Activity activity){
        mediaPlayer = new MediaPlayer();
        AssetManager assets = activity.getAssets();
        try {
        AssetFileDescriptor fd = assets.openFd("Music/background_explore.wav");
        mediaPlayer.setDataSource(fd.getFileDescriptor(), fd.getStartOffset(), fd.getLength());
        mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void play(){
        mediaPlayer.start();
    }
    public void pause(){
        mediaPlayer.pause();
        isPlaying = false;
    }
    public void stop(){
        mediaPlayer.stop();
        isPlaying = false;
    }
    public void setVolume(float v){
        mediaPlayer.setVolume(v,v);
    }
    public boolean isPlaying(){
        if (isPlaying) return true;
        else return false;
    }
    public void dispose(){
        this.stop();
        mediaPlayer.release();
    }
    public void setTrack(Activity activity, int t){
        AssetManager assets = activity.getAssets();
        AssetFileDescriptor fd = null;
        mediaPlayer.reset();
        try {
            if (t == 1) {
                fd = assets.openFd("Music/background_explore.wav");
                mediaPlayer.setDataSource(fd.getFileDescriptor(), fd.getStartOffset(), fd.getLength());
                mediaPlayer.prepare();
            }
            else {
                fd = assets.openFd("Music/background_fight.wav");
                mediaPlayer.setDataSource(fd.getFileDescriptor(), fd.getStartOffset(), fd.getLength());
                mediaPlayer.prepare();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public MediaPlayer getThis(){
        return mediaPlayer;
    }
}
