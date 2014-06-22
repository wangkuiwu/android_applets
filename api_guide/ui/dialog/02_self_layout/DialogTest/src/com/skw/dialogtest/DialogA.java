package com.skw.dialogtest;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.Toast;
import android.content.DialogInterface;
import android.support.v4.app.DialogFragment;

public class DialogA extends DialogFragment implements DialogInterface.OnClickListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.dialoga, null))
               .setMessage(R.string.dialoga_title)
               .setPositiveButton(R.string.ok, this)
               .setNegativeButton(R.string.cancel, this);

        return builder.create();
    }

    @Override
    public void onClick(DialogInterface dialog, int id) {
        switch(id) {
            case AlertDialog.BUTTON_NEGATIVE:
                Toast.makeText(getActivity(), "Negative", Toast.LENGTH_SHORT).show();
                break;
            case AlertDialog.BUTTON_POSITIVE:
                Toast.makeText(getActivity(), "Positive", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
       }
   }
}
