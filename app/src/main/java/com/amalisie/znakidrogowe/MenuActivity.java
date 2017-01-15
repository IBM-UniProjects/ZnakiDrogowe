package com.amalisie.znakidrogowe;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
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

/**
 * Activity for Menu.
 * Implements View.OnClickListener.
 * @author Amadeusz Lisiecki
 */
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
    private TableRow tableRow;
    private TextView dataColumn;
    private TextView timeColumn;
    private TextView scoreColumn;
    private TextView levelColumn;
    private TextView creator;

    private int level;

    /**
     * Overrides onCreate.
     * - sets content view
     * - initializes UI components (views)
     * - sets options
     * @param savedInstanceState
     */
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
        this.tableRow = (TableRow) findViewById(R.id.tableRow);
        this.dataColumn = (TextView) findViewById(R.id.dataColumn);
        this.timeColumn = (TextView) findViewById(R.id.timeColumn);
        this.scoreColumn = (TextView) findViewById(R.id.scoreColumn);
        this.levelColumn = (TextView) findViewById(R.id.levelColumn);
        this.creator = (TextView) findViewById(R.id.creator);

        setOptions();
    }

    /**
     * Sets all UI components invisible.
     */
    private void setAllInvisible() {
        for (int i = 0; i < rootLayout.getChildCount(); i++) {
            rootLayout.getChildAt(i).setVisibility(View.INVISIBLE);
        }
        creator.setVisibility(View.VISIBLE);
    }

    /**
     * Overrides onClick method for View.OnClickListener.
     * - handles radio buttons for level selection
     * - handles new game button
     * - handles options button
     * - handles score board button
     * - handles exit button
     * - handles back button
     * @param v
     */
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

    /**
     * Starts new game.
     * Moves to MemoryGameActivity.
     */
    protected void onNewGame() {
        Intent intent = getBaseContext().getPackageManager()
                .getLaunchIntentForPackage( getBaseContext().getPackageName() );
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();
        startActivity(intent);
    }

    /**
     * Shows score board.
     * Gets scores from score.txt file.
     */
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
        table.setVisibility(View.VISIBLE);
    }

    /**
     * Shows options.
     */
    protected void onOptions() {
        setAllInvisible();
        back.setVisibility(View.VISIBLE);
        radioGroup.setVisibility(View.VISIBLE);
    }

    /**
     * Shows main menu options.
     */
    protected void onBack() {
        saveOptions();
        setAllInvisible();
        newGame.setVisibility(View.VISIBLE);
        scoreBoard.setVisibility(View.VISIBLE);
        options.setVisibility(View.VISIBLE);
        exit.setVisibility(View.VISIBLE);
    }

    /**
     * Saves options to options.txt file.
     */
    protected void saveOptions() {
        String options = "level=" + level;
        Utils.saveTextFile(this, "options.txt", options);
    }

    /**
     * Gets options from options.txt file.
     * Sets game level.
     */
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

    /**
     * Converts dp to px.
     * @param dps
     * @return pixels
     */
    private int getPixels(int dps) {
        final float scale = getResources().getDisplayMetrics().density;
        int pixels = (int) (dps * scale + 0.5f);
        return pixels;
    }

    /**
     * Adds row to score board table.
     * @param date
     * @param time
     * @param score
     * @param level
     */
    private void addRow(String date, String time, String score, String level) {
        TableRow row = new TableRow(this);
        table.addView(row, tableRow.getLayoutParams());
        TextView dateTextView = new TextView(this);
        TextView timeTextView = new TextView(this);
        TextView scoreTextView = new TextView(this);
        TextView levelTextView = new TextView(this);
        row.addView(dateTextView, dataColumn.getLayoutParams());
        row.addView(timeTextView, timeColumn.getLayoutParams());
        row.addView(scoreTextView, scoreColumn.getLayoutParams());
        row.addView(levelTextView, levelColumn.getLayoutParams());
        dateTextView.setText(date);
        timeTextView.setText(time);
        scoreTextView.setText(score);
        levelTextView.setText(level);
    }

    /**
     * Overrides onBackPressed method.
     * Exits application.
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}
