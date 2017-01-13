package com.amalisie.znakidrogowe;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public abstract class Utils {

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
}
