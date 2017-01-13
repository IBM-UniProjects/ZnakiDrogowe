package com.amalisie.znakidrogowe;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MemoryGameActivity extends AppCompatActivity implements View.OnClickListener, Runnable {

    private static final String TAG = MemoryGameActivity.class.getName();

    private RelativeLayout rootLayout;
    private MemoryGame memoryGame;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory_game);

        this.rootLayout = (RelativeLayout) findViewById(R.id.activity_memory_game);
        this.textView = (TextView) findViewById(R.id.text_view);
        this.imageView = (ImageView) findViewById(R.id.image_view);
        this.gridLayout = (GridLayout) findViewById(R.id.grid_layout);
        this.textView3 = (TextView) findViewById(R.id.text_view3);
        this.startButton = (Button) findViewById(R.id.start_button);
        this.startButton.setOnClickListener(this);
        this.helpTextView = (TextView) findViewById(R.id.help_text_view);

        this.memoryGame = new MemoryGame(this);
        this.tilesLeft = memoryGame.pickedSigns.size();

        createTiles();
        addTilesToGrid();
        this.lastTile = null;
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
        for (RoadSign roadSign : memoryGame.pickedSigns.values()) {
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

    public Drawable getImageFromAssets(String img) {
        try(InputStream is = getAssets().open(img)) {
            Drawable d = Drawable.createFromStream(is, img);
            return d;
        } catch (IOException e) {
            Log.e(TAG, "getImageFromAssets: ", e);
        }
        return null;
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
            ((ViewGroup) v.getParent()).removeView(helpTextView);
            ((ViewGroup) v.getParent()).removeView(startButton);
            startTimer();
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
}
