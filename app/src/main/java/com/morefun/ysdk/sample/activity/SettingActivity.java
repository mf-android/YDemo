package com.morefun.ysdk.sample.activity;

import android.Manifest;
import android.os.Bundle;
import android.os.RemoteException;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.util.Log;

import androidx.annotation.Nullable;

import com.morefun.yapi.device.logrecorder.LogRecorder;
import com.morefun.ysdk.sample.R;
import com.morefun.ysdk.sample.device.DeviceHelper;
import com.morefun.ysdk.sample.utils.PermissionUtils;

public class SettingActivity extends PreferenceActivity {

    private static final String TAG = SettingActivity.class.getName();
    private LogRecorder mLogRecorder;
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


    private void setListener() throws RemoteException {
        CheckBoxPreference mCheckbox0 = (CheckBoxPreference) findPreference("logcat_set");
        mLogRecorder = DeviceHelper.getLogRecorder();

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
