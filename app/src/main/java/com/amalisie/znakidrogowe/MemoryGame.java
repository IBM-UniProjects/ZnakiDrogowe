package com.amalisie.znakidrogowe;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

public class MemoryGame {

    private static final String TAG = MemoryGame.class.getName();
    public static final int TILES_NO = 10;
    public ArrayList<RoadSign> roadSigns;
    public HashMap<String, RoadSign> pickedSigns;
    public String opisy;
    private Context context;

    private InputStream is;

    public MemoryGame(Context context) {
        this.context = context;
        this.opisy = openTextFile("opisy.txt");
        parseRoadSigns();
        pickSigns(TILES_NO);

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
            // workaround :( nie wiem czemu pierwszy wiersz jest inny
            if (roadSign.name.matches(".A-1")) {
                roadSign.name = "A-1";
                roadSign.img = ((MemoryGameActivity) context).getImageFromAssets("a1.png");
            } else {
                roadSign.img = ((MemoryGameActivity) context).getImageFromAssets(img);
            }
            roadSigns.add(roadSign);
        }
    }

    private String openTextFile(String file) {
        StringBuilder sb = new StringBuilder();
        try(BufferedReader br = new BufferedReader(new InputStreamReader(context.getAssets().open(file)))) {
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }

        } catch (Exception e) {
            Log.e(TAG, "openTextFile: ", e);
        }
        return sb.toString();
    }
}
