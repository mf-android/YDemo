package com.morefun.ysdk.sample.activity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.RemoteException;
import android.widget.Button;
import android.widget.TextView;

import com.morefun.ysdk.sample.R;
import com.morefun.ysdk.sample.device.DeviceHelper;
import com.morefun.ysdk.sample.utils.SweetDialogUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {

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
        if (checkAppInstalled("com.morefun.ysdk")) {
            login("09000000");
        } else {
            SweetDialogUtil.showError(LoginActivity.this, "Please install ysdk first");
        }

    }

    //TODO if need able dukpt, Please set login with 09000000
    private void login(String bussinessId) {
        Bundle bundle = new Bundle();
        try {
            int ret = DeviceHelper.getDeviceService().login(bundle, bussinessId);
            if (ret == 0) {
                DeviceHelper.setLoginFlag(true);
                SweetDialogUtil.showSuccess(LoginActivity.this, getString(R.string.login_devices) + getString(R.string.success));
                return;
            }
            DeviceHelper.setLoginFlag(false);
            SweetDialogUtil.showError(LoginActivity.this, getString(R.string.login_devices) + getString(R.string.fail));
            return;

        } catch (RemoteException e) {
            textView.setText(e.toString());
        } catch (NullPointerException e) {
            showResult(textView, "Please restart the application");
        }
    }

    protected void setButtonName() {
        button.setText(getString(R.string.login_devices));
    }

    private boolean checkAppInstalled(String pkgName) {
        if (pkgName == null || pkgName.isEmpty()) {
            return false;
        }
        PackageInfo packageInfo;
        try {
            packageInfo = getPackageManager().getPackageInfo(pkgName, 0);
            return packageInfo != null;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }
}