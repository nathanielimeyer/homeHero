package com.jbnm.homehero.ui.goal;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.jbnm.homehero.R;

/**
 * Created by nathanielmeyer on 7/26/17.
 */

public class GoalPickerDialogFragment extends DialogFragment {
    Integer selectedReward = 0;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Pick a new goal")
                .setTitle("New Goal Picker")
                .setItems(R.array.replaceMe, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        selectedReward = i;
                    }
                });
        return(builder.create());
    }
}
