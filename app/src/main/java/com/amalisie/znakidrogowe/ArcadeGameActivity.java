package com.amalisie.znakidrogowe;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class ArcadeGameActivity extends AppCompatActivity implements View.OnClickListener, Runnable {

    private static final String TAG = ArcadeGameActivity.class.getName();

    private RelativeLayout rootLayout;
    private Button startButton;
    private TextView helpTextView;
    private TextView timeView;
    private TextView roadSignGroupView;
    private ImageView imageView;

    public ArrayList<FlyingRoadSign> flyingRoadSigns;

    private Game game;
    private long score;
    private long penalty;
    private int level;
    private String selectedSignGroup;
    private int signsLeft;

    private Handler handler;
    private boolean stopTimer;
    private long startTime;
    private long elapsedTime;
    private boolean gameEnded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arcade_game);
        this.rootLayout = (RelativeLayout) findViewById(R.id.activity_arcade_game);
        setTitle("Znaki Drogowe - POMOC");

        this.startButton = (Button) findViewById(R.id.start_button2);
        this.startButton.setOnClickListener(this);
        this.helpTextView = (TextView) findViewById(R.id.help_text_view2);
        this.helpTextView.setText(Utils.openTextFileFromAssets(this, "pomoc2.txt"));
        this.timeView = (TextView) findViewById(R.id.time_view);
        this.roadSignGroupView = (TextView) findViewById(R.id.road_sign_group_view);
        this.imageView = (ImageView) findViewById(R.id.image_view2);

        Bundle b = getIntent().getExtras();
        this.game = (Game) b.get(Utils.GAME);
        this.game.restoreObject(this);
        this.score = b.getLong(Utils.ELAPSED_TIME);
        this.level = game.level;
        this.signsLeft = 0;
        this.penalty = 0;

        selectSignGroup();
        liftOff();

        this.gameEnded = false;
    }

    @Override
    protected void onPause() {
        stopTimer();
        super.onPause();
    }

    private void selectSignGroup() {
        Random random = new Random();
        Object[] roadSigns = game.pickedSigns.values().toArray();
        int index = random.nextInt(roadSigns.length);
        selectedSignGroup = ((RoadSign) roadSigns[index]).group;
        imageView.setImageDrawable(((RoadSign) roadSigns[index]).img);
        String text = selectedSignGroup
                + " - "
                + game.groupDesc.get(selectedSignGroup);
        roadSignGroupView.setText(text);
    }

    private void checkGameStatus() {
        if (signsLeft == 0) {
            stopTimer();
            gameEnded = true;

            for (FlyingRoadSign flyingRoadSign : flyingRoadSigns) {
                flyingRoadSign.setVisibility(View.INVISIBLE);
            }

            helpTextView.setTextSize(30);
            long scoreMs = score / 1000000;
            long elapsedTimeMs = elapsedTime / 1000000;
            helpTextView.setText("BRAWO!!!\nGra Memory: "
                    + scoreMs
                    + "ms\nGra Arcade: "
                    + elapsedTimeMs
                    + "ms\nRazem: "
                    + (scoreMs + elapsedTimeMs)
                    + "ms\n"
                    + "\nPunktacja - Poziom "
                    + level
                    + ":\n"
                    + ((scoreMs + elapsedTimeMs) / level)
                    + "pkt");
            startButton.setText("DALEJ");

            rootLayout.addView(helpTextView);
            rootLayout.addView(startButton);
        }
    }

    protected void moveToMenuActivity() {
        Intent intent = new Intent(this, MenuActivity.class);
        intent.putExtra(Utils.TOTAL_SCORE, score + elapsedTime);
        startActivity(intent);
    }

    protected void moveToMenuActivityByBackPress() {
        Intent intent = new Intent(this, MenuActivity.class);
        stopTimer();
        startActivity(intent);
    }

    protected void liftOff() {
        flyingRoadSigns = new ArrayList<>();
        for (RoadSign roadSign : game.pickedSigns.values()) {
            FlyingRoadSign flyingRoadSign = new FlyingRoadSign(this);
            flyingRoadSign.setRoadSign(roadSign);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(180, 180);
            params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
            flyingRoadSign.setLayoutParams(params);
            flyingRoadSign.setBackground(roadSign.img);
            flyingRoadSign.setVisibility(View.INVISIBLE);
            flyingRoadSign.setOnClickListener(this);
            flyingRoadSigns.add(flyingRoadSign);
            rootLayout.addView(flyingRoadSign);
            if (selectedSignGroup.equals(roadSign.group))
                signsLeft++;
        }
    }

    protected void startTimer() {
        this.handler = new Handler();
        this.stopTimer = false;
        this.startTime = System.nanoTime();
        this.elapsedTime = System.nanoTime() - startTime;
        handler.post(this);
    }

    protected void stopTimer() {
        stopTimer = true;
    }

    @Override
    public void onClick(View v) {

        if (v instanceof FlyingRoadSign) {
            FlyingRoadSign f = (FlyingRoadSign) v;
            f.vanish(selectedSignGroup);
            if (selectedSignGroup.equals(f.getRoadSign().group)) {
                signsLeft--;
                checkGameStatus();
            }
            else {
                penalty = penalty + (5 * (long) (Math.pow(10,9)));
            }
            return;
        }

        if (v == startButton) {
            if (!gameEnded) {
                ((ViewGroup) v.getParent()).removeView(helpTextView);
                ((ViewGroup) v.getParent()).removeView(startButton);
                setTitle("Gra Arcade - Poziom " + level);
                for (FlyingRoadSign flyingRoadSign : flyingRoadSigns) {
                    flyingRoadSign.setVisibility(View.VISIBLE);
                }
                startTimer();
            } else {
                // go to next activity
                saveScore();
                moveToMenuActivity();
            }
        }
    }

    private void saveScore() {
        String scoreFile = "";
        //scoreFile = Utils.openTextFile(this, "score.txt");
        long scoreMs = score / 1000000;
        long elapsedTimeMs = elapsedTime / 1000000;
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        scoreFile += dateFormat.format(date) + " " + ((scoreMs + elapsedTimeMs) / level) + " " + level + "\n";
        Utils.saveTextFile(this, "score.txt", scoreFile);
    }

    @Override
    public void run() {
        if (stopTimer) {
            handler.removeCallbacks(this);
            return;
        }
        elapsedTime = System.nanoTime() - startTime + penalty;
        timeView.setText(String.valueOf(elapsedTime / 1000000));
        for (FlyingRoadSign flyingRoadSign : flyingRoadSigns) {
            flyingRoadSign.fly();
        }
        handler.postDelayed(this, 70);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        moveToMenuActivityByBackPress();
    }
}
