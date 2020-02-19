package com.morefun.ysdk.sample.activity;

import android.os.Bundle;
import android.os.RemoteException;
import android.widget.Button;
import android.widget.TextView;

import com.morefun.yapi.ServiceResult;
import com.morefun.yapi.card.industry.IndustryCardHandler;
import com.morefun.yapi.device.reader.icc.ICCSearchResult;
import com.morefun.yapi.device.reader.icc.IccCardReader;
import com.morefun.yapi.device.reader.icc.IccCardType;
import com.morefun.yapi.device.reader.icc.IccReaderSlot;
import com.morefun.yapi.device.reader.icc.OnSearchIccCardListener;
import com.morefun.ysdk.sample.R;
import com.morefun.ysdk.sample.device.DeviceHelper;
import com.morefun.ysdk.sample.utils.HexUtil;
import com.morefun.ysdk.sample.utils.SweetDialogUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FelicaCardActivity extends BaseActivity {
    private final String TAG = FelicaCardActivity.class.getName();

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
        showResult(textView, getString(R.string.msg_rfcard));
        searchFelicaCard(new String[]{IccCardType.FELICA});
    }

    private void searchFelicaCard(final String[] cardType) {
        try {
            IccCardReader rfReader = DeviceHelper.getIccCardReader(IccReaderSlot.RFSlOT);

            OnSearchIccCardListener.Stub listener = new OnSearchIccCardListener.Stub() {
                @Override
                public void onSearchResult(int retCode, Bundle bundle) throws RemoteException {
                    rfReader.stopSearch();

                    if (ServiceResult.Success == retCode) {
                        String cardType = bundle.getString(ICCSearchResult.CARDTYPE);
                        if (IccCardType.FELICA.equals(cardType)) {
                            felicaCard();
                        }
                    } else {
                        showResult(textView, "result:" + retCode);
                    }

                }
            };

            rfReader.searchCard(listener, 10, cardType);

        } catch (RemoteException e) {
            e.printStackTrace();
            showResult(textView, e.toString());
        }

    }

    private void felicaCard() {
        try {
            IccCardReader cardReader = DeviceHelper.getIccCardReader(IccReaderSlot.RFSlOT);
            IndustryCardHandler cardHandler = DeviceHelper.getIndustryCardHandler(cardReader);

            if (cardHandler == null) {
                SweetDialogUtil.showError(FelicaCardActivity.this, getString(R.string.msg_readfail_retry));
                return;
            }

            cardHandler.setPowerOn(new byte[]{0x00, 0x00});

            byte[] result = new byte[256];
            byte[] ucPollCmd = new byte[]{0x6, 0x00, (byte)0xFF, (byte)0xFF, 0x0, 0x00};
            byte[] ucReadCmd = new byte[]{0x10, 0x06, 0x01, 0x2e, 0x45, 0x76, (byte)0xba, (byte)0xc5, 0x41, 0x2c, 0x01, 0x0b, 0x00, 0x01, (byte)0x80, (byte)0x82};

            int ret = cardHandler.exchangeCmd(result, ucPollCmd, ucPollCmd.length);

            String msg = String.format("%s%d\n", getString(R.string.label_result), ret);
            if (ret > 0 && result != null) {
                int pos = 2;

                System.arraycopy(ucReadCmd, 2, result, 2, 8);
                pos += 8;
                System.arraycopy(ucReadCmd, pos, ucReadCmd, pos, ucReadCmd.length - pos);
                ret = cardHandler.exchangeCmd(result, ucReadCmd, ucReadCmd.length);

                if (ret > 0) {
                    byte[] buffer = new byte[ret];
                    System.arraycopy(buffer, 0, result, 0, ret);
                    msg = String.format("%s%d\n%s", getString(R.string.label_result), ret, HexUtil.bytesToHexString(buffer));
                }
            }
            cardHandler.setPowerOff();
            showResult(textView, msg);
            return;
        } catch (Exception e) {
            e.printStackTrace();
            showResult(textView, e.toString());
        }
    }

    protected void setButtonName() {
        button.setText(getString(R.string.menu_felica));
    }


}