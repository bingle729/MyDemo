package com.huazun.mydemo.ui.views;


import android.content.Context;

import androidx.appcompat.app.AlertDialog;

import com.huazun.mydemo.R;

public class SimpleMessageDialog {
    private Context context;
    private String title, message;
    public SimpleMessageDialog(Context context, String title, String message){
        this.context = context;
        this.title = title;
        this.message = message;
    }

    public AlertDialog.Builder getDialog(){
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context, R.style.alertDialogStyle);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", null);
        return builder;
    }
}
