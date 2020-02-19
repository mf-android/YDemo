package com.morefun.ysdk.sample.utils;

import android.util.Log;

public class HexUtil {

    public static String byteToHex(byte b) {
        return ("" + "0123456789ABCDEF".charAt(0xf & b >> 4) + "0123456789ABCDEF".charAt(b & 0xf));
    }

    public static byte[] hexStringToByte(String hex) {
        if (hex == null || "".equals(hex)) {
            return null;
        }
        hex = hex.toUpperCase();
        int len = (hex.length() / 2);
        byte[] result = new byte[len];
        char[] achar = hex.toCharArray();
        for (int i = 0; i < len; i++) {
            int pos = i * 2;
            result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
        }
        return result;
    }

    public static byte asc_to_bcd(byte asc) {
        byte bcd;

        if ((asc >= '0') && (asc <= '9'))
            bcd = (byte) (asc - '0');
        else if ((asc >= 'A') && (asc <= 'F'))
            bcd = (byte) (asc - 'A' + 10);
        else if ((asc >= 'a') && (asc <= 'f'))
            bcd = (byte) (asc - 'a' + 10);
        else
            bcd = (byte) (asc - 48);
        return bcd;
    }

    public static byte[] ASCII_To_BCD(byte[] ascii, int asc_len) {
        byte[] bcd = new byte[asc_len / 2];
        int j = 0;
        for (int i = 0; i < (asc_len + 1) / 2; i++) {
            bcd[i] = asc_to_bcd(ascii[j++]);
            bcd[i] = (byte) (((j >= asc_len) ? 0x00 : asc_to_bcd(ascii[j++])) + (bcd[i] << 4));
        }
        return bcd;
    }


    public static String bcd2Str(byte[] bytes) {
        StringBuffer temp = new StringBuffer(bytes.length * 2);
        for (int i = 0; i < bytes.length; i++) {
            temp.append((byte) ((bytes[i] & 0xf0) >>> 4));
            temp.append((byte) (bytes[i] & 0x0f));
        }
        return temp.toString().substring(0, 1).equalsIgnoreCase("0") ? temp.toString().substring(1) : temp.toString();
    }

    public static String bcd2str(byte[] bcds) {
        if (null == bcds) {
            return null;
        }
        char[] ascii = "0123456789abcdef".toCharArray();
        byte[] temp = new byte[bcds.length * 2];
        for (int i = 0; i < bcds.length; i++) {
            temp[i * 2] = (byte) ((bcds[i] >> 4) & 0x0f);
            temp[i * 2 + 1] = (byte) (bcds[i] & 0x0f);
        }
        StringBuffer res = new StringBuffer();

        for (int i = 0; i < temp.length; i++) {
            res.append(ascii[temp[i]]);
        }
        return res.toString().toUpperCase();
    }


    public static byte hex2Byte(String hex) {
        char[] achar = hex.toUpperCase().toCharArray();
        byte b = (byte) (toByte(achar[0]) << 4 | toByte(achar[1]));
        return b;
    }

    private static byte toByte(char c) {
        byte b = (byte) "0123456789ABCDEF".indexOf(c);
        return b;
    }

    public static byte[] int2bytes(int num) {
        byte[] b = new byte[4];
        int mask = 0xff;
        for (int i = 0; i < 4; i++) {
            b[i] = (byte) (num >>> (24 - i * 8));
        }
        return b;
    }

    public static int bytes2int(byte[] b) {

        int mask = 0xff;
        int temp = 0;
        int res = 0;
        for (int i = 0; i < 4; i++) {
            res <<= 8;
            temp = b[i] & mask;
            res |= temp;
        }
        return res;
    }


    public static int bytes2short(byte[] b) {

        int mask = 0xff;
        int temp = 0;
        int res = 0;
        for (int i = 0; i < 2; i++) {
            res <<= 8;
            temp = b[i] & mask;
            res |= temp;
        }
        return res;
    }

    public static String getBinaryStrFromByteArr(byte[] bArr) {
        String result = "";
        for (byte b : bArr) {
            result += getBinaryStrFromByte(b);
        }
        return result;
    }


    public static String getBinaryStrFromByte(byte b) {
        String result = "";
        byte a = b;
        ;
        for (int i = 0; i < 8; i++) {
            byte c = a;
            a = (byte) (a >> 1);
            a = (byte) (a << 1);
            if (a == c) {
                result = "0" + result;
            } else {
                result = "1" + result;
            }
            a = (byte) (a >> 1);
        }
        return result;
    }

