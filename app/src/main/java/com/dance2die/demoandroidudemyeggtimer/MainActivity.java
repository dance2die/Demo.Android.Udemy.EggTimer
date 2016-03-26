package com.dance2die.demoandroidudemyeggtimer;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private SeekBar timerSeekBar;
    private TextView timerTextView;
    private boolean isCounterActive = false;
    private Button controllerButton;
    private CountDownTimer countDownTimer;


    private void updateTimer(int secondsLeft) {
        int minutes = secondsLeft / 60;
        int seconds = secondsLeft - (minutes * 60);

        timerTextView.setText(String.format("%d:%02d", minutes, seconds));
    }

    public void controlTimer(View view){
        if (isCounterActive) {
            resetTimer(countDownTimer);
        } else {
            isCounterActive = true;
            timerSeekBar.setEnabled(!isCounterActive);
            controllerButton.setText("Stop");

            final int ONE_SECOND = 1000;
            // 100 is a hack. Timer starts right away, we need to add 100 at least to make the timer start not right away.
            long timerSeconds = timerSeekBar.getProgress() * ONE_SECOND + 100;

            countDownTimer = new CountDownTimer(timerSeconds, ONE_SECOND){
                @Override
                public void onTick(long millisUntilFinished) {
                    updateTimer((int) (millisUntilFinished / ONE_SECOND));
                }

                @Override
                public void onFinish() {
                    resetTimer(this);
                    Log.i("finished", "timer done!");
                    isCounterActive = false;

                    MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.airhorn);
                    mediaPlayer.start();
                }
            }.start();
        }
    }

    private void resetTimer(CountDownTimer countDownTimer) {
        timerTextView.setText("0:30");
        timerSeekBar.setProgress(30);
        assert countDownTimer != null;
        countDownTimer.cancel();
        controllerButton.setText("Go!");
        timerSeekBar.setEnabled(true);
        isCounterActive = false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        timerSeekBar = (SeekBar) findViewById(R.id.timerSeekBar);
        timerTextView = (TextView) findViewById(R.id.timerTextView);
        controllerButton = (Button) findViewById(R.id.controllerButton);

        timerSeekBar.setMax(600);   // 10 minutes = 60 seconds * 10
        timerSeekBar.setProgress(30);   // 30 seconds
        timerSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updateTimer(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
