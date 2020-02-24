
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
| bundle     | Bundle                | See **PinPadConstrants**.     |
| panBlock   | byte[]                | pan                           |
| mKeyId     | int                   | work key index                |
| pinAlgMode |                       |                               |
| listener   | OnPinPadInputListener | See **OnPinPadInputListener** |


#### Return
| Value | Description               |
| :---- | :------------------------ |
| 0     | succeed. |
| other |  fail. |



#### OnPinPadInputListener

```
void onInputResult(int retCode, in byte[] pin ,String ksn);
	
void onSendKey(byte keyCode);
```

- #### onInputResult

| Parameter | Class  | Description               |
| --------- | ------ | ------------------------- |
| retCode   | int    | 0: succeed;   other: fail |
| pin       | byte[] | Pin data.                 |
| ksn       | String | For dukpt.                |

- #### onSendKey

  | Parameter | Class | Description            |
  | --------- | ----- | ---------------------- |
  | keyCode   | byte  | See  **ServiceResult** |

#### ServiceResult

| Parameter           | Value | Description   |
| ------------------- | ----- | ------------- |
| PinPad_Input_Cancel | -7006 | Cancel button |
| PinPad_Input_OK     | -7044 | Okay button   |
| PinPad_Input_Clear  | -7035 | Clear button  |
| PinPad_Input_Num    | -7045 | Number        |

#### Set Password Length

```
setSupportPinLen(in int[] pinLen);
```

- ##### setSupportPinLen

  | Parameter | Class | Description |
  | --------- | ----- | ----------- |
  | pinLen    | int[] | {max, min}  |

#### For example

```

```



### See also

[Home](../README.md) |[loadEncryptMKey](loadEncryptMKey.md)|[loadWKey](loadWKey.md)|[desEncByWKey](desEncByWKey.md)|[desEncByWKey](desEncByWKey.md)|[getMac](getMac.md)|[inputText](inputText.md)

