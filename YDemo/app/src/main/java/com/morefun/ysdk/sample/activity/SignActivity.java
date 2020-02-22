package com.morefun.ysdk.sample.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.RemoteException;
import android.view.View;
import android.widget.TextView;

import com.morefun.yapi.device.printer.MultipleAppPrinter;
import com.morefun.yapi.device.printer.OnPrintListener;
import com.morefun.ysdk.sample.R;
import com.morefun.ysdk.sample.device.DeviceHelper;
import com.morefun.ysdk.sample.view.PaintView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class SignActivity extends BaseActivity {

    private static final String TAG = SignActivity.class.getName();

    @BindView(R.id.textView)
    TextView textView;

    @BindView(R.id.canvas)
    PaintView mPaintView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signature);

        ButterKnife.bind(this);
    }

    @OnClick({R.id.clear, R.id.print})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.clear:
                mPaintView.clearCanvas();
                break;
            case R.id.print:
                print();
                break;
        }
    }

    private void print() {
        try {
            Bitmap signBitmap = mPaintView.getCanvasBitmap();
            MultipleAppPrinter printer = DeviceHelper.getPrinter();

            printer.printImage(signBitmap, new OnPrintListener.Stub() {
                @Override
                public void onPrintResult(int retCode) throws RemoteException {
                }

            }, new Bundle());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void setButtonName() {

    }
}
