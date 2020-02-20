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
                        downloadAid();
                    }
                }).start();
                break;
            case R.id.clearAid:
                clearAid();
                break;
            case R.id.getAidList:
                getAidList();
                break;
        }
    }

    private void downloadAid() {
        String[] aidList = new String[]{
                "9F0608A000000524010101DF0101009F08020030DF11050000000000DF12050000000000DF130500000000009F1B04000186A0DF150400000000DF160100DF170100DF14039F3704DF1801319F7B06000000010000DF1906000000010000DF2006000000050000DF2106000000004000",
                "9F0607A0000003241010DF0101009F08020001DF11050000000000DF12050000000000DF130500000000009F1B0400001388DF150400000000DF160150DF170120DF14039F3704DF1801319F7B06000000200000DF1906000000200000DF2006000002000000DF2106000000100000",
                "9F0607A00000052410109F08020002DF11050000000000DF12050000000000DF130500000000009F1B04000186A05F2A0203565F360102DF1801319F7B06000000010000DF1906000000010000DF2006000000050000DF170100DF160105DF1504000001F4",
                "9F0607A00000052410119F08020002DF11050000000000DF12050000000000DF130500000000009F1B04000186A05F2A0203565F360102DF1801319F7B06000000010000DF1906000000010000DF2006000000050000DF170100DF160105DF1504000001F4",

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

    private void clearAid() {
        try {
            DeviceHelper.getEmvHandler().clearAIDParam();
            showResult(textView, "Clear aid success!");
        } catch (RemoteException e) {

        }

    }

    private void getAidList() {
        try {
            List<EmvAidPara> aidList = DeviceHelper.getEmvHandler().getAidParaList();
            StringBuilder builder = new StringBuilder();

            for (int i = 0; i < aidList.size(); i++) {
                builder.append("\n" + HexUtil.bytesToHexString(aidList.get(i).getAID()));
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