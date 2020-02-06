package com.morefun.ypos.uitls;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.Hashtable;

/**
 * ZXingUtils  based on zxing;
 * so  must
 * implementation 'com.google.zxing:core:3.3.0'
 * In Your Project build.gradle.
 * 生成条形码和二维码的工具
 */
public class ZXingUtils {
    /**
     * Generates a QR code
     *
     * @param url    QR content
     * @param width  QR Code  width
     * @param height QR Code  height
     * @return Bitmap
     */
    public static Bitmap createQRImage(String url, final int width, final int height) throws WriterException {
        // 判断URL合法性
        if (url == null || "".equals(url) || url.length() < 1) {
            return null;
        }
        Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        // 图像数据转换，使用了矩阵转换
        BitMatrix bitMatrix = new QRCodeWriter().encode(url,
                BarcodeFormat.QR_CODE, width, height, hints);
        int[] pixels = new int[width * height];
        // 下面这里按照二维码的算法，逐个生成二维码的图片，
        // 两个for循环是图片横列扫描的结果
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (bitMatrix.get(x, y)) {
                    pixels[y * width + x] = 0xff000000;
                } else {
                    pixels[y * width + x] = 0xffffffff;
                }
            }
        }
        // 生成二维码图片的格式，使用ARGB_8888
        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    /**
     * Generates a Bar code
     *
     * @param context
     * @param contents      Bar Code Content
     * @param desiredWidth  Bar Code Width
     * @param desiredHeight Bar Code Height
     * @param displayCode   under Bar Code Content
     * @return
     */
    public static Bitmap creatBarcode(Context context, String contents, BarcodeFormat barcodeFormat,
                                      int desiredWidth, int desiredHeight, boolean displayCode) throws WriterException {
        Bitmap ruseltBitmap = null;
        /**
         * 图片两端所保留的空白的宽度
         */
        int marginW = 20;
        /**
         * Type
         */
//        BarcodeFormat barcodeFormat = BarcodeFormat.CODE_128;

        ruseltBitmap = encodeAsBitmap(contents, barcodeFormat,
                desiredWidth, desiredHeight);
        return ruseltBitmap;
    }


    private static Bitmap encodeAsBitmap(String contents,
                                         BarcodeFormat format, int desiredWidth, int desiredHeight) throws WriterException {
        final int WHITE = 0xFFFFFFFF;
        final int BLACK = 0xFF000000;

        MultiFormatWriter writer = new MultiFormatWriter();
        BitMatrix result = null;
        result = writer.encode(contents, format, desiredWidth,
                desiredHeight, null);

        int width = result.getWidth();
        int height = result.getHeight();
        int[] pixels = new int[width * height];
        // All are 0, or black, by default
        for (int y = 0; y < height; y++) {
            int offset = y * width;
            for (int x = 0; x < width; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }


}