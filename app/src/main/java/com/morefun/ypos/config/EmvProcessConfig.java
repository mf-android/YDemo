package com.morefun.ypos.config;

import android.os.Bundle;

import com.morefun.yapi.emv.EMVTag9CConstants;
import com.morefun.yapi.emv.EmvTermCfgConstrants;
import com.morefun.yapi.emv.EmvTransDataConstrants;
import com.morefun.ypos.uitls.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.morefun.ypos.uitls.Utils.getDateTime;
import static com.morefun.ypos.uitls.Utils.pubByteToHexString;

public class EmvProcessConfig {
    public static Bundle getInitTermConfig(){
        Bundle bundle1 = new Bundle();
        bundle1.putByteArray(EmvTermCfgConstrants.TERMCAP, new byte[]{(byte) 0x20, (byte) 0x68, (byte) 0x08});
//        bundle1.putByteArray(EmvTermCfgConstrants.MERID_ANS_9F16, new byte[]{(byte) 0x50, (byte) 0x43, (byte) 0x54,(byte) 0x53, (byte) 0x31, (byte) 0x32, (byte) 0x50, (byte) 0x43, (byte) 0x54,(byte) 0x53 });
        bundle1.putByteArray(EmvTermCfgConstrants.ADDTERMCAP, new byte[]{(byte) 0xF2, (byte) 0x00, (byte)0xF0 , (byte)0xA0 , (byte)0x01});
        //bundle1.putByteArray(EmvTermCfgConstrants.TERMCAP, new byte[]{(byte) 0xE0, (byte) 0xF8, (byte) 0xC8});
        bundle1.putByte(EmvTermCfgConstrants.TERMTYPE, (byte) 0x22);
        // 0356 / 0840
        bundle1.putByteArray(EmvTermCfgConstrants.COUNTRYCODE, new byte[]{(byte) 0x03, (byte) 0x56});
        bundle1.putByteArray(EmvTermCfgConstrants.CURRENCYCODE, new byte[]{(byte) 0x03, (byte) 0x56});
//        bundle1.putByteArray(EmvTermCfgConstrants.TRANS_PROP_9F66, new byte[]{(byte) 0x05, (byte) 0x06,(byte) 0x06,(byte) 0x06});
        bundle1.putByteArray(EmvTermCfgConstrants.TRANS_PROP_9F66,new byte[]{0x36,(byte)0x00,(byte)0xc0,(byte)0x00});
        return bundle1;
    }
    public static byte[] getExampleARPCData() {
        //TODO Data returned by background server ,should be contain 91 tag, if you need to test ARPC
        // such as : 91 0A F9 8D 4B 51 B4 76 34 74 30 30 ,   if need to set 71 and 72  ,Please add this String
        return Utils.str2Bcd("91 0A F9 8D 4B 51 B4 76 34 74 30 30 =".trim());
//        return Utils.str2Bcd("91087BAA1E5500860000");
    }

    /**
     * It's optional
     * @return
     */
    private static ArrayList<String> setTerminalParamByTlvs() {
        //DF811B0130DF8122050101010101DF81180170DF811901185F2A020156
        ArrayList<String> terminalParams = new ArrayList<>();
//        terminalParams.add("DF811B0130");
//        terminalParams.add("DF8122050101010101");
        //master adjust
        terminalParams.add("DF81180170");
        terminalParams.add("DF81190118");
//        terminalParams.add("9F4005F200F0A001");
        return terminalParams;
    }

