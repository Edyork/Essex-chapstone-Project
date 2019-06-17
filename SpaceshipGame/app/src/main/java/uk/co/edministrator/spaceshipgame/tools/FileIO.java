package uk.co.edministrator.spaceshipgame.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import android.content.Context;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class FileIO {
    private Context context;
    private AssetManager assets;
    String externalPath;

    public FileIO(Context context) {
        this.context = context;
        this.assets = context.getAssets();
        this.externalPath = context.getExternalFilesDir(null).getAbsolutePath() + File.separator;
    }

    public InputStream readAsset(String fileName){
        try {
            return assets.open(fileName);
        }
        catch (IOException e){return null;}
    }

    public InputStream readFile(String fileName) throws IOException{
        return new FileInputStream(externalPath + fileName);
    }

    public OutputStream writeFile(String fileName) throws IOException {
        return new FileOutputStream(externalPath + fileName);
    }


    public AssetFileDescriptor myOpenFd(String fd){
        try {
            Log.d("AUDIO_DEBUG","hello");
            AssetFileDescriptor afd = assets.openFd(fd);
            Log.d("AUDIO_DEBUG","there");
            return assets.openFd(fd);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public Bitmap buildBitmaps(String name){
        InputStream is =  this.readAsset(name);
        Bitmap bitmap = BitmapFactory.decodeStream(is);
        return bitmap;
    }
}
