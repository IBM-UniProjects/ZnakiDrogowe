package com.amalisie.znakidrogowe;

import android.content.Context;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Game implements Serializable {

    private static final String TAG = Game.class.getName();
    public static final int SIGNS_NO = 10;
    public ArrayList<RoadSign> roadSigns;
    public HashMap<String, RoadSign> pickedSigns;
    public HashMap<String, String> groupDesc;
    public String opisy;

    public int level;

    private Context context;

    public Context getContext() {
        return this.context;
    }
    public void setContext(Context context) {
        this.context = context;
    }

    public Game(Context context) {
        this.context = context;
        this.opisy = Utils.openTextFileFromAssets(context, "opisy.txt");

        setLevel();

        parseRoadSigns();
        randomize();
        initGroupDesc();
    }

    private void initGroupDesc() {
        groupDesc = new HashMap<>();
        groupDesc.put("A", "Znaki ostrzegawcze");
        groupDesc.put("B", "Znaki zakazu");
        groupDesc.put("C", "Znaki zakazu");
        groupDesc.put("D", "Znaki informacyjne");
        groupDesc.put("E", "Znaki kierunku i miejscowości");
        groupDesc.put("F", "Znaki uzupełniające");
    }

    public void randomize() {
        pickSigns(getNumberOfSigns());
    }

    private int getNumberOfSigns() {
        if (context instanceof MemoryGameActivity)
            return level * 5;
        else if (context instanceof ArcadeGameActivity)
            return level * 10;
        else
            return 0;
    }

    private void setLevel() {
        File optionsFile = new File(context.getFilesDir() + "/options.txt");
        String options;
        if (optionsFile.isFile())
            options = Utils.openTextFile(context, "options.txt");
        else
            options = Utils.openTextFileFromAssets(context, "options.txt");
        level = Integer.parseInt(options.split("=")[1].trim());
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
            roadSign.setImg(context);
            roadSign.group = roadSign.name.split("-")[0];

            roadSigns.add(roadSign);
        }
    }

    public void prepareForSerialization() {
        this.context = null;
        for (RoadSign roadSign : this.roadSigns) {
            roadSign.clearImg();
        }

        for (RoadSign roadSign : this.pickedSigns.values()) {
            roadSign.clearImg();
        }
    }

    public void restoreObject(Context context) {
        this.context = context;
        for (RoadSign roadSign : this.roadSigns) {
            roadSign.setImg(this.context);
        }
        randomize();
    }

}
