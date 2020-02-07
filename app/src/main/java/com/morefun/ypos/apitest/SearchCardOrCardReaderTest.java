package com.morefun.ypos.apitest;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;

import com.morefun.yapi.ServiceResult;
import com.morefun.yapi.device.reader.icc.ICCSearchResult;
import com.morefun.yapi.device.reader.icc.IccCardReader;
import com.morefun.yapi.device.reader.icc.IccCardType;
import com.morefun.yapi.device.reader.icc.IccReaderSlot;
import com.morefun.yapi.device.reader.icc.OnSearchIccCardListener;
import com.morefun.yapi.device.reader.mag.MagCardInfoEntity;
import com.morefun.yapi.device.reader.mag.OnSearchMagCardListener;
import com.morefun.yapi.engine.DeviceServiceEngine;
import com.morefun.ypos.MainActivity;
import com.morefun.ypos.R;
import com.morefun.ypos.config.DukptConfigs;
import com.morefun.ypos.uitls.ActionItems;
import com.morefun.ypos.uitls.CardOrgUtil;

import static com.morefun.ypos.BaseApiTest.getString;

public class SearchCardOrCardReaderTest {
    private static final String TAG = "SearchCardTest";
    private DeviceServiceEngine mEngine;
    private MainActivity.AlertDialogOnShowListener mAlertDialogOnShowListener;

    public SearchCardOrCardReaderTest(DeviceServiceEngine mEngine, MainActivity.AlertDialogOnShowListener mAlertDialogOnShowListener) {
        this.mEngine = mEngine;
        this.mAlertDialogOnShowListener = mAlertDialogOnShowListener;
    }

