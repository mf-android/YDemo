package com.morefun.ypos.apitest;

import android.os.RemoteException;

import com.morefun.yapi.device.beeper.BeepModeConstrants;
import com.morefun.yapi.engine.DeviceServiceEngine;

public class BeepLEDTest {

    public static void setLed(DeviceServiceEngine mSDKManager, boolean isLightLed) {
        try {
            //PowerLed(boolean blue, boolean yellow, boolean green, boolean red)
            mSDKManager.getLEDDriver().PowerLed(isLightLed, isLightLed, isLightLed, isLightLed);

//            mSDKManager.getLEDDriver().setLed(LEDLightConstrants.BLUE, isLightLed);
//            mSDKManager.getLEDDriver().setLed(LEDLightConstrants.YELLOW, isLightLed);
//            mSDKManager.getLEDDriver().setLed(LEDLightConstrants.GREEN, isLightLed);
//            mSDKManager.getLEDDriver().setLed(LEDLightConstrants.RED, isLightLed);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public static void beep(DeviceServiceEngine mSDKManager) throws RemoteException {
        mSDKManager.getBeeper().beep(BeepModeConstrants.NORMAL);
    }
}
