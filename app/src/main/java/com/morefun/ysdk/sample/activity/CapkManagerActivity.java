package com.morefun.ysdk.sample.activity;

import android.os.Bundle;
import android.os.RemoteException;
import android.os.SystemClock;
import android.view.View;
import android.widget.TextView;

import com.morefun.yapi.ServiceResult;
import com.morefun.yapi.emv.EmvCapk;
import com.morefun.ysdk.sample.R;
import com.morefun.ysdk.sample.device.DeviceHelper;
import com.morefun.ysdk.sample.utils.HexUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class CapkManagerActivity extends BaseActivity {
    private final String TAG = CapkManagerActivity.class.getName();

    @BindView(R.id.textView)
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capk);
        ButterKnife.bind(this);

    }

    @OnClick({R.id.downloadPuk, R.id.clearPuk, R.id.getPukList})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.downloadPuk:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        downloadCapk();
                    }
                }).start();
                break;
            case R.id.clearPuk:
                clearCapk();
                break;
            case R.id.getPukList:
                getCapkList();
                break;
        }
    }

    private void downloadCapk() {
        String[] pukList = new String[]{
                "9F0605A0000003339F220102DF050420211231DF060101DF070101DF028190A3767ABD1B6AA69D7F3FBF28C092DE9ED1E658BA5F0909AF7A1CCD907373B7210FDEB16287BA8E78E1529F443976FD27F991EC67D95E5F4E96B127CAB2396A94D6E45CDA44CA4C4867570D6B07542F8D4BF9FF97975DB9891515E66F525D2B3CBEB6D662BFB6C3F338E93B02142BFC44173A3764C56AADD202075B26DC2F9F7D7AE74BD7D00FD05EE430032663D27A57DF040103DF031403BB335A8549A03B87AB089D006F60852E4B8060",
                "9F0605A0000003339F220103DF050420221231DF060101DF070101DF0281B0B0627DEE87864F9C18C13B9A1F025448BF13C58380C91F4CEBA9F9BCB214FF8414E9B59D6ABA10F941C7331768F47B2127907D857FA39AAF8CE02045DD01619D689EE731C551159BE7EB2D51A372FF56B556E5CB2FDE36E23073A44CA215D6C26CA68847B388E39520E0026E62294B557D6470440CA0AEFC9438C923AEC9B2098D6D3A1AF5E8B1DE36F4B53040109D89B77CAFAF70C26C601ABDF59EEC0FDC8A99089140CD2E817E335175B03B7AA33DDF040103DF031487F0CD7C0E86F38F89A66F8C47071A8B88586F26",
                "9F0605A0000003339F220104DF050420221231DF060101DF070101DF0281F8BC853E6B5365E89E7EE9317C94B02D0ABB0DBD91C05A224A2554AA29ED9FCB9D86EB9CCBB322A57811F86188AAC7351C72BD9EF196C5A01ACEF7A4EB0D2AD63D9E6AC2E7836547CB1595C68BCBAFD0F6728760F3A7CA7B97301B7E0220184EFC4F653008D93CE098C0D93B45201096D1ADFF4CF1F9FC02AF759DA27CD6DFD6D789B099F16F378B6100334E63F3D35F3251A5EC78693731F5233519CDB380F5AB8C0F02728E91D469ABD0EAE0D93B1CC66CE127B29C7D77441A49D09FCA5D6D9762FC74C31BB506C8BAE3C79AD6C2578775B95956B5370D1D0519E37906B384736233251E8F09AD79DFBE2C6ABFADAC8E4D8624318C27DAF1DF040103DF0314F527081CF371DD7E1FD4FA414A665036E0F5E6E5"

        };
        int ret = -1;
        try {
            for (int i = 0; i < pukList.length; i++) {
                String tip = "Download Capk" + String.format("(%d)", i);
                String aid = pukList[i];
                ret = DeviceHelper.getEmvHandler().addCAPKParam(HexUtil.hexStringToByte(aid));

                if (ret != ServiceResult.Success) {
                    break;
                }

                showResult(textView, tip);
                SystemClock.sleep(500);
            }
            showResult(textView, "Download puk success!");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void clearCapk() {
        try {
            DeviceHelper.getEmvHandler().clearCAPKParam();
            showResult(textView, "Clear aid success!");
        } catch (RemoteException e) {

        } catch (NullPointerException e) {
            showResult(textView, e.getMessage());
        }

    }

    private void getCapkList() {
        try {
            List<EmvCapk> capkList = DeviceHelper.getEmvHandler().getCapkList();
            StringBuilder builder = new StringBuilder();

            for (int i = 0; i < capkList.size(); i++) {
                builder.append("\nRID:" + HexUtil.bytesToHexString(capkList.get(i).getRID()));
                builder.append("\nCAPK INDEX:" + capkList.get(i).getCA_PKIndex());
            }

            showResult(textView, builder.toString());

        } catch (RemoteException e) {
            showResult(textView, e.getMessage());
        } catch (NullPointerException e) {
            showResult(textView, e.getMessage());
        }

    }


    protected void setButtonName() {
    }


}