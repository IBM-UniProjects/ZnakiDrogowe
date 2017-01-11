package com.amalisie.znakidrogowe;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MemoryGameActivity extends AppCompatActivity {

    private static final String TAG = MemoryGameActivity.class.getName();
    private MemoryGame memoryGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory_game);

        TextView textView = (TextView) findViewById(R.id.text_view);

        String opisy = openTextFile("opisy.txt");
        this.memoryGame = new MemoryGame(opisy);

        textView.setText(opisy);
    }

    protected String openTextFile(String file) {
        StringBuilder sb = new StringBuilder();
        try(BufferedReader br = new BufferedReader(new InputStreamReader(getAssets().open(file)))) {
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
