package com.morefun.ysdk.sample.activity;

import android.os.Bundle;
import android.os.RemoteException;
import android.widget.Button;
import android.widget.TextView;

import com.morefun.yapi.device.scanner.InnerScanner;
import com.morefun.yapi.device.scanner.OnScannedListener;
import com.morefun.yapi.device.scanner.ScannerConfig;
import com.morefun.ysdk.sample.R;
import com.morefun.ysdk.sample.device.DeviceHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ScannerActivity extends BaseActivity {

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
        scanner();
    }

    private void scanner() {
        try {
            final InnerScanner innerScanner = DeviceHelper.getInnerScanner();

            Bundle bundle = new Bundle();
            bundle.putInt(ScannerConfig.COMM_SCANNER_TYPE, 1);

            innerScanner.initScanner(bundle);
            innerScanner.startScan(3, new OnScannedListener.Stub() {
                @Override
                public void onScanResult(final int retCode, final byte[] scanResult) throws RemoteException {
                    showResult(textView, "retCode:" + retCode + "\n" + "result:" + new String(scanResult));
                }
            });
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

    protected void setButtonName() {
        button.setText(getString(R.string.menu_scanner));
    }


}