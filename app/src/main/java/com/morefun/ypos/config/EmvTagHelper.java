package com.morefun.ypos.config;

import android.os.Bundle;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;

import com.morefun.yapi.emv.EmvDataSource;
import com.morefun.yapi.emv.EmvHandler;
import com.morefun.ypos.uitls.HexUtil;
import com.morefun.ypos.uitls.Utils;

import java.util.Arrays;
import java.util.List;

import static com.morefun.ypos.uitls.Utils.string2byte;

public class EmvTagHelper {
    private static final String TAG = "EmvTagHelper";
    private EmvHandler mEmvHandler;

    public EmvTagHelper(EmvHandler mEmvHandler) {
        this.mEmvHandler = mEmvHandler;
    }

    /**
     * TODO : Please add any EMV tag, if you need
     *
     * @return
     */
    public static List<String> getTagList() {
        return Arrays.asList("9F07","9F35","9F34", "9F33", "57", "9F02", "9F03", "9F10", "9F1A", "9F1E", "9F21", "9F26", "9F27", "9F36", "9F37"
                , "9F4E", "9F6E", "4F", "50", "82", "84", "95", "9A", "9C", "5F24", "5F2A", "5F2D", "5F34");
        //"5F20",
    }

    private static final String LINE_BREAK = "\n";

    /**
     * TAG PBOC DATA FROM CARD
     *
     * @return
     */
    public String getTapPBOCData() {
        StringBuilder builder = new StringBuilder();
        List<String> tagList = getTagList();
        for (String tag : tagList) {
            if (tag.length() == 2) {
                builder.append("00").append(tag).append("=");
                if ("50".equalsIgnoreCase(tag)) {
                    builder.append(getPBOCData(tag, false) + LINE_BREAK);
                } else {
                    builder.append(getPBOCData(tag, true) + LINE_BREAK);
                }
            } else {
                String result = getPBOCData(tag, true);
                if ("9F03".equalsIgnoreCase(tag)) {
                    builder.append(tag + "=" + getPBOCHex(tag, 12) + LINE_BREAK);
                } else if ("9F07".equalsIgnoreCase(tag)) {
                    builder.append(tag + "=" + getPBOCHex(tag, 4) + LINE_BREAK);
                } else if ("9F4E".equalsIgnoreCase(tag)) {
                    builder.append(tag + "=" + Utils.flushLeft('0', 30, result) + LINE_BREAK);
                } else if ("5F24".equalsIgnoreCase(tag)) {
                    builder.append(tag + "=" + getPBOCHex(tag, 6) + LINE_BREAK);
                } else if ("5F30".equalsIgnoreCase(tag)) {
                    builder.append(tag + "=" + getPBOCHex(tag, 4) + LINE_BREAK);
                } else {
                    builder.append(tag + "=" + getPBOCData(tag, true) + LINE_BREAK);
                }
            }
        }
        //custom tag
        String C7 = getPBOCData(EmvDataSource.GET_PIN_BLOCK_TAG_C7, true);
        if (!TextUtils.isEmpty(C7)){
            builder.append("00C1" + "=" + getPBOCData(EmvDataSource.GET_PIN_KSN_TAG_C1, false) + LINE_BREAK);
            builder.append("00C7" + "=" + C7 + LINE_BREAK);
        }
        builder.append("00C4" + "=" + getPBOCData(EmvDataSource.GET_MASKED_PAN_TAG_C4, true) + LINE_BREAK);
        builder.append("00C2" + "=" + getPBOCData(EmvDataSource.GET_TRACK2_TAG_C2, true) + LINE_BREAK);
        builder.append("00C0" + "=" + getPBOCData(EmvDataSource.GET_TRACK_KSN_TAG_C0, true) + LINE_BREAK);
        builder.append("9F53" + "=" + getPBOCHex("9F53", 2) + LINE_BREAK);
        builder.append("9F11" + "=" + getPBOCHex("9F11", 2) + LINE_BREAK);
        builder.append("9F12" + "=" + getPBOCHex("9F12", 10) + LINE_BREAK);
        builder.append("9F09" + "=" + getPBOCData("9F09", true) + LINE_BREAK);
        builder.append("009B" + "=" + getPBOCData("9B", true) + LINE_BREAK);
//        builder.append("9F19" + "=" + getPBOCData("9F19", true) + LINE_BREAK);
        appendFromContact(builder);
        Log.d(TAG, "IC card data" + builder.toString());
        return builder.toString();
    }

    /**
     * 5F20=393135303130303536333830333237,
     * 9F16=424354455354313233343536373800,
     * 9F06=A0000000041010
     * 5F30=0226,
     * 00D0=DEB98F599E4DBDBE27414811695E9097,
     * 9F41=00001669,
     */
    public void appendFromContact(StringBuilder bder){
        Log.d(TAG, "contact" );
        StringBuilder builder = new StringBuilder();
        builder.append("5F20" + "=" + getPBOCData("5F20", true) + LINE_BREAK);
        builder.append("9F16" + "=" + getPBOCData("9F16", true) + LINE_BREAK);
        builder.append("9F06" + "=" + getPBOCData("9F06", true) + LINE_BREAK);
        builder.append("5F30" + "=" + getPBOCData("5F30", true) + LINE_BREAK);
        builder.append("00D0" + "=" + getPBOCData("00D0", true) + LINE_BREAK);
        builder.append("9F41" + "=" + getPBOCData("9F41", true) + LINE_BREAK);
        Log.d(TAG, "contact" + builder.toString() );
        bder.append(builder.toString());
    }

    private String getPBOCHex(String tag, int length) {
        String pbocData = getPBOCData(tag, true);
        if (pbocData == null) {
           return Utils.flushLeft('0', length, "");
        }
        return pbocData;
    }

    public String getPBOCData(String tag, boolean isHex) {
        byte[] Tag = string2byte(tag);
        try {
            byte[] tlvs = mEmvHandler.getTlvs(Tag, 0, DukptConfigs.getTrackIPEKBundle());
            if (tlvs != null) {
                if (isHex) {
                    return HexUtil.bytesToHexString(tlvs);
                }
                return new String(tlvs);
            } else {
                return null;
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return null;
    }


}
