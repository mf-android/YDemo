package com.morefun.ysdk.sample.utils;

import android.os.Bundle;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;

import com.morefun.yapi.emv.EMVTag9CConstants;
import com.morefun.yapi.emv.EmvTermCfgConstrants;
import com.morefun.yapi.emv.EmvTransDataConstrants;
import com.morefun.ysdk.sample.device.DeviceHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class EmvUtil {

    private final static String TAG = EmvUtil.class.getName();

    public static final String[] arqcTLVTags = new String[]{
            "9F26",
            "9F27",
            "9F10",
            "9F37",
            "9F36",
            "95",
            "9A",
            "9C",
            "9F02",
            "5F2A",
            "82",
            "9F1A",
            "9F33",
            "9F34",
            "9F35",
            "9F1E",
            "84",
            "9F09",
            "9F63"
    };

    public static final String[] tags = new String[]{
            "9F26",
            "9F27",
            "9F10",
            "9F37",
            "9F36",
            "95",
            "9A",
            "9C",
            "9F02",
            "5F2A",
            "82",
            "9F1A",
            "9F03",
            "9F33",
            "9F34",
            "9F35",
            "9F1E",
            "84",
            "9F09",
            "9F41",
            "9F63"
    };


    public static byte[] getExampleARPCData() {
        //TODO Data returned by background server ,should be contain 91 tag, if you need to test ARPC
        // such as : 91 0A F9 8D 4B 51 B4 76 34 74 30 30 ,   if need to set 71 and 72  ,Please add this String
        return HexUtil.hexStringToByte("910AF98D4B51B47634743030");
    }

    public static Bundle getInitTermConfig() {
        Bundle bundle = new Bundle();
        bundle.putByteArray(EmvTermCfgConstrants.TERMCAP, new byte[]{(byte) 0x20, (byte) 0x68, (byte) 0x08});
        bundle.putByteArray(EmvTermCfgConstrants.ADDTERMCAP, new byte[]{(byte) 0xF2, (byte) 0x00, (byte) 0xF0, (byte) 0xA0, (byte) 0x01});
        bundle.putByte(EmvTermCfgConstrants.TERMTYPE, (byte) 0x22);
        bundle.putByteArray(EmvTermCfgConstrants.COUNTRYCODE, new byte[]{(byte) 0x03, (byte) 0x56});
        bundle.putByteArray(EmvTermCfgConstrants.CURRENCYCODE, new byte[]{(byte) 0x03, (byte) 0x56});
        bundle.putByteArray(EmvTermCfgConstrants.TRANS_PROP_9F66, new byte[]{0x36, (byte) 0x00, (byte) 0xc0, (byte) 0x00});
        return bundle;
    }

    public static Bundle getInitBundleValue(int channelType, String amount, String cashBackAmt) {
        Bundle bundle = new Bundle();

        String date = getCurrentTime("yyMMddHHmmss");

        bundle.putInt(EmvTransDataConstrants.MKEYIDX, 1);
        bundle.putBoolean(EmvTransDataConstrants.ISSUPPORTEC, false);
        bundle.putInt(EmvTransDataConstrants.PROCTYPE, 0);
        bundle.putInt(EmvTransDataConstrants.ISQPBOCFORCEONLINE, 0);
        bundle.putInt(EmvTransDataConstrants.CHANNELTYPE, channelType);

        bundle.putByte(EmvTransDataConstrants.B9C, (byte) EMVTag9CConstants.EMV_TRANS_SALE);
        bundle.putString(EmvTransDataConstrants.TRANSDATE, date.substring(0, 6));
        bundle.putString(EmvTransDataConstrants.TRANSTIME, date.substring(6, 12));
        bundle.putString(EmvTransDataConstrants.SEQNO, "00001");

        bundle.putString(EmvTransDataConstrants.TRANSAMT, amount);
        bundle.putString(EmvTransDataConstrants.CASHBACKAMT, cashBackAmt);

        bundle.putString(EmvTransDataConstrants.MERNAME, "MOREFUN");
        bundle.putString(EmvTransDataConstrants.MERID, "488923");
        bundle.putString(EmvTransDataConstrants.TERMID, "4999000");
        bundle.putString(EmvTransDataConstrants.CONTACTLESS_PIN_FREE_AMT, "200000");
        bundle.putStringArrayList(EmvTransDataConstrants.TERMINAL_TLVS, createArrayList("DF81180170", "DF81190118"));
        return bundle;
    }

    public static String readPan() {
        String pan = getPbocData("5A", true);
        if (!TextUtils.isEmpty(pan) && pan.endsWith("F")) {
            return pan.substring(0, pan.length() - 1);
        } else {
            return getPanFromTrack2();
        }
    }

    public static String getPbocData(String tagName, boolean isHex) {
        try {
            byte[] data = new byte[512];
            Log.d(TAG, "getPbocData Tag:" + tagName);
            int len = DeviceHelper.getEmvHandler().readEmvData(new String[]{tagName.toUpperCase()}, data, new Bundle());
            if (len > 0) {
                TlvData tlvData = TlvData.fromRawData(HexUtil.subByte(data, 0, len), 0);
                if (isHex) {
                    return tlvData.getValue();
                } else {
                    return tlvData.getGBKValue();
                }
            }
            return null;
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static String readTrack2() {
        String track2 = getPbocData("57", true);
        if (!TextUtils.isEmpty(track2) && track2.endsWith("F")) {
            return track2.substring(0, track2.length() - 1);
        }
        return track2;
    }

    protected static String getPanFromTrack2() {
        String track2 = readTrack2();
        if (track2 != null) {

            for (int i = 0; i < track2.length(); i++) {
                if (track2.charAt(i) < '0' || track2.charAt(i) > '9') {
                    return track2.substring(0, i);
                }
            }
        }
        return null;
    }

    public static String getTLVDatas(String[] tags) {
        for (int i = 0; i < tags.length; i++) {
            tags[i] = tags[i].toUpperCase();
        }
        try {
            byte[] buffer = new byte[1024];
            int byteNum = DeviceHelper.getEmvHandler().readEmvData(tags, buffer, new Bundle());
            if (byteNum > 0) {
                return HexUtil.bytesToHexString(HexUtil.subByte(buffer, 0, byteNum));
            } else {
                return "";
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getCurrentTime(String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        Date curDate = new Date(System.currentTimeMillis());
        return df.format(curDate);
    }

    public static <T> ArrayList<T> createArrayList(T ... elements) {
        ArrayList<T> list = new ArrayList<T>();
        for (T element : elements) {
            list.add(element);
        }
        return list;
    }
}
