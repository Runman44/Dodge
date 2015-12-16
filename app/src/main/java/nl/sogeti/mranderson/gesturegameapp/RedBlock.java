package nl.sogeti.mranderson.gesturegameapp;

import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by MrAnderson1 on 16/12/15.
 */
public class RedBlock extends Block {

    public RedBlock(GameView gameView) {
        super(gameView);
    }

    @Override
    public Paint getPaint() {
        Paint p = new Paint();
        p.setAntiAlias(true);
        p.setColor(Color.GREEN);
        p.setStyle(Paint.Style.FILL);
        p.setStrokeWidth(10f);
        return p;
    }
}
