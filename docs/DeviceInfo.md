

#### Device info

```
Bundle devInfo = mSDKManager.getDevInfo();
        String vendor = devInfo.getString(DeviceInfoConstrants.COMMOM_VENDOR);
        String model = devInfo.getString(DeviceInfoConstrants.COMMOM_MODEL);
        String os_ver = devInfo.getString(DeviceInfoConstrants.COMMOM_OS_VER);
        String sn = devInfo.getString(DeviceInfoConstrants.COMMOM_SN);
        String tusn = devInfo.getString(DeviceInfoConstrants.TID_SN);
        String versionCode = devInfo.getString(DeviceInfoConstrants.COMMON_SERVICE_VER);
        String hardware = devInfo.getString("hardware");
```



