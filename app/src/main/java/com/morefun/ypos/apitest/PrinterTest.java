package com.morefun.ypos.apitest;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;

import com.morefun.yapi.ServiceResult;
import com.morefun.yapi.device.printer.FontFamily;
import com.morefun.yapi.device.printer.MulPrintStrEntity;
import com.morefun.yapi.device.printer.MultipleAppPrinter;
import com.morefun.yapi.device.printer.OnPrintListener;
import com.morefun.yapi.device.printer.PrinterConfig;
import com.morefun.yapi.engine.DeviceServiceEngine;
import com.morefun.ypos.MainActivity;
import com.morefun.ypos.R;
import com.morefun.ypos.uitls.FileUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class PrinterTest {
    private static final String TAG = "PrinterTest";

    /**
     * Get TypeFacePath
     *
     * @return if  Build.VERSION.SDK_INT below SDK_VERSION 26  return
     * else return path /data/morefun/
     */
    public static String initTypeFacePath() {
//        String namePath = "Roboto-Ligth";
//        String namePath = "Roboto-Medium";
        String namePath = "wawa";
        String mTypefacePath = null;
        if (Build.VERSION.SDK_INT >= 23) {
            mTypefacePath = FileUtils.getExternalCacheDir(MainActivity.getContext(), namePath + ".ttf");
        } else {
            String filePath = FileUtils.createTmpDir(MainActivity.getContext());
            Log.d(TAG, "filePath = " + filePath);
            mTypefacePath = filePath + namePath + ".ttf";
        }
        Log.d(TAG, "mTypefacePath = " + mTypefacePath);
        try {
            FileUtils.copyFromAssets(MainActivity.getContext().getAssets(), namePath, mTypefacePath, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
//        Utils.chmod("777", mTypefacePath);
        Log.d(TAG, "mTypefacePath = " + mTypefacePath);
        return mTypefacePath;
    }

    private static String getprintitem(String head, String val) {
        StringBuffer sb = new StringBuffer();
        sb.append(head);
        sb.append("\r\n");

        sb.append(val);
        sb.append("\r\n");
        return sb.toString();
    }

    public static void Print(DeviceServiceEngine engine, Context context, String path, final MainActivity.AlertDialogOnShowListener listener) throws RemoteException {
        MultipleAppPrinter printer = engine.getMultipleAppPrinter();
        PrinterTest.PrintDataEn(printer, context, path, listener);
//        PrintImage(engine, context, listener);
    }


    public static void PrintDataEn(MultipleAppPrinter printer, final Context context, String path, final MainActivity.AlertDialogOnShowListener listener) {
        try {
            int fontSize = FontFamily.MIDDLE;
            Bundle config = new Bundle();
//            path = initTypeFacePath();
            if (!TextUtils.isEmpty(path)) {
                config.putString(PrinterConfig.COMMON_TYPEFACE_PATH, path);
            }
            config.putInt(PrinterConfig.COMMON_GRAYLEVEL, 15);

            List<MulPrintStrEntity> list = new ArrayList<>();
            MulPrintStrEntity entity = new MulPrintStrEntity("POS purchase order", fontSize);
//            entity.setGravity(Gravity.CENTER);
            entity.setMarginX(50);
            entity.setUnderline(true);
            entity.setYspace(30);
            list.add(entity);
            MulPrintStrEntity mulPrintStrEntity = new MulPrintStrEntity("=====================", fontSize);
            list.add(mulPrintStrEntity);
            list.add(new MulPrintStrEntity("MERCHANT NAME：Demo shop name", fontSize));
            list.add(new MulPrintStrEntity("MERCHANT NO.：20321545656687", fontSize));
            list.add(new MulPrintStrEntity("TERMINAL NO.：25689753", fontSize));
            list.add(new MulPrintStrEntity("CARD NUMBER", fontSize));
            list.add(new MulPrintStrEntity("62179390*****3426", fontSize));
            list.add(new MulPrintStrEntity("TRANS TYPE", fontSize));
            list.add(new MulPrintStrEntity("SALE", fontSize));
            list.add(new MulPrintStrEntity("EXP DATE：2029", fontSize));
            list.add(new MulPrintStrEntity("BATCH NO：000012", fontSize));
            list.add(new MulPrintStrEntity("VOUCHER NO：000001", fontSize));
            list.add(new MulPrintStrEntity("DATE/TIME：2016-05-23 16:50:32", fontSize));
            list.add(new MulPrintStrEntity("AMOUNT", fontSize));
            list.add(new MulPrintStrEntity("==========================", fontSize));
            //feed pager one line
            list.add(new MulPrintStrEntity("\n", fontSize));
            list.add(new MulPrintStrEntity("CARD HOLDER SIGNATURE", fontSize));
            list.add(new MulPrintStrEntity("\n", fontSize));
            list.add(new MulPrintStrEntity("--------------------------------------", fontSize));
            list.add(new MulPrintStrEntity(" I ACKNOWLEDGE	SATISFACTORY RECEIPT OF RELATIVE GOODS/SERVICES", fontSize));
            list.add(new MulPrintStrEntity(" MERCHANT COPY ", fontSize));
            list.add(new MulPrintStrEntity("---X---X---X---X---X--X--X--X--X--X--\n", fontSize));
            printer.printStr(list, new OnPrintListener.Stub() {
                @Override
                public void onPrintResult(int result) throws RemoteException {
                    Log.d(TAG, "onPrintResult = " + result);
                    listener.showMessage(result == ServiceResult.Success ? context.getString(R.string.msg_succ) : context.getString(R.string.msg_fail));
                }
            }, config);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public static void PrintImage(DeviceServiceEngine engine, final Context context, final MainActivity.AlertDialogOnShowListener listener) throws RemoteException {
        MultipleAppPrinter printer = engine.getMultipleAppPrinter();
        Bitmap bitmap = getImageFromAssetsFile(context, "china_union_pay.bmp.");
        Bundle config = new Bundle();
        config.putInt(PrinterConfig.COMMON_GRAYLEVEL, 30);
        printer.printImage(bitmap, new OnPrintListener.Stub() {
            @Override
            public void onPrintResult(int result) throws RemoteException {
                Log.d(TAG, "onPrintResult = " + result);
                listener.showMessage(result == ServiceResult.Success ? context.getString(R.string.msg_succ) : context.getString(R.string.msg_fail));
            }
        }, config);
    }

    private static Bitmap getImageFromAssetsFile(Context context, String fileName) {
        Bitmap image = null;
        AssetManager am = context.getResources().getAssets();
        try {
            InputStream is = am.open(fileName);
            image = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }
}
