package com.morefun.ypos.apitest;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;

import com.morefun.yapi.ServiceResult;
import com.morefun.yapi.device.beeper.BeepModeConstrants;
import com.morefun.yapi.device.pinpad.DukptCalcObj;
import com.morefun.yapi.device.reader.icc.ICCSearchResult;
import com.morefun.yapi.device.reader.icc.IccCardReader;
import com.morefun.yapi.device.reader.icc.IccCardType;
import com.morefun.yapi.device.reader.icc.IccReaderSlot;
import com.morefun.yapi.device.reader.icc.OnSearchIccCardListener;
import com.morefun.yapi.device.reader.mag.MagCardInfoEntity;
import com.morefun.yapi.device.reader.mag.OnSearchMagCardListener;
import com.morefun.yapi.emv.EmvChannelType;
import com.morefun.yapi.emv.EmvDataSource;
import com.morefun.yapi.emv.EmvErrorCode;
import com.morefun.yapi.emv.EmvErrorConstrants;
import com.morefun.yapi.emv.EmvHandler;
import com.morefun.yapi.emv.EmvListenerConstrants;
import com.morefun.yapi.emv.EmvOnlineRequest;
import com.morefun.yapi.emv.EmvOnlineResult;
import com.morefun.yapi.emv.GoToConstants;
import com.morefun.yapi.emv.OnEmvProcessListener;
import com.morefun.yapi.engine.DeviceServiceEngine;
import com.morefun.ypos.BaseApiTest;
import com.morefun.ypos.MainActivity;
import com.morefun.ypos.R;
import com.morefun.ypos.config.DukptConfigs;
import com.morefun.ypos.config.EmvProcessConfig;
import com.morefun.ypos.config.EmvTagUtils;
import com.morefun.ypos.interfaces.OnInputAmountCallBack;
import com.morefun.ypos.interfaces.OnSelectAppCallBack;
import com.morefun.ypos.uitls.ActionItems;
import com.morefun.ypos.uitls.CardOrgUtil;
import com.morefun.ypos.uitls.Utils;

import java.util.List;

import static com.morefun.ypos.uitls.Utils.byte2string;
import static com.morefun.ypos.uitls.Utils.string2byte;

public class EmvPBOCTest extends BaseApiTest {
    private static final String TAG = "EmvHandlerTest";
    DeviceServiceEngine mSDKManager;
    EmvHandler mEmvHandler;
    MainActivity.AlertDialogOnShowListener mAlertDialogOnShowListener;
    static EmvPBOCTest mEmvHandlerTest;

    public static EmvPBOCTest getInstance() {
        if (mEmvHandlerTest == null) {
            synchronized (EmvPBOCTest.class) {
                if (mEmvHandlerTest == null) {
                    mEmvHandlerTest = new EmvPBOCTest();
                }
            }
        }
        return mEmvHandlerTest;
    }

    private EmvPBOCTest() {
    }

