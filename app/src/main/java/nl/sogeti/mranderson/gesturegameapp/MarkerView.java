package nl.sogeti.mranderson.gesturegameapp;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by MrAnderson1 on 19/06/15.
 */
public class MarkerView {

    private final GameView gameView;
    private int size;
    private float x = 0;
    private float y = 0;
    private boolean active = false;
    private boolean isDeath = false;
    private boolean extraShield;
    private Paint paint;

    public void onDraw(Canvas canvas) {
        canvas.drawCircle(x, y, size, paint);
    }


    private void getPaint() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.GRAY);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(10f);
    }

    public MarkerView(GameView gameView) {
        this.gameView = gameView;
        size = gameView.getHeight() / 30;
        x = gameView.getWidth() / 2;
        y = gameView.getHeight() / 2;
        getPaint();
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setLoc(float x, float y) {
        if (x > gameView.getWidth() - size) {
            this.x = gameView.getWidth() - size;
        } else if (x < 0) {
            this.x = size;
        }
        if (y > gameView.getHeight() - size) {
            this.y = gameView.getHeight() - size;
        } else if (y < 0) {
            this.y = size;
        } else {
            this.x = x;
            this.y = y;
        }

    }

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
        if (active) {
            paint.setColor(Color.BLACK);
        } else {
            paint.setColor(Color.GRAY);
        }
    }

    public boolean getIsDeath() {
        return this.isDeath;
    }

    public void setIsDeath(boolean isDeath) {
        this.isDeath = isDeath;
    }

    public int getSize() {
        return size;
    }

    public void setExtraShield(boolean b) {
        extraShield = b;
        if(b){
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(Color.RED);
        } else {
            getPaint();
        }
    }

    public boolean getExtraShield() {
        return extraShield;
    }
}
