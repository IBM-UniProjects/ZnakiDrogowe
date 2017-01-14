package com.amalisie.znakidrogowe;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ArcadeGameActivity extends AppCompatActivity implements View.OnClickListener, Runnable {

    private static final String TAG = ArcadeGameActivity.class.getName();

    private RelativeLayout rootLayout;
    private Button startButton;
    private TextView helpTextView;
    private TextView timeView;
    private FlyingRoadSign flyingButton;

    private Game game;
    private long score;
    private int level;

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
        this.helpTextView.setText(Utils.openTextFile(this, "pomoc2.txt"));
        this.timeView = (TextView) findViewById(R.id.time_view);

        Bundle b = getIntent().getExtras();
        this.game = (Game) b.get(Utils.GAME);
        this.game.restoreObject(this);
        this.score = b.getLong(Utils.ELAPSED_TIME);
        this.level = game.level;

        flyingButton = new FlyingRoadSign(this);
        flyingButton.setX(0);
        flyingButton.setY(0);

        flyingButton.setVisibility(View.INVISIBLE);
        flyingButton.setOnClickListener(this);

        rootLayout.addView(flyingButton);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) flyingButton.getLayoutParams();
        params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        flyingButton.setLayoutParams(params);

        this.gameEnded = false;
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

        if (v == flyingButton) {
            System.out.println("FLYING BUTTON!!!");
            return;
        }

        if (v == startButton) {
            if (!gameEnded) {
                ((ViewGroup) v.getParent()).removeView(helpTextView);
                ((ViewGroup) v.getParent()).removeView(startButton);
                setTitle("Gra Arcade - Poziom " + level);
                flyingButton.setVisibility(View.VISIBLE);
                startTimer();
            } else {
                // go to next activity
            }
        }
    }

    @Override
    public void run() {
        if (stopTimer) {
            handler.removeCallbacks(this);
            return;
        }
        elapsedTime = System.nanoTime() - startTime;
        timeView.setText(String.valueOf(elapsedTime / 1000000));
        /*float newX = flyingButton.getX() + 40.0f;
        if (newX > rootLayout.getWidth())
            newX = 0 - flyingButton.getWidth();
        flyingButton.setX(newX);*/
        flyingButton.fly();
        handler.postDelayed(this, 70);
    }
}
