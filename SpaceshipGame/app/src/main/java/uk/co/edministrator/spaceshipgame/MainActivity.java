package uk.co.edministrator.spaceshipgame;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.io.InputStream;

import uk.co.edministrator.spaceshipgame.Ships.playerships.Escort;
import uk.co.edministrator.spaceshipgame.Ships.playerships.Fighter;
import uk.co.edministrator.spaceshipgame.Ships.playerships.Frigate;
import uk.co.edministrator.spaceshipgame.gameObjects.PlayerData;
import uk.co.edministrator.spaceshipgame.gameObjects.Ship;
import uk.co.edministrator.spaceshipgame.tools.FileIO;
import uk.co.edministrator.spaceshipgame.views.GameView;



public class MainActivity extends Activity {
    GameView gameView;
    private boolean istesting = false;
    private Button ExploreButton;
    private Button invButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set full screen view
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //Hide title
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //Fix to landscape
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        //set constants
        CONSTANTS.setConstants(this);
        Intent intent = getIntent();
        boolean isNewGame = true;
        Ship ship = null;
        if (intent.getExtras() != null &&
                intent.getIntExtra("Origin",0) == 1){
                isNewGame = true;
                ship = PlayerData.loadShip(this);
        }
        else {
            String selectedString = intent.getStringExtra("Ship");
            FileIO fileIO = new FileIO(this);
            InputStream is;
            if (selectedString.equals("1")) {
                is = fileIO.readAsset("Sprites/player/destroyer.png");
                Bitmap sprite = BitmapFactory.decodeStream(is);
                ship = new Fighter(1, 0, 0, 0, sprite, null, this);
            } else if (selectedString.equals("2")) {
                is = fileIO.readAsset("Sprites/player/escort.png");
                Bitmap sprite = BitmapFactory.decodeStream(is);
                ship = new Escort(1, 0, 0, 0, sprite, null, this);
            } else {
                is = fileIO.readAsset("Sprites/player/frigate.png");
                Bitmap sprite = BitmapFactory.decodeStream(is);
                ship = new Frigate(1, 0, 0, 0, sprite, null, this);
            }
        }
        istesting = intent.getBooleanExtra("Testing", false);

        //Instantiate game
        FrameLayout game = new FrameLayout(this);
        gameView = new GameView(this, ship, istesting, isNewGame);
        LinearLayout widgets = new LinearLayout(this);

        invButton = new Button(this);
        invButton.setText("Inventory");
        invButton.setTextColor(Color.WHITE);
        invButton.setBackgroundResource(R.drawable.slot);
        invButton.setLayoutParams(new LinearLayout.LayoutParams(275,125));
        invButton.setY(CONSTANTS.getScreenHeight()-135);
        invButton.setX(CONSTANTS.getScreenWidth()-560);

        invButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                invButton.setBackgroundResource(R.drawable.slot_eq );
                gameView.Inventory();
            }
        });

        ExploreButton = new Button(this);
        ExploreButton.setText("Explore");
        ExploreButton.setTextColor(Color.WHITE);
        ExploreButton.setBackgroundResource(R.drawable.slot);
        ExploreButton.setLayoutParams(new LinearLayout.LayoutParams(275,125));
        ExploreButton.setY(CONSTANTS.getScreenHeight()-135);

        ExploreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExploreButton.setBackgroundResource(R.drawable.slot_eq );
                gameView.Explore();
            }
        });
        widgets.addView(ExploreButton);
        widgets.addView(invButton);

        game.addView(gameView);
        game.addView(widgets);

        setContentView(game);
}

    @Override
    protected void onResume() {
        ExploreButton.setBackgroundResource(R.drawable.slot);
        invButton.setBackgroundResource(R.drawable.slot);
        gameView.openedInv = true;
        super.onResume();
    }


    @Override
    public void onBackPressed() {
        //Disables back button
        Intent intent = new Intent(this, ShipSelectActivity.class);
        intent.setAction("android.intent.action.MAIN");
        this.startActivity(intent);
    }

}