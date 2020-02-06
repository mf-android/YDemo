package com.morefun.ypos.uitls;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Looper;

import com.morefun.ypos.MainActivity;
import com.morefun.ypos.R;


public class ActionItems {
    private Activity activity;
    public ProgressDialog pdialog;


    public Context getApplicationContext() {
        return this.activity.getApplicationContext();
    }

    public ActionItems(Activity activity) {
        this.activity = activity;

    }

    Object ret = null;

    Object WaitRet(Runnable uirunable) {
        ret = null;

        activity.runOnUiThread(uirunable);

        while (ret == null) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        return (Integer) ret;
    }


    public void blockmsg_s(final String title, final String msg) {
        closewait();
        Builder dlg = new AlertDialog.Builder(activity);
        dlg.setTitle(title).setMessage(msg);

        DialogInterface.OnClickListener r = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, final int arg1) {
//                CardProc.CardProcEnd();
                MainActivity.DLreturn = true;
                // TODO Auto-generated method stub
            }
        };
        dlg.setPositiveButton(R.string.btn_ok, r);
        dlg.show();
    }

    public void blockmsg(final String title, final String msg) {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            if (activity != null) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        blockmsg_s(title, msg);
                    }
                });
            }
        } else {
            blockmsg_s(title, msg);
        }

    }

    public int select(final String title, final String... mList) {
        CharSequence[] m = new CharSequence[mList.length];
        for (int i = 0; i < mList.length; i++) {
            m[i] = mList[i];
        }
        return select(title, m);

    }

    public int select(final String title, final CharSequence[] mList) {

        closewait();
        return (Integer) WaitRet(new Runnable() {
            @Override
            public void run() {

                // TODO Auto-generated method stub
                AlertDialog.Builder listDia = new AlertDialog.Builder(activity);
                listDia.setTitle(title);
                listDia.setItems(mList, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        ret = which;
                    }
                });
                listDia.setCancelable(false);
                listDia.create().show();
            }
        });

    }

    public void showwait(final String title, final String msg) {
        showwait(title, msg, null);
    }

    public interface OnCancelCall {
        void onCancel(DialogInterface dialog);
    }

    public void showwait(final String title, final String msg, final OnCancelCall onCancelCall) {
        if (pdialog == null) {
            pdialog = new ProgressDialog(activity);

            pdialog.setTitle(title);
            pdialog.setMessage(msg);
            pdialog.setCancelable(true);
            pdialog.setCanceledOnTouchOutside(false);
            pdialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    if (onCancelCall != null) {
                        onCancelCall.onCancel(dialogInterface);
                    }
                }
            });
            pdialog.show();
        } else {
            pdialog.setTitle(title);
            pdialog.setMessage(msg);
            pdialog.setCancelable(true);
            pdialog.setCanceledOnTouchOutside(false);
            pdialog.setOnCancelListener(new OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    if (onCancelCall != null) {
                        onCancelCall.onCancel(dialog);
                    }
                }
            });
            pdialog.show();
        }

    }

    public void closewait() {
        // TODO Auto-generated method stub
        if (pdialog != null) {
            pdialog.dismiss();
            pdialog = null;
        }
    }

}