    /**
     *  getEmvHandler().emvProcess(Bundle bundle,
     * @param channelType
     * @param amount
     * @param cashBackAmt
     * @return
     */
    public static Bundle getInitBundleValue(int channelType, String amount ,String cashBackAmt) {
        Bundle bundle = new Bundle();
        byte[] transDate = new byte[3];
        byte[] transTime = new byte[3];

        getDateTime(transDate, transTime);

        bundle.putInt(EmvTransDataConstrants.MKEYIDX, 1);
        bundle.putBoolean(EmvTransDataConstrants.ISSUPPORTEC, false);
        bundle.putInt(EmvTransDataConstrants.PROCTYPE, 0);
        bundle.putInt(EmvTransDataConstrants.ISQPBOCFORCEONLINE, 0);
        bundle.putInt(EmvTransDataConstrants.CHANNELTYPE, channelType);

        bundle.putByte(EmvTransDataConstrants.B9C, getTrans9C());
        bundle.putString(EmvTransDataConstrants.TRANSDATE, pubByteToHexString(transDate));
        bundle.putString(EmvTransDataConstrants.TRANSTIME, pubByteToHexString(transTime));
        bundle.putString(EmvTransDataConstrants.SEQNO, "00001");

        bundle.putString(EmvTransDataConstrants.TRANSAMT, amount);
        bundle.putString(EmvTransDataConstrants.CASHBACKAMT, cashBackAmt);

        bundle.putString(EmvTransDataConstrants.MERNAME, "MOREFUN");
        bundle.putString(EmvTransDataConstrants.MERID, "488923");
        bundle.putString(EmvTransDataConstrants.TERMID, "4999000");
        //Pin Free Amount for contactLess
        bundle.putString(EmvTransDataConstrants.CONTACTLESS_PIN_FREE_AMT, "200000");
        //TODO For online transactions, the terminal must force to enter the password,Please set true
//        bundle.putBoolean(EmvTransDataConstrants.FORCE_ONLINE_CALL_PIN, false);
        //some additional requirements, It's optional
        bundle.putStringArrayList(EmvTransDataConstrants.TERMINAL_TLVS, setTerminalParamByTlvs());
        return bundle;
    }

    /**
     *
     * @return
     */
    public static byte getTrans9C(){
//        return EMVTag9CConstants.EMV_TRANS_CASH;
//        return EMVTag9CConstants.EMV_TRANS_CASHBACK;
        return EMVTag9CConstants.EMV_TRANS_SALE;
//        return EMVTag9CConstants.EMV_TRANS_INQUIRY;
//        return EMVTag9CConstants.EMV_CASH_DISBUERSE;
        //Rupay
//        return EMVTag9CConstants.RUPAY_VOID;
//        return EMVTag9CConstants.RUPAY_LEGACY_MONEY_ADD;
//        return EMVTag9CConstants.RUPAY_MONEY_ADD;
    }
    public static List<String> getTagList() {
        List<String> tagList = new ArrayList<>();
        tagList.add("5A");
        tagList.add("57");
        tagList.add("95");
        tagList.add("81");
        tagList.add("84");
        tagList.add("91");
        tagList.add("99");
        tagList.add("9A");
        tagList.add("9B");
        tagList.add("9C");
        tagList.add("5F20");
        tagList.add("5F24");
        tagList.add("5F25");
        tagList.add("5F2A");
        tagList.add("5F28");
        tagList.add("5F34");
        tagList.add("5F2D");
        tagList.add("82");
        tagList.add("8E");
        tagList.add("9F01");
        tagList.add("9F02");
        tagList.add("9F03");
        tagList.add("9F0D");
        tagList.add("9F0F");
        tagList.add("9F0E");
        tagList.add("9F06");
        tagList.add("9F07");
        tagList.add("9F10");
        tagList.add("9F15");
        tagList.add("9F16");
        tagList.add("9F19");
        tagList.add("9F1A");
        tagList.add("9F1C");
        tagList.add("9F1E");
        tagList.add("9F21");
        tagList.add("9F24");
        tagList.add("9F27");
        tagList.add("9F33");
        tagList.add("9F34");
        tagList.add("9F35");
        tagList.add("9F36");
        tagList.add("9F37");
        tagList.add("9F09");
        tagList.add("9F40");
        tagList.add("9F41");
        tagList.add("9F5D");
        tagList.add("9F63");
        tagList.add("9F26");
        return tagList;
    }

    /**
     * TODO : Please add any EMV tag, if you need
     * @return
     */
    public static List<String> getTagListThirdCompany() {
        return Arrays.asList("9F16","9F34","9F06","5F30","9F33", "57","9F02", "9F03", "9F10", "9F1A", "9F1E", "9F21", "9F26", "9F27", "9F36", "9F37"
                , "9F4E", "9F6E", "4F", "50", "82", "84", "95", "9A", "9C", "5F20", "5F24", "5F2A", "5F2D", "5F34");
    }
}
