package com.morefun.ypos.widget;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.WindowManager;


public class BaseDialogFragment extends DialogFragment {

    private DialogInterface.OnDismissListener mOnDismissListener;
    private DialogInterface.OnKeyListener onKeyListener;
    private boolean mCancelable;
    private Drawable mBackgroundDrawable;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    public void setCanceledOnTouchOutside(boolean canceled) {
        mCancelable = canceled;
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.setCanceledOnTouchOutside(canceled);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = getResources().getDisplayMetrics().widthPixels * 9 / 10;
        Dialog dialog = getDialog();
        dialog.setCanceledOnTouchOutside(mCancelable);
        dialog.getWindow().setAttributes(params);
        if (mOnDismissListener != null) {
            dialog.setOnDismissListener(mOnDismissListener);
        }
        if (onKeyListener != null) {
            dialog.setOnKeyListener(onKeyListener);
        }
        if (mBackgroundDrawable != null) {
            dialog.getWindow().setBackgroundDrawable(mBackgroundDrawable);
        }
    }

    public void setBackgroundDrawable(Drawable drawable) {
        mBackgroundDrawable = drawable;
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setBackgroundDrawable(mBackgroundDrawable);
        }
    }

    public void setOnDismissListener(DialogInterface.OnDismissListener listener) {
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.setOnDismissListener(listener);
        }
        mOnDismissListener = listener;
    }

    public void setOnKeyListener(DialogInterface.OnKeyListener onKeyListener) {
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.setOnKeyListener(onKeyListener);
        }
        this.onKeyListener = onKeyListener;
    }

    public void show(FragmentActivity baseActivity, String tag) {
        show(baseActivity.getSupportFragmentManager(), tag);
    }
}
