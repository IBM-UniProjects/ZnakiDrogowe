package com.amalisie.znakidrogowe;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;

/**
 * Utils class for methods of multiple usage.
 * @author Amadeusz Lisiecki
 */
public abstract class Utils {

    public static final String ELAPSED_TIME = "elapsedTime";
    public static final String GAME = "game";
    public static final String TOTAL_SCORE = "totalScore";

    private static final String TAG = Utils.class.getName();

    /**
     * Opens text file from assets and returns it as String.
     * @param context
     * @param file
     * @return fileToString
     */
    public static String openTextFileFromAssets(Context context, String file) {
        StringBuilder sb = new StringBuilder();
        try(BufferedReader br = new BufferedReader(new InputStreamReader(context.getAssets().open(file)))) {
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }

        } catch (Exception e) {
            Log.e(TAG, "openTextFileFromAssets: ", e);
        }
        return sb.toString();
    }

    /**
     * Opens text file from internal storage and returns it as String.
     * @param context
     * @param file
     * @return fileToString
     */
    public static String openTextFile(Context context, String file) {
        StringBuilder sb = new StringBuilder();
        try(BufferedReader br = new BufferedReader(new InputStreamReader(context.openFileInput(file)))) {
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

    /**
     * Saves file to internal storage.
     * @param context
     * @param file
     * @param text
     */
    public static void saveTextFile(Context context, String file, String text) {
        try(OutputStreamWriter outputWriter = new OutputStreamWriter(context.openFileOutput(file, Context.MODE_PRIVATE))) {
            outputWriter.write(text);
        } catch (Exception e) {
            Log.e(TAG, "saveTextFile: ", e);
        }
    }

    /**
     * Gets Drawable image from assets.
     * @param context
     * @param img
     * @return image
     */
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
