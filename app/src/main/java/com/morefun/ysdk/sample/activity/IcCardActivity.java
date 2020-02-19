package com.morefun.ysdk.sample.activity;

import android.os.Bundle;
import android.os.RemoteException;
import android.widget.Button;
import android.widget.TextView;

import com.morefun.yapi.device.reader.icc.IccCardReader;
import com.morefun.yapi.device.reader.icc.IccCardType;
import com.morefun.yapi.device.reader.icc.IccReaderSlot;
import com.morefun.yapi.device.reader.icc.OnSearchIccCardListener;
import com.morefun.ysdk.sample.R;
import com.morefun.ysdk.sample.device.DeviceHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class IcCardActivity extends BaseActivity {
    private final String TAG = IcCardActivity.class.getName();

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
        showResult(textView, getString(R.string.msg_icorrfid));
        searchIcCard();
    }

    private void searchIcCard() {
        try {
            final IccCardReader iccCardReader = DeviceHelper.getIccCardReader(IccReaderSlot.ICSlOT1);
            final IccCardReader rfReader = DeviceHelper.getIccCardReader(IccReaderSlot.RFSlOT);
            final IccCardReader m1Reader = DeviceHelper.getIccCardReader(IccReaderSlot.RFSlOT);

            OnSearchIccCardListener.Stub listener = new OnSearchIccCardListener.Stub() {
                @Override
                public void onSearchResult(final int retCode, Bundle bundle) throws RemoteException {
                    showResult(textView, "retCode:" + retCode);

                    m1Reader.stopSearch();
                    rfReader.stopSearch();
                    iccCardReader.stopSearch();

                }
            };

            iccCardReader.searchCard(listener, 60, new String[]{IccCardType.CPUCARD, IccCardType.AT24CXX, IccCardType.AT88SC102});
            rfReader.searchCard(listener, 60, new String[]{IccCardType.CPUCARD, IccCardType.AT24CXX, IccCardType.AT88SC102});
            m1Reader.searchCard(listener, 60, new String[]{IccCardType.M1CARD, IccCardType.AT24CXX, IccCardType.AT88SC102});

        } catch (RemoteException e) {
            e.printStackTrace();
            showResult(textView, e.toString());
        }

    }

    protected void setButtonName() {
        button.setText(getString(R.string.menu_ic_card_reader));
    }


}