package com.amalisie.znakidrogowe;

import android.content.Context;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * Game class for Memory and Arcade games.
 * Implements serializable.
 * @author Amadeusz Lisiecki
 */
public class Game implements Serializable {

    private static final String TAG = Game.class.getName();

    public ArrayList<RoadSign> roadSigns;
    public HashMap<String, RoadSign> pickedSigns;
    public HashMap<String, String> groupDesc;
    public String opisy;

    public int level;

    private Context context;

    /**
     * Context getter.
     * @return context
     */
    public Context getContext() {
        return this.context;
    }

    /**
     * Context setter.
     * @param context
     */
    public void setContext(Context context) {
        this.context = context;
    }

    /**
     * Constructor.
     * - opens road signs descriptions file
     * - sets game level
     * - parses road signs
     * - picks road signs by random
     * - sets roads signs groups descriptions
     * @param context
     */
    public Game(Context context) {
        this.context = context;
        this.opisy = Utils.openTextFileFromAssets(context, "opisy.txt");

        setLevel();

        parseRoadSigns();
        randomize();
        initGroupDesc();
    }

    /**
     * Sets road signs groups descriptions.
     */
    private void initGroupDesc() {
        groupDesc = new HashMap<>();
        groupDesc.put("A", "Znaki ostrzegawcze");
        groupDesc.put("B", "Znaki zakazu");
        groupDesc.put("C", "Znaki zakazu");
        groupDesc.put("D", "Znaki informacyjne");
        groupDesc.put("E", "Znaki kierunku i miejscowości");
        groupDesc.put("F", "Znaki uzupełniające");
    }

    /**
     * Picks given number of road signs by random.
     */
    public void randomize() {
        pickSigns(getNumberOfSigns());
    }

    /**
     * Gives number of road signs for given level and game type.
     * @return numberOfSigns
     */
    private int getNumberOfSigns() {
        if (context instanceof MemoryGameActivity)
            return level * 5;
        else if (context instanceof ArcadeGameActivity)
            return level * 10;
        else
            return 0;
    }

    /**
     * Gets game level from options.txt.
     */
    private void setLevel() {
        File optionsFile = new File(context.getFilesDir() + "/options.txt");
        String options;
        if (optionsFile.isFile())
            options = Utils.openTextFile(context, "options.txt");
        else
            options = Utils.openTextFileFromAssets(context, "options.txt");
        level = Integer.parseInt(options.split("=")[1].trim());
    }

    /**
     * Picks given number of road signs by random.
     * @param tilesNo
     */
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

    /**
     * Parse road signs from description file.
     */
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

    /**
     * Prepares for serializations.
     * Removes non-serializable image objects - Drawables.
     */
    public void prepareForSerialization() {
        this.context = null;
        for (RoadSign roadSign : this.roadSigns) {
            roadSign.clearImg();
        }

        for (RoadSign roadSign : this.pickedSigns.values()) {
            roadSign.clearImg();
        }
    }

    /**
     * Restores game object after serialization.
     * Restores non-serializable image objects - Drawables.
     * @param context
     */
    public void restoreObject(Context context) {
        this.context = context;
        for (RoadSign roadSign : this.roadSigns) {
            roadSign.setImg(this.context);
        }
        randomize();
    }

}
