package nl.sogeti.mranderson.gesturegameapp;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by MrAnderson1 on 19/06/15.
 */
public class MarkerView {

    private final GameView gameView;
    private float x = 0;
    private float y = 0;
    Paint paint;

    public void onDraw(Canvas canvas) {
        canvas.drawCircle(x, y, 30, paint);
    }

    public MarkerView(GameView gameView) {
        this.gameView = gameView;
        x = gameView.getWidth() / 2;
        y = gameView.getHeight() / 2;
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(10f);
    }


    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setLoc(float x, float y) {
        if (x > gameView.getWidth() - 30) {
            this.x = gameView.getWidth() - 30;
        } else if (x < 0) {
            this.x = 30;
        }
        if (y > gameView.getHeight() - 30) {
            this.y = gameView.getHeight() - 30;
        } else if (y < 0) {
            this.y = 30;
        } else {
            this.x = x;
            this.y = y;
        }

    }
}
