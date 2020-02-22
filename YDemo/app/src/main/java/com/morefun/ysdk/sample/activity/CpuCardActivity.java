package com.morefun.ysdk.sample.activity;

import android.os.Bundle;
import android.os.RemoteException;
import android.widget.Button;
import android.widget.TextView;

import com.morefun.yapi.ServiceResult;
import com.morefun.yapi.card.cpu.APDUCmd;
import com.morefun.yapi.card.cpu.CPUCardHandler;
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

public class CpuCardActivity extends BaseActivity {
    private final String TAG = CpuCardActivity.class.getName();

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
        searchCPUCard(new String[]{IccCardType.CPUCARD});

    }

    private void searchCPUCard(final String[] cardType) {
        try {
            final IccCardReader icReader = DeviceHelper.getIccCardReader(IccReaderSlot.ICSlOT1);
            final IccCardReader rfReader = DeviceHelper.getIccCardReader(IccReaderSlot.RFSlOT);

            OnSearchIccCardListener.Stub listener = new OnSearchIccCardListener.Stub() {
                @Override
                public void onSearchResult(int retCode, Bundle bundle) throws RemoteException {

                    icReader.stopSearch();
                    rfReader.stopSearch();

                    if (ServiceResult.Success == retCode) {
                        String cardType = bundle.getString(ICCSearchResult.CARDTYPE);
                        if (IccCardType.CPUCARD.equals(cardType)) {
                            int slot = bundle.getInt(ICCSearchResult.CARDOTHER);
                            exchangeApdu(slot);
                        }
                    } else {
                        showResult(textView, "result:" + retCode);
                    }
                }
            };

            icReader.searchCard(listener, 10, cardType);
            rfReader.searchCard(listener, 10, cardType);

        } catch (RemoteException e) {
            e.printStackTrace();
            showResult(textView, e.toString());
        }
    }

    private void exchangeApdu(int slot) throws RemoteException {
        try {
            String cmd = "00A40400";
            String data = "325041592E5359532E4444463031";
            byte le = 0x00;

            IccCardReader cardReader = DeviceHelper.getIccCardReader(slot);
            CPUCardHandler cpuCardHandler = DeviceHelper.getCpuCardHandler(cardReader);

            if (cpuCardHandler == null) {
                SweetDialogUtil.showError(CpuCardActivity.this, getString(R.string.msg_readfail_retry));
                return;
            }

            if (!cpuCardHandler.setPowerOn(new byte[]{0x00, 0x00})) {
                SweetDialogUtil.showError(CpuCardActivity.this, getString(R.string.msg_readfail_retry));
                return;
            }

            byte[] cmdBytes = HexUtil.hexStringToByte(cmd);
            byte[] dataArray = HexUtil.hexStringToByte(data);
            byte[] tmp = new byte[256];

            System.arraycopy(dataArray, 0, tmp, 0, dataArray.length);

            APDUCmd apduCmd = new APDUCmd();
            apduCmd.setCla(cmdBytes[0]);
            apduCmd.setIns(cmdBytes[1]);
            apduCmd.setP1(cmdBytes[2]);
            apduCmd.setP2(cmdBytes[3]);
            apduCmd.setLc(dataArray.length);
            apduCmd.setDataIn(tmp);
            apduCmd.setLe(le);

            int ret = cpuCardHandler.exchangeAPDUCmd(apduCmd);
            cpuCardHandler.setPowerOff();

            if (ret == ServiceResult.Success) {
                if (apduCmd.getDataOutLen() > 0) {
                    showResult(textView, HexUtil.bytesToHexString(apduCmd.getDataOut()));
                } else {

                }
            } else {
                SweetDialogUtil.showError(CpuCardActivity.this, getString(R.string.msg_readfail_retry));
            }
        } catch (Exception e) {
            e.printStackTrace();
            showResult(textView, e.toString());
        }
    }

    protected void setButtonName() {
        button.setText(getString(R.string.menu_apdu));
    }



}