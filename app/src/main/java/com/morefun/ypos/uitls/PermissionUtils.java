package com.morefun.ypos.uitls;/*
 * Copyright (c) 2016, The Linux Foundation. All rights reserved.

 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above
 *       copyright notice, this list of conditions and the following
 *       disclaimer in the documentation and/or other materials provided
 *       with the distribution.
 *     * Neither the name of The Linux Foundation nor the names of its
 *       contributors may be used to endorse or promote products derived
 *       from this software without specific prior written permission.

 * THIS SOFTWARE IS PROVIDED "AS IS" AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT
 * ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR
 * BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN
 * IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */


import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

import java.util.ArrayList;

public class PermissionUtils {
    public enum PermissionType {
        RECORD, PLAY
    }

    private static String[] RECORD_PERMISSIONS = {
            Manifest.permission.READ_PHONE_STATE, Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAPTURE_AUDIO_OUTPUT,
            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE
    };

    private static String[] PLAY_PERMISSIONS = {
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    public static String[] getOperationPermissions(PermissionType type) {
        switch (type) {
            case RECORD:
                return RECORD_PERMISSIONS;
            case PLAY:
                return PLAY_PERMISSIONS;
            default:
                return null;
        }
    }

    public static boolean checkPermissions(final Activity activity, PermissionType type,
                                           int operationHandle) {
        String[] permissions = getOperationPermissions(type);
        return checkPermissions(activity, permissions, operationHandle);
    }

    public static boolean checkPermissions(final Activity activity, String[] permissions,
                                           int operationHandle) {
        if (permissions == null)
            return true;
        boolean isPermissionGranted = true;
        ArrayList<String> permissionList = new ArrayList<String>();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            return true;
        }
        for (String permission : permissions) {
            if (PackageManager.PERMISSION_GRANTED != activity.checkSelfPermission(permission)) {
                permissionList.add(permission);
                isPermissionGranted = false;
            }
        }

        if (!isPermissionGranted) {
            String[] permissionArray = new String[permissionList.size()];
            permissionList.toArray(permissionArray);
            activity.requestPermissions(permissionArray, operationHandle);
        }

        return isPermissionGranted;
    }

    public static boolean checkPermissionResult(String[] permissions, int[] grantResults) {
        if (permissions == null || grantResults == null || permissions.length == 0
                || grantResults.length == 0) {
            return false;
        }

        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }
}
