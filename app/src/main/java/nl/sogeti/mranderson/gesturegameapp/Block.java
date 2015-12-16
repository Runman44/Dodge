package nl.sogeti.mranderson.gesturegameapp;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.Random;

/**
 * Created by MrAnderson1 on 19/06/15.
 */
public class Block {

    private int x = 0;
    private int y = 0;
    private int size;
    private int ySpeed;
    private int xSpeed;
    private GameView gameView;
    private Paint p;

    public Block(GameView gameView) {
        this.gameView = gameView;
        size = gameView.getHeight() / 20;
        Random rnd = new Random();
        x = rnd.nextInt(gameView.getWidth() - size);
        y = rnd.nextInt(gameView.getHeight() - size);
        xSpeed = rnd.nextInt(25);
        ySpeed = rnd.nextInt(25);
        p = new Paint();
        p.setAntiAlias(true);
        p.setColor(Color.RED);
        p.setStyle(Paint.Style.FILL);
        p.setStrokeWidth(10f);
    }

    public void setSpeed(int speed) {
        Random rnd = new Random();
        xSpeed = rnd.nextInt(speed);
        ySpeed = rnd.nextInt(speed);
    }

    public Paint getPaint() {
        return p;
    }


    private void update() {
        if (x > gameView.getWidth() - size - xSpeed || x + xSpeed < 0) {
            xSpeed = -xSpeed;
        }
        x = x + xSpeed;
        if (y > gameView.getHeight() - size - ySpeed || y + ySpeed < 0) {
            ySpeed = -ySpeed;
        }
        y = y + ySpeed;
    }

    public void onDraw(Canvas canvas) {
        update();
        canvas.drawRect(x, y, x + size, y + size, getPaint());
    }

    public boolean isCollition(float markerSize, float x2, float y2, boolean active) {
        if (!active)
            return false;
        float distX = Math.abs(x2 - (this.x + markerSize));
        float distY = Math.abs(y2 - (this.y + markerSize));
        if (distX > (markerSize + markerSize) || distY > (markerSize + markerSize)) {
            return false;
        } else if (distX <= (size) || distY <= (size)) {
            return true;
        }
        return false;
    }
}
