package com.amalisie.znakidrogowe;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public abstract class Utils {

    public static final String ELAPSED_TIME = "elapsedTime";
    public static final String GAME = "game";

    private static final String TAG = Utils.class.getName();

    public static String openTextFile(Context context, String file) {
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

    public static Drawable getImageFromAssets(Context context, String img) {
        try(InputStream is = context.getAssets().open(img)) {
            Drawable d = Drawable.createFromStream(is, img);
            return d;
        } catch (IOException e) {
            Log.e(TAG, "getImageFromAssets: ", e);
        }
        return null;
    }
}
