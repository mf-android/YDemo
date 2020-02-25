
# inputText

### Interface functions
> Input offline pin

### Prototype

```java
int inputText(in Bundle bundle, in OnPinPadInputListener listener, int mode);
```

- #### Parameter
| Parameter | Type                                            | Description                                      |
| :-------- | :---------------------------------------------- | :----------------------------------------------- |
| bundle    | Bundle                                          | See [PinPadConstrants](enum.md#PinPadConstrants) |
| listener  | [OnPinPadInputListener](#OnPinPadInputListener) | Input Callback                                   |
| mode      | int                                             | Reserve                                          |

- #### Return
| Type | Description |
| :--- | :---------- |
| 0    | Succeed     |
| else | Fail        |

### OnPinPadInputListener

```java
void onInputResult(int retCode, in byte[] pin ,String ksn);
	
void onSendKey(byte keyCode);
```

- #### onInputResult

| Parameter | Type   | Description               |
| --------- | ------ | ------------------------- |
| retCode   | int    | 0: succeed;   other: fail |
| pin       | byte[] | Pin data.                 |
| ksn       | String | For dukpt.                |

- #### onSendKey

  | Parameter | Type | Description            |
  | --------- | ---- | ---------------------- |
  | keyCode   | byte | See  **ServiceResult** |



#### For example

```java
  	Bundle bundle = new Bundle();
    bundle.putBoolean(PinPadConstrants.IS_SHOW_PASSWORD_BOX, false);
    bundle.putBoolean(PinPadConstrants.IS_SHOW_TITLE_HEAD, false);
    bundle.putString(PinPadConstrants.TITLE_HEAD_CONTENT, "Please input offline Pin\n");
    pinResult = DeviceHelper.getPinpad().inputText(bundle, new OnPinPadInputListener.Stub() {
    @Override
    public void onInputResult(int ret, byte[] pinBlock, String ksn) throws RemoteException {
    
    }
    @Override
    public void onSendKey(byte keyCode) throws RemoteException {

    }
});
```


### See also

[Home](../README.md) |[loadEncryptMKey](loadEncryptMKey.md)|[loadWKey](loadWKey.md)|[desEncByWKey](desEncByWKey.md)|[desEncByWKey](desEncByWKey.md)|[getMac](getMac.md)|[inputText](inputText.md)|[inputOnlinePin](inputOnlinePin.md)|[setSupportPinLen](setSupportPinLen.md)

