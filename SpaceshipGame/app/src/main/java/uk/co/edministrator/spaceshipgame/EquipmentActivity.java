package uk.co.edministrator.spaceshipgame;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;

import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

import uk.co.edministrator.spaceshipgame.Ships.Inventory;
import uk.co.edministrator.spaceshipgame.gameObjects.Player;
import uk.co.edministrator.spaceshipgame.gameObjects.PlayerData;
import uk.co.edministrator.spaceshipgame.gameObjects.Ship;
import uk.co.edministrator.spaceshipgame.map.SolarSystem;
import uk.co.edministrator.spaceshipgame.map.SolarSystemPoint;
import uk.co.edministrator.spaceshipgame.tools.FileIO;
import uk.co.edministrator.spaceshipgame.views.IconView;
import uk.co.edministrator.spaceshipgame.weapons.Weapon;


public class EquipmentActivity extends Activity {
    public Player player;
    public static TableLayout statsTable;
    public static TableLayout shipTable;
    public static Weapon selectedWeapon;
    public static int SlotsInUse;
    public static int SlotsAvailable;
    private static final int rows = 5;
    private static final int cols = 5;
    private boolean istesting;
    private ArrayList<IconView> views;
    int launchOrigin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Hide title
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // Set full screen view
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //Fix to landscape
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_equipment);

        Intent intent = getIntent();
        istesting = intent.getBooleanExtra("Testing", false);

        //get player
        Inventory inv= PlayerData.loadInventory(this);
        Ship s = PlayerData.loadShip(this);
        this.player = new Player(s, inv, this);
        SlotsAvailable = player.getShip().getWeaponSlots();
        int idNum = 1;
        for (Weapon w : player.getInventory().getWeapons()){
            w.getBitmaps(this);
            w.setId(idNum);
            idNum++;
            if (w.isEquiped()) SlotsInUse++;
        }

        selectedWeapon = player.getInventory().getWeapons().get(0);
        int invIterator = 0;
        TableLayout table = findViewById(R.id.inv);
        statsTable = findViewById(R.id.stats);
        statsTable.setBackground(getResources().getDrawable(R.drawable.stats));
        shipTable = findViewById(R.id.ship);
        shipTable.setBackground(getResources().getDrawable(R.drawable.stats));
        for (int i = 0; i < rows; i++) {

            TableRow row = new TableRow(this);
            row.setPadding(0,0,0,0);
            TableRow.LayoutParams rlayout = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT,1);
            row.setLayoutParams(rlayout);

            for (int j = 0; j < cols; j++) {
                Bitmap sprite = null;

                IconView iconView = new IconView(this, player){
                    @Override
                    protected void onDraw(Canvas canvas) {
                        super.onDraw(canvas);
                        Rect rect = new Rect();
                        Paint paint = new Paint();
                        paint.setStyle(Paint.Style.STROKE);
                        paint.setColor(Color.BLUE);
                        paint.setStrokeWidth(2);
                        getLocalVisibleRect(rect);
                        canvas.drawRect(rect, paint);
                    }
                };
                iconView.setScaleType(ImageView.ScaleType.FIT_XY);

                try {
                    if (invIterator < player.getInventory().getWeapons().size()) {

                        //testing purposes
                        if (istesting){
                            views.add(iconView);
                        }

                        String originalAddr = player.getInventory().getWeapons().get(invIterator).getBitmapAddr();
                        InputStream is = (new FileIO(this).readAsset(originalAddr));
                        sprite = Bitmap.createScaledBitmap(BitmapFactory.decodeStream(is), CONSTANTS.getScreenWidth() / (cols*2) - 4, CONSTANTS.getScreenHeight() / (rows) - 4, true);
                        iconView.setImageBitmap(sprite);
                        iconView.setWeapon(player.getInventory().getWeapons().get(invIterator));

                        String eqAddr = iconView.getWeapon().getBitmapAddrEq();
                        is =(new FileIO(this).readAsset(eqAddr));
                        Bitmap eq = Bitmap.createScaledBitmap(BitmapFactory.decodeStream(is),CONSTANTS.getScreenWidth() / (cols*2) - 4, CONSTANTS.getScreenHeight() / (rows) - 4, true) ;
                        iconView.setEq(eq);

                        String uneqAddr = iconView.getWeapon().getBitmapAddrUnEq();
                        is =(new FileIO(this).readAsset(uneqAddr));
                        Bitmap uneq = Bitmap.createScaledBitmap(BitmapFactory.decodeStream(is),CONSTANTS.getScreenWidth() / (cols*2) - 4, CONSTANTS.getScreenHeight() / (rows) - 4, true);
                        iconView.setUneq(uneq);

                        invIterator++;
                        iconView.setPadding(0, 0, 0, 0);
                        if (iconView.getWeapon().isEquiped())
                        {
                            iconView.setState(1);
                        }
                        else {
                            iconView.setState(0);
                        }
                        iconView.setOnTouchListener(new customTouch());
                    }else {
                        InputStream is = (new FileIO(this).readAsset("UI/inventory/slot.png"));
                        Bitmap original = BitmapFactory.decodeStream(is);
                        sprite = Bitmap.createScaledBitmap(original, CONSTANTS.getScreenWidth() / (cols*2) - 4, CONSTANTS.getScreenHeight() / (rows) - 4, true);
                        iconView.setImageBitmap(sprite);
                        iconView.setPadding(0, 0, 0, 0);
                        is.close();

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (sprite != null)
                {

                }
                TextView tv = new TextView(this);
//                tv.setText("Hello!");
//                row.addView(tv);
                row.addView(iconView);
            }
            table.addView(row);
        }
        /** Gun stats table **/
        TableRow trTitle = new TableRow(this);
        TextView tvTitle = new TextView(this);
        trTitle.setGravity(Gravity.CENTER_HORIZONTAL);
        tvTitle.setTextSize(20);
        tvTitle.setTypeface(null,Typeface.BOLD);
        tvTitle.setTextColor(Color.WHITE);
        tvTitle.setText(selectedWeapon.getName());
        tvTitle.setPadding(0,15,0,0);
        trTitle.addView(tvTitle);
        //trTitle.setBackgroundColor(Color.WHITE);
        trTitle.setPadding(5,5,5,0);
        TableRow trImage = new TableRow(this);
        ImageView iconImage = new ImageView(this);
        InputStream is = (new FileIO(this).readAsset("UI/inventory/slot.png"));
        Bitmap original = Bitmap.createScaledBitmap(BitmapFactory.decodeStream(is), CONSTANTS.getScreenWidth() / (cols*2) - 4, CONSTANTS.getScreenHeight() / (rows) - 4, true);
        try {
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        iconImage.setImageBitmap(original);
        trImage.addView(iconImage);
        trImage.setGravity(Gravity.CENTER_HORIZONTAL);
        trImage.setPadding(5,5,5,0);
        TableRow trStats = new TableRow(this);
        TextView tvStats = new TextView(this);
        trStats.setGravity(Gravity.CENTER_HORIZONTAL);
        tvStats.setTextColor(Color.WHITE);
        tvStats.setText("TYPE: " + selectedWeapon.getGreg()  +"\n" + "DAMAGE: " + selectedWeapon.getDmg());
        trStats.addView(tvStats);
        trStats.setPadding(5,0,5,0);
        statsTable.addView(trTitle);
        statsTable.addView(trImage);
        statsTable.addView(trStats);


        /** ship stats table **/
        TableRow trTitle2 = new TableRow(this);
        TextView tvTitle2 = new TextView(this);
        trTitle2.setGravity(Gravity.CENTER_HORIZONTAL);
        tvTitle2.setTextSize(20);
        tvTitle2.setTypeface(null,Typeface.BOLD);
        tvTitle2.setText("Ship stats");
        tvTitle2.setTextColor(Color.WHITE);
        trTitle2.addView(tvTitle2);
        trTitle2.setPadding(5,5,5,0);
        TableRow trStats2 = new TableRow(this);
        TextView tvStats2 = new TextView(this);
        tvStats2.setTextColor(Color.WHITE);
        tvStats2.setPadding(20,0,0,0);
        String stats =
                "Level: "+player.getShip().getLevel()+"\n"+
                "XP: "+player.getShip().getXp()+"/"+player.getShip().getXpThreshold()+"\n"+
                "Health: "+player.getShip().getTotalHp()+"\n"+
                "Total weapon slots: "+player.getShip().getWeaponSlots()+"\n"+
                "Armour: "+player.getShip().getArmour()+"%\n"+
                "Evasion: "+player.getShip().getEvasion()+"%\n"+
                "Shield: "+player.getShip().getShield()
                ;
        tvStats2.setText(stats);
        trStats2.addView(tvStats2);
        trStats2.setPadding(5,0,5,0);
        shipTable.addView(trTitle2);
        shipTable.addView(trStats2);


        /**testing**/
        if (istesting){
            Random r = new Random();
            //Randomly Unequips / Equips weapon with 50% chance of happening.
            for (int n = 0; n < views.size(); n++){
                int i = r.nextInt(2);
                if (i == 1){
                    if (views.get(n).getState() == 0){
                        views.get(n).setState(1);
                    }
                    else {
                        views.get(n).setState(0);
                    }
                }
            }

            this.onDestroy();
        }
    }

    @Override
    protected void onDestroy() {
        PlayerData.savePlayer(this,player);
        super.onDestroy();
    }
}

