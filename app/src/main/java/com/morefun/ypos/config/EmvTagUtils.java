package com.morefun.ypos.config;

import android.os.Build;
import android.util.Log;

import com.morefun.yapi.device.pinpad.DukptCalcObj;
import com.morefun.yapi.emv.EmvDataSource;
import com.morefun.yapi.emv.EmvHandler;

import java.util.Arrays;
import java.util.List;

public class EmvTagUtils {

    private EmvHandler mEmvHandler;

    public EmvTagUtils(EmvHandler mEmvHandler) {
        this.mEmvHandler = mEmvHandler;
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
        /*StringBuilder builder  = new StringBuilder();
        List<String> tagList = getTagList();
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
        return builder.toString();*/
        return null;
    }

}
