package com.devdelhi.weather.UI;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;

import com.devdelhi.weather.R;

public class AlertDialogFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Context context = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.Error_Title)
                .setMessage(R.string.Error_Message)
                .setPositiveButton("OK", null);


        AlertDialog dialogue = builder.create();
        return dialogue;
    }
}
