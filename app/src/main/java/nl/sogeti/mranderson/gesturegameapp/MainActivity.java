package nl.sogeti.mranderson.gesturegameapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends Activity implements GameCallBack {
    /**
     * Called when the activity is first created.
     */
    private GameView gameView;
    private RelativeLayout overlay;
    private TextView score;
    private TextView highscore;
    private MediaPlayer backgroundMusic;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        gameView = (GameView) findViewById(R.id.surfaceView1);
        overlay = (RelativeLayout) findViewById(R.id.overlay);
        gameView.setGameCallback(this);
        score = (TextView) findViewById(R.id.score);
        highscore = (TextView) findViewById(R.id.best);
        highscore.setText(String.format(getString(R.string.highscore), getBestScore()));
        playBackgroundMusic();
    }

    public void onStart(View v) {
        playClick();
        removeOverlay();
        startGame();
    }



    public void onShare(View v) {
        playClick();
        String shareBody = "Check out my points:";
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Dodge Game" + "\n");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, ""));
    }

    private void startGame() {
        gameView.prepareTime();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                gameView.startTime();
            }
        }, 3000);
    }

    private void removeOverlay() {
        overlay.setVisibility(View.GONE);
    }

    @Override
    public void onGameOver(final String endTime) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                overlay.setVisibility(View.VISIBLE);
                if (!endTime.equals("")) {
                    score.setText(String.format(getString(R.string.score), endTime));
                    setBestScore(endTime);
                    highscore.setText(String.format(getString(R.string.highscore), getBestScore()));
                }
            }
        });
    }

    private void setBestScore(String endTime) {
        SharedPreferences sharedPref = this.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        String highScore = sharedPref.getString(getString(R.string.saved_high_score), "0:0");
        if (newHighScore(highScore, endTime)) {
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(getString(R.string.saved_high_score), endTime);
            editor.apply();
        }
    }

    private boolean newHighScore(String highScore, String endTime) {
        String[] newSeparated = endTime.split(":");
        String[] currentSeparated = highScore.split(":");
        int currentMin = Integer.parseInt(currentSeparated[0]);
        int currentSec = Integer.parseInt(currentSeparated[1]);
        int newMin = Integer.parseInt(newSeparated[0]);
        int newSec = Integer.parseInt(newSeparated[1]); // this will contain "Fruit"
        return !(currentMin >= newMin && currentSec >= newSec);
    }

    private String getBestScore() {
        SharedPreferences sharedPref = this.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        return sharedPref.getString(getString(R.string.saved_high_score), "0:0");
    }

    private void playBackgroundMusic() {
        backgroundMusic = MediaPlayer.create(this, R.raw.background);
        backgroundMusic.setVolume(0.5f, 0.5f);
        backgroundMusic.setLooping(true);
        backgroundMusic.start();
    }

    private void playClick() {
        MediaPlayer backgroundMusic = MediaPlayer.create(this, R.raw.click);
        backgroundMusic.start();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            gameView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        backgroundMusic.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        backgroundMusic.start();
    }
}
