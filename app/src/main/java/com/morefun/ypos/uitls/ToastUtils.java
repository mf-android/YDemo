package com.morefun.ypos.uitls;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Toast;

/**
 * Created by John on 2016/3/21.
 */
public class ToastUtils {

    private static final String TAG = ToastUtils.class.getSimpleName();

    private static Handler mHandler = new Handler(Looper.getMainLooper());
    private static Toast mToast;

    public static void show(final Context context, final String message) {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (mToast != null) {
                        mToast.cancel();
                    }
                    mToast = Toast.makeText(context, message, Toast.LENGTH_LONG);
                    mToast.setText(message);
                    mToast.show();
                }
            });
        } else {
            if (mToast != null) {
                mToast.cancel();
            }
            mToast = Toast.makeText(context, message, Toast.LENGTH_LONG);
            mToast.setText(message);
            mToast.show();
        }

    }

    public static void show(final Context context, final int gravity, final View view) {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (mToast != null) {
                        mToast.cancel();
                    }
                    mToast = new Toast(context);
                    mToast.setDuration(Toast.LENGTH_LONG);
                    mToast.setGravity(gravity, 0, 0);
                    mToast.setView(view);
                    mToast.show();
                }
            });
        } else {
            if (mToast != null) {
                mToast.cancel();
            }
            mToast = new Toast(context);
            mToast.setDuration(Toast.LENGTH_LONG);
            mToast.setView(view);
            mToast.setGravity(gravity, 0, 0);
            mToast.show();
        }

    }

    public static void show(final Toast toast) {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (mToast != null) {
                        mToast.cancel();
                    }
                    mToast = toast;
                    mToast.show();
                }
            });
        } else {
            if (mToast != null) {
                mToast.cancel();
            }
            mToast = toast;
            mToast.show();
        }
    }

    public static Handler getmHandler() {
        return mHandler;
    }
}
