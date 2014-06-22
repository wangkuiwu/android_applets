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

    public interface NoticeDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog);
        public void onDialogNegativeClick(DialogFragment dialog);
    }

    // Use this instance of the interface to deliver action events
    NoticeDialogListener mListener;
 
    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (NoticeDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement NoticeDialogListener");
        }
    }

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
            case AlertDialog.BUTTON_POSITIVE:
                //Toast.makeText(getActivity(), "Negative", Toast.LENGTH_SHORT).show();
                mListener.onDialogPositiveClick(DialogA.this);
                break;
            case AlertDialog.BUTTON_NEGATIVE:
                //Toast.makeText(getActivity(), "Positive", Toast.LENGTH_SHORT).show();
                mListener.onDialogNegativeClick(DialogA.this);
                break;
            default:
                break;
       }
   }
}
