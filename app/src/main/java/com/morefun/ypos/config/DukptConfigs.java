package com.morefun.ypos.config;

import android.os.Bundle;
import android.os.RemoteException;
import com.morefun.yapi.device.pinpad.DukptCalcObj;
import com.morefun.yapi.device.pinpad.DukptLoadObj;
import com.morefun.yapi.device.pinpad.PinPad;


public class DukptConfigs {
    public static final int TRACK_GROUP_INDEX = DukptCalcObj.DukptKeyIndexEnum.KEY_INDEX_0.toInt();
    public static final int EMV_GROUP_INDEX = DukptCalcObj.DukptKeyIndexEnum.KEY_INDEX_1.toInt();
    public static final int PIN_GROUP_INDEX = DukptCalcObj.DukptKeyIndexEnum.KEY_INDEX_2.toInt();
//    public static final int TRACK_GROUP_INDEX = DukptCalcObj.DukptKeyIndexEnum.KEY_INDEX_3.toInt();
//    public static final int EMV_GROUP_INDEX = DukptCalcObj.DukptKeyIndexEnum.KEY_INDEX_4.toInt();
//    public static final int PIN_GROUP_INDEX = DukptCalcObj.DukptKeyIndexEnum.KEY_INDEX_5.toInt();
    public static boolean isDukpt = false;
    public static DukptConfigs mDukptConfigs;
    String trackKsn;
    String emvKsn;
    String pinKsn;


    private DukptConfigs() {
    }

    public static DukptConfigs getInstance() {
        if (mDukptConfigs == null) {
            synchronized (DukptConfigs.class) {
                if (mDukptConfigs == null) {
                    mDukptConfigs = new DukptConfigs();
                }
            }
        }
        return mDukptConfigs;
    }

    private static final String TAG = "DukptConfigs";

    public static void testInjectIPEK3(PinPad pinPad) throws RemoteException {
        /*String IPEK = "C1D0F8FB4958670DBA40AB1F3752EF0D";
        //KSN must be 20 length String. 95A62700021021000000
        String ksn = "FFFF9876543210" + "000000";
        DukptLoadObj dukptLoadObj = new DukptLoadObj(IPEK, ksn
                , DukptLoadObj.DukptKeyTypeEnum.DUKPT_BDK_PLAINTEXT
                , DukptLoadObj.DukptKeyIndexEnum.KEY_INDEX_0);
        int ret = pinPad.dukptLoad(dukptLoadObj);
        IPEK = "11D0F8FB4958670DBA40AB1F3752EF0D";
        ksn = "00000123456789" + "000000";
        dukptLoadObj.setKey(IPEK);
        dukptLoadObj.setKsn(ksn);
        dukptLoadObj.setKeyIndex(DukptLoadObj.DukptKeyIndexEnum.KEY_INDEX_1);
        ret = pinPad.dukptLoad(dukptLoadObj);
        //ksn = "00001122334455000000";
        ksn = "00001122334455" + "000000";
        dukptLoadObj.setKey(IPEK);
        dukptLoadObj.setKsn(ksn);
        dukptLoadObj.setKeyIndex(DukptLoadObj.DukptKeyIndexEnum.KEY_INDEX_2);
        ret = pinPad.dukptLoad(dukptLoadObj);*/
    }

    /**
     * TODO if need able dukpt, Please set login with 09000000
     *
     * @return
     */
    public static String getDukptBussinessId() {
        isDukpt = true;
        return "09000000";
    }

    public static String getBussinessId() {
        isDukpt = false;
        return "00000000";
    }


    public void increaseKSN(PinPad pinPad) throws RemoteException {
        boolean isIncrease = true;
        trackKsn = pinPad.increaseKSN(TRACK_GROUP_INDEX, isIncrease);
        emvKsn = pinPad.increaseKSN(EMV_GROUP_INDEX, true);
        pinKsn = pinPad.increaseKSN(PIN_GROUP_INDEX, true);
        //check 57=
    }

    public static Bundle getMacIPEKBundle() {
        return getBundle(EMV_GROUP_INDEX);
    }

    //keyType DUKPT_PIN
    public static Bundle getPinIPEKBundle() {
        return getBundle(PIN_GROUP_INDEX);
    }

    public static Bundle getTrackIPEKBundle() {
        return getBundle(TRACK_GROUP_INDEX);
    }

    /**
     * dukpt
     *
     * @retur
     */
    public static Bundle getBundle(int key_index) {
        Bundle bundle = new Bundle();
        bundle.putInt(DukptCalcObj.Param.DUKPT_KEY_INDEX, key_index);
        return bundle;
    }


    public String getTrackKsn() {
        return trackKsn;
    }

    public String getEmvKsn() {
        return emvKsn;
    }

    public String getPinKsn() {
        return pinKsn;
    }

}
