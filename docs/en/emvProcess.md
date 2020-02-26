
# emvProcess

### Interface functions
> Emv process

### Prototype

```java
int emvProcess(in Bundle data, in OnEmvProcessListener listener);
```

- #### Parameter
| Name     | Type                                          | Description  |
| :------- | :-------------------------------------------- | :----------- |
| data     | Bundle                                        |              |
| listener | [OnEmvProcessListener](#OnEmvProcessListener) | Emv Callback |
| mode     | int                                           | Reserve      |

- #### Return
| Type | Description |
| :--- | :---------- |
| 0    | Succeed     |
| else | Fail        |

### OnEmvProcessListener

```java
	void onSelApp(in List<String> appNameList, boolean isFirstSelect);
	
	void onConfirmCardNo(String cardNo);
	
	void onCardHolderInputPin(boolean isOnlinePin,int leftTimes);
	
	void onOnlineProc(in Bundle data);

    void onContactlessOnlinePlaceCardMode(int mode);

	void onFinish(int retCode,in Bundle data);
```

- #### onSelApp

  > On Select App CallBack and.

| Name          | Type    | Description            |
| ------------- | ------- | ---------------------- |
| appNameList   | List    | App list               |
| isFirstSelect | boolean | true: mean first time. |

- #### onConfirmCardNo

  > Confirm CardNum and import the result.

  | Name   | Type   | Description |
  | ------ | ------ | ----------- |
  | cardNo | String | Card number |

- #### onCardHolderInputPin

  > Inform user  enter password and import PINblock results.
  
  | Name        | Type    | Description                         |
  | ----------- | ------- | ----------------------------------- |
  | isOnlinePin | boolean | true: online trans, false: offline. |
  | leftTimes   | int     |                                     |
  
- #### onOnlineProc

  > Work online here and import online results.

  | Name | Type   | Description |
  | ---- | ------ | ----------- |
  | data | Bundle |             |

- #### onContactlessOnlinePlaceCardMode

  > Rupay tow tap callback and import the result.
  
  | Name | Type | Description |
  | ---- | ---- | ----------- |
  | mode | int  | rupay       |


- #### onFinish

  > Final callback, inform transaction results, transaction error information.
  
  | Name    | Type   | Description |
  | ------- | ------ | ----------- |
  | retCode | String |             |
  | data    | Bundle |             |

### For Example

```java
    public static Bundle getInitBundleValue(int channelType, String amount, String cashBackAmt) {
        Bundle bundle = new Bundle();

        String date = getCurrentTime("yyMMddHHmmss");

        bundle.putInt(EmvTransDataConstrants.MKEYIDX, 1);
        bundle.putBoolean(EmvTransDataConstrants.ISSUPPORTEC, false);
        bundle.putInt(EmvTransDataConstrants.PROCTYPE, 0);
        bundle.putInt(EmvTransDataConstrants.ISQPBOCFORCEONLINE, 0);
        bundle.putInt(EmvTransDataConstrants.CHANNELTYPE, channelType);

        bundle.putByte(EmvTransDataConstrants.B9C, (byte) EMVTag9CConstants.EMV_TRANS_SALE);
        bundle.putString(EmvTransDataConstrants.TRANSDATE, date.substring(0, 6));
        bundle.putString(EmvTransDataConstrants.TRANSTIME, date.substring(6, 12));
        bundle.putString(EmvTransDataConstrants.SEQNO, "00001");

        bundle.putString(EmvTransDataConstrants.TRANSAMT, amount);
        bundle.putString(EmvTransDataConstrants.CASHBACKAMT, cashBackAmt);

        bundle.putString(EmvTransDataConstrants.MERNAME, "MOREFUN");
        bundle.putString(EmvTransDataConstrants.MERID, "488923");
        bundle.putString(EmvTransDataConstrants.TERMID, "4999000");
        bundle.putString(EmvTransDataConstrants.CONTACTLESS_PIN_FREE_AMT, "200000");
        bundle.putStringArrayList(EmvTransDataConstrants.TERMINAL_TLVS, createArrayList("DF81180170", "DF81190118"));
        return bundle;
    }
```

```
DeviceHelper.getEmvHandler().emvProcess(EmvUtil.getInitBundleValue(channel, amount, "0.02"), new OnEmvProcessListener.Stub() {

            @Override
            public void onSelApp(List<String> appNameList, boolean isFirstSelect) throws RemoteException {
                showResult(textView, "onSelApp");
                selApp(appNameList);
            }

            @Override
            public void onConfirmCardNo(String cardNo) throws RemoteException {
                showResult(textView, "onConfirmCardNo:" + cardNo);
                DeviceHelper.getEmvHandler().onSetConfirmCardNoResponse(true);
            }

            @Override
            public void onCardHolderInputPin(boolean isOnlinePin, int leftTimes) throws RemoteException {
                showResult(textView, "onCardHolderInputPin");
                String cardNo = EmvUtil.readPan();

                inputPin(cardNo, new OnInputPinListener() {
                    @Override
                    public void onInputPin(byte[] pinBlock) {
						DeviceHelper.getEmvHandler().onSetCardHolderInputPin(pinBlock);
                    }
                });
            }

            @Override
            public void onPinPress(byte keyCode) throws RemoteException {
                showResult(textView, "Callback:onPinPress");
            }

            @Override
            public void onCertVerify(String certName, String certInfo) throws RemoteException {}

            @Override
            public void onOnlineProc(Bundle data) throws RemoteException {
                showResult(textView, "Callback:onOnlineProc");
                onlineProc();
            }

            @Override
            public void onContactlessOnlinePlaceCardMode(int mode) throws RemoteException 			 {}

            @Override
            public void onFinish(int retCode, Bundle data) throws RemoteException {
                showResult(textView, "Callback:onFinish");
                emvFinish(retCode, data);
            }

            @Override
            public void onSetAIDParameter(String aid) throws RemoteException {
            }

            @Override
            public void onSetCAPubkey(String rid, int index, int algMode) throws RemoteException {}

            @Override
            public void onTRiskManage(String pan, String panSn) throws RemoteException {
            }

            @Override
            public void onSelectLanguage(String language) throws RemoteException {
            }

            @Override
            public void onSelectAccountType(List<String> accountTypes) throws RemoteException {
            }

            @Override
            public void onIssuerVoiceReference(String pan) throws RemoteException {
            }

        });
```




### See also

[Home](../README.md)|[emvProcess](emvProcess.md) |[readEmvData](readEmvData.md)|[initTermConfig](initTermConfig.md)

