package com.morefun.ypos;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.morefun.yapi.device.printer.FontFamily;
import com.morefun.yapi.device.printer.OnPrintListener;
import com.morefun.yapi.device.printer.Printer;
import com.morefun.yapi.engine.DeviceServiceEngine;
import com.morefun.ypos.uitls.ActionItems;
import com.morefun.ypos.widget.PaintView;


public class SignActivity extends Activity {
    private static final String TAG = "SignActivity";

    private Button btn1, btn2;
    private PaintView mPaintView = null;

    private ActionItems ac;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signature);

        ac = new ActionItems(this);

        mPaintView = (PaintView) findViewById(R.id.auth_signature_canvas);

        btn1 = (Button) findViewById(R.id.button1);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    //获取签名bitmap
                    Bitmap signBitmap = mPaintView.getCanvasBitmap();
                    DeviceServiceEngine mDeviceServiceEngine = SDKManager.getInstance().getDeviceServiceEngine();
                    if (mDeviceServiceEngine == null){
                        return;
                    }
                    Printer printer = mDeviceServiceEngine.getPrinter();
                    int initRet = printer.initPrinter();
                    if (initRet == 0) {
//                    ac.showwait( getString(R.string.msg_title) , getString(R.string.msg_printing)  );
                        Bitmap bitmap = BitmapFactory.decodeStream(getApplicationContext().getAssets().open("china_union_pay.bmp"));
                        printer.appendImage(bitmap);
                        printer.appendPrnStr("\n \n \n \n \n", FontFamily.MIDDLE, false);
                        printer.appendImage(signBitmap);
                        printer.appendPrnStr("\n \n \n", FontFamily.MIDDLE, false);
                        int ret = printer.startPrint(new OnPrintListener.Stub() {
                            @Override
                            public void onPrintResult(int i) throws RemoteException {
                                Log.d(TAG, "onPrintResult = " + i);
//                            ac.showwait( getString(R.string.msg_title) , getString(R.string.msg_succ)  );
                            }
                        });
                    } else {

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        btn2 = (Button) findViewById(R.id.button2);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPaintView.clearCanvas();
            }
        });
    }


}
