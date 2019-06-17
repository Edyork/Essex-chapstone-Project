package uk.co.edministrator.spaceshipgame;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.InputStream;

import uk.co.edministrator.spaceshipgame.Ships.ShipClass;
import uk.co.edministrator.spaceshipgame.gameObjects.Player;
import uk.co.edministrator.spaceshipgame.tools.FileIO;
import uk.co.edministrator.spaceshipgame.tools.Music;

public class ShipSelectActivity extends AppCompatActivity {
    public static int SelectedShip;
    private Context context;
    public static Music music;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        //Hide title
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_ship_select);
        context = this;
        final TextView top = findViewById(R.id.textView2);
        final ImageButton fighterBut = findViewById(R.id.FighterImgButton);
        final ImageButton escortBut = findViewById(R.id.EscortImgButton);
        final ImageButton frigateBut = findViewById(R.id.FrigateImgButton);
        Button start = findViewById(R.id.StartButton);
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setBackground(context.getDrawable(R.drawable.select));
                if (v.getId() == R.id.FighterImgButton){
                    SelectedShip = 1;
                    escortBut.setBackground(context.getDrawable(R.drawable.unselect));
                    frigateBut.setBackground(context.getDrawable(R.drawable.unselect));
                    top.setText("Fighter\n Shield: 100\t Weapon Slots: 7 \tHealth: 300");

                }
                else if (v.getId() == R.id.EscortImgButton) {
                    SelectedShip = 2;
                    frigateBut.setBackground(context.getDrawable(R.drawable.unselect));
                    fighterBut.setBackground(context.getDrawable(R.drawable.unselect));
                    top.setText("Escort\n Evasion: 25% \t Weapon Slots: 6\t Health: 400");
                }
                else {
                    SelectedShip = 3;
                    escortBut.setBackground(context.getDrawable(R.drawable.unselect));
                    fighterBut.setBackground(context.getDrawable(R.drawable.unselect));
                    top.setText("Frigate\n Armour: 20% \t Weapon Slots: 6\t Health: 350");
                }
            }
        };

        fighterBut.setOnClickListener(listener);
        escortBut.setOnClickListener(listener);
        frigateBut.setOnClickListener(listener);

//        SharedPreferences settings = getSharedPreferences("Player", MODE_PRIVATE);
//        SharedPreferences.Editor editor = settings.edit();
//        editor.remove("Player");
//        editor.apply();

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Selected ship is type: " + SelectedShip);
                Intent intent = new Intent(context, MainActivity.class);
                intent.putExtra("Ship", SelectedShip + "");
                intent.putExtra("Testing", false);
                context.startActivity(intent);
            }
        });

        //music

            music = new Music(this);
            music.play();

        //testing
        if (getIntent() != null) {
            if (getIntent().getAction() != null) {
                if (getIntent().getAction().equals("com.google.intent.action.TEST_LOOP")) {
                    System.out.println("Selected ship is type: " + SelectedShip);
                    Intent intent = new Intent(context, MainActivity.class);
                    intent.putExtra("Ship", SelectedShip + "");
                    intent.putExtra("Testing", true);
                    context.startActivity(intent);
                }
            }
        }
    }
}
