package com.morefun.ypos.config;

import android.os.Bundle;
import android.os.RemoteException;

import com.morefun.yapi.emv.EmvDataSource;
import com.morefun.yapi.emv.EmvHandler;
import com.morefun.ypos.uitls.HexUtil;

import java.util.Arrays;
import java.util.List;

import static com.morefun.ypos.uitls.Utils.string2byte;

public class EmvTagHelper {

    private EmvHandler mEmvHandler;
    Bundle inoutBundle;
    public EmvTagHelper(EmvHandler mEmvHandler, Bundle inoutBundle) {
        this.mEmvHandler = mEmvHandler;
        this.inoutBundle = inoutBundle;
    }

    /**
     * TODO : Please add any EMV tag, if you need
     *
     * @return
     */
    public static List<String> getTagList() {
        return Arrays.asList("9F16", "9F34", "9F06", "5F30", "9F33", "57", "9F02", "9F03", "9F10", "9F1A", "9F1E", "9F21", "9F26", "9F27", "9F36", "9F37"
                , "9F4E", "9F6E", "4F", "50", "82", "84", "95", "9A", "9C", "5F20", "5F24", "5F2A", "5F2D", "5F34");
    }

    public String getICCardData(){
        StringBuilder builder  = new StringBuilder();
        List<String> tagList = getTagList();
        for (String tag : tagList) {
            if (tag.length() == 2) {
                builder.append("00" + tag + "=" + getPBOCData(tag, true) + "\n");
            } else {
                String result = getPBOCData(tag, true);
                if ("9F03".equalsIgnoreCase(tag)) {
                    builder.append(tag + "=" + (result == null ? "000000000000" : result) + "\n");
                } else if ("9F4E".equalsIgnoreCase(tag)) {
                    builder.append(tag + "=" + getPBOCData(tag, false) + "\n");
                } else if ("5F20".equalsIgnoreCase(tag)) {
                    builder.append(tag + "=" + (getPBOCData(tag, false) == null ? "0000" : getPBOCData(tag, false)) + "\n");
                } else if ("5F30".equalsIgnoreCase(tag)) {
                    builder.append(tag + "=" + (result == null ? "0000" : result) + "\n");
                } else {
                    builder.append(tag + "=" + getPBOCData(tag, true) + "\n");
                }
            }
        }
        //custom tag
        builder.append("PinKsn 00C1" + "=" + getPBOCData(EmvDataSource.GET_PIN_KSN_TAG_C1, true) + "\n");
        builder.append("PinBlock 00C7" + "=" + getPBOCData(EmvDataSource.GET_PIN_BLOCK_TAG_C7, true) + "\n");
        builder.append("Masked pan 00C4" + "=" + getPBOCData(EmvDataSource.GET_MASKED_PAN_TAG_C4, true) + "\n");
        builder.append("track2 00C2" + "=" + getPBOCData(EmvDataSource.GET_TRACK2_TAG_C2, true) + "\n");
        builder.append("Track ksn 00C0" + "=" + getPBOCData(EmvDataSource.GET_TRACK_KSN_TAG_C0, true) + "\n");
        return builder.toString();
    }

    public String getPBOCData(String tag, boolean isHex) {
        byte[] Tag = string2byte(tag);
        try {
            byte[] tlvs = mEmvHandler.getTlvs(Tag, 0, inoutBundle);
            if (tlvs != null) {
                if (isHex){
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
