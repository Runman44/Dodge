package nl.sogeti.mranderson.gesturegameapp;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.games.Games;
import com.google.example.games.basegameutils.BaseGameActivity;

public class MainActivity extends BaseGameActivity implements GameCallBack {

    private static final int ACHIEVEMENTS = 1;
    private static final int LEADERBOARD = 2;
    private GameView gameView;
    private RelativeLayout overlay;
    private TextView score;
    private TextView highScore;
    private MediaPlayer backgroundMusic;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        gameView = (GameView) findViewById(R.id.surfaceView1);
        overlay = (RelativeLayout) findViewById(R.id.overlay);
        gameView.setGameCallback(this);
        score = (TextView) findViewById(R.id.score);
        highScore = (TextView) findViewById(R.id.best);
        highScore.setText(String.format(getString(R.string.highscore), getBestScore()));
        playBackgroundMusic();

//        AdRequest adRequest = new AdRequest.Builder()
//                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)        // All emulators
//                .build();
////
//        AdView mAdView = (AdView) findViewById(R.id.adView);
////        AdRequest adRequest = new AdRequest.Builder().build();
//        mAdView.loadAd(adRequest);
    }


    public void onStart(View v) {
        playClick();
        removeOverlay();
        gameView.setStartState();
        newAchievement("CgkIxqqBqbcNEAIQCw");
    }

    private void newAchievement(String id) {
        if (getApiClient().isConnected()) {
            Games.Achievements.unlock(getApiClient(), id);
        }
    }

    private void newIncrementAchievement(String id) {
        if (getApiClient().isConnected()) {
            Games.Achievements.increment(getApiClient(), id, 1);
        }
    }

    public void onRating(View v) {
        playClick();
        newAchievement("CgkIxqqBqbcNEAIQBg");
        Uri uri = Uri.parse("market://details?id=" + this.getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + this.getPackageName())));
        }
    }


    public void onShare(View v) {
        playClick();
        newAchievement("CgkIxqqBqbcNEAIQBQ");
        String shareBody = "Check out my score: " + getBestScore() + "! - Download - http://play.google.com/store/apps/details?id=" + this.getPackageName();
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Dodge Game" + "\n");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, ""));
    }

    public void onStats(View v) {
        playClick();
        if (getApiClient().isConnected()) {
            startActivityForResult(Games.Leaderboards.getLeaderboardIntent(
                    getApiClient(), getString(R.string.leaderboard)),
                    LEADERBOARD);
        } else {
            getApiClient().reconnect();
        }
    }

    public void onAchievements(View v) {
        playClick();
        if (getApiClient().isConnected()) {
            startActivityForResult(Games.Achievements.getAchievementsIntent(getApiClient()),
                    ACHIEVEMENTS);
        } else {
            getApiClient().reconnect();
        }
    }


    private void removeOverlay() {
        overlay.setVisibility(View.GONE);
    }

    @Override
    public void onGameOver(final long endTime, final boolean isDeath) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                overlay.setVisibility(View.VISIBLE);
                if (isDeath) {
                    playDeath();
                    newIncrementAchievement("CgkIxqqBqbcNEAIQDA");
                    newIncrementAchievement("CgkIxqqBqbcNEAIQDQ");
                    newIncrementAchievement("CgkIxqqBqbcNEAIQDg");
                }
                setBestScore(endTime);
                isAchievement(endTime);
                score.setText(String.format(getString(R.string.score), endTime));
                highScore.setText(String.format(getString(R.string.highscore), getBestScore()));
                if (getApiClient().isConnected()) {
                    Games.Leaderboards.submitScore(getApiClient(),
                            getString(R.string.leaderboard), getBestScore());
                }
            }
        });
    }

    @Override
    public void onBonusTouched() {
        playBonus();
    }

    @Override
    public void onSecondLife() {
        //TODO play sound here
        playSecondLife();
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        gameView.startSecondLife();
                    }
                }, 1000);

            }
        });

    }


    private void isAchievement(long endTime) {
        if (getApiClient().isConnected()) {
            if (endTime >= 20) {
                Games.Achievements.unlock(getApiClient(), "CgkIxqqBqbcNEAIQAg");
            }
            if (endTime >= 60) {
                Games.Achievements.unlock(getApiClient(), "CgkIxqqBqbcNEAIQAw");
            }
            if (endTime >= 180) {
                Games.Achievements.unlock(getApiClient(), "CgkIxqqBqbcNEAIQBA");
            }
        }
    }

    private void setBestScore(long endTime) {
        SharedPreferences sharedPref = this.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        long highScore = sharedPref.getLong(getString(R.string.saved_high_score), 0);
        if (highScore < endTime) {
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putLong(getString(R.string.saved_high_score), endTime);
            editor.apply();
        }
    }

    private long getBestScore() {
        SharedPreferences sharedPref = this.getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        return sharedPref.getLong(getString(R.string.saved_high_score), 0);
    }

    // <-----SOUND----->

    private void playBackgroundMusic() {
        backgroundMusic = MediaPlayer.create(this, R.raw.background);
        backgroundMusic.setVolume(0.5f, 0.5f);
        backgroundMusic.setLooping(true);
        backgroundMusic.start();
    }

    private void playSecondLife() {
        MediaPlayer secondMusic = MediaPlayer.create(this, R.raw.hit);
        secondMusic.start();
    }

    private void playClick() {
        MediaPlayer clickMusic = MediaPlayer.create(this, R.raw.click);
        clickMusic.start();
    }

    private void playBonus() {
        MediaPlayer clickMusic = MediaPlayer.create(this, R.raw.bam);
        clickMusic.start();
    }

    private void playDeath() {
        MediaPlayer deathMusic = MediaPlayer.create(this, R.raw.die);
        deathMusic.start();
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
        gameView.setGameOverState();
    }

    @Override
    protected void onResume() {
        super.onResume();
        backgroundMusic.start();
    }

    @Override
    public void onSignInFailed() {
        // TODO - the right thing.
    }

    @Override
    public void onSignInSucceeded() {
        // TODO - the right thing.
    }
}
