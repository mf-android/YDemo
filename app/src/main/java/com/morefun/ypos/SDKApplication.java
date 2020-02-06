package com.morefun.ypos;

import android.app.Application;

public class SDKApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SDKManager.getInstance().bindService(getApplicationContext());
    }
}
