
# inputOnlinePin

### Interface functions
> Input online pin

### Prototype

```java
int inputOnlinePin(in Bundle bundle,in byte[] panBlock, int mKeyId, int pinAlgMode, in OnPinPadInputListener listener);
```

#### Parameter
| Name  | Type                 | Description                   |
| :--------- | :-------------------- | :---------------------------- |
| bundle     | Bundle                | [PinPadConstrants](enum.md#PinPadConstrants) |
| panBlock   | byte[]                | pan                           |
| mKeyId     | int                   | work key index                |
| pinAlgMode | int | [PinAlgorithmMode](enum.md#PinAlgorithmMode) |
| listener   | [OnPinPadInputListener](#OnPinPadInputListener) | Input Callback |


#### Return
| Value | Description               |
| :---- | :------------------------ |
| 0     | succeed. |
| other |  fail. |


#### OnPinPadInputListener

```java
void onInputResult(int retCode, in byte[] pin ,String ksn);
	
void onSendKey(byte keyCode);
```

- #### onInputResult

| Name    | Type   | Description               |
| ------- | ------ | ------------------------- |
| retCode | int    | 0: succeed;   other: fail |
| pin     | byte[] | Pin data.                 |
| ksn     | String | For dukpt.                |

- #### onSendKey

  | Name    | Type | Description            |
  | ------- | ---- | ---------------------- |
  | keyCode | byte | [ServiceResult](enum.md#ServiceResult) |

#### For example

```java
Bundle bundle = new Bundle();
bundle.putBoolean(PinPadConstrants.IS_SHOW_PASSWORD_BOX, false);
bundle.putBoolean(PinPadConstrants.IS_SHOW_TITLE_HEAD, false);
bundle.putString(PinPadConstrants.TITLE_HEAD_CONTENT, "Please input onine Pin\n");
 byte[] panBlock = "6799998900000074324".getBytes();
mSDKManager.getPinPad().inputOnlinePin(bundle, panBlock, 0, PinAlgorithmMode.ISO9564FMT1, new OnPinPadInputListener.Stub() {
            @Override
            public void onInputResult(int ret, byte[] pinBlock, String pinKsn) throws RemoteException {
                
            }

            @Override
            public void onSendKey(byte keyCode) throws RemoteException {

            }
        });
```



### See also

[Home](../README.md) |[loadEncryptMKey](loadEncryptMKey.md)|[loadWKey](loadWKey.md)|[desEncByWKey](desEncByWKey.md)|[desEncByWKey](desEncByWKey.md)|[getMac](getMac.md)|[inputText](inputText.md)|[inputOnlinePin](inputOnlinePin.md)|[setSupportPinLen](setSupportPinLen.md)

