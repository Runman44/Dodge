package nl.sogeti.mranderson.gesturegameapp;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

public class MainActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    private GameView gameview;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        gameview = new GameView(this, this);
        setContentView(gameview);
    }


    protected void onPause() {
        super.onPause();
        Log.v("MY_ACTIVITY", "onPause");
    }

    protected void onResume() {
        super.onResume();
        Log.v("MY_ACTIVITY", "onResume");
    }
}
