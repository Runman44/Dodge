package nl.sogeti.mranderson.gesturegameapp;

import android.graphics.Canvas;

public class GameLoopThread extends Thread {
    private GameView view;
    static final long FPS = 30;

    public GameLoopThread(GameView view) {
        this.view = view;
    }

    @Override
    public void run() {
        long ticksPS = 1000 / FPS;
        long startTime;
        long sleepTime;
        while (!this.currentThread().isInterrupted()) {
            Canvas c = null;
            startTime = System.currentTimeMillis();
            try {
                c = view.getHolder().lockCanvas();
                synchronized (view.getHolder()) {
                    view.onDraw(c);
                }
            } finally {
                if (c != null) {
                    view.getHolder().unlockCanvasAndPost(c);
                }
            }
            sleepTime = ticksPS - (System.currentTimeMillis() - startTime);
            try {
                if (sleepTime > 0)
                    sleep(sleepTime);
                else
                    sleep(20);
            } catch (Exception e) {
            }
        }
    }
}