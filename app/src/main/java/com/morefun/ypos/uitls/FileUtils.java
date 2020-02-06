package com.morefun.ypos.uitls;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileUtils {

    private static final String TAG = "FileUtils";
    public static final String ROOT_DIR = "/Android/data/";

    public static File getExternalStorageDirectory() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return Environment.getExternalStorageDirectory();
        }
        return null;
    }
    //
    public static String createTmpDir(Context context) {
        String sampleDir = "typeFace";
        String tmpDir = Environment.getExternalStorageDirectory().toString() + File.separator + sampleDir + File.separator;
        if (!makeDir(tmpDir)) {
            tmpDir = context.getExternalFilesDir(sampleDir).getAbsolutePath();
            if (!makeDir(sampleDir)) {
                throw new RuntimeException("create model resources dir failed :" + tmpDir);
            }
        }
        return tmpDir;
    }


    public static String getExternalCacheDir(Context context, String fileName) {
        File externalCacheDir = context.getExternalCacheDir();
        if (externalCacheDir != null && isExists(externalCacheDir.getAbsolutePath())) {
            File file = new File(externalCacheDir.getAbsolutePath() + File.separator + fileName);
            Log.d(TAG, "=============getExternalCacheDirPath=====================" + file.getPath());
            return file.getPath();
        }
        return null;
    }

    public static boolean isExists(String filePath) {
        return !TextUtils.isEmpty(filePath) && new File(filePath).exists();
    }
    public static boolean fileCanRead(String filename) {
        File f = new File(filename);
        return f.canRead();
    }

    public static boolean makeDir(String dirPath) {
        File file = new File(dirPath);
        if (!file.exists()) {
            return file.mkdirs();
        } else {
            return true;
        }
    }

    public static void copyFromAssets(AssetManager assets, String source, String dest, boolean isCover)
            throws IOException {
        File file = new File(dest);
        if (isCover || (!isCover && !file.exists())) {
            InputStream is = null;
            FileOutputStream fos = null;
            try {
                is = assets.open(source);
                String path = dest;
                fos = new FileOutputStream(path);
                byte[] buffer = new byte[1024];
                int size = 0;
                while ((size = is.read(buffer, 0, 1024)) >= 0) {
                    fos.write(buffer, 0, size);
                }
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } finally {
                        if (is != null) {
                            is.close();
                        }
                    }
                }
            }
        }
    }
}
