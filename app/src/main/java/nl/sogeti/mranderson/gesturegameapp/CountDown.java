package nl.sogeti.mranderson.gesturegameapp;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by MrAnderson1 on 19/06/15.
 */
public class CountDown {

    private int x = 0;
    private int y = 0;
    private Paint p;

    public CountDown(GameView gameView) {
        p = new Paint();
        p.setAntiAlias(true);
        p.setColor(Color.BLACK);
        p.setStyle(Paint.Style.FILL);
        p.setTextSize(200);
        p.setTextAlign(Paint.Align.CENTER);
        x = gameView.getWidth() / 2;
        y = (int) ((gameView.getHeight() / 2) - ((p.descent() + p.ascent()) / 2));
    }

    public void onDraw(Canvas canvas, String time) {
        canvas.drawText(time, x, y, p);
    }

}
