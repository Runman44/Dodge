package nl.sogeti.mranderson.gesturegameapp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Handler;
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

    private static int MIN_DXDY = 2;
    private final GameView gameView;
    private SurfaceHolder holder;
    private GameLoopThread gameLoopThread;
    private List<Block> sprites = new ArrayList<>();
    private MarkerView activeMarker;
    private Timer timer;
    private GameCallBack mGameCallback;
    private GAME_STATE STATE = GAME_STATE.MENU;

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
                if (gameLoopThread == null) {
                    createSprites();
                    gameLoopThread = new GameLoopThread(GameView.this);
                    gameLoopThread.start();
                    setOnTouchListener(GameView.this);
                }
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
        sprites.add(new RedBlock(this));
        sprites.add(new Block(this));
        sprites.add(new Block(this));
        sprites.add(new Block(this));
        sprites.add(new Block(this));
        sprites.add(new Block(this));
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        if (canvas != null) {

            canvas.drawColor(Color.WHITE);
            for (Block sprite : sprites) {
                sprite.onDraw(canvas);
            }

            if (STATE == GAME_STATE.PREPARE) {
                activeMarker.onDraw(canvas);
                timer.onDraw(canvas);
            }

            if (STATE == GAME_STATE.PLAYING) {
                activeMarker.onDraw(canvas);
                timer.onDraw(canvas);
                for (Block sprite : sprites) {
                    if (sprite.isCollition(activeMarker.getSize(), activeMarker.getX(), activeMarker.getY(), activeMarker.getActive())) {
                        activeMarker.setIsDeath(true);
                        setGameOverState();
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

    public void setGameCallback(GameCallBack cb) {
        this.mGameCallback = cb;
    }

    public void setStartState() {
        activeMarker.setIsDeath(false);
        STATE = GAME_STATE.PREPARE;
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                setStartPlayingState();
            }
        }, 1500);
    }

    public void setStartPlayingState() {
        STATE = GAME_STATE.PLAYING;
        timer.startTime();
        activeMarker.setActive(true);
    }

    public void setGameOverState() {
        STATE = GAME_STATE.MENU;
        activeMarker.setActive(false);
        mGameCallback.onGameOver(timer.getT(), activeMarker.getIsDeath());
        timer.restart();
        activeMarker.setIsDeath(false);
    }

    public enum GAME_STATE {

        MENU,

        PREPARE,

        PLAYING;
    }
}