class
customTouch implements View.OnTouchListener {

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        Log.d("Inventory","Touch registered");
        if (v instanceof IconView){
            Log.d("Inventory","view instance of icon View");
            Weapon w = ((IconView) v).getWeapon();
            Log.d("Inventory","Got weapon W: "+w.toString());
            EquipmentActivity.statsTable.removeAllViews();
            Log.d("Inventory","Removed previous stats");
            TableRow trTitle = new TableRow(v.getContext());
            Log.d("Inventory","creating new row");
            trTitle.setPadding(5,5,5,5);
            Log.d("Inventory","setting padding");
            TextView tvTitle = new TextView(v.getContext());
            Log.d("Inventory","creating table title");
            trTitle.setGravity(Gravity.CENTER_HORIZONTAL);
            tvTitle.setTextSize(20);
            tvTitle.setTypeface(null,Typeface.BOLD);
            tvTitle.setTextColor(Color.WHITE);
            tvTitle.setPadding(0,15,0,0);
            Log.d("Inventory","set type styles");
            tvTitle.setText(w.getName());
            Log.d("Inventory","got weapon name: "+w.getName());
            trTitle.addView(tvTitle);
            Log.d("Inventory","added to table");
            TableRow trImage = new TableRow(v.getContext());
            Log.d("Inventory","creating image row");
            ImageView iconImage = new ImageView(v.getContext());
            Log.d("Inventory","creating icon image view");
            String bitmapaddr = w.getBitmapAddr();
            FileIO assests = new FileIO(v.getContext());
            InputStream is = assests.readAsset(bitmapaddr);
            Bitmap sprite = BitmapFactory.decodeStream(is);
            iconImage.setImageBitmap(sprite);
            try {
                is.close();
                assests = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.d("Inventory","set bitmap to weapon inventory sprite (via get method)");
            trImage.setGravity(Gravity.CENTER_HORIZONTAL);
            trImage.setPadding(5,5,5,5);
            trImage.addView(iconImage);
            Log.d("Inventory","Iconview added");
            TableRow trStats = new TableRow(v.getContext());
            Log.d("Inventory","created stats row");
            TextView tvStats = new TextView(v.getContext());
            Log.d("Inventory","created stats text view");
            trStats.setGravity(Gravity.CENTER_HORIZONTAL);
            tvStats.setTextColor(Color.WHITE);
            Log.d("Inventory","set style");
            String s = "TYPE: " + w.getGreg() + "\t" + "DAMAGE: " + w.getDmg();
            tvStats.setText(s);
            Log.d("Inventory","Built weapon info string");
            trStats.setPadding(5,5,5,5);
            trStats.addView(tvStats);
            Log.d("Inventory","added stats");
            EquipmentActivity.statsTable.addView(trTitle);
            EquipmentActivity.statsTable.addView(trImage);
            EquipmentActivity.statsTable.addView(trStats);
            Log.d("Inventory","added rows to table");
            if (!((IconView) v).getWeapon().isEquiped() && ((IconView)v).hasSlot())
            {
                ((IconView) v).getWeapon().setEquiped(true);
                ((IconView) v).setImageBitmap(((IconView) v).eq);
                EquipmentActivity.SlotsInUse++;
                return false;
            }
            else if (((IconView) v).getWeapon().isEquiped())
            {
                ((IconView) v).getWeapon().setEquiped(false);
                ((IconView) v).setImageBitmap(((IconView) v).uneq);
                EquipmentActivity.SlotsInUse--;
                return false;
            }
            return false;
        }
        return false;
    }
}

