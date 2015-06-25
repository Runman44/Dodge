package nl.sogeti.mranderson.gesturegameapp;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MrAnderson1 on 20/06/15.
 */

public class GameView extends SurfaceView implements View.OnTouchListener {

    public static int GAME_STATE = 0;
    private static int MIN_DXDY = 2;
    private final Activity atc;
    private SurfaceHolder holder;
    private GameLoopThread gameLoopThread;
    private List<Block> sprites = new ArrayList<>();
    private MarkerView activeMarker;
    private Timer timer;
    public static String endTime;
    private int FLAG = 1;
    private boolean init = false;

    public GameView(Context context, Activity atc) {
        super(context);
        this.atc = atc;
        MediaPlayer backgroundMusic = MediaPlayer.create(context, R.raw.intro);
        backgroundMusic.setLooping(true);
        backgroundMusic.start();
        holder = getHolder();
        holder.addCallback(new SurfaceHolder.Callback() {

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                if (gameLoopThread != null)
                    gameLoopThread.interrupt();
            }

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                gameLoopThread = new GameLoopThread(GameView.this);
                gameLoopThread.start();
                createSprites();
                setOnTouchListener(GameView.this);

            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format,
                                       int width, int height) {
            }
        });
    }

    public void createSprites() {
        activeMarker = new MarkerView(this);
        timer = new Timer(this);
        sprites.add(new Block(this));
        sprites.add(new Block(this));
        sprites.add(new Block(this));
        sprites.add(new Block(this));
        sprites.add(new Block(this));
        sprites.add(new Block(this));
        sprites.add(new Block(this));
        sprites.add(new Block(this));
    }

    private void showMenu() {
        atc.runOnUiThread(new Runnable() {   // Use the context here
            @Override
            public void run() {
                DialogFragment newFragment = new MenuDialogFragment();
                newFragment.show(atc.getFragmentManager(), "missiles");
            }
        });
    }

    private void showFinish() {
        atc.runOnUiThread(new Runnable() {   // Use the context here
            @Override
            public void run() {
                DialogFragment newFragment = new FinishedDialogFragment();
                newFragment.show(atc.getFragmentManager(), "missiles");
            }
        });
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        if (!init) {
            init = true;
            showMenu();
        }

        canvas.drawColor(Color.WHITE);
        activeMarker.onDraw(canvas);
        for (Block sprite : sprites) {
            sprite.onDraw(canvas);
        }


        if (GAME_STATE == 1) {
            GAME_STATE = -1;
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            GAME_STATE = 2;
            timer.startTime();
        } else if (GAME_STATE == 2) {
            timer.onDraw(canvas);
            for (Block sprite : sprites) {
                if (sprite.isCollition(activeMarker.getX(), activeMarker.getY())) {
                    endTime = timer.getElapsedTime();
                    showFinish();
                    GAME_STATE = -1;
                }
            }
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getActionMasked()) {

            case MotionEvent.ACTION_MOVE: {
                int pointerIndex = event.getActionIndex();

                if (Math.abs(activeMarker.getX() - event.getX(pointerIndex)) > MIN_DXDY || Math.abs(activeMarker.getY() - event.getY(pointerIndex)) > MIN_DXDY) {
                    activeMarker.setLoc(event.getX(pointerIndex), event.getY(pointerIndex));
                }
                break;
            }
            default:
        }
        return true;
    }


}
