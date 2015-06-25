package nl.sogeti.mranderson.gesturegameapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class FinishedDialogFragment extends DialogFragment implements View.OnClickListener {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().setCanceledOnTouchOutside(false);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        View view = inflater.inflate(R.layout.fragment_finished, null);
        Button button = (Button) view.findViewById(R.id.restart);
        TextView text = (TextView) view.findViewById(R.id.score);
        text.setText("Your score is: " + GameView.endTime);
        button.setOnClickListener(this);
        builder.setView(view);


        return builder.create();
    }

    @Override
    public void onClick(View v) {
        FinishedDialogFragment.this.getDialog().cancel();
        GameView.GAME_STATE = 1;
    }
}