package com.morefun.ypos.apitest;

import android.content.DialogInterface;
import android.os.RemoteException;
import android.os.SystemClock;
import android.util.Log;

import com.morefun.yapi.ServiceResult;
import com.morefun.yapi.device.serialport.SerialPortDriver;
import com.morefun.yapi.engine.DeviceServiceEngine;
import com.morefun.ypos.MainActivity;
import com.morefun.ypos.uitls.ActionItems;
import com.morefun.ypos.uitls.Utils;

public class SerialPortTest {
    private static final String TAG = "SerialPortTest";

    /**
     * Before the call, you must first install the serial port driver
     * on the computer Window system.
     * COM & LPT: Qualcomm HS-USB Android GPS 9020
     */
    public static void SerialPort(final DeviceServiceEngine mSDKManager, final MainActivity.AlertDialogOnShowListener alertDialogOnShowListener) {
        //call before must install driver in PC.
        //Qualcomm HS-USB Android GPS 9020

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final SerialPortDriver serialPortDriver = mSDKManager.getSerialPortDriver(4);
                    int connect = serialPortDriver.connect("115200,N,8,1");
                    Log.d(TAG, "SerialPortDriver connect reuslt = " + (connect == ServiceResult.Success));
                    alertDialogOnShowListener.showProgress("start", new ActionItems.OnCancelCall() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            alertDialogOnShowListener.dismissProgress();
                            try {
                                serialPortDriver.disconnect();
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    if (connect == ServiceResult.Success) {
                        int count = 10;
                        //test wait 1 second
                        SystemClock.sleep(1_000);
                        byte[] sendData = ("test Mag" + count).getBytes();
                        int send = serialPortDriver.send(sendData, sendData.length);
                        Log.d(TAG, "SerialPortDriver send reuslt = " + (send == ServiceResult.Success));
                        // read
                        while (count > 0) {
                            count--;
                            byte[] recvBytes = new byte[1024];
                            int read = serialPortDriver.recv(recvBytes, recvBytes.length, 10_000);
                            Log.d(TAG, "SerialPortDriver read reuslt = " + (read > ServiceResult.Success));
                            if (read > 0) {
                                Log.d(TAG, "SerialPortDriver recv reuslt = " + Utils.pubByteToHexString(Utils.getByteArray(recvBytes, 0, read)));
                                sendData = (" recv msg success" + count).getBytes();
                                serialPortDriver.send(sendData, sendData.length);
                            }
                            SystemClock.sleep(1_000);
                        }
                    }
                    alertDialogOnShowListener.dismissProgress();
                    serialPortDriver.disconnect();

                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
