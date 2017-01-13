package com.amalisie.znakidrogowe;

import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
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

public class MemoryGameActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = MemoryGameActivity.class.getName();
    private MemoryGame memoryGame;
    private TextView textView;
    private ArrayList<Button> tiles;
    private RelativeLayout mainLayout;
    private GridLayout gridLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mainLayout = (RelativeLayout) findViewById(R.id.activity_memory_game);
        setContentView(R.layout.activity_memory_game);

        this.textView = (TextView) findViewById(R.id.text_view);
        this.gridLayout = (GridLayout) findViewById(R.id.grid_layout);

        this.memoryGame = new MemoryGame(this);
        createTiles();
        addTilesToGrid();

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
            nameTile.roadSign = imgTile.roadSign = roadSign;
            //nameTile.setText(roadSign.name);
            //imgTile.setBackground(roadSign.img);
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
        if (v instanceof NameTile) {
            ((NameTile) v).uncover();
        }

        if (v instanceof ImgTile) {
            ((ImgTile) v).uncover();
        }
        textView.setText(((NameTile) v).roadSign.desc);
    }
}
