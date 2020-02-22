package com.morefun.ysdk.sample.utils;

import android.app.Activity;
import android.graphics.Color;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class SweetDialogUtil {
    private static SweetAlertDialog pDialog;

    public static void showNormal(final Activity context, final String contextText) {
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new SweetAlertDialog(context, SweetAlertDialog.NORMAL_TYPE)
                        .setContentText(contextText)
                        .show();
            }
        });

    }

    public static void showSuccess(final Activity context, final String contextText) {
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                        .setContentText(contextText)
                        .show();
            }
        });

    }

    public static void showError(final Activity context, final String contextText) {
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                        .setContentText(contextText)
                        .show();
            }
        });

    }

    public static void showProgress(Activity context, String text, boolean cancelable) {
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
                pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                pDialog.setTitleText(text);
                pDialog.setCancelable(cancelable);
                pDialog.show();
            }
        });


    }

    public static void changeAlertType(Activity activity, String text, int type) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (pDialog != null) {
                    pDialog.changeAlertType(type);
                    pDialog.setTitleText(text);
                }
            }
        });

    }

    public static void dismiss(Activity activity) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (pDialog != null) {
                    pDialog.dismiss();
                }
            }
        });

    }

    public static void setProgressText(String text) {
        pDialog.setTitleText(text);
    }

}
