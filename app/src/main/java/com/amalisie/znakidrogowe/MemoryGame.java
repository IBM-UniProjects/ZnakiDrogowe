package com.amalisie.znakidrogowe;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class MemoryGame {

    private static final String TAG = MemoryGame.class.getName();
    private static final int TILES_NO = 9;
    public ArrayList<ArrayList<String>> roadSigns;
    public ArrayList<ArrayList<String>> tiles;
    public String opisy;

    private InputStream is;

    public MemoryGame(String opisy) {
        this.opisy = opisy;
        parseRoadSigns();
        pickTiles(TILES_NO);
    }

    private void pickTiles(int tilesNo){
        Random random = new Random();
        tiles = new ArrayList<>(tilesNo);
        for (int i = 0; i < tilesNo; i++) {
            int index = random.nextInt(roadSigns.size());
            if (!tiles.contains(roadSigns.get(index)))
                tiles.add(roadSigns.get(index));
            else i--;
        }
    }

    private void parseRoadSigns() {
        roadSigns = new ArrayList<>();
        for (String line : opisy.split(System.lineSeparator())) {
            String name = line.substring(0, line.indexOf(" "));
            String desc = line.substring(line.indexOf(" ") + 1);
            String img = name.split("-")[0].toLowerCase() + name.split("-")[1].toLowerCase();
            roadSigns.add(new ArrayList<>(Arrays.asList(name, desc, img)));
        }
    }
}
