package com.jbnm.homehero.ui.goal;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

/**
 * Created by nathanielmeyer on 7/26/17.
 */

public class GoalPickerDialogFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Pick a new goal")
                .setTitle("New Goal Picker");
        return(builder.create());
    }
}
