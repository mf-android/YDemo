package com.morefun.ypos.apitest;

import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;

import com.morefun.yapi.engine.DeviceInfoConstrants;
import com.morefun.yapi.engine.DeviceServiceEngine;
import com.morefun.ypos.BaseApiTest;
import com.morefun.ypos.MainActivity;
import com.morefun.ypos.R;

public class DevicesInfoTest extends BaseApiTest {
    private static final String TAG = "DevicesInfoTest";

    public static void getDeviceInfo(DeviceServiceEngine mSDKManager, MainActivity.AlertDialogOnShowListener alertDialogOnShowListener) throws RemoteException {
        Bundle devInfo = mSDKManager.getDevInfo();
        String vendor = devInfo.getString(DeviceInfoConstrants.COMMOM_VENDOR);
        String model = devInfo.getString(DeviceInfoConstrants.COMMOM_MODEL);
        String os_ver = devInfo.getString(DeviceInfoConstrants.COMMOM_OS_VER);
        String sn = devInfo.getString(DeviceInfoConstrants.COMMOM_SN);
        String tusn = devInfo.getString(DeviceInfoConstrants.TID_SN);
        String versionCode = devInfo.getString(DeviceInfoConstrants.COMMON_SERVICE_VER);
        String hardware = devInfo.getString("hardware");
        StringBuilder builder = new StringBuilder();
        builder.append(getString(R.string.vendorInfo) + vendor + "\n");
        builder.append(getString(R.string.model) + model + "\n");
        builder.append(getString(R.string.systemVer) + os_ver + "\n");
        builder.append(getString(R.string.serialNo) + sn + "\n");
        builder.append(getString(R.string.tusn) + tusn + "\n");
        builder.append(getString(R.string.basechip) + hardware + "\n");
        builder.append("VersionCode:" + versionCode + "\n");
        Log.d(TAG, " " + builder.toString());
        alertDialogOnShowListener.showMessage(builder.toString());
    }

    public static void setSystemTime(DeviceServiceEngine mSDKManager) {
        //datetime - (YYYYMMDDHHMMSS)
        try {
            mSDKManager.setSystemClock("20190730113422");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
