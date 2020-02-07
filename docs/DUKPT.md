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
String ksn = mSDKManager.getPinPad().increaseKSN(KeyIndex, new Bundle());
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
