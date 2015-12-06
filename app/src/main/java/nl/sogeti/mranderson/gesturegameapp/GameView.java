package nl.sogeti.mranderson.gesturegameapp;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
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
    private final GameView gameView;
    private SurfaceHolder holder;
    private GameLoopThread gameLoopThread;
    private List<Block> sprites = new ArrayList<>();
    private MarkerView activeMarker;
    private Timer timer;
    public static String endTime;
    private boolean init = false;
    private Activity atc;
    private GameCallBack mGameCallback;

    public GameView(Context context) {
        super(context);
        this.gameView = this;
        setup();
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.gameView = this;
        setup();
    }

    public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.gameView = this;
        setup();
    }

    public GameView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.gameView = this;
        setup();
    }

    private void setup() {
        holder = getHolder();
        holder.addCallback(new SurfaceHolder.Callback() {

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                if (gameLoopThread != null)
                    gameLoopThread.interrupt();
            }

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                createSprites();
                gameLoopThread = new GameLoopThread(GameView.this);
                gameLoopThread.start();
                setOnTouchListener(GameView.this);
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format,
                                       int width, int height) {
            }
        });
    }

    public void createSprites() {
        sprites = new ArrayList<>();
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

    @Override
    protected void onDraw(final Canvas canvas) {
        if (!init) {
            init = true;
            GAME_STATE = 1;
        }
        if (canvas != null) {

            canvas.drawColor(Color.WHITE);
            activeMarker.onDraw(canvas);
            timer.onDraw(canvas);
            for (Block sprite : sprites) {
                sprite.onDraw(canvas);
            }


            if (GAME_STATE == 2) {
                activeMarker.setActive(true);

                for (Block sprite : sprites) {
                    if (sprite.isCollition(activeMarker.getX(), activeMarker.getY(), activeMarker.getActive())) {
                        endTime = timer.getElapsedTime();
                        restart();
                    }
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

    private void restart() {
        GAME_STATE = -1;
        timer.restart();
        activeMarker.setActive(false);
        mGameCallback.onGameOver(endTime);
    }


    public void prepareTime() {
        timer.prepareTime();
    }

    public void startTime() {
        GAME_STATE = 2;
        timer.startTime();
    }


    public void setGameCallback(GameCallBack cb) {
        this.mGameCallback = cb;
    }
}
