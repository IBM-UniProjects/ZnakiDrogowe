package com.amalisie.znakidrogowe;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class MemoryGameActivity extends AppCompatActivity implements View.OnClickListener, Runnable {

    private static final String TAG = MemoryGameActivity.class.getName();

    private RelativeLayout rootLayout;
    private Game game;
    private TextView textView;
    private TextView textView3;
    private Button startButton;
    private TextView helpTextView;
    private ImageView imageView;
    private ArrayList<Button> tiles;
    private GridLayout gridLayout;
    private Tile lastTile;
    private long startTime;
    private long elapsedTime;
    private Handler handler;
    private boolean stopTimer;
    private int tilesLeft;
    private boolean gameEnded;
    private int level;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory_game);
        setTitle(getTitle() + " - POMOC");

        this.rootLayout = (RelativeLayout) findViewById(R.id.activity_memory_game);
        this.textView = (TextView) findViewById(R.id.text_view);
        this.imageView = (ImageView) findViewById(R.id.image_view);
        this.gridLayout = (GridLayout) findViewById(R.id.grid_layout);
        this.textView3 = (TextView) findViewById(R.id.text_view3);
        this.startButton = (Button) findViewById(R.id.start_button);
        this.startButton.setOnClickListener(this);
        this.helpTextView = (TextView) findViewById(R.id.help_text_view);
        this.helpTextView.setText(Utils.openTextFileFromAssets(this, "pomoc1.txt"));

        this.game = new Game(this);
        this.level = game.level;
        this.tilesLeft = game.pickedSigns.size();

        createTiles();
        addTilesToGrid();
        this.lastTile = null;
        this.gameEnded = false;
    }

    @Override
    protected void onPause() {
        stopTimer();
        super.onPause();
    }

    protected void startTimer() {
        this.handler = new Handler();
        this.stopTimer = false;
        this.startTime = System.nanoTime();
        this.elapsedTime = System.nanoTime() - startTime;
        handler.post(this);
    }

    protected void stopTimer() {
        stopTimer = true;
    }

    protected void checkGameStatus() {
        if (tilesLeft == 0) {
            stopTimer();
            gameEnded = true;

            helpTextView.setTextSize(30);
            long elapsedTimeMs = elapsedTime / 1000000;
            helpTextView.setText("BRAWO!!!\nGra Memory: "
                    + elapsedTimeMs
                    + "ms\n"
                    + "\nPunktacja - Poziom "
                    + level
                    + ":\n"
                    + (elapsedTimeMs / level)
                    + "pkt");
            startButton.setText("DALEJ");

            rootLayout.addView(helpTextView);
            rootLayout.addView(startButton);
        }
    }

    protected void addTilesToGrid() {
        Random random = new Random();
        for (int i = 0; i < tiles.size(); i++) {
            int index = random.nextInt(tiles.size());
            Button tile = tiles.get(index);
            if (tile.getParent() == null) {
                gridLayout.addView(tile);
            } else i--;
        }
    }

    protected void createTiles() {
        tiles = new ArrayList<>();
        for (RoadSign roadSign : game.pickedSigns.values()) {
            NameTile nameTile = new NameTile(this);
            ImgTile imgTile = new ImgTile(this);
            nameTile.setRoadSign(roadSign);
            imgTile.setRoadSign(roadSign);
            nameTile.setBackgroundResource(android.R.drawable.btn_default);
            imgTile.setBackgroundResource(android.R.drawable.btn_default);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(180, 180);
            nameTile.setLayoutParams(params);
            imgTile.setLayoutParams(params);
            nameTile.setOnClickListener(this);
            imgTile.setOnClickListener(this);
            tiles.add(nameTile);
            tiles.add(imgTile);
        }
    }

    protected void moveToArcadeGameActivity() {
        Intent intent = new Intent(this, ArcadeGameActivity.class);
        intent.putExtra(Utils.ELAPSED_TIME, elapsedTime);
        game.prepareForSerialization();
        intent.putExtra(Utils.GAME, game);
        startActivity(intent);
    }

    protected void moveToMenuActivityByBackPress() {
        Intent intent = new Intent(this, MenuActivity.class);
        stopTimer();
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        if (v instanceof Tile) {
            Tile tile = (Tile) v;
            imageView.setImageDrawable(tile.getRoadSign().img);
            textView.setText(tile.getRoadSign().name + "\n" + tile.getRoadSign().desc);

            if (lastTile == null) {
                lastTile = tile;
                tile.uncover();
                return;
            }

            if (!tile.isCovered())
                return;
            if (tile == lastTile)
                return;

            tile.uncover();

            if (tile.getRoadSign() != lastTile.getRoadSign()) {
                if (!lastTile.isPaired())
                    lastTile.cover();
            }
            else {
                tile.setPaired();
                lastTile.setPaired();
                tilesLeft--;
                checkGameStatus();
            }
            lastTile = tile;
            return;
        }

        if (v == startButton) {
            if (!gameEnded) {
                ((ViewGroup) v.getParent()).removeView(helpTextView);
                ((ViewGroup) v.getParent()).removeView(startButton);
                setTitle("Gra Memory - Poziom " + level);
                startTimer();
            } else {
                // go to next activity
                moveToArcadeGameActivity();
            }
        }
    }

    @Override
    public void run() {
        if (stopTimer) {
            handler.removeCallbacks(this);
            return;
        }
        elapsedTime = System.nanoTime() - startTime;
        textView3.setText(String.valueOf(elapsedTime / 1000000));
        handler.postDelayed(this, 70);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        moveToMenuActivityByBackPress();
    }
}