    private EmvPBOCTest setEmvHandler(DeviceServiceEngine engine, MainActivity.AlertDialogOnShowListener listener) {
        try {
            mSDKManager = engine;
            mEmvHandler = engine.getEmvHandler();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        mAlertDialogOnShowListener = listener;
        return this;
    }

    public static void PBOC(final DeviceServiceEngine mSDKManager, final MainActivity.AlertDialogOnShowListener listener) {
        listener.onInputAmount(new OnInputAmountCallBack() {
            @Override
            public void onInputAmount(final String amount) {
                if (amount == null || amount.length() == 0) {
                    listener.showMessage("input amount fail");
                    return;
                }
                try {
                    //TODO >>> if is dukpt, Please check if you need KSN add one and genrate new PEK
                    if (DukptConfigs.isDukpt) {
                        DukptConfigs.testInjectIPEK3(mSDKManager.getPinPad());
                        DukptConfigs.getInstance().increaseKSN(mSDKManager.getPinPad());
                    }
                    EmvPBOCTest.getInstance().setEmvHandler(mSDKManager, listener).searchCard(new MainActivity.OnSearchListener() {
                        @Override
                        public void onSearchResult(int retCode, Bundle bundle) {
                            if (ServiceResult.Success == retCode) {
                                EmvPBOCTest.getInstance().emvPBOC(bundle, amount);
                            } else {
                                listener.showMessage("searchCard fail: " + retCode);
                            }
                        }
                    });
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    OnEmvProcessListener.Stub mOnEmvProcessListener;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    Log.d(TAG, "msg.what = showOnlinePinPanKeyK");
                    try {
                        PinPadTest.showOnlinePinPanKeyK(mSDKManager, mEmvHandler, cardNum, null, mAlertDialogOnShowListener);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                case 2:
                    Log.d(TAG, "msg.what = showOffLinePinKey");
                    try {
                        PinPadTest.showOffLinePinKey(mSDKManager, mEmvHandler, mAlertDialogOnShowListener);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }
        }
    };
    private String cardNum;

    public void initEmvListener() {
        mOnEmvProcessListener = new OnEmvProcessListener.Stub() {
            @Override
            public void onSelApp(List<String> appNameList, boolean isFirstSelect) throws RemoteException {
                Log.d(TAG, "onSelApp = " + isFirstSelect);
                mAlertDialogOnShowListener.onSelApp(appNameList, isFirstSelect, new OnSelectAppCallBack() {
                    @Override
                    public void onSetSelAppResponse(int index) {
                        try {
                            mEmvHandler.onSetSelAppResponse(index);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onConfirmCardNo(String cardNo) throws RemoteException {
                Log.d(TAG, "cardNo = " + cardNo);
                cardNum = cardNo;
                mEmvHandler.onSetConfirmCardNoResponse(true);
            }

            @Override
            public void onCardHolderInputPin(boolean isOnlinePin, int messageType) throws RemoteException {
                Log.d(TAG, "onCardHolderInputPin isOnlinePin = " + isOnlinePin + "," + messageType);
                String messagePrompt = "Please enter PIN";
                if (messageType == 3) {
                    messagePrompt = "Please enter PIN";
                } else if (messageType == 2) {
                    messagePrompt = "Enter PIN again";
                } else if (messageType == 1) {
                    messagePrompt = "Enter  laster PIN ";
                }
                //7B 74 FF F7 25 82 58 35
                byte[] pinBlock = string2byte("7B74FFF725825835");
                if (!isOnlinePin) {
                    //YS1DK
                    handler.sendEmptyMessage(2);
                } else {
                    handler.sendEmptyMessage(1);
                    //this is test Pinblock.
//                    mEmvHandler.onSetCardHolderInputPin(pinBlock);
                }
            }

            @Override
            public void onPinPress(byte keyCode) throws RemoteException {
                Log.d(TAG, "onPinPress = " + keyCode);
            }

            @Override
            public void onCertVerify(String pszCertNO, String cCertType) throws RemoteException {
                Log.d(TAG, "onCardHolderInputPin = " + pszCertNO + ", s1 = " + cCertType);
                mEmvHandler.onSetCertVerifyResponse(true);
            }

            @Override
            public void onOnlineProc(Bundle bundle) throws RemoteException {
                Log.d(TAG, "onOnlineProc = " + bundle);
//                beep(true);
                showOnlineDeal(bundle);
            }

            @Override
            public void onContactlessOnlinePlaceCardMode(int mode) throws RemoteException {
                //TODO RuPay Card need to check card again.
                Log.d(TAG, "onRfOnlinePlaceCardMode = " + mode);
                if (mode == EmvListenerConstrants.NEED_CHECK_CONTACTLESS_CARD_AGAIN) {
                    searchRfCard(new MainActivity.OnSearchListener() {
                        @Override
                        public void onSearchResult(int retCode, Bundle bundle) {
                            try {
                                mEmvHandler.onSetContactlessOnlinePlaceCardModeResponse(ServiceResult.Success == retCode);
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } else {
                    //show Dialog Prompt the user not to remove the card
                    mEmvHandler.onSetContactlessOnlinePlaceCardModeResponse(true);
                }
            }

            //TODO  onFinish : it will return EMV result after ARPC (issuer script ) perform.
            @Override
            public void onFinish(int ret, Bundle bundle) throws RemoteException {
                Log.d(TAG, "onFinish = " + ret);
                byte[] errorCode = bundle.getByteArray(EmvErrorConstrants.EMV_ERROR_CODE);
                if (errorCode != null) {
                    Log.d(TAG, "onFinish error= " + new String(errorCode).trim());
                }
                mAlertDialogOnShowListener.dismissProgress();
                if (ret == ServiceResult.Success) {//trans accept
                    beep(true);
                    // DF31
//                    byte[] scriptResult = bundle.getByteArray(EmvProcessResult.SCRIPTRESULT);
//                    if (scriptResult != null && scriptResult.length > 0) {
//                        mAlertDialogOnShowListener.showMessage(new String(scriptResult));
//                    }
                } else if (ret == ServiceResult.Emv_FallBack) {// fallback
                    mAlertDialogOnShowListener.showMessage("Emv_FallBack");
                } else if (ret == ServiceResult.Emv_Terminate) {// trans end
                    beep(false);
                    int gotoCode = bundle.getInt(EmvErrorConstrants.EMV_GOTO_CODE, 0);
                    Log.d(TAG, "gotoCode error= " + (gotoCode == GoToConstants.GOTO_CDV_TRY_AGAIN));
                    Log.d(TAG, "gotoCode error= " + (gotoCode == GoToConstants.GOTO_TRY_AGAIN));
                    if (errorCode != null) {
                        mAlertDialogOnShowListener.showMessage("terminate, Error Code: " + new String(errorCode).trim());
                        //TODO if the amount of connect less transactions is more than 2,0000. The interface prompts you to swipe or insert a card.
                        if (mEmvHandler.isErrorCode(EmvErrorCode.QPBOC_ERR_PRE_AMTLIMIT)) {
                            mAlertDialogOnShowListener.showMessage("RF Limit Exceed, Pls try another page! ");
                        }
                    } else {
                        mAlertDialogOnShowListener.showMessage("trans terminate");
                    }
                    //custom define or check ErrorCode
                    boolean isWithContactChip = mChannel == EmvChannelType.FROM_ICC;
                    if (mEmvHandler.isErrorCode(EmvErrorCode.EMV_ERR_ICCOP_SELECTAID)) {
                        mAlertDialogOnShowListener.showMessage("EmvErrorCode is EMV_ERR_ICCOP_SELECTAID: " + isWithContactChip);
                    }
                } else if (ret == ServiceResult.Emv_Declined) {// trans refuse
                    beep(false);
                    //TODO Please noted android time is correct ?
                    if (errorCode != null) {
                        mAlertDialogOnShowListener.showMessage("trans refuse, Error Code: " + new String(errorCode).trim());
                    } else {
                        mAlertDialogOnShowListener.showMessage("trans refuse");
                    }
                } else if (ret == ServiceResult.Emv_Cancel) {// trans cancel
                    beep(false);
                    mAlertDialogOnShowListener.showMessage("trans cancel");
                } else {
                    beep(false);
                    mAlertDialogOnShowListener.showMessage(getString(R.string.other_trans_result));
                    try {
                        mSDKManager.getBeeper().beep(BeepModeConstrants.FAIL);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
                onFinishShow();
            }

            @Override
            public void onSetAIDParameter(String s) throws RemoteException {
                //reserved
            }

            @Override
            public void onSetCAPubkey(String s, int i, int i1) throws RemoteException {
                //reserved
            }

            @Override
            public void onTRiskManage(String pan, String panSn) throws RemoteException {
                //reserved
            }

            @Override
            public void onSelectLanguage(String language) throws RemoteException {
                //reserved
            }

            @Override
            public void onSelectAccountType(List<String> accountTypes) throws RemoteException {
                //reserved
            }

            @Override
            public void onIssuerVoiceReference(String sPan) throws RemoteException {
                //reserved
            }
        };
    }

    public void showOnlineDeal(final Bundle bundle) throws RemoteException {
        Bundle inoutBundle = DukptConfigs.getTrackIPEKBundle();
        Log.d(TAG, "cardNum =" + cardNum);
        if (TextUtils.isEmpty(cardNum)) {
            cardNum = getTag("5A", inoutBundle);
        }
        Log.d(TAG, "cardNum after =" + cardNum);
        mAlertDialogOnShowListener.dismissProgress();
        StringBuilder builder = new StringBuilder();
        final byte[] pinBytes = bundle.getByteArray(EmvOnlineRequest.PIN);
        Log.d(TAG, "EmvOnlineRequest.PIN = " + byte2string(pinBytes));
        final byte[] cardSn = bundle.getByteArray(EmvOnlineRequest.CARDSN);
        builder.append("CardSn = " + byte2string(cardSn) + "\n");
        builder.append("PinBlock = " + byte2string(pinBytes) + "\n");

        //Authorisation Response Cryptogram of an EMV transaction
        Bundle online = new Bundle();
        //TODO onlineRespCode is DE 39—RESPONSE CODE, detail see ISO8583
        String onlineRespCode = "00";
        //TODO DE 55.
        byte[] arpcData = EmvProcessConfig.getExampleARPCData();
        //Please check ARPC data.
        if (arpcData == null) {
            Log.e(TAG, "arpcData  is empty");
            return;
        }
        online.putString(EmvOnlineResult.REJCODE, onlineRespCode);
        online.putByteArray(EmvOnlineResult.RECVARPC_DATA, arpcData);
        Log.d(TAG, "onSetOnlineProcResponse start ");
        //
        int OnlineProcResponse = true ? ServiceResult.Success : ServiceResult.Fail;
        mSDKManager.getEmvHandler().onSetOnlineProcResponse(OnlineProcResponse, online);

        Log.d(TAG, "" + builder.toString());
        mAlertDialogOnShowListener.showMessage("" + builder.toString());
    }

    void onFinishShow() throws RemoteException {
        Bundle inoutBundle = DukptConfigs.getTrackIPEKBundle();
        Log.d(TAG, "cardNum =" + cardNum);
        if (TextUtils.isEmpty(cardNum) || TextUtils.equals(cardNum, "NULL")) {
            cardNum = getTag("5A", inoutBundle);
        }
        Log.d(TAG, "cardNum after =" + cardNum);
        mAlertDialogOnShowListener.dismissProgress();
        StringBuilder builder = new StringBuilder();
        builder.append("CardNum = " + cardNum + "\n");
        builder.append("CardOrg = " + CardOrgUtil.EMVGetChipKernelId(getTagByBytes("4F")) + "\n");
        List<String> tagList = EmvTagUtils.getTagList();
        String[] taglist = EmvProcessConfig.getTagList().toArray(new String[EmvProcessConfig.getTagList().size()]);
        byte[] data = new byte[1024];

        int readLength = mEmvHandler.readEmvData(taglist, data, inoutBundle);
        String ksn = inoutBundle.getString(DukptCalcObj.DUKPT_KSN);
        Log.d(TAG, "track ksn =" + ksn);
        builder.append("trackKsn = " + ksn);
        builder.append("\nIC data \n");
        for (String tag : tagList) {
            if (tag.length() == 2) {
                builder.append("00" + tag + "=" + getTag(tag, inoutBundle) + "\n");
            } else {
                String result = getTag(tag, inoutBundle);
                String ascResult = getTagByHex2asc(tag, inoutBundle);
                if ("9F03".equalsIgnoreCase(tag)) {
                    builder.append(tag + "=" + (result == null ? "000000000000" : result) + "\n");
                } else if ("9F4E".equalsIgnoreCase(tag)) {
                    builder.append(tag + "=" + ascResult + "\n");
                } else if ("5F20".equalsIgnoreCase(tag)) {
                    builder.append(tag + "=" + (ascResult == null ? "0000" : ascResult) + "\n");
                } else if ("5F30".equalsIgnoreCase(tag)) {
                    builder.append(tag + "=" + (result == null ? "0000" : result) + "\n");
                } else {
                    builder.append(tag + "=" + getTag(tag, inoutBundle) + "\n");
                }
            }
        }
        //custom tag
        builder.append("PinKsn 00C1" + "=" + getTagByHex2asc(EmvDataSource.GET_PIN_KSN_TAG_C1, inoutBundle) + "\n");
        builder.append("PinBlock 00C7" + "=" + getTag(EmvDataSource.GET_PIN_BLOCK_TAG_C7, inoutBundle) + "\n");
        builder.append("Masked pan 00C4" + "=" + getTag(EmvDataSource.GET_MASKED_PAN_TAG_C4, inoutBundle) + "\n");
        builder.append("track2 00C2" + "=" + getTag(EmvDataSource.GET_TRACK2_TAG_C2, inoutBundle) + "\n");
        builder.append("Track ksn 00C0" + "=" + getTag(EmvDataSource.GET_TRACK_KSN_TAG_C0, inoutBundle) + "\n");
        ksn = inoutBundle.getString(DukptCalcObj.DUKPT_KSN);
        Log.d(TAG, "track ksn =" + ksn);
        if (readLength > 0) {
            byte[] ARQCData = Utils.getByteArray(data, 0, readLength);
            builder.append(Utils.byte2string(ARQCData));
        }
        Log.d(TAG, "" + builder.toString());
        mAlertDialogOnShowListener.showMessage("" + builder.toString());
    }

    private int mChannel;

    public void emvPBOC(Bundle bundle, String amount) {
        int channel = bundle.getInt(ICCSearchResult.CARDOTHER) == IccReaderSlot.ICSlOT1 ? EmvChannelType.FROM_ICC : EmvChannelType.FROM_PICC;
        mChannel = channel;
        Bundle inBundle = EmvProcessConfig.getInitBundleValue(channel, "0.52", "0.22");
        cardNum = "";
        try {
            initTermConfig();
            int ret = mSDKManager.getEmvHandler().emvProcess(inBundle, mOnEmvProcessListener);
            //ARPC deal result.
            Log.d(TAG, "ret = " + ret);
            if (ret < 0) { //deal Fail

            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * Contactless check card
     *
     * @param onSearchListener
     * @throws RemoteException
     */
    public void searchRfCard(final MainActivity.OnSearchListener onSearchListener) throws RemoteException {
        final IccCardReader rfReader = mSDKManager.getIccCardReader(IccReaderSlot.RFSlOT);
        mAlertDialogOnShowListener.showProgress(getString(R.string.msg_icorrfid), new ActionItems.OnCancelCall() {
            @Override
            public void onCancel(DialogInterface dialog) {
                try {
                    //m1Reader.stopSearch();
                    rfReader.stopSearch();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
        OnSearchIccCardListener.Stub listener = new OnSearchIccCardListener.Stub() {
            @Override
            public void onSearchResult(int retCode, Bundle bundle) throws RemoteException {
                Log.d(TAG, "retCode= " + retCode);
                Log.d(TAG, "rfReader  used by RF card  = " + (bundle.getInt(ICCSearchResult.CARDOTHER) == IccReaderSlot.RFSlOT));
                Log.d(TAG, "iccCardReader used by IC  " + (bundle.getInt(ICCSearchResult.CARDOTHER) == IccReaderSlot.ICSlOT1));

                rfReader.stopSearch();
                onSearchListener.onSearchResult(retCode, bundle);
            }
        };
        rfReader.searchCard(listener, 60, new String[]{IccCardType.CPUCARD, IccCardType.AT24CXX, IccCardType.AT88SC102});
    }

    public void searchCard(final MainActivity.OnSearchListener onSearchListener) throws RemoteException {
        final IccCardReader iccCardReader = mSDKManager.getIccCardReader(IccReaderSlot.ICSlOT1);
        final IccCardReader rfReader = mSDKManager.getIccCardReader(IccReaderSlot.RFSlOT);
        mAlertDialogOnShowListener.showProgress(getString(R.string.msg_icorrfid), new ActionItems.OnCancelCall() {
            @Override
            public void onCancel(DialogInterface dialog) {
                try {
                    //m1Reader.stopSearch();
                    rfReader.stopSearch();
                    iccCardReader.stopSearch();
                    mSDKManager.getMagCardReader().stopSearch();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
        OnSearchIccCardListener.Stub listener = new OnSearchIccCardListener.Stub() {
            @Override
            public void onSearchResult(int retCode, Bundle bundle) throws RemoteException {
                Log.d(TAG, "retCode= " + retCode);
                Log.d(TAG, "rfReader  used by RF card  = " + (bundle.getInt(ICCSearchResult.CARDOTHER) == IccReaderSlot.RFSlOT));
                Log.d(TAG, "iccCardReader used by IC  " + (bundle.getInt(ICCSearchResult.CARDOTHER) == IccReaderSlot.ICSlOT1));

                rfReader.stopSearch();
                iccCardReader.stopSearch();
                mSDKManager.getMagCardReader().stopSearch();
                onSearchListener.onSearchResult(retCode, bundle);
//                mSDKManager.getBeeper().beep(retCode == ServiceResult.Success ? BeepModeConstrants.SUCCESS : BeepModeConstrants.FAIL);
            }
        };
        iccCardReader.searchCard(listener, 60, new String[]{IccCardType.CPUCARD, IccCardType.AT24CXX, IccCardType.AT88SC102});
        rfReader.searchCard(listener, 60, new String[]{IccCardType.CPUCARD, IccCardType.AT24CXX, IccCardType.AT88SC102});
        //m1Reader.searchCard(listener, 60, new String[]{IccCardType.M1CARD});
        Bundle bundle = DukptConfigs.getTrackIPEKBundle();
        //Mag CardReader
        mSDKManager.getMagCardReader().searchCard(new OnSearchMagCardListener.Stub() {
            @Override
            public void onSearchResult(int retCode, MagCardInfoEntity magCardInfoEntity) throws RemoteException {
                if (retCode == ServiceResult.Success) {
                    mAlertDialogOnShowListener.showMessage(SearchCardOrCardReaderTest.getCardInfo(magCardInfoEntity));
                    rfReader.stopSearch();
                    iccCardReader.stopSearch();
                    mSDKManager.getMagCardReader().stopSearch();
                    mSDKManager.getBeeper().beep(retCode == ServiceResult.Success ? BeepModeConstrants.SUCCESS : BeepModeConstrants.FAIL);
//                    mAlertDialogOnShowListener.showPinPad(magCardInfoEntity.getCardNo());
                }
            }
        }, 60, bundle);
    }

    private String getTag(String tag, Bundle bundle) {
        return getTagByEmv(tag, mEmvHandler, bundle);
    }

    private byte[] getTagByBytes(String tag) {
        return getTagByEmvs(tag, mEmvHandler);
    }

    public static byte[] getTagByEmvs(String tag, EmvHandler emvHandler) {
        byte[] Tag = string2byte(tag);
        try {
            byte[] tlvs = emvHandler.getTlvs(Tag, 0, new Bundle());
//            Log.d(TAG, "value =" + byte2string(tlvs));
            return tlvs;
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getTagByEmv(String tag, EmvHandler emvHandler, Bundle bundle) {
        byte[] Tag = string2byte(tag);
        try {
            byte[] tlvs = emvHandler.getTlvs(Tag, 0, bundle);
//            Log.d(TAG, "value =" + byte2string(tlvs));
            return byte2string(tlvs);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getTagByHex2asc(String tag, Bundle bundle) {
        byte[] Tag = string2byte(tag);
        try {
            byte[] tlvs = mEmvHandler.getTlvs(Tag, 0, bundle);
//            Log.d(TAG, "value =" + byte2string(tlvs));
            if (tlvs != null) {
                return new String(tlvs);
            } else {
                return null;
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void initTermConfig() throws RemoteException {
        mSDKManager.getEmvHandler().initTermConfig(EmvProcessConfig.getInitTermConfig());
        Log.d(TAG, "initTermConfig after ");
    }

    public void beep(boolean isSuccess) {
        try {
            mSDKManager.getBeeper().beep(isSuccess ? BeepModeConstrants.SUCCESS : BeepModeConstrants.FAIL);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

}
