package com.buschmais.xpl;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * Created by training on 3/13/14.
 */
public class MyDialog extends DialogFragment{

    private  DialogInterface.OnClickListener listener;

    @Override
    public void onAttach(Activity activity) {

        super.onAttach(activity);

        if (activity instanceof DialogInterface.OnClickListener)

            listener = (DialogInterface.OnClickListener) activity;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder  = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_DEVICE_DEFAULT_DARK);

        builder.setMessage(R.string.dialog_message);
        builder.setPositiveButton(R.string.dialog_button_ok, listener);
        builder.setNegativeButton(R.string.dialog_button_nok, null);

        return builder.create();
    }
}
