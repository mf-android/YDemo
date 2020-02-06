package com.morefun.ypos.apitest;

import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;

import com.morefun.yapi.ServiceResult;
import com.morefun.yapi.device.scanner.InnerScanner;
import com.morefun.yapi.device.scanner.OnScannedListener;
import com.morefun.yapi.device.scanner.ScannerConfig;
import com.morefun.yapi.engine.DeviceServiceEngine;
import com.morefun.ypos.BaseApiTest;
import com.morefun.ypos.MainActivity;
import com.morefun.ypos.R;
import com.morefun.ypos.uitls.ActionItems;

public class ScannerApi extends BaseApiTest {

    public static void ScannerApi(DeviceServiceEngine mSDKManager, final MainActivity.AlertDialogOnShowListener listener) {
        try {
            final InnerScanner innerScanner = mSDKManager.getInnerScanner();
            Bundle bundle = new Bundle();
            bundle.putInt(ScannerConfig.COMM_SCANNER_TYPE, 1);
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                listener.showProgress(getString(R.string.msg_running), new ActionItems.OnCancelCall() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        try {
                            innerScanner.stopScan();
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
            innerScanner.initScanner(bundle);
            innerScanner.startScan(3, new OnScannedListener.Stub() {
                @Override
                public void onScanResult(final int retCode, final byte[] scanResult) throws RemoteException {
                    Log.d("ScannerApi","scanResult = " + new String(scanResult));
                    listener.showMessage((retCode == ServiceResult.Success) ?
                            "scanResult = " + new String(scanResult)
                            : "Scan Fail " + retCode);
                }
            });
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
