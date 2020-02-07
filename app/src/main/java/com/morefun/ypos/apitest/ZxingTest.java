package com.morefun.ypos.apitest;

import android.graphics.Bitmap;
import android.util.Log;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.morefun.ypos.MainActivity;
import com.morefun.ypos.uitls.ZXingUtils;

public class ZxingTest {
    private static final String TAG = "ZxingTest";

    public Bitmap qrCode() {
        try {
            Bitmap bitQrCode = ZXingUtils.createQRImage("I am test Content..............", 600, 600);
            Log.d(TAG, "xiaode  =" + bitQrCode);
            return bitQrCode;
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Bitmap barcode() {
        try {
            Bitmap bitQrCode = ZXingUtils.creatBarcode(MainActivity.getContext(), "12345678901234", BarcodeFormat.CODE_128
                    , 400, 150, false);
            return bitQrCode;
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }
}
