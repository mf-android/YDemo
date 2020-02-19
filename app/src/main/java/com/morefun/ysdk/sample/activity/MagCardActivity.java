package com.morefun.ysdk.sample.activity;

import android.os.Bundle;
import android.os.RemoteException;
import android.widget.Button;
import android.widget.TextView;

import com.morefun.yapi.ServiceResult;
import com.morefun.yapi.device.reader.mag.MagCardInfoEntity;
import com.morefun.yapi.device.reader.mag.MagCardReader;
import com.morefun.yapi.device.reader.mag.OnSearchMagCardListener;
import com.morefun.ysdk.sample.R;
import com.morefun.ysdk.sample.device.DeviceHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MagCardActivity extends BaseActivity {
    private final String TAG = MagCardActivity.class.getName();

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
        showResult(textView, getString(R.string.please_swipe_card));
        searchMagCard();
    }

    private void searchMagCard() {
        try {
            final MagCardReader magCardReader = DeviceHelper.getMagCardReader();

            magCardReader.searchCard(new OnSearchMagCardListener.Stub() {
                @Override
                public void onSearchResult(int ret, MagCardInfoEntity magCardInfoEntity) throws RemoteException {
                    if (ret == ServiceResult.Success) {
                        StringBuilder builder = new StringBuilder();

                        builder.append("Card: " + magCardInfoEntity.getCardNo());
                        builder.append("\nTk1: " + magCardInfoEntity.getTk1());
                        builder.append("\nTk2: " + magCardInfoEntity.getTk2());
                        builder.append("\nTk3: " + magCardInfoEntity.getTk3());
                        builder.append("\ntrackKSN: " + magCardInfoEntity.getKsn());
                        builder.append("\nServiceCode: " + magCardInfoEntity.getServiceCode());

                        showResult(textView, builder.toString());
                    } else {
                        showResult(textView, "retCode:" + ret);
                    }
                }
            }, 10, new Bundle());

        } catch (RemoteException e) {
            e.printStackTrace();
            showResult(textView, e.toString());
        }

    }

    protected void setButtonName() {
        button.setText(getString(R.string.menu_mag_card_reader));
    }


}