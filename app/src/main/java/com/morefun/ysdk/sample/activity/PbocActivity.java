package com.morefun.ysdk.sample.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.RemoteException;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.TextView;

import com.morefun.yapi.ServiceResult;
import com.morefun.yapi.device.pinpad.OnPinPadInputListener;
import com.morefun.yapi.device.pinpad.PinAlgorithmMode;
import com.morefun.yapi.device.reader.icc.ICCSearchResult;
import com.morefun.yapi.device.reader.icc.IccCardReader;
import com.morefun.yapi.device.reader.icc.IccCardType;
import com.morefun.yapi.device.reader.icc.IccReaderSlot;
import com.morefun.yapi.device.reader.icc.OnSearchIccCardListener;
import com.morefun.yapi.device.reader.mag.MagCardInfoEntity;
import com.morefun.yapi.device.reader.mag.MagCardReader;
import com.morefun.yapi.device.reader.mag.OnSearchMagCardListener;
import com.morefun.yapi.emv.EmvChannelType;
import com.morefun.yapi.emv.EmvErrorCode;
import com.morefun.yapi.emv.EmvErrorConstrants;
import com.morefun.yapi.emv.EmvListenerConstrants;
import com.morefun.yapi.emv.EmvOnlineResult;
import com.morefun.yapi.emv.OnEmvProcessListener;
import com.morefun.ysdk.sample.R;
import com.morefun.ysdk.sample.device.DeviceHelper;
import com.morefun.ysdk.sample.dialog.InputDialog;
import com.morefun.ysdk.sample.listener.OnInputPinListener;
import com.morefun.ysdk.sample.utils.CardUtil;
import com.morefun.ysdk.sample.utils.EmvUtil;
import com.morefun.ysdk.sample.utils.HexUtil;
import com.morefun.ysdk.sample.utils.TlvData;
import com.morefun.ysdk.sample.utils.TlvDataList;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class PbocActivity extends BaseActivity {
    private final String TAG = PbocActivity.class.getName();

    @BindView(R.id.textView)
    TextView textView;

    @BindView(R.id.button)
    Button button;

    private IccCardReader iccCardReader;
    private IccCardReader rfReader;
    private MagCardReader magCardReader;

    private String amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_with_button);
        ButterKnife.bind(this);

        setButtonName();

    }

    @OnClick({R.id.button})
    public void onClick() {
        inputAmout();
    }

    protected void setButtonName() {
        button.setText(getString(R.string.menu_pboc));
    }

    private void inputAmout() {
        InputDialog inputAmout = new InputDialog(this);

        inputAmout.setOnClickBottomListener(new InputDialog.OnClickBottomListener() {
            @Override
            public void onPositiveClick(String content) {
                amount = content;

                if (amount != null && !amount.isEmpty() && Double.parseDouble(amount) > 0) {
                    inputAmout.dismiss();
                    try {
                        showResult(textView, getString(R.string.msg_icorrfid));
                        searchCard();
                    } catch (RemoteException e) {

                    }

                } else {
                    showResult(textView, "Please input amount!");
                }
            }

            @Override
            public void onNegtiveClick() {
                inputAmout.dismiss();
            }
        }).show();
    }

    public void searchCard() throws RemoteException {
        button.setEnabled(false);

        iccCardReader = DeviceHelper.getIccCardReader(IccReaderSlot.ICSlOT1);
        rfReader = DeviceHelper.getIccCardReader(IccReaderSlot.RFSlOT);
        magCardReader = DeviceHelper.getMagCardReader();

        OnSearchIccCardListener.Stub listener = new OnSearchIccCardListener.Stub() {
            @Override
            public void onSearchResult(int retCode, Bundle bundle) throws RemoteException {
                stopSearch();
                if (retCode == ServiceResult.Success) {
                    int channel = bundle.getInt(ICCSearchResult.CARDOTHER) == IccReaderSlot.ICSlOT1 ? EmvChannelType.FROM_ICC : EmvChannelType.FROM_PICC;
                    emvProcess(channel, amount);
                }
            }
        };
        iccCardReader.searchCard(listener, 60, new String[]{IccCardType.CPUCARD, IccCardType.AT24CXX, IccCardType.AT88SC102});
        rfReader.searchCard(listener, 60, new String[]{IccCardType.CPUCARD, IccCardType.AT24CXX, IccCardType.AT88SC102});
        magCardReader.searchCard(new OnSearchMagCardListener.Stub() {
            @Override
            public void onSearchResult(int retCode, MagCardInfoEntity magCardInfoEntity) throws RemoteException {
                if (retCode == ServiceResult.Success) {
                    StringBuilder builder = new StringBuilder();

                    builder.append("Card: " + magCardInfoEntity.getCardNo());
                    builder.append("\nTk1: " + magCardInfoEntity.getTk1());
                    builder.append("\nTk2: " + magCardInfoEntity.getTk2());
                    builder.append("\nTk3: " + magCardInfoEntity.getTk3());
                    builder.append("\ntrackKSN: " + magCardInfoEntity.getKsn());
                    builder.append("\nServiceCode: " + magCardInfoEntity.getServiceCode());

                    showResult(textView, builder.toString());
                }
                stopSearch();
            }
        }, 60, new Bundle());
    }

    private void emvProcess(int channel, String amount) throws RemoteException {

        DeviceHelper.getEmvHandler().initTermConfig(EmvUtil.getInitTermConfig());
        DeviceHelper.getEmvHandler().emvProcess(EmvUtil.getInitBundleValue(channel, amount, "0.02"), new OnEmvProcessListener.Stub() {

            @Override
            public void onSelApp(List<String> appNameList, boolean isFirstSelect) throws RemoteException {
                showResult(textView, "onSelApp");
                selApp(appNameList);
            }

            @Override
            public void onConfirmCardNo(String cardNo) throws RemoteException {
                showResult(textView, "onConfirmCardNo:" + cardNo);

                DeviceHelper.getEmvHandler().onSetConfirmCardNoResponse(true);
            }

            @Override
            public void onCardHolderInputPin(boolean isOnlinePin, int leftTimes) throws RemoteException {
                showResult(textView, "onCardHolderInputPin");

                String cardNo = EmvUtil.readPan();

                inputPin(cardNo, new OnInputPinListener() {
                    @Override
                    public void onInputPin(byte[] pinBlock) {
                        try {
                            DeviceHelper.getEmvHandler().onSetCardHolderInputPin(pinBlock);
                        } catch (RemoteException e) {

                        }

                    }
                });
            }

            @Override
            public void onPinPress(byte keyCode) throws RemoteException {
                showResult(textView, "Callback:onPinPress");
            }

            @Override
            public void onCertVerify(String certName, String certInfo) throws RemoteException {
                showResult(textView, "Callback:onCertVerify");

                DeviceHelper.getEmvHandler().onSetCertVerifyResponse(true);

            }

            @Override
            public void onOnlineProc(Bundle data) throws RemoteException {
                showResult(textView, "Callback:onOnlineProc");
                onlineProc();

            }

            @Override
            public void onContactlessOnlinePlaceCardMode(int mode) throws RemoteException {
                showResult(textView, "Callback:onContactlessOnlinePlaceCardMode");

                if (mode == EmvListenerConstrants.NEED_CHECK_CONTACTLESS_CARD_AGAIN) {

                    rfReader = DeviceHelper.getIccCardReader(IccReaderSlot.RFSlOT);

                    OnSearchIccCardListener.Stub listener = new OnSearchIccCardListener.Stub() {
                        @Override
                        public void onSearchResult(int retCode, Bundle bundle) throws RemoteException {
                            stopSearch();
                            try {
                                DeviceHelper.getEmvHandler().onSetContactlessOnlinePlaceCardModeResponse(ServiceResult.Success == retCode);
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                        }
                    };

                    rfReader.searchCard(listener, 60, new String[]{IccCardType.CPUCARD, IccCardType.AT24CXX, IccCardType.AT88SC102});
                } else {
                    //show Dialog Prompt the user not to remove the card
                    DeviceHelper.getEmvHandler().onSetContactlessOnlinePlaceCardModeResponse(true);
                }
            }

            @Override
            public void onFinish(int retCode, Bundle data) throws RemoteException {
                showResult(textView, "Callback:onFinish");
                emvFinish(retCode, data);
            }

            @Override
            public void onSetAIDParameter(String aid) throws RemoteException {
                showResult(textView, "Callback:onSetAIDParameter");
            }

            @Override
            public void onSetCAPubkey(String rid, int index, int algMode) throws RemoteException {
                showResult(textView, "Callback:onSetCAPubkey");
            }

            @Override
            public void onTRiskManage(String pan, String panSn) throws RemoteException {
                showResult(textView, "Callback:onTRiskManage");
            }

            @Override
            public void onSelectLanguage(String language) throws RemoteException {
                showResult(textView, "Callback:onSelectLanguage");
            }

            @Override
            public void onSelectAccountType(List<String> accountTypes) throws RemoteException {
                showResult(textView, "Callback:onSelectAccountType");
            }

            @Override
            public void onIssuerVoiceReference(String pan) throws RemoteException {
                showResult(textView, "Callback:onIssuerVoiceReference");
            }

        });

    }

    private void inputPin(String pan, OnInputPinListener listener) {
        showResult(textView, "inputPin:" + pan);

        byte[] panBlock = pan.getBytes();
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

                    listener.onInputPin(pinBlock);
                }

                @Override
                public void onSendKey(byte keyCode) throws RemoteException {

                }

            });
        } catch (RemoteException e) {

        }

    }

    private void selApp(List<String> appList) {

        String[] options = new String[appList.size()];
        for (int i = 0; i < appList.size(); i++) {
            options[i] = appList.get(i);
        }

        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setTitle("Please select app");
        alertBuilder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int index) {
                try {
                    DeviceHelper.getEmvHandler().onSetSelAppResponse(index + 1);
                } catch (RemoteException e) {

                }
            }
        });
        AlertDialog alertDialog1 = alertBuilder.create();
        alertDialog1.show();

    }


    private void emvFinish(int ret, Bundle bundle) throws RemoteException {
        byte[] errorCode = bundle.getByteArray(EmvErrorConstrants.EMV_ERROR_CODE);
        if (errorCode != null) {

        }

        if (ret == ServiceResult.Success) {//trans accept

        } else if (ret == ServiceResult.Emv_FallBack) {// fallback

        } else if (ret == ServiceResult.Emv_Terminate) {// trans end
            if (errorCode != null) {
                showResult(textView, "terminate, Error Code: " + new String(errorCode).trim());
                //TODO if the amount of connect less transactions is more than 2,0000. The interface prompts you to swipe or insert a card.
                if (DeviceHelper.getEmvHandler().isErrorCode(EmvErrorCode.QPBOC_ERR_PRE_AMTLIMIT)) {
                    showResult(textView, "RF Limit Exceed, Pls try another page! ");
                }
            } else {
                showResult(textView, "trans terminate");
            }

        } else if (ret == ServiceResult.Emv_Declined) {// trans refuse
            //TODO Please noted android time is correct ?
            if (errorCode != null) {
                showResult(textView, "trans refuse, Error Code: " + new String(errorCode).trim());
            } else {
                showResult(textView, "trans refuse");
            }
        } else if (ret == ServiceResult.Emv_Cancel) {// trans cancel
            showResult(textView, "Emv cancel");
        } else {
            showResult(textView, getString(R.string.other_trans_result));
        }
        onFinishShow();
    }

    private void onlineProc() throws RemoteException {
        StringBuilder builder = new StringBuilder();
        String arqcTlv = EmvUtil.getTLVDatas(EmvUtil.arqcTLVTags);

        builder.append(EmvUtil.getTLVDatas(EmvUtil.arqcTLVTags));
        builder.append("9F0306000000000000");


        if (!TlvDataList.fromBinary(arqcTlv).contains("9F27")) {
            builder.append(TlvData.fromData("9F27", new byte[]{(byte) 0x80}));
        }

        TlvDataList tlvDataList = TlvDataList.fromBinary(builder.toString());
        arqcTlv = tlvDataList.toString();

        String appLabel = EmvUtil.getPbocData("50", true);
        String apn = EmvUtil.getPbocData("9F12", true);


        String resultTlv = arqcTlv + TlvData.fromData("AC", TlvDataList.fromBinary(arqcTlv).getTLV("9F26").getBytesValue())
                + TlvData.fromData("9B", new byte[2])
                + TlvData.fromData("9F34", new byte[3])
                + (TextUtils.isEmpty(appLabel) ? "" : TlvData.fromData("50", HexUtil.hexStringToByte(appLabel)))
                + (TextUtils.isEmpty(apn) ? "" : TlvData.fromData("9F12", HexUtil.hexStringToByte(apn)));


        Bundle online = new Bundle();
        //TODO onlineRespCode is DE 39—RESPONSE CODE, detail see ISO8583
        String onlineRespCode = "00";
        //TODO DE 55.
        byte[] arpcData = EmvUtil.getExampleARPCData();

        if (arpcData == null) {
            return;
        }
        online.putString(EmvOnlineResult.REJCODE, onlineRespCode);
        online.putByteArray(EmvOnlineResult.RECVARPC_DATA, arpcData);

        DeviceHelper.getEmvHandler().onSetOnlineProcResponse(ServiceResult.Success, online);

        showResult(textView, resultTlv);
    }

    void onFinishShow() throws RemoteException {
        StringBuilder builder = new StringBuilder();

        String tlv = EmvUtil.getTLVDatas(EmvUtil.tags);

        TlvDataList tlvDataList = TlvDataList.fromBinary(tlv);

        builder.append("Card No:" + EmvUtil.readPan() + "\n");
        builder.append("Card Org:" + CardUtil.getCardTypFromAid(EmvUtil.getPbocData("4F", true)) + "\n");

        for (String tag : EmvUtil.tags) {
            if ("9F4E".equalsIgnoreCase(tag)) {
                builder.append(tag + "=" + tlvDataList.getTLV(tag) + "\n");
            } else if ("5F20".equalsIgnoreCase(tag)) {
                builder.append(tag + "=" + tlvDataList.getTLV(tag) + "\n");
            } else {
                builder.append(tag + "=" + tlvDataList.getTLV(tag) + "\n");
            }
        }

        showResult(textView, builder.toString());

    }

    private void stopSearch() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                button.setEnabled(true);
                try {
                    iccCardReader.stopSearch();
                    rfReader.stopSearch();
                    magCardReader.stopSearch();
                } catch (RemoteException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        });

    }


}