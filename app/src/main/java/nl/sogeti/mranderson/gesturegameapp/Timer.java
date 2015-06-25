package nl.sogeti.mranderson.gesturegameapp;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by MrAnderson1 on 19/06/15.
 */
public class Timer {

    private long startTime;
    private int x = 0;
    private int y = 0;
    private GameView gameView;
    private Paint p;
    private String t;

    public Timer(GameView gameView) {
        this.gameView = gameView;
        p = new Paint();
        p.setAntiAlias(true);
        p.setColor(Color.BLACK);
        p.setStyle(Paint.Style.FILL);
        p.setAlpha(70);
        p.setTextSize(200);
        p.setTextAlign(Paint.Align.CENTER);
        x = gameView.getWidth() / 2;
        y = (int) ((gameView.getHeight() / 2) - ((p.descent() + p.ascent()) / 2));

    }

    public void startTime() {
        startTime = System.currentTimeMillis();
    }

    private void update() {
        t = getElapsedTimeMin() + ":" + getElapsedTimeSecs();
    }

    public void onDraw(Canvas canvas) {
        update();
        canvas.drawText(t, x, y, p);
    }

    public String getElapsedTime() {
        return getElapsedTimeMin() + ":" + getElapsedTimeSecs();
    }

    //elaspsed time in seconds
    public long getElapsedTimeSecs() {
        long elapsed = 0;
        elapsed = ((System.currentTimeMillis() - startTime) / 1000 % 60);
        return elapsed;
    }

    //elaspsed time in minutes
    public long getElapsedTimeMin() {
        long elapsed = 0;
        elapsed = (((System.currentTimeMillis() - startTime) / 1000) / 60) % 60;
        return elapsed;
    }
}
