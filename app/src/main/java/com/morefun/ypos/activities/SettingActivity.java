package com.morefun.ypos.activities;

import android.Manifest;
import android.os.Bundle;
import android.os.RemoteException;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.support.annotation.Nullable;
import android.util.Log;

import com.morefun.yapi.device.logrecorder.LogRecorder;
import com.morefun.ypos.R;
import com.morefun.ypos.SDKManager;
import com.morefun.ypos.uitls.PermissionUtils;

public class SettingActivity extends PreferenceActivity {

    private static final String TAG = "SettingActivity";
    boolean mCheck = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref_set);
        try {
            setListener();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    LogRecorder mLogRecorder;

    private void setListener() throws RemoteException {
        CheckBoxPreference mCheckbox0 = (CheckBoxPreference) findPreference("logcat_set");
        SDKManager instance = SDKManager.getInstance();
        mLogRecorder = instance.getDeviceServiceEngine().getLogRecorder();
        mCheckbox0.setChecked(mLogRecorder.getStatus() == 0);
        mCheckbox0.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {

            @Override
            public boolean onPreferenceChange(Preference arg0, Object newValue) {
                boolean check = (Boolean) newValue;
                mCheck = check;
                Log.d(TAG, "check = " + check);
                int clickId = 2;
                String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
                if (PermissionUtils.checkPermissions(SettingActivity.this, permissions, clickId)) {
                    processClickEvent(check);
                }
                return true;
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (PermissionUtils.checkPermissionResult(permissions, grantResults)) {
            processClickEvent(mCheck);
        }
    }

    private void processClickEvent(boolean check) {
        try {
            if (check) {
                if (mLogRecorder != null) {
                    mLogRecorder.openRecorder(null);
                }
            } else {
                if (mLogRecorder != null) {
                    mLogRecorder.closeRecorder(null);
                }
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

}
