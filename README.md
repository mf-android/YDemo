TAQ

# YDemo
YDemo base on YSDK, for Morefun Android POS

## INSTALLATION

#####  install YSDK apk

YSDK/YSDK.apk.

```
adb install  YSDK.apk
```

##### You may need to add mfysdk.jar to your project.

```
path: app/libs/mfysdk.jar 
```

YSDK/Demo.apk

APK compiled with project, can be used to run viewing functions directly.




# FEATURES

[Login](https://github.com/mf-android/YDemo/blob/master/docs/Login.md "Markdown")

Device info

PBOC

AID & CAPK (Add、Delete、Get)

Magnetic stripe 

Contact & Contactless(Dip & Tap)

PinPad

------

Printer

LED & Buzzer

Scanner

------

DUKPT （Derived Unique Key Per Transaction）《ANSI X9.24》

Load BDK

Load IPEK

IncreaseKSN

Dukptcalculation

------

MK/SK

Calculate MAC

M1Card

FELICA Card



# DOCUMENT

[CHANGES.md](https://github.com/mf-android/YDemo/blob/master/CHANGES.md "Markdown")

MFYSDK_Android_Programming_Manual.pdf

This manual is applicable to MF919 POS Terminal (hereinafter referred to as “MFPOS”).

```
path:docs/MFYSDK_Android_Programming_Manual.pdf
```




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



###  View the values of AID & CAPK 

Open YDEMO.apk;

AID& CAPK menu -> View AID list、 View AID list.



### About Dukpt

##### 1.Inject IPEK key & KSN 

Please see YDEMO source code (init IPEK And Ksn).

```
String IPEK = "C1D0F8FB4958670DBA40AB1F3752EF0D";
//KSN must be 20 length String. 95A627000210210 00000
String ksn = "FFFF9876543210" + "000000";
int ret = DeviceServiceEngine.getPinPad().initDukptIPEKAndKsn(PIN_GROUP_INDEX, IPEK, ksn,true, "00000");
```
##### 2.Inject BDK key & KSN 

```
//BDK 32 length String.
String BDK = "C1D0F8FB4958670DBA40AB1F3752EF0D";
//KSN must be 20 length String. 95A627000210210 00000
String ksn = "FFFF9876543210" + "000000";
int ret = DeviceServiceEngine.getPinPad().initDukptBDKAndKsn(KeyIndex, BDK, ksn,true, "00000");
```
##### 3. Get KSN

increaseKSN API : Generate new PEK and return new KSN.

```
public void increaseKSN(PinPad pinPad) throws RemoteException {
        // one transaction can only be called once, Every time you get it, the PEK key changes
        //  need to get the ksn first before search card.
        // increaseKSN API : Generate PEK and return new KSN
        trackKsn = pinPad.increaseKSN(TRACK_GROUP_INDEX, new Bundle());
}

```
##### 4.Encrypt data

```
String ksn = engine.getPinPad().increaseKSN(KeyIndex, new Bundle());
        // data length should be is multiple of 8.
         byte[] inputData = Utils.str2Bcd("04953DFFFF9D9D7B".trim());
		 byte[] data = Utils.checkInputData(inputData);
        byte keyType = DukptKeyType.MF_DUKPT_DES_KEY_PIN;
        //only support TDES.
        int desAlgorithmType = DesAlgorithmType.TDES_CBC;
        int desMode = DesMode.ENCRYPT; // DesMode.ENCRYPT DesMode.DECRYPT
//        String dukptCalculation(int keyIndex, byte keyType, int desAlgorithmType, byte[] data, int dataLen, int desMode, Bundle bundle)
        String calculationData = engine.getPinPad().dukptCalculation(DukptKeyGid.GID_GROUP_EMV_IPEK, keyType, desAlgorithmType ,data, data.length , desMode, new Bundle());
        Log.d(TAG,"calculationData = " + calculationData);
        Log.d(TAG,"ksn = " + ksn);
        alertDialogOnShowListener.showMessage(
                "multiple of 8 = " + (data.length / 8 ==0)
                +"\n dukptCalculation ksn :" +(ksn)
                + "\n" + " calculationData :" + calculationData);
```

##### 5.How many sets of keys can I use ?

​	At most 6 groups of keys are supported, so the API needs to pass in the relevant key index.

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