package nl.sogeti.mranderson.gesturegameapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;

public class MainActivity extends Activity implements GameCallBack {
    /**
     * Called when the activity is first created.
     */
    private GameView gameView;
    private RelativeLayout overlay;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        gameView = (GameView) findViewById(R.id.surfaceView1);
        overlay = (RelativeLayout) findViewById(R.id.overlay);
        gameView.setGameCallback(this);

        playMusic();
    }

    public void onStart(View v) {
        removeOverlay();
        startGame();
    }

    public void onShare(View v) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Awesome game");
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
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
    public void onGameOver() {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                overlay.setVisibility(View.VISIBLE);
            }
        });
    }

    private void playMusic() {
//        MediaPlayer backgroundMusic = MediaPlayer.create(this, R.raw.intro);
//        backgroundMusic.setLooping(true);
//        backgroundMusic.start();
    }
}
