package com.morefun.ysdk.sample.activity;

import android.os.Bundle;
import android.os.RemoteException;
import android.widget.Button;
import android.widget.CheckBox;

import com.morefun.ysdk.sample.R;
import com.morefun.ysdk.sample.device.DeviceHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LedActivity extends BaseActivity {
    private final String TAG = LedActivity.class.getName();

    @BindView(R.id.button)
    Button button;

    @BindView(R.id.led1)
    CheckBox led1;

    @BindView(R.id.led2)
    CheckBox led2;

    @BindView(R.id.led3)
    CheckBox led3;

    @BindView(R.id.led4)
    CheckBox led4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_led);
        ButterKnife.bind(this);

        setButtonName();
    }

    @OnClick({R.id.button})
    public void onClick() {
        setLed();
    }

    private void setLed() {
        try {
            DeviceHelper.getLedDriver().PowerLed(led1.isChecked(), led2.isChecked(), led3.isChecked(), led4.isChecked());
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

    protected void setButtonName() {
        button.setText(getString(R.string.menu_led));
    }

}