package com.morefun.ypos.uitls;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.util.Log;

import java.io.File;
import java.util.List;

public class SilentInstallUtils {

    public static void silentInstallApp(Context context, File apkFile) {
//        AppInstallReceiver.isInstall =true;
        Intent install_intent = new Intent("android.intent.action.INSTALL.HIDE");
        if (Build.VERSION.SDK_INT >= 24) {
            Uri apkUri = getUri(context, apkFile);
            install_intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
//            getPermission(context, install_intent , apkUri);
            install_intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else {
            install_intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
            install_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        if (isIntentAvailable(context, install_intent)) {//
            context.startActivity(install_intent);
        } else {
            installApp(context, apkFile);
        }
    }

    //Intent available
    private static boolean isIntentAvailable(Context context, Intent intent) {
        List resolves = context.getPackageManager().queryIntentActivities(intent, 0);
        return resolves.size() > 0;
    }

    /**
     * install app
     *
     * @param context
     * @param apkFile
     */
    public static void installApp(Context context, File apkFile) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 24) {
            Uri apkUri = getUri(context, apkFile);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
//            getPermission(context, intent , apkUri);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
        }

        if (isIntentAvailable(context, intent)) {
            context.startActivity(intent);
        } else {
            Log.d(TAG, "apkFile = " + apkFile.getName());
        }
    }

    private static final String TAG = "SilentInstallUtils";

    public static Uri getUri(Context context, File apkFile) {
        try {
            return FileProvider.getUriForFile(context, context.getPackageName() + ".fileProvider", apkFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void getPermission(Context context, Intent intent, Uri fileUri) {
        String packageName = "com.android.packageinstaller";
        context.grantUriPermission(packageName, fileUri, Intent.FLAG_GRANT_READ_URI_PERMISSION
                | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
    }
}
