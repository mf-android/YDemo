package com.morefun.ypos.uitls;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.format.Time;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Utils {

    public static byte[] getByteArray(byte[] src, int off, int size) {
        byte[] dst = new byte[size];
        memcpy(dst, 0, src, off, size);
        return dst;
    }

    public static void memset(byte[] dst, int data, int len) {
        memset(dst, 0, data, len);
    }

    public static void memset(byte[] dst, int index, int data, int len) {
        len = len < dst.length ? len : dst.length;
        try {
            for (int i = 0; i < len; i++) {
                dst[i + index] = (byte) (data & 0xff);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public static int memcpy(byte [] dst , byte[] src ,  int len){
        return memcpy(dst , 0 ,src ,0, len);
    }
    public static int memcpy(byte[] dst, int des_i, byte[] src, int src_i, int len) {
        try {
            if (len > dst.length - des_i) {
                len = dst.length - des_i;
            }
            if (len > src.length - src_i) {
                len = src.length - src_i;
            }
            for (int i = 0; i < len; i++) {
                dst[i + des_i] = src[i + src_i];
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return len;
    }

    public static String hex2asc(byte[] hex, int off, int len, int type) {
        byte[] bytes = getByteArray(hex, off, len);
        String str = hex2asc(bytes, len, type);
        return str;
    }

    // 0 左对齐 1右对齐
    public static String hex2asc(byte[] b, int len, int type) { // 二进制转字符串
        String hs = "";
        String stmp = "";
        int start = 0;
        int count = (len + 1) / 2;        // 转成需要的字节长度

        for (int n = 0; n < count; n++) {
            stmp = (java.lang.Integer.toHexString(b[n] & 0XFF)).toUpperCase();
            if (stmp.length() == 1) {
                hs = hs + "0" + stmp;
            } else {
                hs = hs + stmp;
            }
        }

        if ((len & 0x01) == 0x1 && type == 1) {    // 长度奇数且右对齐
            start = 1;
        }

        hs = StrSubString(hs, start, start + len);

        return hs;
    }

    public static String StrSubString(String str, int start, int end) {
        String tmp = "";

        try {
            if (end >= 0) {
                tmp = str.substring(start, end);
            } else {
                tmp = str.substring(start);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return tmp;
    }

    public static int getDateTime(byte[] itransDate, byte[] itransTime) {

        Time t = new Time(); //
        t.setToNow(); // 取得系统时间。

        int year = t.year;
        itransDate[0] = str2Bcd(String.valueOf(year % 100))[0];
        //LakalaDebug.LOG("year",year);
        int month = t.month + 1;
        itransDate[1] = str2Bcd(String.valueOf(month))[0];
        //LakalaDebug.LOG("month",month);
        int date = t.monthDay;
        itransDate[2] = str2Bcd(String.valueOf(date))[0];
        //LakalaDebug.LOG("date",date);
        int hour = t.hour; // 0-23
        itransTime[0] = str2Bcd(String.valueOf(hour))[0];
        //LakalaDebug.LOG("hour",hour);
        int minute = t.minute;
        itransTime[1] = str2Bcd(String.valueOf(minute))[0];
        //LakalaDebug.LOG("minute",minute);
        int second = t.second;
        itransTime[2] = str2Bcd(String.valueOf(second))[0];
        //LakalaDebug.LOG("second",second);

        return 0;
    }

    public static byte[] str2Bcd(String asc) {
        if (asc == null) return null;
        int len = asc.length();
        int mod = len % 2;
        if (mod != 0) {
            asc = "0" + asc;
            len = asc.length();
        }
        byte abt[] = new byte[len];
        if (len >= 2) {
            len = len / 2;
        }
        byte bbt[] = new byte[len];
        abt = asc.getBytes();
        int j, k;
        for (int p = 0; p < asc.length() / 2; p++) {
            if ((abt[2 * p] >= 'a') && (abt[2 * p] <= 'z')) {
                j = abt[2 * p] - 'a' + 0x0a;
            } else if ((abt[2 * p] >= 'A') && (abt[2 * p] <= 'Z')) {
                j = abt[2 * p] - 'A' + 0x0a;
            } else {
                j = abt[2 * p] - '0';
            }

            if ((abt[2 * p + 1] >= 'a') && (abt[2 * p + 1] <= 'z')) {
                k = abt[2 * p + 1] - 'a' + 0x0a;
            } else if ((abt[2 * p + 1] >= 'A') && (abt[2 * p + 1] <= 'Z')) {
                k = abt[2 * p + 1] - 'A' + 0x0a;
            } else {
                k = abt[2 * p + 1] - '0';
            }

            int a = (j << 4) + k;
            byte b = (byte) a;
            bbt[p] = b;
        }
        return bbt;
    }

    public static String pubByteToHexString(byte[] byteHex) {
        String strHex = "";
        if (byteHex == null)
            return "";
        for (int i = 0; i < byteHex.length; i++) {
            strHex = strHex + String.format("%02X", byteHex[i]);
        }
        return strHex;
    }

    public static void CreatInitDataFile(Context ctx, int id, String path) {
        File file = new File(path);
        if (file.length() > 0) return;
        byte[] buffer = rawRead(ctx, id);
        if (buffer != null) {
            try {
                FileOutputStream fs = new FileOutputStream(path);
                fs.write(buffer, 0, buffer.length);
                fs.flush();
                fs.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        chmod("777", path);
    }

    public static void copyAsstes(Context ctx, String fileName, String path) {
        File file = new File(path);
        if (file.length() > 0) return;
        byte[] buffer = rawAssets(ctx, fileName);
        if (buffer != null) {
            try {
                FileOutputStream fs = new FileOutputStream(path);
                fs.write(buffer, 0, buffer.length);
                fs.flush();
                fs.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static byte[] rawRead(Context ctx, int id) {
        byte[] buffer = null;
        try {
            InputStream is = ctx.getResources().openRawResource(id);
            int len = is.available();
            buffer = new byte[len];
            is.read(buffer);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return buffer;
    }

    public static byte[] rawAssets(Context ctx, String fileName) {
        byte[] buffer = null;
        try {
            InputStream is = ctx.getResources().getAssets().open(fileName);
            int len = is.available();
            buffer = new byte[len];
            is.read(buffer);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return buffer;
    }
    public static byte[] checkInputData(byte[] data){
        int len = ((data.length + 7) / 8 * 8);
        byte[] inputData = new byte[len];
        memset(inputData, 0, 0xFF, len);
        memcpy(inputData, data, data.length);
        return inputData;
    }
    public static void chmod(String permission, String path) {
        try {
            String command = "chmod " + permission + " " + path;
            Runtime runtime = Runtime.getRuntime();
            runtime.exec(command);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String byte2string(byte[] b) {
        if (b == null || b.length == 0) {
            return "NULL";
        }
        String asci = new String();
        for (int i = 0; i < b.length; ++i) {
            asci += String.format("%02X", b[i]);
        }
        return asci;
    }

    public static byte[] string2byte(String key) {
        if (key == null) {
            return null;
        }

        int keyLen = key.length();
        //转为bcd码，和62域一致
        if (keyLen % 2 == 1) {            // 长度奇数
            key = key + "0";        //左对齐
        }
        int bcdLen = (keyLen + 1) / 2;
        byte[] bcdByte = new byte[bcdLen];
        for (int i = 0; i < keyLen; i += 2) {
            try {
                bcdByte[i / 2] = (byte) Integer.decode("0x" + key.substring(i, i + 2)).intValue();
            } catch (Exception e) {
                Log.v("error", "asc2hex error");
            }
        }
        return bcdByte;
    }

    public static String getVersion(Context context) {
        PackageManager manager = context.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            return info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return "unknown";
        }
    }
}
