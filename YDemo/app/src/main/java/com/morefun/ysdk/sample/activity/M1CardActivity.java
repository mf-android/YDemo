package com.morefun.ysdk.sample.activity;

import android.os.Bundle;
import android.os.RemoteException;
import android.widget.Button;
import android.widget.TextView;

import com.morefun.yapi.ServiceResult;
import com.morefun.yapi.card.mifare.M1CardHandler;
import com.morefun.yapi.card.mifare.M1CardOperType;
import com.morefun.yapi.card.mifare.M1KeyTypeConstrants;
import com.morefun.yapi.device.reader.icc.ICCSearchResult;
import com.morefun.yapi.device.reader.icc.IccCardReader;
import com.morefun.yapi.device.reader.icc.IccCardType;
import com.morefun.yapi.device.reader.icc.IccReaderSlot;
import com.morefun.yapi.device.reader.icc.OnSearchIccCardListener;
import com.morefun.ysdk.sample.R;
import com.morefun.ysdk.sample.device.DeviceHelper;
import com.morefun.ysdk.sample.utils.HexUtil;
import com.morefun.ysdk.sample.utils.SweetDialogUtil;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class M1CardActivity extends BaseActivity {
    private final String TAG = M1CardActivity.class.getName();

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
        searchM1Card(new String[]{IccCardType.M1CARD});
    }

    private void searchM1Card(final String[] cardType) {
        try {
            final IccCardReader rfReader = DeviceHelper.getIccCardReader(IccReaderSlot.RFSlOT);

            OnSearchIccCardListener.Stub listener = new OnSearchIccCardListener.Stub() {
                @Override
                public void onSearchResult(int retCode, Bundle bundle) throws RemoteException {
                    rfReader.stopSearch();
                    if (ServiceResult.Success == retCode) {
                        String cardType = bundle.getString(ICCSearchResult.CARDTYPE);
                        if (IccCardType.M1CARD.equals(cardType)) {
                            int slot = bundle.getInt(ICCSearchResult.CARDOTHER);
                            m1Card(slot);
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

    private void m1Card(int slot) {
        try {
            byte[] key = new byte[]{(byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff};
            byte[] uid = new byte[64];

            StringBuilder builder = new StringBuilder();
            IccCardReader cardReader = DeviceHelper.getIccCardReader(slot);
            M1CardHandler m1CardHandler = DeviceHelper.getM1CardHandler(cardReader);

            if (m1CardHandler == null) {
                SweetDialogUtil.showError(M1CardActivity.this, getString(R.string.msg_readfail_retry));
                return;
            }

            int ret = m1CardHandler.authority(M1KeyTypeConstrants.KEYTYPE_A, 0, key, uid);
            if (ret != ServiceResult.Success) {
                showResult(textView, "M1 card authority:" + ret);
                return;
            }

            int len = 16;
            byte[] buf = new byte[len];
            byte[] value = new byte[]{(byte) 0x0A};
            byte[] write = new byte[]{(byte) 0x12, (byte) 0x34, (byte) 0x56, (byte) 0x78, (byte) 0x90, (byte) 0xAB, (byte) 0xCD, (byte) 0xEF, (byte) 0x12, (byte) 0x34, (byte) 0x56, (byte) 0x78, (byte) 0x90, (byte) 0xAB, (byte) 0xCD, (byte) 0xEF};

            Arrays.fill(write, (byte)0x00);

            write[4] = (byte) 0xff;
            write[5] = (byte) 0xff;
            write[6] = (byte) 0xff;
            write[7] = (byte) 0xff;
            write[12] = ~0;
            write[13] = 0;
            write[14] = ~0;
            write[15] = 0;

            builder.append("M1Card Test\n");

            m1CardHandler.writeBlock(1, write);
            m1CardHandler.readBlock(0, buf);
            builder.append("readBlock0:" + HexUtil.bytesToHexString(buf) + "\n");

            m1CardHandler.readBlock(1, buf);
            builder.append("readBlock1:" + HexUtil.bytesToHexString(buf) + "\n");

            m1CardHandler.operateBlock(M1CardOperType.INCREMENT, 1, value, 0);
            m1CardHandler.readBlock(1, buf);
            builder.append("=====operateBlock INCREMENT=====\n");
            builder.append("readBlock1:" + HexUtil.bytesToHexString(buf) + "\n");


            m1CardHandler.operateBlock(M1CardOperType.DECREMENT, 1, value, 0);
            m1CardHandler.readBlock(1, buf);
            builder.append("=====operateBlock DECREMENT=====\n");
            builder.append("readBlock1:" + HexUtil.bytesToHexString(buf) + "\n");

            m1CardHandler.operateBlock(M1CardOperType.BACKUP, 0, null, 1);
            m1CardHandler.readBlock(1, buf);
            builder.append("=====operateBlock BACKUP=====\n");
            builder.append("readBlock1:" + HexUtil.bytesToHexString(buf) + "\n");

            showResult(textView, builder.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void setButtonName() {
        button.setText(getString(R.string.menu_m1card));
    }


}