package com.pils.disconnect;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import android.os.SystemClock;
import android.widget.Button;
import android.widget.Chronometer;
//import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.TimerTask;
import java.util.Timer;


public class BlackScreen extends AppCompatActivity {

    private Chronometer chronometer;
    private boolean running;
    private long pauseOffset;
    private socketHandler sha;
    private boolean end = false;

    int counter = 0;
    long elapsedMillis = 0;
    int reward_threshold = 100;
    int points = 0;
    String displaying_points ;
    //private ProgressBar progressbar;
    private TextView score;
    private Button rewardButton;
    private boolean displayRewardButton = false;
    private Button leaveButton;
    private boolean host;
    private TextView varscore;
    Handler handler = new Handler();

    private int activityReferences = 0;
    private boolean isActivityChangingConfigurations = false;

    @Override
    public void onStart(){
        super.onStart();
        if (++activityReferences == 1 && !isActivityChangingConfigurations) {
        }
    }

    @Override
    public void onStop(){
        super.onStop();
        if (--activityReferences == 0 && !isActivityChangingConfigurations) {
            // If the screen is off then the device has been locked
            PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
            boolean isScreenOn = powerManager.isScreenOn();
            if (!isScreenOn) {
            }
            if (isScreenOn && end == false){
                // App enters background
                try {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            sha.send("DECO:");
                        }
                    }).start();
                } catch (Exception e){
                    System.err.println(e);
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final partieHandler ph = (partieHandler) getIntent().getSerializableExtra("partieHandler");
        final socketHandler sh = new socketHandler();
        sh.syncpartieHandler(ph);
        this.sha = sh;
        Thread t = new Thread(sh);
        t.start();
        while(sh.getIsInit()==false){
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.black_screen);

        chronometer = findViewById(R.id.chronometer);
        chronometer.setFormat("Time: %s");
        chronometer.setBase(SystemClock.elapsedRealtime());

        //progressbar.setPadding(0,24,0,24);

        score = (TextView) findViewById(R.id.score);
        varscore = findViewById(R.id.varscore);
        rewardButton = (Button) findViewById(R.id.rewardbutton);
        rewardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            sh.send("GETSCORE:");
                        }
                    }).start();
                } catch (Exception e){
                    System.err.println(e);
                }
            }
        });

        leaveButton = (Button) findViewById(R.id.leaveButton);
        while(sh.getIsInit()==false){
        }

        Runnable r = new Runnable() {
            @Override
            public void run() {
                varscore.setText(Integer.toString(sh.getPh().getScore()));
                if (sh.getIsOver() == true){
                    Intent intent = new Intent(com.pils.disconnect.BlackScreen.this, Leave.class);
                    intent.putExtra("partieHandler",sh.getPh());
                    sh.setRunning(false);
                    while(sh.getRunning() == true){
                    }
                    startActivity(intent);
                } else {
                    handler.postDelayed(this, 500);
                }
            }
        };
        handler.postDelayed(r,500);
        leaveButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               end = true;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                            sh.send("STOP:");
                    }
                 }).start();
           }
       });

        startChronometer();
        //setProgressBar();
    }

    public void startChronometer() {
        if (!running) {
            chronometer.start();
            running = true;
        }
    }

    /*public void setProgressBar() {

        final Timer t = new Timer();
        progressbar.setMax(100);
        TimerTask tt = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        elapsedMillis = SystemClock.elapsedRealtime() - chronometer.getBase();
                        displaying_points = "SCORE : " + Integer.toString(points);
                        score.setText(displaying_points);
                        if (points > 200) {
                            rewardButton.setVisibility(View.VISIBLE);
                        }
                    }
                });
                points = points + 2 ;
                counter++;
                progressbar.setProgress(counter);
            }
        };
        t.schedule(tt, 0, reward_threshold);
    };*/

    /*public void setDisplayRewardButton(boolean displayRewardButton) {
        if (this.displayRewardButton) {
            rewardButton.setVisibility(View.VISIBLE);
        }
    }*/
}
