package com.morefun.ypos.config;

import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;

import com.morefun.yapi.device.pinpad.DesAlgorithmType;
import com.morefun.yapi.device.pinpad.DukptKeyGid;
import com.morefun.yapi.device.pinpad.KSNConstrants;
import com.morefun.yapi.device.pinpad.PinPad;

import static com.morefun.yapi.device.pinpad.DukptKeyType.MF_DUKPT_DES_KEY_DATA1;
import static com.morefun.yapi.device.pinpad.DukptKeyType.MF_DUKPT_DES_KEY_MAC1;
import static com.morefun.yapi.device.pinpad.DukptKeyType.MF_DUKPT_DES_KEY_PIN;

public class DukptConfigs {
    public static final int TRACK_GROUP_INDEX = DukptKeyGid.GID_GROUP_TRACK_IPEK;
    public static final int EMV_GROUP_INDEX = DukptKeyGid.GID_GROUP_EMV_IPEK;
    public static final int PIN_GROUP_INDEX = DukptKeyGid.GID_GROUP_PIN_IPEK;
    //    public static final int TRACK_GROUP_INDEX = DukptKeyGid.GID_GROUP_TRACK_IPEK2;
//    public static final int EMV_GROUP_INDEX = DukptKeyGid.GID_GROUP_EMV_IPEK2;
//    public static final int PIN_GROUP_INDEX = DukptKeyGid.GID_GROUP_PIN_IPEK2;
    public static boolean isDukpt = false;
    public static DukptConfigs mDukptConfigs;

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
        String IPEK = "C1D0F8FB4958670DBA40AB1F3752EF0D";
        //KSN must be 20 length String. 95A62700021021000000
        String ksn = "FFFF9876543210" + "000000";
        int ret = pinPad.initDukptIPEKAndKsn(PIN_GROUP_INDEX, IPEK, ksn, true, "00000");
        IPEK = "C1D0F8FB4958670DBA40AB1F3752EF0D";
        ksn = "00000123456789" + "000000";
        ret = pinPad.initDukptIPEKAndKsn(TRACK_GROUP_INDEX, IPEK, ksn, true, "00000");
        //ksn = "00001122334455000000";
        ksn = "00001122334455" + "000000";
        ret = pinPad.initDukptIPEKAndKsn(EMV_GROUP_INDEX, IPEK, ksn, true, "00000");
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

    String trackKsn;
    String emvKsn;
    String pinKsn;

    public void increaseKSN(PinPad pinPad) throws RemoteException {
        //TODO plsease save the ksn to you Application.
        // one transaction can only be called once, Every time you get it, the PEK key changes
        //  need to get the ksn first before search card.
        // increaseKSN API : Generate PEK and return new KSN
        trackKsn = pinPad.increaseKSN(TRACK_GROUP_INDEX, new Bundle());
        emvKsn = pinPad.increaseKSN(EMV_GROUP_INDEX, new Bundle());
        pinKsn = pinPad.increaseKSN(PIN_GROUP_INDEX, new Bundle());
        //check 57=
    }

    public static Bundle getMacIPEKBundle() {
        return getBundle(MF_DUKPT_DES_KEY_MAC1, EMV_GROUP_INDEX);
    }

    //keyType DUKPT_PIN
    public static Bundle getPinIPEKBundle() {
        return getBundle(MF_DUKPT_DES_KEY_PIN, PIN_GROUP_INDEX);
    }

    public static Bundle getTrackIPEKBundle() {
        return getBundle(MF_DUKPT_DES_KEY_DATA1, TRACK_GROUP_INDEX);
    }

    /**
     * dukpt
     *
     * @retur
     */
    public static Bundle getBundle(Byte keyType, int key_index) {
        Bundle bundle = new Bundle();
        //default value is  DukptKeyType.MF_DUKPT_DES_KEY_DATA1
        bundle.putByte(KSNConstrants.DukptKeyType, keyType);
        //default is 0
        bundle.putInt(KSNConstrants.DUKPT_KEY_GID, key_index);
        Log.d("DukptConfigs", "gid =" + key_index);
        //default value is  DesAlgorithmType.TDES_CBC
        bundle.putInt(KSNConstrants.DesAlgorithmType, DesAlgorithmType.TDES_CBC);
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