    public void searchRFCard(final String[] cardtype, final MainActivity.OnSearchListener onSearchListener) {
        if (mEngine == null) {
            //Please check NullPointException
            return;
        }
        try {
            final IccCardReader rfReader = mEngine.getIccCardReader(IccReaderSlot.RFSlOT);
            mAlertDialogOnShowListener.showProgress(getString(R.string.msg_rfcard), new ActionItems.OnCancelCall() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    try {
                        Log.v(TAG, "rfReader.stopSearch();");
                        rfReader.stopSearch();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            });
            OnSearchIccCardListener.Stub listener = new OnSearchIccCardListener.Stub() {
                @Override
                public void onSearchResult(int retCode, Bundle bundle) throws RemoteException {
//                    ICCSearchResult.CARDTYPE
                    if (retCode == 0) {
                        Log.d(TAG, "retCode= " + retCode);
                        Log.d(TAG, "cardType:" + bundle.getString(ICCSearchResult.CARDTYPE));
                        rfReader.stopSearch();
                        beeper(retCode);
                        onSearchListener.onSearchResult(retCode, bundle);
                    } else {
                        mAlertDialogOnShowListener.dismissProgress();
                    }
                }
            };
            rfReader.searchCard(listener, 30, cardtype);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void searchCPUCard(final String[] cardtype, final MainActivity.OnSearchListener onSearchListener) {
        try {
            final IccCardReader icReader = mEngine.getIccCardReader(IccReaderSlot.ICSlOT1);
            final IccCardReader rfReader = mEngine.getIccCardReader(IccReaderSlot.RFSlOT);

            mAlertDialogOnShowListener.showProgress(getString(R.string.msg_icorrfid), new ActionItems.OnCancelCall() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    try {
                        icReader.stopSearch();
                        rfReader.stopSearch();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            });
            OnSearchIccCardListener.Stub listener = new OnSearchIccCardListener.Stub() {
                @Override
                public void onSearchResult(int retCode, Bundle bundle) throws RemoteException {
                    Log.d(TAG, "retCode:" + retCode
                            + ", cardType:" + bundle.getString(ICCSearchResult.CARDTYPE)
                            + ", cardOther:" + bundle.getInt(ICCSearchResult.CARDOTHER)
                            + ", is contact :" + (bundle.getInt(ICCSearchResult.CARDOTHER) == IccReaderSlot.ICSlOT1));

                    icReader.stopSearch();
                    rfReader.stopSearch();
                    beeper(retCode);
                    onSearchListener.onSearchResult(retCode, bundle);
                }
            };
            icReader.searchCard(listener, 30, cardtype);
            rfReader.searchCard(listener, 30, cardtype);

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void ICCardReaderApi() throws RemoteException {
        //RF ICCCard
        final IccCardReader iccCardReader = mEngine.getIccCardReader(IccReaderSlot.ICSlOT1);
        final IccCardReader rfReader = mEngine.getIccCardReader(IccReaderSlot.RFSlOT);
        final IccCardReader m1Reader = mEngine.getIccCardReader(IccReaderSlot.RFSlOT);
        mAlertDialogOnShowListener.showProgress(getString(R.string.msg_running), new ActionItems.OnCancelCall() {
            @Override
            public void onCancel(DialogInterface dialog) {
                try {
                    m1Reader.stopSearch();
                    rfReader.stopSearch();
                    iccCardReader.stopSearch();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
        OnSearchIccCardListener.Stub listener = new OnSearchIccCardListener.Stub() {
            @Override
            public void onSearchResult(final int retCode, Bundle bundle) throws RemoteException {
//                    ICCSearchResult.CARDTYPE
                String cardType = bundle.getString(ICCSearchResult.CARDTYPE);
                int cardOther = bundle.getInt(ICCSearchResult.CARDOTHER, -1);
                Log.d(TAG, "retCode= " + retCode + "," + cardType + "," + cardOther);
                m1Reader.stopSearch();
                rfReader.stopSearch();
                iccCardReader.stopSearch();
                beeper(retCode);
                mAlertDialogOnShowListener.showMessage("OnSearchIccCardListener result = " + (retCode == ServiceResult.Success));
            }
        };
        //start serach Card API
        iccCardReader.searchCard(listener, 60, new String[]{IccCardType.CPUCARD, IccCardType.AT24CXX, IccCardType.AT88SC102});
        rfReader.searchCard(listener, 60, new String[]{IccCardType.CPUCARD, IccCardType.AT24CXX, IccCardType.AT88SC102});
        m1Reader.searchCard(listener, 60, new String[]{IccCardType.M1CARD, IccCardType.AT24CXX, IccCardType.AT88SC102});
    }

    private void beeper(int retCode) throws RemoteException {
//        mEngine.getBeeper().beep(retCode == ServiceResult.Success ? BeepModeConstrants.SUCCESS : BeepModeConstrants.FAIL);
    }


    public void MagCardReaderApi() throws RemoteException {
        mAlertDialogOnShowListener.showProgress(getString(R.string.msg_running), new ActionItems.OnCancelCall() {
            @Override
            public void onCancel(DialogInterface dialog) {
                try {
                    mEngine.getMagCardReader().stopSearch();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
        Bundle bundle = DukptConfigs.getTrackIPEKBundle();
        mEngine.getMagCardReader().searchCard(new OnSearchMagCardListener.Stub() {
            @Override
            public void onSearchResult(int retCode, MagCardInfoEntity magCardInfoEntity) throws RemoteException {
                if (retCode == ServiceResult.Success) {
                    mAlertDialogOnShowListener.dismissProgress();
                    mAlertDialogOnShowListener.showMessage(getCardInfo(magCardInfoEntity));
                    mEngine.getMagCardReader().stopSearch();
                    mAlertDialogOnShowListener.showPinPad(magCardInfoEntity.getCardNo());
                }
            }
        }, 60, bundle);// second
    }

    public static String getCardInfo(MagCardInfoEntity magCardInfoEntity) {
        Log.d(TAG, "ret ===" + magCardInfoEntity.getCardNo());
        Log.d(TAG, "Tk1 ===" + magCardInfoEntity.getTk1());
        Log.d(TAG, "Tk2 ===" + magCardInfoEntity.getTk2());
        Log.d(TAG, "Tk3 ===" + magCardInfoEntity.getTk3());
        Log.d(TAG, "trackKSN ===" + magCardInfoEntity.getKsn());
        Log.d(TAG, "ServiceCode ===" + magCardInfoEntity.getServiceCode());
        StringBuilder builder = new StringBuilder();
        builder.append("Card: " + magCardInfoEntity.getCardNo());
        builder.append("CardOrg: " + CardOrgUtil.EMVGetMagKernelId(magCardInfoEntity.getCardNo()));
        builder.append("\nTk1: " + magCardInfoEntity.getTk1());
        builder.append("\nTk2: " + magCardInfoEntity.getTk2());
        builder.append("\nTk3: " + magCardInfoEntity.getTk3());
        builder.append("\ntrackKSN: " + magCardInfoEntity.getKsn());
        builder.append("\nServiceCode: " + magCardInfoEntity.getServiceCode());
        return builder.toString();
    }

    public void checkCardIsExits() throws RemoteException {
        IccCardReader iccCardReader = mEngine.getIccCardReader(IccReaderSlot.RFSlOT);
        boolean cardExists = iccCardReader.isCardExists();
        StringBuilder builder = new StringBuilder();
        builder.append("check Contactless have the card = " + cardExists);
        iccCardReader = mEngine.getIccCardReader(IccReaderSlot.ICSlOT1);
        cardExists = iccCardReader.isCardExists();
        builder.append("\ncheck Contact have the Card = " + cardExists);
        mAlertDialogOnShowListener.showMessage(builder.toString());
    }
}
