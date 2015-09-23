package com.mhzdev.apptemplate.Controller.Dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;

/**
 * Created by Michele Stefanelli (MhzDev) on 14/09/2015.
 */
public class DialogListBuilder {
    public static AlertDialog showDialog(
            Context context, int titleRes, int messageRes, String[] dataList, final boolean cancelable, @NonNull final DialogCallback callback) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if(titleRes!=0)
            builder.setTitle(titleRes);

        if(messageRes!=0)
            builder.setMessage(messageRes);

        builder.setCancelable(cancelable);

        builder.setItems(dataList, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                callback.onItemClick(which);
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        return alertDialog;
    }

    public interface DialogCallback {
        void onItemClick(int position);
    }
}
