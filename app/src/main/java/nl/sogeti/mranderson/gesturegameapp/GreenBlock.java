package nl.sogeti.mranderson.gesturegameapp;

import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by MrAnderson1 on 16/12/15.
 */
public class GreenBlock extends Block {


    public GreenBlock(GameView gameView) {
        super(gameView);
    }

    @Override
    public Paint getPaint() {
        Paint p = new Paint();
        p.setAntiAlias(true);
        p.setColor(Color.parseColor("#4CAF50"));
        p.setStyle(Paint.Style.FILL);
        p.setStrokeWidth(10f);
        return p;
    }
}
