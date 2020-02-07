package com.morefun.ypos.apitest;

import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.morefun.yapi.engine.DeviceServiceEngine;
import com.morefun.yapi.engine.OnUninstallAppListener;
import com.morefun.ypos.MainActivity;
import com.morefun.ypos.uitls.FileUtils;
import com.morefun.ypos.uitls.Utils;

public class InstallApkTest {

    private static final String TAG = "InstallApkTest";

    public static void InstallApp() {
        String apkName = "test_install.apk";
        String path = FileUtils.getExternalCacheDir(MainActivity.getContext(), apkName);
        Log.d(TAG, "path = " + path);
        if (TextUtils.isEmpty(path)) {
            Toast.makeText(MainActivity.getContext(), "File path is empty", Toast.LENGTH_SHORT).show();
            return;
        }
//        Utils.copyAsstes(MainActivity.getContext(), apkName, path);
//        SilentInstallUtils.installApp(this, new File(path));
    }

    public static void InstallApp(DeviceServiceEngine mSDKManager) {
        String apkName = "test_install.apk";
        String path = FileUtils.getExternalCacheDir(MainActivity.getContext(), apkName);
        Log.d(TAG, "path = " + path);
        if (TextUtils.isEmpty(path)) {
            Toast.makeText(MainActivity.getContext(), "File path is empty", Toast.LENGTH_SHORT).show();
            return;
        }
        Utils.copyAsstes(MainActivity.getContext(), apkName, path);
        try {
            mSDKManager.installApp(path, "", "com.morefun.test.install");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public static void uninstallApp(DeviceServiceEngine mSDKManager) {
        try {
            String packageName = "com.morefun.test.install";
//            String packageName = "com.android.nfc";
            mSDKManager.uninstallApp(packageName, new OnUninstallAppListener.Stub() {
                @Override
                public void onUninstallAppResult(int retCode) throws RemoteException {
                    Log.d(TAG, "retCode = " + retCode);
                }
            });
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
