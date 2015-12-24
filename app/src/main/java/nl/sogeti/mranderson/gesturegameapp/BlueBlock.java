package nl.sogeti.mranderson.gesturegameapp;

import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by MrAnderson1 on 23/12/15.
 */
public class BlueBlock extends Block {


    public BlueBlock(GameView gameView) {
        super(gameView);
    }

    @Override
    public Paint getPaint() {
        Paint p = new Paint();
        p.setAntiAlias(true);
        p.setColor(Color.parseColor("#03A9F4"));
        p.setStyle(Paint.Style.FILL);
        p.setStrokeWidth(10f);
        return p;
    }
}
