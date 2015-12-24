package nl.sogeti.mranderson.gesturegameapp;

/**
 * Created by MrAnderson1 on 05/12/15.
 */
public interface GameCallBack {

    void onGameOver(long endTime, boolean isDeath);

    void onBonusTouched();

    void onSecondLife();
}
