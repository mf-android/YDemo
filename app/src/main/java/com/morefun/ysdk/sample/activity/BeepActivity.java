package com.morefun.ysdk.sample.activity;

import android.os.Bundle;
import android.os.RemoteException;
import android.widget.RadioButton;

import com.morefun.ysdk.sample.R;
import com.morefun.ysdk.sample.device.DeviceHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BeepActivity extends BaseActivity {

    @BindView(R.id.normal)
    RadioButton normal;

    @BindView(R.id.success)
    RadioButton success;

    @BindView(R.id.fail)
    RadioButton fail;

    @BindView(R.id.interval)
    RadioButton interval;

    @BindView(R.id.error)
    RadioButton error;

    private final String TAG = BeepActivity.class.getName();
    public static final int NORMAL = 0;
    public static final int SUCCESS = 1;
    public static final int FAIL = 2;
    public static final int INTERVAL = 3;
    public static final int ERROR = 4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beep);
        ButterKnife.bind(this);

        setButtonName();
    }

    @OnClick({R.id.button})
    public void onClick() {
        setLed();
    }

    private void setLed() {
        int beepType;
        try {
            if (normal.isChecked()) {
                beepType = NORMAL;
            } else if (success.isChecked()) {
                beepType = SUCCESS;
            } else if (fail.isChecked()) {
                beepType = FAIL;
            } else if (interval.isChecked()) {
                beepType = INTERVAL;
            } else {
                beepType = ERROR;
            }

            DeviceHelper.getBeeper().beep(beepType);

        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

    protected void setButtonName() {

    }

}