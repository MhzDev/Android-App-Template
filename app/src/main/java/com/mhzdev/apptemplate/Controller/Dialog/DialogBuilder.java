package com.mhzdev.apptemplate.Controller.Dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;

import com.mhzdev.apptemplate.R;


public class DialogBuilder {

    private enum BUTTON_TYPE{
        CLOSE,
        CANCEL_OK
    }

    public static AlertDialog showMessageDialog(Context context, int titleRes, int messageRes, boolean cancelable){
        return showDialog(context, titleRes, messageRes, cancelable, BUTTON_TYPE.CLOSE, null);
    }

    public static AlertDialog showConfirmDialog(Context context, int titleRes, int messageRes, boolean cancelable, @Nullable DialogCallback callback){
        return showDialog(context, titleRes, messageRes, cancelable, BUTTON_TYPE.CANCEL_OK, callback);
    }

    public static AlertDialog showDialog(
            Context context, int titleRes, int messageRes, final boolean cancelable,
            BUTTON_TYPE buttonType, @Nullable final DialogCallback callback) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if(titleRes!=0)
            builder.setTitle(titleRes);

        if(messageRes!=0)
            builder.setMessage(messageRes);

        builder.setCancelable(cancelable);

        builder.setPositiveButton(R.string.confirmation_dialog_ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();

                if(callback!=null)
                    callback.onPositiveClick();
            }
        });

        if(buttonType == BUTTON_TYPE.CANCEL_OK)
        builder.setNegativeButton(R.string.confirmation_dialog_cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        return alertDialog;
    }

    public interface DialogCallback {
        void onPositiveClick();
    }
}
