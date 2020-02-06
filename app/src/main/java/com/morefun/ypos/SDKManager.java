package com.morefun.ypos;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.SystemClock;
import android.util.Log;

import com.morefun.yapi.engine.DeviceServiceEngine;
import com.morefun.ypos.config.DukptConfigs;


public class SDKManager {
    DeviceServiceEngine mDeviceServiceEngine;
    static SDKManager mZebraManager = new SDKManager();
    private static final String TAG = "SDKManager";

    private SDKManager() {

    }

    public static SDKManager getInstance() {
        return mZebraManager;
    }

    public DeviceServiceEngine getDeviceServiceEngine()throws NullPointerException {
        int count = 0;
        while (mDeviceServiceEngine == null) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            count++;
            if (count > 20) {
                break;
            }
        }
        Bundle bundle = new Bundle();
        if (mDeviceServiceEngine != null){
            //TODO if need dukpt ,Please set 09000000.(DukptConfigs.getDukptBussinessId())
            try {
                int ret = mDeviceServiceEngine.login(bundle, DukptConfigs.getDukptBussinessId());
                Log.d(TAG, "auto login result = " + ret);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return mDeviceServiceEngine;
    }

    ServiceConnection mServiceConnection;
    volatile boolean isConnected;

    public void bindService(final Context context) {
        final Intent intent = new Intent();
        intent.setAction("com.morefun.ysdk.service");
        intent.setPackage("com.morefun.ysdk");
        final long mTime = System.currentTimeMillis();
        Log.d(TAG, "===========================");
        mServiceConnection = new ServiceConnection() {

            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                mDeviceServiceEngine = DeviceServiceEngine.Stub.asInterface(service);
                isConnected = true;
                Log.d(TAG, (System.currentTimeMillis() - mTime) + "ms ==============SDKManager=============" + mDeviceServiceEngine);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                Log.e(TAG, " ==============onServiceDisconnected=============");
                isConnected = false;
                tryConnectAgain(context);
            }
        };
        context.bindService(intent, mServiceConnection, Service.BIND_AUTO_CREATE);
    }

    public void unBindService(Context context) {
        context.unbindService(mServiceConnection);
        mServiceConnection = null;
    }

    private void tryConnectAgain(final Context context) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!isConnected) {
                    Log.d(TAG, "ms ==============mBtService=============");
                    SystemClock.sleep(4_000);
                    bindService(context);
                }
            }
        }).start();

    }
}
