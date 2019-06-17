package uk.co.edministrator.spaceshipgame;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import uk.co.edministrator.spaceshipgame.gameObjects.Player;
import uk.co.edministrator.spaceshipgame.gameObjects.PlayerData;
import uk.co.edministrator.spaceshipgame.map.Planet;
import uk.co.edministrator.spaceshipgame.map.SolarSystem;
import uk.co.edministrator.spaceshipgame.tools.FileIO;
import uk.co.edministrator.spaceshipgame.views.SolarSystemView;


public class SolarSystemActivity extends Activity {
    SolarSystemView solarSystemView;
    private Button ExploreButton;
    private Button invButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("SolarSystemActivity", "activity made");
        // Set full screen view
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //Hide title
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Fix to landscape
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        //set constants
        CONSTANTS.setConstants(this);
        //Instantiate game
        Intent intent = getIntent();
        SolarSystem ss = PlayerData.loadSolarSystem(this);
       // SolarSystem ss = new SolarSystem(new FileIO(this), 0);
        for (Planet p : ss.getMap()){
            p.createBitmap(this);
        }
//        SolarSystem ss = new SolarSystem(new FileIO(this), 0);
        boolean istesting = intent.getBooleanExtra("testing", false);

        //Instantiate game
        FrameLayout game = new FrameLayout(this);
        solarSystemView = new SolarSystemView(this, ss.getMap(), ss.getSun(),  istesting);
        LinearLayout widgets = new LinearLayout(this);

        invButton = new Button(this);
        invButton.setText("Inventory");
        invButton.setTextColor(Color.WHITE);
        invButton.setBackgroundResource(R.drawable.slot);
        invButton.setLayoutParams(new LinearLayout.LayoutParams(250,150));
        invButton.setY(CONSTANTS.getScreenHeight()-160);
        invButton.setX(CONSTANTS.getScreenWidth()-520);

        invButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                invButton.setBackgroundResource(R.drawable.slot_eq );
                solarSystemView.Inventory();
            }
        });

        ExploreButton = new Button(this);
        ExploreButton.setText("Explore");
        ExploreButton.setTextColor(Color.WHITE);
        ExploreButton.setBackgroundResource(R.drawable.slot);
        ExploreButton.setLayoutParams(new LinearLayout.LayoutParams(250,150));
        ExploreButton.setY(CONSTANTS.getScreenHeight()-160);

        ExploreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExploreButton.setBackgroundResource(R.drawable.slot_eq );
                solarSystemView.Explore();
            }
        });
        widgets.addView(ExploreButton);
        widgets.addView(invButton);

        game.addView(solarSystemView);
        game.addView(widgets);

        setContentView(game);
    }

    @Override
    protected void onResume() {
        ExploreButton.setBackgroundResource(R.drawable.slot);
        invButton.setBackgroundResource(R.drawable.slot);
        solarSystemView.openedInv = true;
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
