package com.morefun.ysdk.sample.activity;

import android.os.Bundle;
import android.os.RemoteException;
import android.view.View;
import android.widget.TextView;

import com.morefun.yapi.device.pinpad.DesAlgorithmType;
import com.morefun.yapi.device.pinpad.DesMode;
import com.morefun.yapi.device.pinpad.DukptCalcObj;
import com.morefun.yapi.device.pinpad.DukptKeyGid;
import com.morefun.yapi.device.pinpad.DukptKeyType;
import com.morefun.yapi.device.pinpad.DukptLoadObj;
import com.morefun.yapi.device.pinpad.MacAlgorithmType;
import com.morefun.yapi.device.pinpad.OnPinPadInputListener;
import com.morefun.yapi.device.pinpad.PinAlgorithmMode;
import com.morefun.ysdk.sample.R;
import com.morefun.ysdk.sample.device.DeviceHelper;
import com.morefun.ysdk.sample.utils.HexUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class PinpadActivity extends BaseActivity {

    @BindView(R.id.textView)
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pinpad);
        ButterKnife.bind(this);

        setButtonName();

    }

    @OnClick({R.id.inputPin, R.id.dukptInit, R.id.dukptDeccrypt, R.id.dukptEncrypt, R.id.calcMac, R.id.increaseKsn, R.id.getKsn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.inputPin:
                inputPin();
                break;
            case R.id.increaseKsn:
                increaseKsn(true);
                break;
            case R.id.getKsn:
                increaseKsn(false);
                break;
            case R.id.dukptInit:
                dukptInit();
                break;
            case R.id.dukptEncrypt:
                dukptEncrypt();
                break;
            case R.id.dukptDeccrypt:
                dukptDecrypt();
                break;
            case R.id.calcMac:
                calcMac();
                break;
        }
    }

    private void increaseKsn(boolean isIncrease) {
        try {
            String ksn = DeviceHelper.getPinpad().increaseKsn(isIncrease);
            showResult(textView, "ksn:" + ksn);
        } catch (RemoteException e) {

        }

    }

    private void calcMac() {
        try {
            String hexData = "00000000005010016222620910029130840241205100100367FD3414057DB801BE18A309A544C5174CC777525974CBD467BCC56EA16629F3B016488A6C314921485C75F57066D4682FEDC1F910C5C8136A201279B590898B40D7098461D345168810CCFEBC61204B3E6F364A95175EF54C7EBAAEC2A6AEE44D9783747124D313B78A3F754C5ECC611533C4957377DD2067DF927C80461C4E4C20A8A4CC57EF1CCE2BC1AEEA442431256F66A25AB855912BA82FB8AD308F0EDE358CDDDEA63C95401B8335C8689E5735E0FB96733426FD71A7248E140A95CB4B4313AC0DBDA1E70EA8800000000000";
            int keyId = 0;
            byte[] data = HexUtil.hexStringToByte(hexData);
            byte[] macBlock = DeviceHelper.getPinpad().getMac(keyId, MacAlgorithmType.ECB, DesAlgorithmType.TDES, data, new Bundle());

            showResult(textView, "macBlock:" + HexUtil.bytesToHexString(macBlock));
        } catch (RemoteException e) {

        }
    }

    private void dukptDecrypt() {
        try {
            String data = "494B83B9C30A7EB1";

            DukptCalcObj.DukptAlg alg = DukptCalcObj.DukptAlg.DUKPT_ALG_CBC;
            DukptCalcObj.DukptOper oper = DukptCalcObj.DukptOper.DUKPT_DECRYPT;
            DukptCalcObj.DukptType type = DukptCalcObj.DukptType.DUKPT_DES_KEY_DATA1;

            DukptCalcObj dukptCalcObj = new DukptCalcObj(type, oper, alg, data);

            Bundle bundle = DeviceHelper.getPinpad().dukptCalcDes(dukptCalcObj);

            showResult(textView, "dukpt decrypt:" + bundle.getString(DukptCalcObj.DUKPT_DATA));
            showResult(textView, "ksn:" + bundle.getString(DukptCalcObj.DUKPT_KSN));

        } catch (RemoteException e) {

        }
    }

    private void dukptEncrypt() {
        try {
            String data = "12345678ABCDEFGH";

            DukptCalcObj.DukptAlg alg = DukptCalcObj.DukptAlg.DUKPT_ALG_CBC;
            DukptCalcObj.DukptOper oper = DukptCalcObj.DukptOper.DUKPT_ENCRYPT;
            DukptCalcObj.DukptType type = DukptCalcObj.DukptType.DUKPT_DES_KEY_DATA1;

            DukptCalcObj dukptCalcObj = new DukptCalcObj(type, oper, alg, data);

            Bundle bundle = DeviceHelper.getPinpad().dukptCalcDes(dukptCalcObj);

            showResult(textView, "dukpt encrypt:" + bundle.getString(DukptCalcObj.DUKPT_DATA));
            showResult(textView, "ksn:" + bundle.getString(DukptCalcObj.DUKPT_KSN));

        } catch (RemoteException e) {

        }
    }

    private void dukptInit() {
        try {
            String key = "C1D0F8FB4958670DBA40AB1F3752EF0D";
            String ksn = "FFFF9876543210" + "000000";

            DukptLoadObj dukptLoadObj = new DukptLoadObj(key, ksn, DukptLoadObj.DukptKeyType.DUKPT_BDK_PLAINTEXT, DukptLoadObj.DukptKeyIndex.KEY_INDEX_0);

            int ret = DeviceHelper.getPinpad().dukptLoad(dukptLoadObj);
            showResult(textView, "Dukpt init:" + ret);

        } catch (RemoteException e) {
            showResult(textView, e.getMessage());
        }

    }

    private void inputPin() {

        byte[] panBlock = "1234567890123456".getBytes();
        Bundle bundle = new Bundle();

        try {
            DeviceHelper.getPinpad().inputOnlinePin(bundle, panBlock, 0, PinAlgorithmMode.ISO9564FMT1, new OnPinPadInputListener.Stub() {
                @Override
                public void onInputResult(int ret, byte[] pinBlock, String ksn) throws RemoteException {
                    StringBuilder builder = new StringBuilder();

                    builder.append("onInputResult:" + ret);
                    builder.append("\npinBlock:" + HexUtil.bytesToHexString(pinBlock));
                    builder.append("\nksn:" + ksn);

                    showResult(textView, builder.toString());
                }

                @Override
                public void onSendKey(byte keyCode) throws RemoteException {

                }

            });
        } catch (RemoteException e) {

        }

    }

    protected void setButtonName() {

    }


}