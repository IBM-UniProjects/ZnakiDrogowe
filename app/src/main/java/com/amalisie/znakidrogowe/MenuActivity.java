package com.amalisie.znakidrogowe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.io.File;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = MenuActivity.class.getName();

    private RelativeLayout rootLayout;
    private Button newGame;
    private Button scoreBoard;
    private Button options;
    private Button exit;
    private Button back;
    private RadioGroup radioGroup;
    private RadioButton level1;
    private RadioButton level2;
    private RadioButton level3;
    private TextView scoreView;
    private TableLayout table;
    private TextView creator;

    private int level;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        rootLayout = (RelativeLayout) findViewById(R.id.activity_menu);
        setTitle("Znaki Drogowe - MENU");

        this.newGame = (Button) findViewById(R.id.new_game);
        this.newGame.setOnClickListener(this);
        this.scoreBoard = (Button) findViewById(R.id.score_board);
        this.scoreBoard.setOnClickListener(this);
        this.options = (Button) findViewById(R.id.options);
        this.options.setOnClickListener(this);
        this.exit = (Button) findViewById(R.id.exit);
        this.exit.setOnClickListener(this);
        this.back = (Button) findViewById(R.id.back);
        this.back.setOnClickListener(this);
        this.radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        this.level1 = (RadioButton) findViewById(R.id.level1);
        this.level1.setOnClickListener(this);
        this.level2 = (RadioButton) findViewById(R.id.level2);
        this.level2.setOnClickListener(this);
        this.level3 = (RadioButton) findViewById(R.id.level3);
        this.level3.setOnClickListener(this);
        this.scoreView = (TextView) findViewById(R.id.score_view);
        this.table = (TableLayout) findViewById(R.id.table);
        this.creator = (TextView) findViewById(R.id.creator);

        setOptions();
        for (String file : fileList()) {
            System.out.println(file);
        }
    }

    private void setAllInvisible() {
        for (int i = 0; i < rootLayout.getChildCount(); i++) {
            rootLayout.getChildAt(i).setVisibility(View.INVISIBLE);
        }
        creator.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        if (v instanceof RadioButton) {
            RadioButton rb = (RadioButton) v;
            if (rb == this.level1) {
                this.level = 1;
                return;
            }
            if (rb == this.level2) {
                this.level = 2;
                return;
            }
            if (rb == this.level3) {
                this.level = 3;
                return;
            }
        }

        Button b = (Button) v;

        if (b == this.newGame) {
            onNewGame();
            return;
        }
        if (b == this.exit) {
            onBackPressed();
            return;
        }
        if (b == this.scoreBoard) {
            onScoreBoard();
            return;
        }
        if (b == this.options) {
            onOptions();
            return;
        }
        if (b == this.back) {
            onBack();
        }
    }

    protected void onNewGame() {
        Intent intent = getBaseContext().getPackageManager()
                .getLaunchIntentForPackage( getBaseContext().getPackageName() );
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();
        startActivity(intent);
    }

    protected void onScoreBoard() {
        setAllInvisible();
        back.setVisibility(View.VISIBLE);
        String scoreFile = Utils.openTextFile(this, "score.txt");
        scoreView.setText("WYNIKI");
        scoreView.setVisibility(View.VISIBLE);
        for (String line : scoreFile.split(System.lineSeparator())) {
            String[] splittedLine = line.split(" ");
            addRow(splittedLine[0].trim()
                    , splittedLine[1].trim()
                    , splittedLine[2].trim()
                    , splittedLine[3].trim());
        }
    }

    protected void onOptions() {
        setAllInvisible();
        back.setVisibility(View.VISIBLE);
        radioGroup.setVisibility(View.VISIBLE);
    }

    protected void onBack() {
        saveOptions();
        setAllInvisible();
        newGame.setVisibility(View.VISIBLE);
        scoreBoard.setVisibility(View.VISIBLE);
        options.setVisibility(View.VISIBLE);
        exit.setVisibility(View.VISIBLE);
    }

    protected void saveOptions() {
        String options = "level=" + level;
        Utils.saveTextFile(this, "options.txt", options);
    }

    protected void setOptions() {
        File optionsFile = new File(getFilesDir() + "/options.txt");
        String options;
        if (optionsFile.isFile())
            options = Utils.openTextFile(this, "options.txt");
        else
            options = Utils.openTextFileFromAssets(this, "options.txt");
        level = Integer.parseInt(options.split("=")[1].trim());

        switch (level) {
            case 1:
                level1.setChecked(true);
                break;
            case 2:
                level2.setChecked(true);
                break;
            case 3:
                level3.setChecked(true);
                break;
        }
    }

    private int getPixels(int dps) {
        final float scale = getResources().getDisplayMetrics().density;
        int pixels = (int) (dps * scale + 0.5f);
        return pixels;
    }

    private void addRow(String date, String time, String score, String level) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}
