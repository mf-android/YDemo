package com.morefun.ysdk.sample.activity;

import android.os.Bundle;
import android.os.RemoteException;
import android.widget.Button;
import android.widget.TextView;

import com.morefun.yapi.engine.DeviceInfoConstrants;
import com.morefun.ysdk.sample.R;
import com.morefun.ysdk.sample.device.DeviceHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DeviceInfoActivity extends BaseActivity {

    @BindView(R.id.textView)
    TextView textView;

    @BindView(R.id.button)
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_with_button);
        ButterKnife.bind(this);

        setButtonName();

    }

    @OnClick({R.id.button})
    public void onClick() {
        getDeviceInfo();
    }

    private void getDeviceInfo() {
        try {
            Bundle devInfo = DeviceHelper.getDeviceService().getDevInfo();
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

            showResult(textView, builder.toString());

        } catch (RemoteException e) {

        }

    }

    protected void setButtonName() {
        button.setText(getString(R.string.device_info));
    }


}