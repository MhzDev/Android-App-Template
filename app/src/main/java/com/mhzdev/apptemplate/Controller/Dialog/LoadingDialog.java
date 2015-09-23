package com.mhzdev.apptemplate.Controller.Dialog;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.mhzdev.apptemplate.R;
import com.rey.material.widget.ProgressView;

/**
 * Generic loading dialog
 */
public class LoadingDialog {
    public static ProgressDialog show(Context context){
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        //dialog.setMessage(context.getString(R.string.loading_text));
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        return dialog;
    }


    public static AlertDialog showDialog(Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        RelativeLayout progressView = (RelativeLayout) ((Activity) context).getLayoutInflater().inflate(R.layout.progress_view, null);
        builder.setView(progressView);
        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        //dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialog.show();
        //Material.Drawable.Ripple.Wave

        return dialog;
    }
}
