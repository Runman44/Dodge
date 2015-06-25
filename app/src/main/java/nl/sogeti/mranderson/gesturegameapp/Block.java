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
    private int ySpeed;
    private int xSpeed;
    private GameView gameView;
    private Paint p;

    public Block(GameView gameView) {
        this.gameView = gameView;
        Random rnd = new Random();
        x = rnd.nextInt(gameView.getWidth() - 60);
        y = rnd.nextInt(gameView.getHeight() - 60);
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

    private void update() {
        if (x > gameView.getWidth() - 60 - xSpeed || x + xSpeed < 0) {
            xSpeed = -xSpeed;
        }
        x = x + xSpeed;
        if (y > gameView.getHeight() - 60 - ySpeed || y + ySpeed < 0) {
            ySpeed = -ySpeed;
        }
        y = y + ySpeed;
    }

    public void onDraw(Canvas canvas) {
        update();
        canvas.drawRect(x, y, x + 60, y + 60, p);
    }

    public boolean isCollition(float x2, float y2) {
        float distX = Math.abs(x2 - (this.x + 30));
        float distY = Math.abs(y2 - (this.y + 30));
        if (distX > (30 + 30) || distY > (30 + 30)) {
            return false;
        } else if (distX <= (60) || distY <= (60)) {
            return true;
        }
        return false;
    }
}
