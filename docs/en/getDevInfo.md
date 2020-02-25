# getDevInfo

### Interface functions
> Get system information.

### Prototype

```java
Bundle getDevInfo();
```

- #### Parameter

  > void

- #### Return
| Type   | Description                                              |
| :----- | :------------------------------------------------------- |
| Bundle | See [DeviceInfoConstrants](enum.md#DeviceInfoConstrants) |


### For example:

```java
Bundle devInfo = DeviceHelper.getDeviceService().getDevInfo();
String vendor = devInfo.getString(DeviceInfoConstrants.COMMOM_VENDOR);
String model = devInfo.getString(DeviceInfoConstrants.COMMOM_MODEL);
String os_ver = devInfo.getString(DeviceInfoConstrants.COMMOM_OS_VER);
String sn = devInfo.getString(DeviceInfoConstrants.COMMOM_SN);
String tusn = devInfo.getString(DeviceInfoConstrants.TID_SN);
String versionCode = devInfo.getString(DeviceInfoConstrants.COMMON_SERVICE_VER);
String hardware = devInfo.getString("hardware");
```



### See also

[Home](../README.md) |[login](login.md)|[setSystemClock](setSystemClock.md)

