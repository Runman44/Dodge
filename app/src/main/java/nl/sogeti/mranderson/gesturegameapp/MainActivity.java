package nl.sogeti.mranderson.gesturegameapp;

import android.app.Activity;
import android.content.Intent;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        gameView = (GameView) findViewById(R.id.surfaceView1);
        overlay = (RelativeLayout) findViewById(R.id.overlay);
        gameView.setGameCallback(this);
        score = (TextView) findViewById(R.id.score);
        score.setVisibility(View.GONE);
        playMusic();
    }

    public void onStart(View v) {
        removeOverlay();
        startGame();
    }

    public void onShare(View v) {
        String shareBody = "Check out my points:";
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Dodge Game" + "\n");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, ""));
    }

    private void startGame() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                gameView.prepareTime();
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
                    score.setVisibility(View.VISIBLE);
                    score.setText(String.format(getString(R.string.score), endTime));
                }
            }
        });
    }

    private void playMusic() {
//        MediaPlayer backgroundMusic = MediaPlayer.create(this, R.raw.intro);
//        backgroundMusic.setLooping(true);
//        backgroundMusic.start();
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
}
