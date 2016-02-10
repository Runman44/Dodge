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
import java.util.Iterator;
import java.util.List;

/**
 * Created by MrAnderson1 on 20/06/15.
 */

public class GameView extends SurfaceView implements View.OnTouchListener, TimeCallBack {

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
        sprites.add(new Block(this));
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        if (canvas != null) {

            canvas.drawColor(Color.WHITE);
            //TODO test FIX
            Iterator<Block> j = sprites.iterator();
            while (j.hasNext()) {
                Block sprite = j.next(); // must be called before you can call i.remove()
                sprite.onDraw(canvas);
            }
//            for (Block sprite : sprites) {
//                //TODO CRAASH HERE !
//                sprite.onDraw(canvas);
//            }

            if (STATE == GAME_STATE.PREPARE) {
                activeMarker.onDraw(canvas);
                timer.onDraw(canvas);
            }

            if (STATE == GAME_STATE.PLAYING) {
                activeMarker.onDraw(canvas);
                timer.onDraw(canvas);


                Iterator<Block> i = sprites.iterator();
                while (i.hasNext()) {
                    Block sprite = i.next(); // must be called before you can call i.remove()

                    if (sprite.isCollition(activeMarker.getSize(), activeMarker.getX(), activeMarker.getY(), activeMarker.getActive())) {
                        if (sprite instanceof GreenBlock) {
                            i.remove();
                            setBonusTouched();
                            for (Block sprite2 : sprites) {
                                sprite2.decreaseSpeed();
                            }
                        } else if (sprite instanceof BlueBlock) {
                            i.remove();
                            setBonusTouched();
                            activeMarker.setExtraShield(true);
                        } else {
                            if (!activeMarker.getExtraShield()) {
                                activeMarker.setIsDeath(true);
                                setGameOverState();
                                break;
                            } else {
                                activeMarker.setActive(false);
                                activeMarker.setExtraShield(false);
                                mGameCallback.onSecondLife();
                            }
                        }
                    }
                }

            }
        }
    }

    void startSecondLife() {
        activeMarker.setActive(true);
    }

    private void setBonusTouched() {
        mGameCallback.onBonusTouched();
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
        timer.setTimeCallback(this);
    }

    public void setGameOverState() {
        STATE = GAME_STATE.MENU;
        mGameCallback.onGameOver(timer.getT(), activeMarker.getIsDeath());
        setRestartState();
    }

    private void setRestartState() {
        activeMarker.setActive(false);
        timer.restart();
        activeMarker.setIsDeath(false);
        Iterator<Block> i = sprites.iterator();
        while (i.hasNext()) {
            Block sprite = i.next();
            i.remove();
        }
        sprites.add(new Block(this));
        sprites.add(new Block(this));
        sprites.add(new Block(this));
    }

    @Override
    public void onMinute() {
        BlueBlock blueBlock = new BlueBlock(this);
        sprites.add(blueBlock);
    }

    @Override
    public void onThirtySeconds() {
        GreenBlock greenBlock = new GreenBlock(this);
        sprites.add(greenBlock);
    }

    @Override
    public void onTenSeconds() {
        if (sprites.size() < 7)
            sprites.add(new Block(this));
    }

    public enum GAME_STATE {

        MENU,

        PREPARE,

        PLAYING;
    }
}
