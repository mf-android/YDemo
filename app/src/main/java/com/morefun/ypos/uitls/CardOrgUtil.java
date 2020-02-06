package com.morefun.ypos.uitls;

import android.util.Log;


public class CardOrgUtil {

    public static String EMVGetMagKernelId(String pan){
        return convertKernelId(getMagKernelId(pan));
    }

    public static String EMVGetChipKernelId(byte[] bAid){
        return convertKernelId(getChipKernelId(bAid));
    }

    public static String convertKernelId(int code){
        String ret ="";
        switch (code){
            case 0x02:
                ret = "MASTER";
                break;
            case 0x03:
                ret = "VISA";
                break;
            case 0x04:
                ret = "AMEX";
                break;
            case 0x05:
                ret = "JCB";
                break;
            case 0x06:
                ret = "DISCOVER";
                break;
            case 0x07:
                ret = "PBOC";
                break;
            case 0x0D:
                ret = "RUPAY";
                break;
            default:
                break;
        }
        return ret;
    }
    private static int getMagKernelId(String sPan)        //cardtype   0---mag card    1--- nfc card
    {
        int CardInfo3 = Integer.parseInt(sPan.substring(0, 3));
        int CardInfo4 = Integer.parseInt(sPan.substring(0, 4));
        int CardInfo6 = Integer.parseInt(sPan.substring(0, 6));
        int panLength = sPan.length();
        Log.d("CardOrgUtil", "CardInfo3 = " + CardInfo3);
        Log.d("CardOrgUtil", "CardInfo4 = " + CardInfo4);
        Log.d("CardOrgUtil", "CardInfo6 = " + CardInfo6);

        if ((sPan.startsWith("34")|| sPan.startsWith("37"))){
            return 0x04;        //AMEX Head 34 37  length 15
        }else if (sPan.startsWith("4")){
            return 0x03;        //VISA Head 4
        }else if (sPan.startsWith("51") || sPan.startsWith("52")
                || sPan.startsWith("53") || sPan.startsWith("54")|| sPan.startsWith("55")){
            return 0x02;        //MASTER Head 51~55
        }else if ((sPan.startsWith("60") && ! sPan.startsWith("6011"))
                || sPan.startsWith("6521")|| sPan.startsWith("6522")){
            return 0x0D;        //RUPAY  HEAD 60  \6521\6522
        }else if (CardInfo4 >= 3528 && CardInfo4 < 3589 ) {
            return 0x05;        //JCB  3528~ 3589 length 16
        }else if ((sPan.startsWith("65") || sPan.startsWith("6011")
                || (CardInfo3 >= 644 && CardInfo3 <= 649)
                || (CardInfo6 >= 622126 && CardInfo6 <= 622925))
                && panLength == 16){
            //6011 622126-622925 644-649 65
            return 0x06;        //DISCOVER 65\6011
        }else if (sPan.startsWith("62")){
            return 0x07;        //PBOC 62 head
        }
        return 0x00;
    }

    private static int getChipKernelId(byte[] bAid) {
        if (bAid == null){
            return 0x00;
        }
        //EMV_TAG_4F
        if (0 == memcmp(bAid, new byte[]{(byte) 0xA0, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x04}, 5)) {
            return 0x02;        //MASTER
        } else if (0 == memcmp(bAid, new byte[]{(byte) 0xA0, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x03}, 5)) {
            return 0x03;        //VISA
        } else if (0 == memcmp(bAid, new byte[]{(byte) 0xA0, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x25}, 5)) {
            return 0x04;        //AMEX
        } else if (0 == memcmp(bAid, new byte[]{(byte) 0xA0, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x65}, 5)) {
            return 0x05;        //JCB
        } else if (0 == memcmp(bAid, new byte[]{(byte) 0xA0, (byte) 0x00, (byte) 0x00, (byte) 0x01, (byte) 0x52}, 5) ||
                0 == memcmp(bAid, new byte[]{(byte) 0xA0, (byte) 0x00, (byte) 0x00, (byte) 0x03, (byte) 0x24}, 5)) {
            return 0x06;        //DISCOVER
        } else if (0 == memcmp(bAid, new byte[]{(byte) 0xA0, (byte) 0x00, (byte) 0x00, (byte) 0x03, (byte) 0x33}, 5)) {
            return 0x07;        //PBOC
        } else if (0 == memcmp(bAid, new byte[]{(byte) 0xA0, (byte) 0x00, (byte) 0x00, (byte) 0x05, (byte) 0x24}, 5)) {
            return 0x0D;        //RUPAY
        }
        return 0x00;
    }

    public static int memcmp(byte [] dst , byte[] src ,  int len){
        return memcmp(dst, 0 , src, 0, len);
    }
    public static int memcmp(byte [] dst , int des_i , byte[] src , int src_i ,  int len){
        try {
            for(int i = 0; i < len ; i++){
                if(dst[i + des_i] >  src[i + src_i])
                    return 1;
                else if(dst[i + des_i] <  src[i + src_i])
                    return -1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
