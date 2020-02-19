package com.morefun.ysdk.sample.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public abstract class BaseActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    protected void showResult(final TextView textView, final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                textView.append(text +"\r\n");
            }
        });
    }

    protected abstract void setButtonName();

}