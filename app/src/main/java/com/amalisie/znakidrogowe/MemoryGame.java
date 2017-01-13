package com.amalisie.znakidrogowe;

import android.content.Context;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class MemoryGame {

    private static final String TAG = MemoryGame.class.getName();
    public static final int SIGNS_NO = 10;
    public ArrayList<RoadSign> roadSigns;
    public HashMap<String, RoadSign> pickedSigns;
    public String opisy;
    private Context context;
    public int level;

    private InputStream is;

    public MemoryGame(Context context) {
        this.context = context;
        this.opisy = Utils.openTextFile(context, "opisy.txt");

        setLevel();

        parseRoadSigns();
        pickSigns(getNumberOfSigns());

    }

    private int getNumberOfSigns() {
        return level * 5;
    }

    private void setLevel() {
        level = 2;
    }

    private void pickSigns(int tilesNo){
        Random random = new Random();
        pickedSigns = new HashMap<>();
        for (int i = 0; i < tilesNo; i++) {
            int index = random.nextInt(roadSigns.size());
            RoadSign roadSign = roadSigns.get(index);
            if (!pickedSigns.containsKey(roadSign.name))
                pickedSigns.put(roadSign.name, roadSign);
            else i--;
        }
    }

    private void parseRoadSigns() {
        roadSigns = new ArrayList<>();
        for (String line : opisy.split(System.lineSeparator())) {
            line = line.trim();
            RoadSign roadSign = new RoadSign();
            roadSign.name = line.substring(0, line.indexOf(" "));
            roadSign.desc = line.substring(line.indexOf(" ") + 1);
            String img = roadSign.name.split("-")[0].toLowerCase()
                    + roadSign.name.split("-")[1].toLowerCase()
                    + ".png";
            roadSign.img = ((MemoryGameActivity) context).getImageFromAssets(img);

            roadSigns.add(roadSign);
        }
    }
}
