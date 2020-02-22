package com.morefun.ysdk.sample.activity;

import android.os.Bundle;
import android.os.RemoteException;
import android.os.SystemClock;
import android.view.View;
import android.widget.TextView;

import com.morefun.yapi.ServiceResult;
import com.morefun.yapi.emv.EmvAidPara;
import com.morefun.ysdk.sample.R;
import com.morefun.ysdk.sample.device.DeviceHelper;
import com.morefun.ysdk.sample.utils.HexUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class AidManagerActivity extends BaseActivity {
    private final String TAG = AidManagerActivity.class.getName();

    @BindView(R.id.textView)
    TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aid);
        ButterKnife.bind(this);

    }

    @OnClick({R.id.downloadAid, R.id.clearAid, R.id.getAidList})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.downloadAid:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        downloadAID();
                    }
                }).start();
                break;
            case R.id.clearAid:
                clearAID();
                break;
            case R.id.getAidList:
                getAIDList();
                break;
        }
    }

    private void downloadAID() {
        String[] aidList = new String[]{
                "9F0608A000000524010101DF0101009F08020030DF11050000000000DF12050000000000DF130500000000009F1B04000186A0DF150400000000DF160100DF170100DF14039F3704DF1801319F7B06000000010000DF1906000000010000DF2006000000050000DF2106000000004000",
                "9F0607A0000003241010DF0101009F08020001DF11050000000000DF12050000000000DF130500000000009F1B0400001388DF150400000000DF160150DF170120DF14039F3704DF1801319F7B06000000200000DF1906000000200000DF2006000002000000DF2106000000100000",
                "9F0607A00000052410109F08020002DF11050000000000DF12050000000000DF130500000000009F1B04000186A05F2A0203565F360102DF1801319F7B06000000010000DF1906000000010000DF2006000000050000DF170100DF160105DF1504000001F4",
                "9F0607A00000052410119F08020002DF11050000000000DF12050000000000DF130500000000009F1B04000186A05F2A0203565F360102DF1801319F7B06000000010000DF1906000000010000DF2006000000050000DF170100DF160105DF1504000001F4",
                //maestro -> 9F1D
                "9F0607A0000000041010DF0101009F08020002DF11050000000000DF12050000000000DF130540000000009F1B0400004E20DF150400000000DF160150DF1701209f1d086C00000000000000DF2106000000200000DF2006000000050000",
                "9F0607A0000000043060DF0101009F08020002DF11050000000000DF12050000000000DF130540000000009F1B0400004E20DF150400000000DF160150DF1701209f1d084C00000000000000DF2106000000200000DF2006000000050000"
        };
        int ret = -1;
        try {
            for (int i = 0; i < aidList.length; i++) {
                String tip = "Download aid" + String.format("(%d)", i);

                showResult(textView, tip);
                String aid = aidList[i];
                ret = DeviceHelper.getEmvHandler().addAidParam(HexUtil.hexStringToByte(aid));

                if (ret != ServiceResult.Success) {
                    break;
                }
                SystemClock.sleep(500);
            }
            showResult(textView, "Download aid success!");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void clearAID() {
        try {
            DeviceHelper.getEmvHandler().clearAIDParam();
            showResult(textView, "Clear aid success!");
        } catch (RemoteException e) {

        }

    }

    private void getAIDList() {
        try {
            List<EmvAidPara> aidList = DeviceHelper.getEmvHandler().getAidParaList();
            StringBuilder builder = new StringBuilder();

            for (int i = 0; i < aidList.size(); i++) {
                EmvAidPara emvAidPara = aidList.get(i);
                builder.append("\n AID:\n" + HexUtil.bytesToHexString(emvAidPara.getAID()).substring(0,16));
                builder.append("\n TermAppVer(9F09):" + HexUtil.bytesToHexString(emvAidPara.getTermAppVer()));
                builder.append("\n TFL_Domestic(9F1B):" + HexUtil.bytesToHexString(emvAidPara.getTFL_Domestic()));
                builder.append("\n TAC_Default(DF11):" + HexUtil.bytesToHexString(emvAidPara.getTAC_Default()));
                builder.append("\n TAC_Online(DF12):" + HexUtil.bytesToHexString(emvAidPara.getTAC_Online()));
                builder.append("\n TAC_Denial(DF13):" + HexUtil.bytesToHexString(emvAidPara.getTAC_Denial()));
                builder.append("\n RFOfflineLimit(DF19):" + HexUtil.bytesToHexString(emvAidPara.getRFOfflineLimit()));
                builder.append("\n RFTransLimit(DF20):" + HexUtil.bytesToHexString(emvAidPara.getRFTransLimit()));
                builder.append("\n RFCVMLimit(DF21):" + HexUtil.bytesToHexString(emvAidPara.getRFCVMLimit()));
                builder.append("\n EC_TFL(9F7B):" + HexUtil.bytesToHexString(emvAidPara.getEC_TFL()));
            }

            showResult(textView, builder.toString());

        } catch (RemoteException e) {

        } catch (NullPointerException e) {
            showResult(textView, e.getMessage());
        }

    }

    protected void setButtonName() {

    }


}