    public static String getBinaryStrFromByte2(byte b) {
        String result = "";
        byte a = b;
        ;
        for (int i = 0; i < 8; i++) {
            result = (a % 2) + result;
            a = (byte) (a >> 1);
        }
        return result;
    }

    public static String getBinaryStrFromByte3(byte b) {
        String result = "";
        byte a = b;
        ;
        for (int i = 0; i < 8; i++) {
            result = (a % 2) + result;
            a = (byte) (a / 2);
        }
        return result;
    }


    public static byte[] toByteArray(int iSource, int iArrayLen) {
        byte[] bLocalArr = new byte[iArrayLen];
        for (int i = 0; (i < 4) && (i < iArrayLen); i++) {
            bLocalArr[i] = (byte) (iSource >> 8 * i & 0xFF);

        }
        return bLocalArr;
    }

    public static byte[] xor(byte[] op1, byte[] op2) {
        if (op1.length != op2.length) {
            throw new IllegalArgumentException("参数错误，长度不一致");
        }
        byte[] result = new byte[op1.length];
        for (int i = 0; i < op1.length; i++) {
            result[i] = (byte) (op1[i] ^ op2[i]);
        }
        return result;
    }

    public static byte xorBytes(byte[] op, int start, int end) {
        byte xorResult = 0x00;
        String xorResultStr = "";
        if ((start >= 0) && (start < end) && (end > 1)) {
            for (int i = start; i < end; i++) {
                xorResult = (byte) (xorResult ^ op[i]);
                xorResultStr = xorResultStr + String.format("%02x ", xorResult);
            }
        }
        Log.i("xorBytes", "xorResultStr = " + xorResultStr);

        return xorResult;
    }

    public static final String bytesToHexString(byte[] bArray) {
        StringBuffer sb = new StringBuffer(bArray.length);

        String sTemp;
        int j = 0;
        for (int i = 0; i < bArray.length; i++) {
            sTemp = Integer.toHexString(0xFF & bArray[i]);
            if (sTemp.length() < 2)
                sb.append(0);

            sb.append(sTemp.toUpperCase());
            j++;

        }
        return sb.toString();
    }

    public static String str2HexStr(String str) {

        char[] chars = "0123456789ABCDEF".toCharArray();
        StringBuilder sb = new StringBuilder("");
        byte[] bs = str.getBytes();
        int bit;

        for (int i = 0; i < bs.length; i++) {
            bit = (bs[i] & 0x0f0) >> 4;
            sb.append(chars[bit]);
            bit = bs[i] & 0x0f;
            sb.append(chars[bit]);
        }
        return sb.toString().trim();
    }

    public static byte[] subBytes(byte[] src, int begin, int count) {
        byte[] bs = new byte[count];
        System.arraycopy(src, begin, bs, 0, count);
        return bs;
    }

    public static byte[] mergeBytes(byte[] bytesA, byte[] bytesB) {
        if (bytesA != null && bytesA.length != 0) {
            if (bytesB != null && bytesB.length != 0) {
                byte[] bytes = new byte[bytesA.length + bytesB.length];
                System.arraycopy(bytesA, 0, bytes, 0, bytesA.length);
                System.arraycopy(bytesB, 0, bytes, bytesA.length, bytesB.length);
                return bytes;
            } else {
                return bytesA;
            }
        } else {
            return bytesB;
        }
    }

    public static byte[] merge(byte[]... data) {
        if (data == null) {
            return null;
        } else {
            byte[] bytes = null;

            for(int i = 0; i < data.length; ++i) {
                bytes = mergeBytes(bytes, data[i]);
            }

            return bytes;
        }
    }

    public static byte[] subByte(byte[] srcBytes, int offset, int len) {
        if (srcBytes == null) {
            return null;
        } else if (len <= srcBytes.length && offset + len <= srcBytes.length && offset < srcBytes.length) {
            byte[] bytes;
            if (len == -1) {
                bytes = new byte[srcBytes.length - offset];
                System.arraycopy(srcBytes, offset, bytes, 0, srcBytes.length - offset);
            } else {
                bytes = new byte[len];
                System.arraycopy(srcBytes, offset, bytes, 0, len);
            }

            return bytes;
        } else {
            return null;
        }
    }


}
