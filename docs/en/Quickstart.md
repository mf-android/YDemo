# Quickstart

1. #### Install YSDK apk

> Install using the following adb command

```
adb install YSDK.apk
```

2. ### Linked mfysdk.jar

add mfysdk.jar to your project.

3. ### Add permission to your project AndroidManifest.xml

```
    <uses-permission android:name="android.permission.CLOUDPOS_EMV" />

    <uses-permission android:name="android.permission.CLOUDPOS_MSR" />
    <!-- Printer -->
    <uses-permission android:name="android.permission.CLOUDPOS_PRINTER" />
    <!-- LED -->
    <uses-permission android:name="android.permission.CLOUDPOS_LED" />
    <!-- Pinpad -->
    <uses-permission android:name="android.permission.CLOUDPOS_PINPAD" />
    <uses-permission android:name="android.permission.CLOUDPOS_PIN_GET_PIN_BLOCK" />
    <uses-permission android:name="android.permission.CLOUDPOS_PIN_MAC" />
    <uses-permission android:name="android.permission.CLOUDPOS_PIN_ENCRYPT_DATA" />
    <uses-permission android:name="android.permission.CLOUDPOS_PIN_UPDATE_USER_KEY" />
    <uses-permission android:name="android.permission.CLOUDPOS_PIN_UPDATE_MASTER_KEY" />
    <!-- SAFE_MODULE -->
    <uses-permission android:name="android.permission.CLOUDPOS_SAFE_MODULE_READONLY" />
    <uses-permission android:name="android.permission.CLOUDPOS_SAFE_MODULE" />
```

4. ### Binding SDK service in Application onCreate function

```
    public void bindDeviceService() {
        if (null != deviceServiceEngine) {
            return;
        }

        Intent intent = new Intent();
        intent.setAction(SERVICE_ACTION);
        intent.setPackage(SERVICE_PACKAGE);

        bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }
```

#### Register a ServiceConnection

```
    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            deviceServiceEngine = null;
            Log.e(TAG, "======onServiceDisconnected======");
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            deviceServiceEngine = DeviceServiceEngine.Stub.asInterface(service);
            Log.d(TAG, "======onServiceConnected======");

            try {
                DeviceHelper.reset();
                DeviceHelper.initDevices(MyApplication.this);
            } catch (RemoteException e) {
                e.printStackTrace();
            }

            linkToDeath(service);
        }

        private void linkToDeath(IBinder service) {
            try {
                service.linkToDeath(new IBinder.DeathRecipient() {
                    @Override
                    public void binderDied() {
                        Log.d(TAG, "======binderDied======");
                        deviceServiceEngine = null;
                        bindDeviceService();
                    }
                }, 0);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    };
```

#### Get device instances for each function

```
try {
                DeviceHelper.reset();
                DeviceHelper.initDevices(MyApplication.this);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
```

[DeviceHelper](../../YDemo\app\src\main\java\com\morefun\ysdk\sample\device\DeviceHelper.java)  class  From reference Ydemo project.

### Reference related functions
[Function table](../README.md) | [login](login.md)

