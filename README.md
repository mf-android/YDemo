# YDemo
YDemo base on YSDK, for Morefun Android POS

# DOCUMENT

PDF: [English](Ydemo/MFYSDK_Android_Programming_Manual.pdf)

Markdown: [English](docs/README.md) 

SDK changes: [CHANGES.md](Ydemo/CHANGES.md)



# QUICKSTART

1. #### Install YSDK apk

> Install using the following adb command

```
adb install YSDK.apk
```

2. ### Linked mfysdk.jar

   add mfysdk.jar to your project.

3. ### Binding SDK service in Application onCreate function

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

- #### Register a ServiceConnection


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

            initDevices();

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

- #### Init DeviceHelper


```
public void initDevices(){
	try {
        DeviceHelper.reset();
        DeviceHelper.initDevices(MyApplication.this);
    } catch (RemoteException e) {
        e.printStackTrace();
    }
}
```

[DeviceHelper](YDemo\app\src\main\java\com\morefun\ysdk\sample\device\DeviceHelper.java)  class  From reference Ydemo project.


#  FAQ

### Offline Pin error message
```
//Pin call back ,need jude the messageType. There are three cases.
            @Override
            public void onCardHolderInputPin(boolean isOnlinePin, int messageType) throws RemoteException {
                Log.d(TAG, "onCardHolderInputPin isOnlinePin = " + isOnlinePin + "," + messageType);
                String messagePrompt = "Please enter PIN";
                if (messageType == 3) {
                    messagePrompt = "Please enter PIN";
                } else if (messageType == 2) {
                    messagePrompt = "Enter PIN again";
                } else if (messageType == 1) {
                    messagePrompt = "Enter  laster PIN ";
                }
```

### AID tag details
Could I load the aids of each brand card? Visa, visa electron, master card and amex? 

We have sorted out the related parameter information of the card group AID, you first understand.

```
9F06(07)    <T> Terminal Application Identifier
9F09(02)   <T> Application Version Number
DF11(05)   <T> terminal Action Code-Default
DF12(05)  <T> terminal Action Code-Online
DF13(05)  <T> terminal Action Code-Denial
9F1B(04)  <T> Terminal Floor Limit
5F2A(02)  <T> Transaction Currency Code
5F36(01)  <T> Transaction Currency Exponent
DF19(06)  <T> Contactless Floor Limit
DF20(06)  <T> Contactless Transaction Limit
DF21(06)  <T> Contactless CVM Limit
DF17(01)  <T> Target Percentage for Random Selection
DF16(01)  <T> Maximum Target Percentage for Random Selection
DF15(04)  <T> Threshold Value for Biased Random Selection  
50(16)   <T>Application Label, e.g.(0xA0 0x00 0x00 0x00 0x03 0x10 0x10 0x00 0x00 0x00 0x00 0x00 0x00 0x00 0x00 0x00)
DF01(01)  <T> Application selection indicator,0 or 1, e.g.(0x00)
DF18(01)  <T> Terminal online pin capability, 0x30 or 0x31 , e.g.(0x31)
DF14(252)  <T> Default DDOL(Hex), e.g.(0x9F 0x37 0x04 0x00 ...)  
9F7B(06)  <T> EC Terminal Transaction Limit, e.g.(0x00 0x00 0x00 0x00 0x20 0x00)--for UnionPay cards
```



The method of setting aid is like the way provided by demo.

Contactless Transaction Limit : If the limit is exceeded, the transaction will fail.

Contactless Floor Limit            :  If the limit is exceeded, the transaction may request online.

Contactless CVM Limit             :  If the limit is exceeded, the transaction will request CVM method.

###  the type contactless mode
After the transaction is over, you can judge by obtaining the value of 9F39tag, 07 is contactless EMV mode, 91 is contactless mag-stripe mode.

###  View the values of AID & CAPK 

Open YDEMO.apk;

AID& CAPK menu -> View AID list、 View AID list.



### About Dukpt

##### 1.Inject key 

```
String key = "C1D0F8FB4958670DBA40AB1F3752EF0D";
String ksn = "FFFF9876543210" + "000000";
DukptLoadObj dukptLoadObj = new DukptLoadObj(key, ksn, 
	DukptLoadObj.DukptKeyType.DUKPT_BDK_PLAINTEXT, 
	DukptLoadObj.DukptKeyIndex.KEY_INDEX_0);
DeviceHelper.getPinpad().dukptLoad(dukptLoadObj);
```
```

```
##### 3. Get KSN

increaseKsn API : Increase new KSN or get last KSN.

```
boolean isIncrease = false;
String ksn = DeviceHelper.getPinpad().increaseKsn(isIncrease);

```
##### 4.Encrypt data

```
String data = "12345678ABCDEFGH";

DukptCalcObj.DukptAlg alg = DukptCalcObj.DukptAlg.DUKPT_ALG_CBC;
DukptCalcObj.DukptOper oper = DukptCalcObj.DukptOper.DUKPT_ENCRYPT;
DukptCalcObj.DukptType type = DukptCalcObj.DukptType.DUKPT_DES_KEY_DATA1;

DukptCalcObj dukptCalcObj = new DukptCalcObj(type, oper, alg, data);
Bundle bundle = DeviceHelper.getPinpad().dukptCalcDes(dukptCalcObj);

String encrypt = bundle.getString(DukptCalcObj.DUKPT_DATA);
String ksn = bundle.getString(DukptCalcObj.DUKPT_KSN));
```

##### 5.How many sets of keys can I use ?

​	At most 8 groups of keys are supported, so the API needs to pass in the relevant key index.

```
public interface DukptKeyGid {
    int GID_GROUP_EMV_IPEK = 0;
    int GID_GROUP_TRACK_IPEK = 1;
    int GID_GROUP_PIN_IPEK = 2;
    int GID_GROUP_EMV_IPEK2 = 3;
    int GID_GROUP_TRACK_IPEK2 = 4;
    int GID_GROUP_PIN_IPEK2 = 5;
}
```

#### Devices leaving the factory

you will provide us with an apk file in advance, and preset the apk version when leaving the factory. 
Later end users can also update themselves through the appstore.

#### ADB
my laptop can't see terminal
how to turn debug on?

MFSetting->More settings -> Developer options

if you don't see the Developer options, Please follow
About Phone -> Build number 
(Click 5 times continuously)

(for product devices)
There have other for new system.
There is a window to verify. You need to enter a verification code.

Please send the random code on the window to the discussion group, 
and we will give you the corresponding verification code.
